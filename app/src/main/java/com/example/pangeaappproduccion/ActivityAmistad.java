package com.example.pangeaappproduccion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pangeaappproduccion.Adapters.AdapterAmistad;
import com.example.pangeaappproduccion.Util.UtilActivity;
import com.example.pangeaappproduccion.ui.PerfilAmigos.PerfilAmigosActivity;
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

public class ActivityAmistad extends UtilActivity {

    private List<SolicitudesList> listAmistad;
    private AdapterAmistad adapterAmistad;
    private RecyclerView recyclerViewAmistad;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amistad);

        SharedPreferences preferences = getSharedPreferences("accesos", Context.MODE_PRIVATE);
        String email_perfil = preferences.getString("email", "No name defined");



        establecerIdioma();
        recyclerViewAmistad = findViewById(R.id.recyclerAmistad);
        listAmistad = new ArrayList<>();
        adapterAmistad = new AdapterAmistad(listAmistad, new AdapterAmistad.ItemClickListener() {
            @Override
            public void onItemClick(SolicitudesList listSolicitudes) {
                Toast.makeText(getApplicationContext(),listSolicitudes.getUserName(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ActivityAmistad.this, PerfilAmigosActivity.class);
                Bundle b = new Bundle();
                b.putString("nombreUsuario", listSolicitudes.getUserName()); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }

        });
        recyclerViewAmistad.setAdapter(adapterAmistad);
        recyclerViewAmistad.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewAmistad.setHasFixedSize(true);



        FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
        dbDataPerfil.collection("users").whereEqualTo("emailAddress",email_perfil).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                String usuario = documentSnapshot.getString("uid");
                                SharedPreferences.Editor editor = getSharedPreferences("usuarioObtenido", MODE_PRIVATE).edit();
                                editor.putString("usuarioObtenido", usuario);
                                editor.apply();
                            }
                        }
                    }
                });


        SharedPreferences preferencesIdioma = getSharedPreferences("usuarioObtenido", Context.MODE_PRIVATE);
        String usuarioObtenido = preferencesIdioma.getString("usuarioObtenido", "No existe idioma");
        Toast.makeText(getApplicationContext(),usuarioObtenido+" usuario obtenido22",Toast.LENGTH_LONG).show();



        FirebaseFirestore.getInstance().collection("users").document(usuarioObtenido).collection("Solicitudes").whereEqualTo("estatus","aceptada").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d(TAG, "Error:" + error.getMessage());
                } else {
                    for (DocumentChange documentChange : value.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            listAmistad.add(documentChange.getDocument().toObject(SolicitudesList.class));
                            adapterAmistad.notifyDataSetChanged();
                            recyclerViewAmistad.smoothScrollToPosition(listAmistad.size());

                        }
                    }
                }
            }
        });
    }
}