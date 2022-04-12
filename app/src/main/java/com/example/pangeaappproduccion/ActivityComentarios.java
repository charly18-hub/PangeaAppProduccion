package com.example.pangeaappproduccion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pangeaappproduccion.Adapters.AdapterComentarios;
import com.example.pangeaappproduccion.databinding.FragmentSlideshowBinding;
import com.example.pangeaappproduccion.ui.slideshow.SlideshowViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ActivityComentarios extends AppCompatActivity {


    TextView Usuario,Publicacion;
    Button Comentar;
    EditText CajaComentario;


    private List<com.example.pangeaappproduccion.listPublicaciones> listPublicaciones;
    private AdapterComentarios adapterComentarios;

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;
    private RecyclerView recyclerViewComentarios;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);


        SharedPreferences preferences = getApplicationContext().getSharedPreferences("accesos", Context.MODE_PRIVATE);
        String email_perfil = preferences.getString("email", "No name defined");


        String idPublicacion = getIntent().getExtras().getString("id");

        Usuario = (TextView) findViewById(R.id.UsuarioPublicacion2);
        Publicacion = (TextView) findViewById(R.id.publicacionForo);


        FirebaseFirestore dbDataUserPerfil = FirebaseFirestore.getInstance();
        dbDataUserPerfil.collection("users").whereEqualTo("email",email_perfil).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){


                                String usuarioEmisor = documentSnapshot.getString("usuario");

                                Toast.makeText(getApplicationContext(), "el usuario existe en la base" + usuarioEmisor, Toast.LENGTH_LONG).show();


                                SharedPreferences.Editor editor1 = getSharedPreferences("usuario_recibido_chat2", MODE_PRIVATE).edit();
                                editor1.putString("usuario_recibido_chat2", usuarioEmisor);
                                editor1.apply();
                            }
                        }
                    }
                });

        SharedPreferences preferencesusuario = getSharedPreferences("usuario_recibido_chat2", Context.MODE_PRIVATE);
        String usuario_pangea = preferencesusuario.getString("usuario_recibido_chat2", "No existe idioma");

        Toast.makeText(getApplicationContext(), "el usuario de pangea es" + usuario_pangea, Toast.LENGTH_LONG).show();


        FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
        dbDataPerfil.collection("redSocial").whereEqualTo("id", idPublicacion).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                Usuario.setText(documentSnapshot.getString("usuario"));
                                Publicacion.setText(documentSnapshot.getString("mensaje"));

                            }
                        }
                    }
                });

        recyclerViewComentarios = findViewById(R.id.recyclerPreguntas);


        listPublicaciones = new ArrayList<>();
        adapterComentarios = new AdapterComentarios(listPublicaciones);
        recyclerViewComentarios.setAdapter(adapterComentarios);
        recyclerViewComentarios.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewComentarios.setHasFixedSize(true);

        String clave = UUID.randomUUID().toString().toUpperCase();


        FirebaseFirestore.getInstance().collection("comentarios"+"/"+idPublicacion+"/"+"comentarios").addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d(TAG, "Error:" + error.getMessage());
                    Toast.makeText(getApplicationContext(), "Error" + error.getMessage(), Toast.LENGTH_LONG).show();

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


        CajaComentario=(EditText)findViewById(R.id.foroComentar);
        Comentar=(Button) findViewById(R.id.publicarPreguntaBtn2);

        Comentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(idPublicacion.length() == 0 || CajaComentario.length() == 0)
                    return;

                com.example.pangeaappproduccion.listPublicaciones listPublicaciones1 = new listPublicaciones();
                listPublicaciones1.setMensaje(CajaComentario.getText().toString());
                listPublicaciones1.setUsuario(usuario_pangea);
                FirebaseFirestore.getInstance().collection("comentarios"+"/"+idPublicacion+"/"+"comentarios").add(listPublicaciones1);
                CajaComentario.setText("");
            }
        });



    }
}