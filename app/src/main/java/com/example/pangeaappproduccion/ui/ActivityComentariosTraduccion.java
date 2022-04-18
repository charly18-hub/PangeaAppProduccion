package com.example.pangeaappproduccion.ui;

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

import com.example.pangeaappproduccion.AdapterComentariosTraduccion;
import com.example.pangeaappproduccion.Listas.listTraducciones;
import com.example.pangeaappproduccion.R;
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

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ActivityComentariosTraduccion extends AppCompatActivity {

    TextView Usuario,Pregunta;
    Button ComentarPregunta;
    EditText CajaComentarioPregunta;


    private List<com.example.pangeaappproduccion.Listas.listTraducciones> listTraducciones;
    private AdapterComentariosTraduccion adapterComentariosTraduccion;

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;
    private RecyclerView recyclerViewComentarioTraduccion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios_traduccion);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("accesos", Context.MODE_PRIVATE);
        String email_perfil = preferences.getString("email", "No name defined");



        String idPregunta = getIntent().getExtras().getString("id");
        Toast.makeText(getApplicationContext(), "llego a activityComentarios" + idPregunta, Toast.LENGTH_LONG).show();

        Usuario = (TextView) findViewById(R.id.UsuarioPublicacionTraduccion);
        Pregunta = (TextView) findViewById(R.id.publicacionTraduccion);



        FirebaseFirestore dbDataUserPerfil = FirebaseFirestore.getInstance();
        dbDataUserPerfil.collection("users").whereEqualTo("email",email_perfil).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){


                                String usuarioEmisor = documentSnapshot.getString("usuario");


                                SharedPreferences.Editor editor1 = getSharedPreferences("usuario_recibido_chat1", MODE_PRIVATE).edit();
                                editor1.putString("usuario_recibido_chat1", usuarioEmisor);
                                editor1.apply();
                            }
                        }
                    }
                });

        SharedPreferences preferencesusuario = getSharedPreferences("usuario_recibido_chat1", Context.MODE_PRIVATE);
        String usuario_foro = preferencesusuario.getString("usuario_recibido_chat1", "No existe idioma");



        FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
        dbDataPerfil.collection("traducciones").whereEqualTo("id", idPregunta).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                Usuario.setText(documentSnapshot.getString("usuario"));
                                Pregunta.setText(documentSnapshot.getString("traduccion"));

                            }
                        }
                    }
                });

        recyclerViewComentarioTraduccion = findViewById(R.id.recyclerTraducciones);


        listTraducciones = new ArrayList<>();
        adapterComentariosTraduccion = new AdapterComentariosTraduccion(listTraducciones);
        recyclerViewComentarioTraduccion.setAdapter(adapterComentariosTraduccion);
        recyclerViewComentarioTraduccion.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewComentarioTraduccion.setHasFixedSize(true);


        FirebaseFirestore.getInstance().collection("traducciones"+"/"+idPregunta+"/"+"comentario").addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d(TAG, "Error:" + error.getMessage());
                    Toast.makeText(getApplicationContext(), "Error" + error.getMessage(), Toast.LENGTH_LONG).show();

                } else {

                    for (DocumentChange documentChange : value.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            listTraducciones.add(documentChange.getDocument().toObject(listTraducciones.class));
                            adapterComentariosTraduccion.notifyDataSetChanged();
                            recyclerViewComentarioTraduccion.smoothScrollToPosition(listTraducciones.size());
                        }
                    }
                }
            }
        });


        CajaComentarioPregunta=(EditText)findViewById(R.id.traduccionComentar);
        ComentarPregunta=(Button) findViewById(R.id.publicarTraduccion);

        ComentarPregunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                com.example.pangeaappproduccion.Listas.listTraducciones listTraducciones = new listTraducciones();
                listTraducciones.setTraduccion(CajaComentarioPregunta.getText().toString());
                listTraducciones.setUsuario(usuario_foro);
                FirebaseFirestore.getInstance().collection("traducciones"+"/"+idPregunta+"/"+"comentario").add(listTraducciones);
                CajaComentarioPregunta.setText("");
            }
        });




    }
}