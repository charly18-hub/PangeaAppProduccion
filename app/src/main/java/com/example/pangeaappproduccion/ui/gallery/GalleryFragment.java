package com.example.pangeaappproduccion.ui.gallery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.pangeaappproduccion.Publicaciones;
import com.example.pangeaappproduccion.R;
import com.example.pangeaappproduccion.databinding.FragmentGalleryBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;

    private TextView etMensaje2;
    ImageView foto;
    Button subir, seleccionar;
    DatabaseReference imgref;
    ProgressDialog cargando;
    Bitmap thumb_bitmap = null;
    StorageReference storageReference;
    String usuario_recibido_chat1 = "Homero Simpson";
    private DatabaseReference mDataBase;
    private String downloadImageUrl;

    private static final int GALLERY_PICKER =1;
    StorageReference mStorage = FirebaseStorage.getInstance().getReference();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        foto = view.findViewById(R.id.imgFoto);
        etMensaje2 = view.findViewById(R.id.publicacion2);
        seleccionar = view.findViewById(R.id.btnImagen2);
        subir = view.findViewById(R.id.publicar2);
        imgref = FirebaseDatabase.getInstance().getReference().child("RedSocial");
        storageReference = FirebaseStorage.getInstance().getReference().child("fotos");
        cargando = new ProgressDialog(getContext());
        mDataBase = FirebaseDatabase.getInstance().getReference("redSocial");



        seleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intentImagen = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // intentImagen.setType("image/*");



                //startActivityForResult(intentImagen,GALLERY_PICKER);

                startActivityForResult(intentImagen,GALLERY_PICKER);



            }
        });




        return view;


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Toast.makeText(getActivity(),"entro", Toast.LENGTH_LONG).show();


        if(requestCode == GALLERY_PICKER &&  resultCode == RESULT_OK) {
            Uri uri = data.getData();
            foto.setImageURI(uri);

            Log.i("imagen",uri.toString());


            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setCustomMetadata("descripcion","Esta es una Prueba")
                    .setCustomMetadata("usuario",usuario_recibido_chat1)
                    .build();

            StorageReference filePath = mStorage.child("fotos").child(uri.getLastPathSegment());



            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(),"se subio el archivo",Toast.LENGTH_LONG).show();


                    Task<Uri> uiriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uiriTask.isSuccessful());
                    Uri dowloadUri = uiriTask.getResult();



                    Publicaciones publicaciones = new Publicaciones();
                    publicaciones.setMensaje(etMensaje2.getText().toString());
                    publicaciones.setMultimedia(dowloadUri.toString());
                    publicaciones.setUsuario(usuario_recibido_chat1);
                    FirebaseFirestore.getInstance().collection("redSocial").add(publicaciones);
                    etMensaje2.setText("");









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




}