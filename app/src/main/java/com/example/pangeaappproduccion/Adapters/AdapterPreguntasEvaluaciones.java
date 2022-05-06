package com.example.pangeaappproduccion.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pangeaappproduccion.ActivityPreguntasEvaluacion;
import com.example.pangeaappproduccion.ActivityResponderEvaluacion;
import com.example.pangeaappproduccion.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterPreguntasEvaluaciones extends RecyclerView.Adapter<AdapterPreguntasEvaluaciones.PreguntasEvaluacionesHolder> {

    private List<com.example.pangeaappproduccion.Adapters.listEvaluaciones> listEvaluaciones;
    private Context context;

    public AdapterPreguntasEvaluaciones(Context context1,List<listEvaluaciones> listEvaluaciones)
    {

        this.context = context1;

        this.listEvaluaciones = listEvaluaciones;

    }


    @NonNull
    @NotNull
    @Override
    public AdapterPreguntasEvaluaciones.PreguntasEvaluacionesHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_evaluaciones,viewGroup,false);


        return new AdapterPreguntasEvaluaciones.PreguntasEvaluacionesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterPreguntasEvaluaciones.PreguntasEvaluacionesHolder preguntasEvaluacionesHolder, int i) {

        preguntasEvaluacionesHolder.publicacion2.setText(listEvaluaciones.get(i).getPregunta());



        preguntasEvaluacionesHolder.comentarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();


                dbDataPerfil.collection("Evaluaciones").document("TG5G3jev3T8pUXEuU8L8").collection("Evaluacion1").document(listEvaluaciones.get(i).getId()).get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {

                                DocumentSnapshot document = task.getResult();


                                String idEvaluacion = document.getString("idEvaluacion");

                                Toast.makeText(context.getApplicationContext(), idEvaluacion + "Evaluacion", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(context.getApplicationContext(), ActivityResponderEvaluacion.class);
                                intent.putExtra("id", document.getId());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);




                            }
                        });
            }
        });


    }


    @Override
    public int getItemCount() {
        return listEvaluaciones.size();


    }

    class  PreguntasEvaluacionesHolder extends  RecyclerView.ViewHolder{

        private TextView publicacion2;
        private Button comentarBtn;


        public PreguntasEvaluacionesHolder(@NonNull View itemView){
            super(itemView);

            publicacion2 = itemView.findViewById(R.id.pregunta);
            comentarBtn = itemView.findViewById(R.id.responder);


        }
    }

}
