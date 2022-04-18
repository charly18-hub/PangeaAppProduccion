package com.example.pangeaappproduccion.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pangeaappproduccion.ActivityAmistad;
import com.example.pangeaappproduccion.Adapters.AdapterSolicitudes;
import com.example.pangeaappproduccion.EnviarSolicitud;
import com.example.pangeaappproduccion.R;
import com.example.pangeaappproduccion.SolicitudesList;
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

public class ActivitySolicitudes extends AppCompatActivity {



    private List<SolicitudesList> listSolicitudes;
    private AdapterSolicitudes adapterSolicitudes;
    private RecyclerView recyclerViewSolicitudes;
    private Button Amistad, Solicitudes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes);


        SharedPreferences preferences = getSharedPreferences("accesos", Context.MODE_PRIVATE);
        String email_perfil = preferences.getString("email", "No name defined");


        recyclerViewSolicitudes = findViewById(R.id.recyclerAmistad);
        listSolicitudes = new ArrayList<>();
        adapterSolicitudes = new AdapterSolicitudes(listSolicitudes, new AdapterSolicitudes.ItemClickListener() {
            @Override
            public void onItemClick(SolicitudesList listSolicitudes) {
                Toast.makeText(getApplicationContext(),listSolicitudes.getUsuario(),Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), EnviarSolicitud.class);
                intent.putExtra("perfil_a_enviar", listSolicitudes.getUsuario());
                startActivity(intent);



            }

        });
        recyclerViewSolicitudes.setAdapter(adapterSolicitudes);
        recyclerViewSolicitudes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewSolicitudes.setHasFixedSize(true);





        FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
        dbDataPerfil.collection("users").whereEqualTo("email",email_perfil).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){


                                String idiomaInteres = documentSnapshot.getString("idioma_interes");

                                Toast.makeText(getApplicationContext(),idiomaInteres+" lenguaje obtenido",Toast.LENGTH_LONG).show();

                                SharedPreferences.Editor editor = getSharedPreferences("idiomaInteres", MODE_PRIVATE).edit();
                                editor.putString("idioma_interes", idiomaInteres);
                                editor.apply();
                            }
                        }
                    }
                });


        SharedPreferences preferencesIdioma = getSharedPreferences("idiomaInteres", Context.MODE_PRIVATE);
        String idiomaObtenidoInteres = preferencesIdioma.getString("idioma_interes", "No existe idioma");

        Toast.makeText(getApplicationContext(),idiomaObtenidoInteres,Toast.LENGTH_LONG).show();


        FirebaseFirestore.getInstance().collection("users").whereEqualTo("idioma_interes",idiomaObtenidoInteres).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d(TAG, "Error:" + error.getMessage());
                } else {
                    for (DocumentChange documentChange : value.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {


                            listSolicitudes.add(documentChange.getDocument().toObject(SolicitudesList.class));
                            adapterSolicitudes.notifyDataSetChanged();
                            recyclerViewSolicitudes.smoothScrollToPosition(listSolicitudes.size());


                        }
                    }
                }
            }
        });

        Solicitudes = (Button)findViewById(R.id.solicitudes);

        Solicitudes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent = new Intent(getApplicationContext(),ActivitySolicitudesRegistradas.class);
                startActivity(intent);
            }
        });

        Amistad = (Button)findViewById(R.id.amistades);

        Amistad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent = new Intent(getApplicationContext(), ActivityAmistad.class);
                startActivity(intent);
            }
        });

    }
}