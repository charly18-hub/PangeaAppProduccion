package com.example.pangeaappproduccion.ui.PerfilAmigos;

import static androidx.constraintlayout.widget.Constraints.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.pangeaappproduccion.Adapters.AdapterPublicacion;
import com.example.pangeaappproduccion.Model.listPublicaciones;
import com.example.pangeaappproduccion.Publicaciones;
import com.example.pangeaappproduccion.databinding.ActivityPerfilAmigosBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PerfilAmigosActivity extends AppCompatActivity {
    ActivityPerfilAmigosBinding binding;
    Context context;
    String nombreUsuario, id, token, correo;
    String miNombreUsuario, miId, miToken, miCorreo;
    private static final int GALLERY_PICKER = 1;
    private static final int AudioSend = 2;
    private static final int ACTION_POST = 3;
    private Uri urlAudio;
    StorageReference mStorage = FirebaseStorage.getInstance().getReference();


    private List<listPublicaciones> listPublicaciones;
    private AdapterPublicacion adapterPublicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPerfilAmigosBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        nombreUsuario = getIntent().getExtras().getString("nombreUsuario");

        binding.nombreUsuario.setText(nombreUsuario);
        context = getApplicationContext();

        SharedPreferences pref = getSharedPreferences("usuarioRegistroNormal", MODE_PRIVATE);
        miNombreUsuario = pref.getString("userName", "No name defined");
        miCorreo = pref.getString("email", "No name defined");
        miId = pref.getString("id", "No name defined");


        FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
        dbDataPerfil.collection("users").whereEqualTo("userName", nombreUsuario).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                correo = documentSnapshot.getString("emailAddress");
                                id = documentSnapshot.getString("uid");
                                token = documentSnapshot.getString("tokenFCM");
                            }


                            FirebaseFirestore.getInstance().collection("fotoPerfil").whereEqualTo("usuario", id).get()
                                    .addOnCompleteListener(tasks -> {
                                        if (tasks.isSuccessful()) {
                                            for (QueryDocumentSnapshot documentSnapshot : tasks.getResult()) {
                                                RequestOptions requestOptions = new RequestOptions();
                                                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
                                                Glide.with(context)
                                                        .load(documentSnapshot.get("multimedia"))
                                                        .apply(requestOptions)
                                                        .circleCrop()
                                                        .into(binding.imgHeader);
                                            }
                                        }
                                    });


                            FirebaseFirestore.getInstance().collection("fotosHeader").whereEqualTo("usuario", id).get()
                                    .addOnCompleteListener(task2 -> {
                                        if (task2.isSuccessful()) {
                                            for (QueryDocumentSnapshot documentSnapshot : task2.getResult()) {
                                                Log.d("imagen", documentSnapshot.getId() + "la imagen es" + documentSnapshot.get("multimedia"));
                                                RequestOptions requestOptions = new RequestOptions();
                                                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
                                                Glide.with(context)
                                                        .load(documentSnapshot.get("multimedia"))
                                                        .apply(requestOptions)
                                                        .into(binding.fotoPerfil);
                                            }
                                        }
                                    });
                        }
                    }
                });

        binding.btnImagen.setOnClickListener(view13 -> {
            Intent intentImagen = new Intent(Intent.ACTION_PICK);
            intentImagen.setType("image/*");
            startActivityForResult(intentImagen, ACTION_POST);
        });

        binding.btnAudio.setOnClickListener(view12 -> {
            Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
            startActivityForResult(intent, AudioSend);
        });

        binding.btnReproAudio.setOnClickListener(view14 -> {
            MediaPlayer mediaPlayer = MediaPlayer.create(this, urlAudio);
            mediaPlayer.start();
        });

        binding.publicar.setOnClickListener(view1 -> {
            FirebaseFirestore dbDataPerfils = FirebaseFirestore.getInstance();
            DocumentReference publicacion = dbDataPerfils.collection("redSocial").document();
            String clave = publicacion.getId();
            String id = UUID.randomUUID().toString().toUpperCase();
            Publicaciones publicaciones = new Publicaciones();
            publicaciones.setMensaje(binding.pregunta.getText().toString());
            publicaciones.setUsuario(nombreUsuario);
            publicaciones.setUsuarioPublico(miNombreUsuario);
            publicaciones.setId(id);
            publicaciones.setClave(clave);
            publicaciones.setStatus("0");
            FirebaseFirestore.getInstance().collection("redSocial").document(clave).set(publicaciones).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                            recreate();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
        });

        obtenerPublicaciones();
        listPublicaciones = new ArrayList<>();
        adapterPublicacion = new AdapterPublicacion(listPublicaciones);
        binding.recyclerViewPublicaciones.setAdapter(adapterPublicacion);
        binding.recyclerViewPublicaciones.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerViewPublicaciones.setHasFixedSize(true);

    }

    private void obtenerPublicaciones() {
        FirebaseFirestore.getInstance().collection("redSocial").whereEqualTo("usuario", nombreUsuario).addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.d(TAG, "Error:" + error.getMessage());
            } else {
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        listPublicaciones.add(documentChange.getDocument().toObject(listPublicaciones.class));
                        adapterPublicacion.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTION_POST && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            StorageReference filePath = mStorage.child("redSocial").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uiriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uiriTask.isSuccessful()) ;
                    Uri dowloadUri = uiriTask.getResult();
                    FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
                    DocumentReference publicacion = dbDataPerfil.collection("redSocial").document();
                    String clave = publicacion.getId();
                    String id = UUID.randomUUID().toString().toUpperCase();
                    Publicaciones PublicacionesImagenes = new Publicaciones();
                    PublicacionesImagenes.setMultimedia(dowloadUri.toString());
                    PublicacionesImagenes.setUsuario(nombreUsuario);
                    PublicacionesImagenes.setUsuarioPublico(miNombreUsuario);
                    PublicacionesImagenes.setMensaje(binding.pregunta.getText().toString());
                    PublicacionesImagenes.setId(id);
                    PublicacionesImagenes.setClave(clave);
                    PublicacionesImagenes.setStatus("1");
                    FirebaseFirestore.getInstance().collection("redSocial").document(clave).set(PublicacionesImagenes)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    recreate();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                    //adapterPublicacion.notifyDataSetChanged();
                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("FileManager", "Error en uploadImg ==>" + e);
                }
            });
        }

        if (requestCode == AudioSend && resultCode == RESULT_OK) {
            urlAudio = data.getData();
            StorageReference filePath = mStorage.child("audios").child(urlAudio.getLastPathSegment());
            filePath.putFile(urlAudio).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> uiriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uiriTask.isSuccessful()) ;
                Uri dowloadUri = uiriTask.getResult();
                FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
                DocumentReference publicacion = dbDataPerfil.collection("redSocial").document();
                String clave = publicacion.getId();
                String id = UUID.randomUUID().toString().toUpperCase();
                Publicaciones publicaciones = new Publicaciones();
                publicaciones.setMensaje(binding.pregunta.getText().toString());
                publicaciones.setMultimedia(dowloadUri.toString());
                publicaciones.setUsuario(nombreUsuario);
                publicaciones.setUsuarioPublico(miNombreUsuario);
                publicaciones.setStatus("2");
                publicaciones.setId(id);
                publicaciones.setClave(clave);
                FirebaseFirestore.getInstance().collection("redSocial").document(clave).set(publicaciones).addOnSuccessListener(aVoid -> {
                            recreate();
                            binding.pregunta.setText("");
                        })
                        .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
            }).addOnFailureListener(e -> Log.e("FileManager", "Error en uploadImg ==>" + e));
        }

    }

}