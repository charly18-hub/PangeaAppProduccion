package com.example.pangeaappproduccion;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pangeaappproduccion.Adapters.AdapterPreguntasEvaluaciones;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ActivityPreguntasEvaluacion extends AppCompatActivity {

    private List<com.example.pangeaappproduccion.Adapters.listEvaluaciones> listEvaluaciones;
    private AdapterPreguntasEvaluaciones adapterPreguntasEvaluaciones;

    private RecyclerView recyclerViewPreguntasEvaluaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas_evaluacion);

        String idPregunta = getIntent().getExtras().getString("id");

        recyclerViewPreguntasEvaluaciones = findViewById(R.id.recyclerPreguntasEvaluacion);


        Toast.makeText(getApplicationContext(), idPregunta, Toast.LENGTH_LONG).show();




        listEvaluaciones = new ArrayList<>();
        adapterPreguntasEvaluaciones = new AdapterPreguntasEvaluaciones(getApplicationContext(),listEvaluaciones);
        recyclerViewPreguntasEvaluaciones.setAdapter(adapterPreguntasEvaluaciones);
        recyclerViewPreguntasEvaluaciones.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewPreguntasEvaluaciones.setHasFixedSize(true);




        /*FirebaseFirestore dbDataPerfilType = FirebaseFirestore.getInstance();

        dbDataPerfilType.collection("Evaluaciones").document("TG5G3jev3T8pUXEuU8L8").collection("Evaluados").whereEqualTo("userName", "zeus1").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                                       String EvaluacionObtenida = documentSnapshot.getString("evaluation");


                                                       if (EvaluacionObtenida.equals("TRUE")) {


                                                           FirebaseFirestore.getInstance().collection("Evaluaciones").document("3nDweB5nuWBtfLnkm9xi").collection("Evaluacion2").whereEqualTo("idEvaluacion", idPregunta).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                               @Override
                                                               public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                                                                   if (error != null) {
                                                                       Log.d(TAG, "Error:" + error.getMessage());
                                                                   } else {
                                                                       for (DocumentChange documentChange : value.getDocumentChanges()) {
                                                                           if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                                                               listEvaluaciones.add(documentChange.getDocument().toObject(com.example.pangeaappproduccion.Adapters.listEvaluaciones.class));
                                                                               adapterPreguntasEvaluaciones.notifyDataSetChanged();
                                                                               recyclerViewPreguntasEvaluaciones.smoothScrollToPosition(listEvaluaciones.size());
                                                                           }
                                                                       }
                                                                   }
                                                               }
                                                           });
                                                       }

                                                       if (EvaluacionObtenida.equals("")){


                                                           AlertDialog alertDialog = new AlertDialog.Builder(ActivityPreguntasEvaluacion.this)
                                                                   //set icon
                                                                   .setIcon(android.R.drawable.ic_dialog_alert)
                                                                   //set title
                                                                   .setTitle("Evaluaciones")
                                                                   //set message
                                                                   .setMessage("¡Realiza la primera evaluacion!")

                                                                   //set negative button
                                                                   .setNegativeButton("Regresar Al Panel", new DialogInterface.OnClickListener() {
                                                                       @Override
                                                                       public void onClick(DialogInterface dialogInterface, int i) {
                                                                           //set what should happen when negative button is clicked
                                                                           Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                                                                       }
                                                                   })
                                                                   .show();

                                                       }
                                                   }
                                               }
                                           }
                                       });*/









            FirebaseFirestore.getInstance().collection("Evaluaciones").document("TG5G3jev3T8pUXEuU8L8").collection("Evaluacion1").whereEqualTo("idEvaluacion", idPregunta).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.d(TAG, "Error:" + error.getMessage());
                    } else {
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                listEvaluaciones.add(documentChange.getDocument().toObject(com.example.pangeaappproduccion.Adapters.listEvaluaciones.class));
                                adapterPreguntasEvaluaciones.notifyDataSetChanged();
                                recyclerViewPreguntasEvaluaciones.smoothScrollToPosition(listEvaluaciones.size());
                            }
                        }
                    }
                }
            });

        FirebaseFirestore.getInstance().collection("Evaluaciones").document("3nDweB5nuWBtfLnkm9xi").collection("Evaluacion2").whereEqualTo("idEvaluacion", idPregunta).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d(TAG, "Error:" + error.getMessage());
                } else {
                    for (DocumentChange documentChange : value.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            listEvaluaciones.add(documentChange.getDocument().toObject(com.example.pangeaappproduccion.Adapters.listEvaluaciones.class));
                            adapterPreguntasEvaluaciones.notifyDataSetChanged();
                            recyclerViewPreguntasEvaluaciones.smoothScrollToPosition(listEvaluaciones.size());
                        }
                    }
                }
            }
        });

    }
}