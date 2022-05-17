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
import com.example.pangeaappproduccion.Model.Registro.Language;
import com.example.pangeaappproduccion.Model.Registro.RegistroRedesSociales;
import com.example.pangeaappproduccion.Publicaciones;
import com.example.pangeaappproduccion.R;
import com.example.pangeaappproduccion.Util.UtilFragment;
import com.example.pangeaappproduccion.databinding.FragmentHomeBinding;
import com.example.pangeaappproduccion.Model.listPublicaciones;
import com.example.pangeaappproduccion.translate_api;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeFragment extends UtilFragment {
    HomeViewModel homeViewModel;
    FragmentHomeBinding binding;
    TextView traducidoTexto;

    private TextView etMensaje, userPerfil, PaisPerfil, CursoPerfil, nivelPerfil, nombreUsuario;
    private Button buttonChat, buttonImagen, buttonAudio, buttonReproAudio, buttonTraducir;
    private ImageView imgPerfil, imgHeader;
    private List<listPublicaciones> listPublicaciones;
    private AdapterPublicacion adapterPublicacion;
    private RecyclerView recyclerViewPublicaciones;
    private static final int IMG_Header = 0;
    private static final int GALLERY_PICKER = 1;
    private static final int AudioSend = 2;
    private static final int ACTION_POST = 3;
    Context context;
    private String imagenUsuario;
    String email;
    String id;
    String username;
    Uri urlAudio;
    boolean portada = false;

    StorageReference mStorage = FirebaseStorage.getInstance().getReference();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        establecerIdioma();
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        View view = inflater.inflate(R.layout.fragment_home, container, false);


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
        CursoPerfil = view.findViewById(R.id.CursoPerfil);
        nombreUsuario = view.findViewById(R.id.nombreUsuario);


        context = view.getContext();


        listPublicaciones = new ArrayList<>();
        adapterPublicacion = new AdapterPublicacion(listPublicaciones);
        recyclerViewPublicaciones.setAdapter(adapterPublicacion);
        recyclerViewPublicaciones.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewPublicaciones.setHasFixedSize(true);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            id = user.getUid();
            DocumentReference docRef = db.collection("users").document(id);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            username = document.get("userName").toString();
                            SharedPreferences.Editor editor = requireActivity().getSharedPreferences("usuarioRegistroNormal", MODE_PRIVATE).edit();
                            editor.putString("email", email);
                            editor.putString("firstName", document.get("firstName").toString());
                            editor.putString("lastName", document.get("lastName").toString());
                            editor.putString("userName", username);
                            RegistroRedesSociales interes = document.toObject(RegistroRedesSociales.class);
                            assert interes != null;
                            String language = interes.getLanguage().getGoalLearning();
                            editor.putString("languageInterest", language);
                            editor.putString("id", id);
                            nombreUsuario.setText(username);
                            FirebaseMessaging.getInstance().getToken()
                                    .addOnCompleteListener(task1 -> {
                                        String token = task1.getResult();
                                        editor.putString("tokenCFM", token);
                                        editor.apply();
                                        DocumentReference userRef = db.collection("users").document(id);
                                        userRef.update("tokenFCM", token)
                                                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                                                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
                                        FirebaseFirestore.getInstance().collection("redSocial").whereEqualTo("usuario", username).addSnapshotListener((value, error) -> {
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

                                    });

                        }
                    }
                }
            });
        }
        db.collection("fotoPerfil").whereEqualTo("usuario", id).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
                            Glide.with(context)
                                    .load(documentSnapshot.get("multimedia"))
                                    .apply(requestOptions)
                                    .apply(new RequestOptions().override(300, 300))
                                    .circleCrop()
                                    .into(imgPerfil);
                        }
                    }
                });

        db.collection("fotosHeader").document(id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                RequestOptions requestOptions = new RequestOptions();
                                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
                                Glide.with(context)
                                        .load(document.get("multimedia"))
                                        .apply(requestOptions)
                                        .into(imgHeader);
                                portada = true;
                            }
                        }
                    }
                });

        imgPerfil.setOnClickListener(view12 ->

        {
            Intent intentImagen = new Intent(Intent.ACTION_PICK);
            intentImagen.setType("image/*");
            startActivityForResult(intentImagen, GALLERY_PICKER);

        });

        imgHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentImagen = new Intent(Intent.ACTION_PICK);
                intentImagen.setType("image/*");
                startActivityForResult(intentImagen, IMG_Header);

            }
        });

        buttonImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentImagen = new Intent(Intent.ACTION_PICK);
                intentImagen.setType("image/*");
                startActivityForResult(intentImagen, ACTION_POST);

            }
        });

        buttonAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                startActivityForResult(intent, AudioSend);
            }
        });

        buttonReproAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), urlAudio);
                mediaPlayer.start();
            }
        });


        buttonChat.setOnClickListener(view1 ->

        {
            FirebaseFirestore dbDataPerfils = FirebaseFirestore.getInstance();
            DocumentReference publicacion = dbDataPerfils.collection("redSocial").document();
            String clave = publicacion.getId();
            String id = UUID.randomUUID().toString().toUpperCase();
            Publicaciones publicaciones = new Publicaciones();
            publicaciones.setMensaje(etMensaje.getText().toString());
            publicaciones.setUsuario(username);
            publicaciones.setId(id);
            publicaciones.setClave(clave);
            publicaciones.setStatus("0");
            FirebaseFirestore.getInstance().collection("redSocial").document(clave).set(publicaciones).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                            adapterPublicacion.notifyDataSetChanged();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
        });

        buttonTraducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translate_api translate = new translate_api();
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
                translate.execute(etMensaje.getText().toString(), "es", "en");
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
    public void onActivityResult(int requestCode, int resultCode,
                                 @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AudioSend && resultCode == RESULT_OK) {
            Toast.makeText(getContext(), "Subiendo audio, no cierre la app", Toast.LENGTH_LONG).show();
            urlAudio = data.getData();
            StorageReference filePath = mStorage.child("audios").child(urlAudio.getLastPathSegment());
            filePath.putFile(urlAudio).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uiriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uiriTask.isSuccessful()) ;
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
                    FirebaseFirestore.getInstance().collection("redSocial").document(clave).set(publicaciones).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    adapterPublicacion.notifyDataSetChanged();
                                    Toast.makeText(getContext(), "Audio subido", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });
                    etMensaje.setText("");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("FileManager", "Error en uploadImg ==>" + e);
                }
            });

            etMensaje.setText("");
        }

        if (requestCode == IMG_Header && resultCode == RESULT_OK) {
            Toast.makeText(getContext(), "Subiendo imagen, no cierre la app", Toast.LENGTH_LONG).show();
            if (portada) {
                Uri uri = data.getData();
                StorageReference filePath = mStorage.child("fotosHeader").child(uri.getLastPathSegment());
                filePath.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> uiriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uiriTask.isSuccessful()) ;
                    Uri dowloadUri = uiriTask.getResult();
                    Map<String, Object> fotoPerfil = new HashMap<>();
                    fotoPerfil.put("multimedia", dowloadUri.toString());
                    fotoPerfil.put("usuario", id);
                    FirebaseFirestore.getInstance().collection("fotosHeader").document(id).update(fotoPerfil).addOnSuccessListener(unused -> {
                                Toast.makeText(getContext(), "Imagen subida", Toast.LENGTH_LONG).show();
                                RequestOptions requestOptions = new RequestOptions();
                                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
                                Glide.with(context)
                                        .load(dowloadUri.toString())
                                        .apply(requestOptions)
                                        .into(imgHeader);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            });

                }).addOnFailureListener(e -> Log.e("FileManager", "Error en uploadImg ==>" + e));
            } else {
                Uri uri = data.getData();
                StorageReference filePath = mStorage.child("fotosHeader").child(uri.getLastPathSegment());
                filePath.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> uiriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uiriTask.isSuccessful()) ;
                    Uri dowloadUri = uiriTask.getResult();
                    FotoPerfil fotoPerfil = new FotoPerfil();
                    fotoPerfil.setMultimedia(dowloadUri.toString());
                    fotoPerfil.setUsuario(id);
                    FirebaseFirestore.getInstance().collection("fotosHeader").add(fotoPerfil).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
                            Glide.with(context)
                                    .load(dowloadUri.toString())
                                    .apply(requestOptions)
                                    .into(imgHeader);
                            Toast.makeText(getContext(), "Imagen subida", Toast.LENGTH_LONG).show();
                            portada = true;
                        }
                    });
                }).addOnFailureListener(e -> Log.e("FileManager", "Error en uploadImg ==>" + e));
            }


        }

        if (requestCode == GALLERY_PICKER && resultCode == RESULT_OK) {
            Toast.makeText(getContext(), "Subiendo imagen, no cierre la app", Toast.LENGTH_LONG).show();
            Uri uri = data.getData();
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
            Glide.with(context)
                    .load(uri)
                    .apply(requestOptions)
                    .circleCrop()
                    .into(imgPerfil);
            StorageReference filePath = mStorage.child("fotoPerfil").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> uiriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uiriTask.isSuccessful()) ;
                Uri dowloadUri = uiriTask.getResult();

                FirebaseFirestore db2 = FirebaseFirestore.getInstance();

                Map<String, Object> fotoPerfil = new HashMap<>();
                fotoPerfil.put("multimedia", dowloadUri.toString());
                fotoPerfil.put("usuario", id);
                db2.collection("fotoPerfil").document(id).update(fotoPerfil).addOnSuccessListener(unused ->
                                Toast.makeText(getContext(), "Imagen subida", Toast.LENGTH_LONG).show())
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        });
                etMensaje.setText("");

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("FileManager", "Error en uploadImg ==>" + e);
                }
            });
        }

        if (requestCode == ACTION_POST && resultCode == RESULT_OK) {
            Toast.makeText(getContext(), "Subiendo imagen, no cierre la app", Toast.LENGTH_LONG).show();
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
                    PublicacionesImagenes.setUsuario(username);
                    PublicacionesImagenes.setMensaje(etMensaje.getText().toString());
                    PublicacionesImagenes.setId(id);
                    PublicacionesImagenes.setClave(clave);
                    PublicacionesImagenes.setStatus("1");

                    FirebaseFirestore.getInstance().collection("redSocial").document(clave).set(PublicacionesImagenes)
                            .addOnSuccessListener(aVoid -> {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                adapterPublicacion.notifyDataSetChanged();
                                Toast.makeText(getContext(), "Imagen subida", Toast.LENGTH_LONG).show();
                            })
                            .addOnFailureListener(e -> {
                                Log.w(TAG, "Error writing document", e);
                                adapterPublicacion.notifyDataSetChanged();
                            });
                }
            }).addOnFailureListener(e -> Log.e("FileManager", "Error en uploadImg ==>" + e));
            etMensaje.setText("");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
