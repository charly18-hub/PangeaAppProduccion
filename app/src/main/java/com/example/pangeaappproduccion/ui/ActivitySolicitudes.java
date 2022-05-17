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
import com.example.pangeaappproduccion.Lenguages;
import com.example.pangeaappproduccion.Model.Registro.Language;
import com.example.pangeaappproduccion.R;
import com.example.pangeaappproduccion.SolicitudesList;
import com.example.pangeaappproduccion.Util.UtilActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ActivitySolicitudes extends UtilActivity {



    private List<SolicitudesList> listSolicitudes;
    private AdapterSolicitudes adapterSolicitudes;
    private RecyclerView recyclerViewSolicitudes;
    private Button Amistad, Solicitudes;
    private String idiomaInteres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes);
        establecerIdioma();
        recyclerViewSolicitudes = findViewById(R.id.recyclerAmistad);
        listSolicitudes = new ArrayList<>();
        adapterSolicitudes = new AdapterSolicitudes(listSolicitudes, listSolicitudes -> {
            Intent intent = new Intent(getApplicationContext(), EnviarSolicitud.class);
            intent.putExtra("perfil_a_enviar", listSolicitudes.getUserName());
            startActivity(intent);
        });

        Solicitudes = findViewById(R.id.solicitudes);
        Amistad = findViewById(R.id.amistades);

        Solicitudes.setOnClickListener(view -> {
            Intent  intent = new Intent(getApplicationContext(),ActivitySolicitudesRegistradas.class);
            startActivity(intent);
        });


        Amistad.setOnClickListener(view -> {
            Intent  intent = new Intent(getApplicationContext(), ActivityAmistad.class);
            startActivity(intent);
        });

        recyclerViewSolicitudes.setAdapter(adapterSolicitudes);
        recyclerViewSolicitudes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewSolicitudes.setHasFixedSize(true);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("usuarioRegistroNormal", Context.MODE_PRIVATE);
        idiomaInteres = pref.getString("languageInterest", "");
        FirebaseFirestore.getInstance().collection("users").whereEqualTo("language.goalLearning",idiomaInteres).addSnapshotListener((value, error) -> {
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
        });


    }
}