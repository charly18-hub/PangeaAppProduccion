package com.example.pangeaappproduccion.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pangeaappproduccion.Adapters.AdapterComentarios;
import com.example.pangeaappproduccion.Model.listPublicaciones;
import com.example.pangeaappproduccion.Model.Comentario;
import com.example.pangeaappproduccion.R;
import com.example.pangeaappproduccion.Util.UtilActivity;
import com.example.pangeaappproduccion.ui.slideshow.SlideshowViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ActivityComentarios extends UtilActivity {


    TextView Usuario,Publicacion;
    Button Comentar;
    EditText CajaComentario;
    String urlFotoPerfil;
    String email;
    String id;
    String username;


    private List<listPublicaciones> listPublicaciones;
    private List<Comentario> listPublicacionesMultimedia;
    private AdapterComentarios adapterComentarios;

    private SlideshowViewModel slideshowViewModel;
    private RecyclerView recyclerViewComentarios;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);


        establecerIdioma();
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("accesos", Context.MODE_PRIVATE);
        String email_perfil = preferences.getString("email", "No name defined");
        String idPublicacion = getIntent().getExtras().getString("clave","");
        int multimedia = getIntent().getExtras().getInt("multimedia",0);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("usuarioRegistroNormal", Context.MODE_PRIVATE);
        email = pref.getString("email", "No name defined");
        id = pref.getString("id", "No name defined");
        username = pref.getString("userName", "No name defined");


        Usuario = findViewById(R.id.UsuarioPublicacion2);
        Publicacion = findViewById(R.id.publicacionForo);

    //obtenemos el usuario activo (no id ni key) con una busqueda de su correo
        FirebaseFirestore dbDataUserPerfil = FirebaseFirestore.getInstance();
        dbDataUserPerfil.collection("users").whereEqualTo("email",email_perfil).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                String usuarioEmisor = documentSnapshot.getString("user");
                                SharedPreferences.Editor editor1 = getSharedPreferences("usuario_recibido_chat2", MODE_PRIVATE).edit();
                                editor1.putString("usuario_recibido_chat2", usuarioEmisor);
                                editor1.apply();
                            }
                        }
                    }
                });


        //obtenemos la url de la foto del que esta comentando
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("fotoPerfil").whereEqualTo("usuario", id).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            urlFotoPerfil = documentSnapshot.get("multimedia").toString();
                        }
                    }
                });

        //ahora con el id, buscamos la publicacion
        FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
        dbDataPerfil.collection("redSocial").document(idPublicacion).get()
                .addOnCompleteListener((OnCompleteListener<DocumentSnapshot>) task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Usuario.setText(document.getString("usuario"));
                            Publicacion.setText(document.getString("mensaje"));
                        } else {
                        }
                    }
                });

        recyclerViewComentarios = findViewById(R.id.recyclerPreguntas);
        CajaComentario=(EditText)findViewById(R.id.foroComentar);
        Comentar=(Button) findViewById(R.id.publicarPreguntaBtn2);
        if(multimedia==1 || multimedia==2){
            listPublicacionesMultimedia = new ArrayList<>();
            adapterComentarios = new AdapterComentarios(listPublicacionesMultimedia,"audio");
            recyclerViewComentarios.setAdapter(adapterComentarios);
            recyclerViewComentarios.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerViewComentarios.setHasFixedSize(true);
            String haber="comentarios"+"/"+idPublicacion+"/"+"comentarios";
            haber=haber+"";
            //obtenemos los comentarios de la publicacion
            FirebaseFirestore.getInstance().collection("comentarios"+"/"+idPublicacion+"/"+"comentarios").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.d(TAG, "Error:" + error.getMessage());

                    } else {
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                listPublicacionesMultimedia.add(documentChange.getDocument().toObject(Comentario.class));
                                adapterComentarios.notifyDataSetChanged();
                                recyclerViewComentarios.smoothScrollToPosition(listPublicacionesMultimedia.size());
                            }
                        }
                    }
                }
            });

            Comentar.setOnClickListener(view -> {
                if(idPublicacion.length() == 0 || CajaComentario.length() == 0)
                    return;
                Date currentTime = Calendar.getInstance().getTime();
                String clave = UUID.randomUUID().toString().toUpperCase();
                Comentario listPublicaciones1 = new Comentario();
                listPublicaciones1.setFecha(currentTime.toString());
                listPublicaciones1.setId(clave);
                listPublicaciones1.setComentario(CajaComentario.getText().toString());
                listPublicaciones1.setMultimedia(urlFotoPerfil);
                listPublicaciones1.setUsuario(username);
                FirebaseFirestore.getInstance().collection("comentarios"+"/"+idPublicacion+"/"+"comentarios").add(listPublicaciones1);
                CajaComentario.setText("");
            });
        }else{
            listPublicaciones = new ArrayList<>();
            adapterComentarios = new AdapterComentarios(listPublicaciones);
            recyclerViewComentarios.setAdapter(adapterComentarios);
            recyclerViewComentarios.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerViewComentarios.setHasFixedSize(true);
            String haber="redSocial"+"/"+idPublicacion+"/"+"comentarios";
            haber=haber+"";
            //obtenemos los comentarios de la publicacion
            FirebaseFirestore.getInstance().collection("redSocial"+"/"+idPublicacion+"/"+"comentarios").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.d(TAG, "Error:" + error.getMessage());

                    } else {
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                listPublicaciones.add(documentChange.getDocument().toObject(listPublicaciones.class));
                                adapterComentarios.notifyDataSetChanged();
                                recyclerViewComentarios.smoothScrollToPosition(listPublicaciones.size());
                            }
                        }
                    }
                }
            });
            Comentar.setOnClickListener(view -> {
                if(idPublicacion.length() == 0 || CajaComentario.length() == 0)
                    return;
                String clave = dbDataPerfil.collection("redSocial").document().getId();
                Date currentTime = Calendar.getInstance().getTime();
                String id = UUID.randomUUID().toString().toUpperCase();
                Comentario listPublicaciones1 = new Comentario();
                listPublicaciones1.setFecha(currentTime.toString());
                listPublicaciones1.setId(id);
                listPublicaciones1.setClave(clave);
                listPublicaciones1.setComentario(CajaComentario.getText().toString());
                listPublicaciones1.setMultimedia(urlFotoPerfil);
                listPublicaciones1.setUsuario(username);
                FirebaseFirestore.getInstance().collection("redSocial"+"/"+idPublicacion+"/"+"comentarios").add(listPublicaciones1);
                CajaComentario.setText("");
            });
        }




    }
}