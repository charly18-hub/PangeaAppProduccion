package com.example.pangeaappproduccion.ui.slideshow;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.pangeaappproduccion.Adapters.AdapterPublicacion;
import com.example.pangeaappproduccion.ImagenesPublicacion;
import com.example.pangeaappproduccion.MultimediaFragment;
import com.example.pangeaappproduccion.Publicaciones;
import com.example.pangeaappproduccion.PublicacionesTextFragment;
import com.example.pangeaappproduccion.R;
import com.example.pangeaappproduccion.Util.UtilFragment;
import com.example.pangeaappproduccion.databinding.FragmentSlideshowBinding;
import com.example.pangeaappproduccion.ui.gallery.GalleryFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SlideshowFragment extends UtilFragment {
    private List<com.example.pangeaappproduccion.Listas.listPublicaciones> listPublicaciones;
    private AdapterPublicacion adapterPublicacion;

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;
    private RecyclerView recyclerViewPublicaciones;
    private Button buttonChat, buttonImagen, buttonAudio,buttonReproAudio,buttonTraducir;
    private TextView etMensaje;
    StorageReference mStorage = FirebaseStorage.getInstance().getReference();

    private static final int IMG_Header = 0;
    private static final int GALLERY_PICKER =1;
    private static final  int AudioSend = 2;
    private static final  int ACTION_POST = 3;
    Uri urlAudio;
    String email;
    String id;
    String username;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);


        View view = inflater.inflate(R.layout.fragment_slideshow, container, false);

        establecerIdioma();
        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpagerPublicaciones);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) view.findViewById(R.id.result_tabsPublicaciones);
        tabs.setupWithViewPager(viewPager);

        buttonChat = view.findViewById(R.id.publicar2);
        buttonImagen = view.findViewById(R.id.btnImagen2);
        etMensaje = view.findViewById(R.id.publicacion2);
        buttonAudio = view.findViewById(R.id.btnAudio);
        buttonReproAudio = view.findViewById(R.id.btnReproAudio);

        SharedPreferences preferences = getActivity().getSharedPreferences("accesos", MODE_PRIVATE);
        String email_perfil = preferences.getString("email", "No name defined");


        SharedPreferences preferences1 = getActivity().getSharedPreferences("usuarioRegistroNormal", MODE_PRIVATE);
        username = preferences1.getString("userName", "No name defined");
        id = preferences1.getString("id", "No name defined");
        email = preferences1.getString("email", "No name defined");


        String usuario_recibido = email_perfil;
        buttonChat.setOnClickListener(view1 -> {
            FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
            DocumentReference publicacion = dbDataPerfil.collection("redSocial").document();
            String clave = publicacion.getId();
            String id = UUID.randomUUID().toString().toUpperCase();
            Publicaciones publicaciones = new Publicaciones();
            publicaciones.setMensaje(etMensaje.getText().toString());
            publicaciones.setUsuario(username);
            publicaciones.setStatus("0");
            publicaciones.setId(id);
            publicaciones.setClave(clave);
            FirebaseFirestore.getInstance().collection("redSocial").document(clave).set(publicaciones);
            etMensaje.setText("");
        });
        buttonImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentImagen = new Intent(Intent.ACTION_PICK);
                intentImagen.setType("image/*");
                startActivityForResult(intentImagen,ACTION_POST);

            }
        });
        buttonAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                startActivityForResult(intent,AudioSend);
            }
        });

        buttonReproAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(),urlAudio);
                mediaPlayer.start();
            }
        });
        return view;
    }



    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {


        SlideshowFragment.Adapter adapter = new SlideshowFragment.Adapter(getChildFragmentManager());
        Fragment miFragment = null;
        Bundle datos_a_fragment = new Bundle();
        miFragment = new GalleryFragment();
        miFragment.setArguments(datos_a_fragment);


        adapter.addFragment(new PublicacionesTextFragment(), "Publicaciones");
        adapter.addFragment(new ImagenesPublicacion(), "Imagenes");
        adapter.addFragment(new MultimediaFragment(), "Multimedia");



        viewPager.setAdapter(adapter);
    }


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SharedPreferences preferences = getActivity().getSharedPreferences("accesos", MODE_PRIVATE);
        String email_perfil = preferences.getString("email", "No name defined");

        SharedPreferences preferences1 = getActivity().getSharedPreferences("usuario_post", MODE_PRIVATE);
        String usuario_post_final = preferences1.getString("usuario_post", "No name defined");

        if(requestCode == AudioSend && resultCode == RESULT_OK) {

            urlAudio = data.getData();
            StorageReference filePath = mStorage.child("audios").child(urlAudio.getLastPathSegment());
            filePath.putFile(urlAudio).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(),"se subio el archivo",Toast.LENGTH_LONG).show();
                    Task<Uri> uiriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uiriTask.isSuccessful());
                    Uri dowloadUri = uiriTask.getResult();

                    FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
                    DocumentReference publicacion = dbDataPerfil.collection("redSocial").document();
                    String clave = publicacion.getId();
                    String id = UUID.randomUUID().toString().toUpperCase();
                    Publicaciones publicaciones = new Publicaciones();
                    publicaciones.setMensaje(etMensaje.getText().toString());
                    publicaciones.setMultimedia(dowloadUri.toString());
                    publicaciones.setUsuario(username);
                    publicaciones.setStatus("2");
                    publicaciones.setId(id);
                    publicaciones.setClave(clave);
                    FirebaseFirestore.getInstance().collection("redSocial").document(clave).set(publicaciones);


                    etMensaje.setText("");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("FileManager","Error en uploadImg ==>"+e);
                }
            });
        }

        if(requestCode == ACTION_POST &&  resultCode == RESULT_OK) {
            Uri uri = data.getData();
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setCustomMetadata("descripcion","Esta es una Prueba")
                    .setCustomMetadata("usuario",usuario_post_final)
                    .build();
            StorageReference filePath = mStorage.child("redSocial").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(),"se subio el archivo",Toast.LENGTH_LONG).show();
                    Task<Uri> uiriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uiriTask.isSuccessful());
                    Uri dowloadUri = uiriTask.getResult();


                    FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
                    DocumentReference publicacion = dbDataPerfil.collection("redSocial").document();
                    String clave = publicacion.getId();
                    String id = UUID.randomUUID().toString().toUpperCase();
                    Publicaciones PublicacionesImagenes = new Publicaciones();
                    PublicacionesImagenes.setMensaje(etMensaje.getText().toString());
                    PublicacionesImagenes.setMultimedia(dowloadUri.toString());
                    PublicacionesImagenes.setUsuario(username);
                    PublicacionesImagenes.setStatus("1");
                    PublicacionesImagenes.setId(id);
                    PublicacionesImagenes.setClave(clave);
                    FirebaseFirestore.getInstance().collection("redSocial").document(clave).set(PublicacionesImagenes);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("FileManager","Error en uploadImg ==>"+e);
                }
            });
            etMensaje.setText("");
        }
    }

}