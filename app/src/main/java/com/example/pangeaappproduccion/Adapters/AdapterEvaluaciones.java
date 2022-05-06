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
import com.example.pangeaappproduccion.Adapters.AdapterPublicacionForo;
import com.example.pangeaappproduccion.Listas.listForo;
import com.example.pangeaappproduccion.R;
import com.example.pangeaappproduccion.ui.ActivityComentarios;
import com.example.pangeaappproduccion.ui.ActivityComentariosForo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterEvaluaciones  extends RecyclerView.Adapter<AdapterEvaluaciones.EvaluacionesHolder> {

    private List <com.example.pangeaappproduccion.Adapters.listEvaluaciones> listEvaluaciones;
    private Context context;

    public AdapterEvaluaciones(Context context1,List<listEvaluaciones> listEvaluaciones)
    {

        this.context = context1;

        this.listEvaluaciones = listEvaluaciones;

    }


    @NonNull
    @NotNull
    @Override
    public AdapterEvaluaciones.EvaluacionesHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_evaluaciones,viewGroup,false);


        return new AdapterEvaluaciones.EvaluacionesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterEvaluaciones.EvaluacionesHolder evaluacionesHolder, int i) {

        evaluacionesHolder.publicacion2.setText(listEvaluaciones.get(i).getId());



        evaluacionesHolder.comentarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
                dbDataPerfil.collection("Evaluaciones").document(listEvaluaciones.get(i).getId()).get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {

                                DocumentSnapshot document = task.getResult();


                                Intent intent = new Intent(context.getApplicationContext(), ActivityPreguntasEvaluacion.class);
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

    class  EvaluacionesHolder extends  RecyclerView.ViewHolder{

        private TextView publicacion2;
        private Button comentarBtn;


        public EvaluacionesHolder(@NonNull View itemView){
            super(itemView);

            publicacion2 = itemView.findViewById(R.id.pregunta);
            comentarBtn = itemView.findViewById(R.id.responder);


        }
    }

}
