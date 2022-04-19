package com.example.pangeaappproduccion.ui.home;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.pangeaappproduccion.Adapters.AdapterPublicacion;
import com.example.pangeaappproduccion.Buscador;
import com.example.pangeaappproduccion.FotoPerfil;
import com.example.pangeaappproduccion.Publicaciones;
import com.example.pangeaappproduccion.R;
import com.example.pangeaappproduccion.databinding.FragmentHomeBinding;
import com.example.pangeaappproduccion.Listas.listPublicaciones;
import com.example.pangeaappproduccion.translate_api;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private TextView traducidoTexto;

    private TextView etMensaje ,userPerfil,PaisPerfil,CursoPerfil,nivelPerfil,nombreUsuario;
    private Button buttonChat, buttonImagen, buttonAudio,buttonReproAudio,buttonTraducir;
    private ImageView imgPerfil,imgHeader;
    private List<listPublicaciones> listPublicaciones;
    private AdapterPublicacion adapterPublicacion;
    private RecyclerView recyclerViewPublicaciones;
    private String username;
    private static final int IMG_Header = 0;
    private static final int GALLERY_PICKER =1;
    private static final  int AudioSend = 2;
    private static final  int ACTION_POST = 3;
    private String imagenUsuario;

    Uri urlAudio;

    StorageReference mStorage = FirebaseStorage.getInstance().getReference();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        SharedPreferences preferences = requireActivity().getSharedPreferences("accesos", MODE_PRIVATE);
        String email_perfil = preferences.getString("email", "No name defined");


        recyclerViewPublicaciones = view.findViewById(R.id.recyclerViewChat);
        etMensaje = view.findViewById(R.id.pregunta);
        buttonChat = view.findViewById(R.id.publicar);
        buttonImagen = view.findViewById(R.id.btnImagen);
        imgPerfil = view.findViewById(R.id.foto_perfil);
        imgHeader = view.findViewById(R.id.imgHeader);
        buttonAudio = view.findViewById(R.id.btnAudio);
        buttonReproAudio = view.findViewById(R.id.btnReproAudio);
        buttonTraducir = view.findViewById(R.id.btnTraducir);
        traducidoTexto = view.findViewById(R.id.traducido);
        nivelPerfil = view.findViewById(R.id.nivelPerfil);
        userPerfil = view.findViewById(R.id.userPerfil);
        PaisPerfil = view.findViewById(R.id.PaisPerfil);
        CursoPerfil =  view.findViewById(R.id.CursoPerfil);
        nombreUsuario =  view.findViewById(R.id.nombreUsuario);


        Context context = view.getContext();


        listPublicaciones = new ArrayList<>();
        adapterPublicacion = new AdapterPublicacion(listPublicaciones);
        recyclerViewPublicaciones.setAdapter(adapterPublicacion);
        recyclerViewPublicaciones.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewPublicaciones.setHasFixedSize(true);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(email_perfil);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    username = document.getString("user");
                    nombreUsuario.setText(document.getString("user"));
                    FirebaseFirestore.getInstance().collection("redSocial").whereEqualTo("usuario",username).addSnapshotListener((value, error) -> {
                        if (error != null) {
                            Log.d(TAG, "Error:" + error.getMessage());
                        } else {
                            for (DocumentChange documentChange : value.getDocumentChanges()) {
                                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                    listPublicaciones.add(documentChange.getDocument().toObject(listPublicaciones.class));
                                    adapterPublicacion.notifyDataSetChanged();
                                    recyclerViewPublicaciones.smoothScrollToPosition(listPublicaciones.size());
                                }
                            }
                        }
                    });
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });



        db.collection("fotoPerfil").whereEqualTo("usuario", email_perfil).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                RequestOptions requestOptions = new RequestOptions();
                                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
                                Glide.with(context)
                                        .load(documentSnapshot.get("multimedia"))
                                        .apply(requestOptions)
                                        .circleCrop()
                                        .into(imgPerfil);
                            }
                        }
                    }
                });




        FirebaseFirestore db2 = FirebaseFirestore.getInstance();
        db2.collection("fotosHeader").whereEqualTo("usuario", email_perfil).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){


                                Log.d("imagen", documentSnapshot.getId()+"la imagen es"+ documentSnapshot.get("multimedia"));
                                RequestOptions requestOptions = new RequestOptions();
                                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));

                                Glide.with(context)
                                        .load(documentSnapshot.get("multimedia"))
                                        .apply(requestOptions)
                                        .into(imgHeader);

                            }
                        }
                    }
                });







        FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
        dbDataPerfil.collection("users").whereEqualTo("email", email_perfil).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){




                                userPerfil.setText(documentSnapshot.getString("usuario"));
                                PaisPerfil.setText(documentSnapshot.getString("ciudad"));
                                nivelPerfil.setText(documentSnapshot.getString("nivel"));
                                CursoPerfil.setText(documentSnapshot.getString("idioma_interes"));


                                String usuarioFinal = userPerfil.getText().toString();

                                SharedPreferences.Editor editor =  getActivity().getSharedPreferences("usuario_post", MODE_PRIVATE).edit();
                                editor.putString("usuario_post", usuarioFinal );
                                editor.apply();


                            }
                        }
                    }
                });
        SharedPreferences preferences1 = getActivity().getSharedPreferences("usuario_post", MODE_PRIVATE);
        String usuario_post_final = preferences1.getString("usuario_post", "No name defined");








        imgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentImagen = new Intent(Intent.ACTION_PICK);
                intentImagen.setType("image/*");
                startActivityForResult(intentImagen,GALLERY_PICKER);

            }
        });

        imgHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentImagen = new Intent(Intent.ACTION_PICK);
                intentImagen.setType("image/*");
                startActivityForResult(intentImagen,IMG_Header);

            }
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


        buttonChat.setOnClickListener(view1 -> {
            String clave = UUID.randomUUID().toString().toUpperCase();
            Publicaciones publicaciones = new Publicaciones();
            publicaciones.setMensaje(etMensaje.getText().toString());
            publicaciones.setUsuario(username);
            publicaciones.setid(clave);
            publicaciones.setStatus("0");
            FirebaseFirestore.getInstance().collection("redSocial").add(publicaciones);
        });

        buttonTraducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                translate_api translate=new translate_api();
                translate.setOnTranslationCompleteListener(new translate_api.OnTranslationCompleteListener() {
                    @Override
                    public void onStartTranslation() {

                    }

                    @Override
                    public void onCompleted(String text) {

                        etMensaje.setText(text);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                translate.execute(etMensaje.getText().toString(),"es","en");


            }
        });

        FloatingActionButton fab = view.findViewById(R.id.buscar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), Buscador.class);
                startActivity(intent);

            }
        });


        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SharedPreferences preferences = getActivity().getSharedPreferences("accesos", MODE_PRIVATE);
        String email_perfil = preferences.getString("email", "No name defined");

        SharedPreferences preferences1 = getActivity().getSharedPreferences("usuario_post", MODE_PRIVATE);
        String usuario_post_final = preferences1.getString("usuario_post", "No name defined");

        if(requestCode == AudioSend && resultCode == RESULT_OK){
            urlAudio = data.getData();
            StorageReference filePath = mStorage.child("audios").child(urlAudio.getLastPathSegment());
            filePath.putFile(urlAudio).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uiriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uiriTask.isSuccessful());
                    Uri dowloadUri = uiriTask.getResult();

                    String clave = UUID.randomUUID().toString().toUpperCase();


                    Publicaciones publicaciones = new Publicaciones();
                    publicaciones.setMensaje(etMensaje.getText().toString());
                    publicaciones.setMultimedia(dowloadUri.toString());
                    publicaciones.setUsuario(username);
                    publicaciones.setStatus("2");
                    publicaciones.setid(clave);
                    FirebaseFirestore.getInstance().collection("redSocial").add(publicaciones);
                    etMensaje.setText("");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("FileManager","Error en uploadImg ==>"+e);
                }
            });

        }


        if(requestCode == IMG_Header &&  resultCode == RESULT_OK) {
            Uri uri = data.getData();
            imgHeader.setImageURI(uri);


            StorageReference filePath = mStorage.child("fotosHeader").child(uri.getLastPathSegment());

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uiriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uiriTask.isSuccessful());
                    Uri dowloadUri = uiriTask.getResult();


                    FotoPerfil fotoPerfil = new FotoPerfil();
                    fotoPerfil.setMultimedia(dowloadUri.toString());
                    fotoPerfil.setUsuario(usuario_post_final);
                    FirebaseFirestore.getInstance().collection("fotosHeader").add(fotoPerfil);



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("FileManager","Error en uploadImg ==>"+e);
                }
            });
        }


        if(requestCode == GALLERY_PICKER &&  resultCode == RESULT_OK) {






            Uri uri = data.getData();
            imgPerfil.setImageURI(uri);

            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setCustomMetadata("descripcion","Esta es una Prueba")
                    .setCustomMetadata("usuario",email_perfil)
                    .build();
            StorageReference filePath = mStorage.child("fotoPerfil").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uiriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uiriTask.isSuccessful());
                    Uri dowloadUri = uiriTask.getResult();

                    FirebaseFirestore db2 = FirebaseFirestore.getInstance();

                    Map<String, Object> fotoPerfil = new HashMap<>();
                    fotoPerfil.put("multimedia",dowloadUri.toString());
                    fotoPerfil.put("usuario",username);
                    db2.collection("fotoPerfil").document(email_perfil).update(fotoPerfil).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(),"se subio el archivo",Toast.LENGTH_LONG).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();

                        }
                    });


                    FotoPerfil fotoPerfils = new FotoPerfil();
                    fotoPerfils.setMultimedia(dowloadUri.toString());
                    fotoPerfils.setUsuario(email_perfil);
                    FirebaseFirestore.getInstance().collection("fotoPerfil").add(fotoPerfils);
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
            imgPerfil.setImageURI(uri);

            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setCustomMetadata("descripcion","Esta es una Prueba")
                    .setCustomMetadata("usuario",username)
                    .build();

            StorageReference filePath = mStorage.child("redSocial").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uiriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uiriTask.isSuccessful());
                    Uri dowloadUri = uiriTask.getResult();

                    String clave = UUID.randomUUID().toString().toUpperCase();

                    Publicaciones PublicacionesImagenes = new Publicaciones();
                    PublicacionesImagenes.setMultimedia(dowloadUri.toString());
                    PublicacionesImagenes.setUsuario(username);
                    PublicacionesImagenes.setMensaje(etMensaje.getText().toString());
                    PublicacionesImagenes.setid(clave);
                    PublicacionesImagenes.setStatus("1");

                    FirebaseFirestore.getInstance().collection("redSocial").add(PublicacionesImagenes);



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("FileManager","Error en uploadImg ==>"+e);
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public class AdapterPublicaciones extends RecyclerView.Adapter<AdapterPublicaciones.PublicacionesHolder> {



        FirebaseUser fuser;
        private List<com.example.pangeaappproduccion.Listas.listPublicaciones> listPublicaciones;

        public  AdapterPublicaciones(List<listPublicaciones> listPublicaciones){
            this.listPublicaciones = listPublicaciones;
        }





        @NonNull
        @NotNull
        @Override
        public PublicacionesHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {



            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_publicaciones,viewGroup,false);

            return new PublicacionesHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull PublicacionesHolder publicacionesHolder, int i) {

            publicacionesHolder.Publicacion.setText(listPublicaciones.get(i).getMensaje());
            publicacionesHolder.Nombre.setText(listPublicaciones.get(i).getUsuario());
            // Uri uri = Uri.parse(listPublicaciones.get(i).getMultimedia());
            // publicacionesHolder.imgPublicacion.setImageURI(uri);
            Glide.with(getActivity()).load(listPublicaciones.get(i).getMultimedia()).into(publicacionesHolder.imgPublicacion);




        }

        @Override
        public int getItemCount() {

            return listPublicaciones.size();
        }

        public class PublicacionesHolder extends RecyclerView.ViewHolder{

            private TextView Publicacion;
            private TextView Nombre;
            private ImageView imgPublicacion;

            public PublicacionesHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                Publicacion = itemView.findViewById(R.id.pregunta);
                Nombre = itemView.findViewById(R.id.usuarioForo);
                imgPublicacion = itemView.findViewById(R.id.imgPublicacion);
            }
        }

    }
}
