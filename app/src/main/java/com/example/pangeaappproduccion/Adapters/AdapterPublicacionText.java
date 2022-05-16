package com.example.pangeaappproduccion.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pangeaappproduccion.ui.ActivityComentarios;
import com.example.pangeaappproduccion.R;
import com.example.pangeaappproduccion.Model.listPublicaciones;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterPublicacionText extends RecyclerView.Adapter<AdapterPublicacionText.PublicacionHolder> {

    private List<com.example.pangeaappproduccion.Model.listPublicaciones> listPublicaciones;
    private Context context;

    public AdapterPublicacionText(Context context1, List<listPublicaciones> listPublicaciones) {
        this.context = context1;
        this.listPublicaciones = listPublicaciones;

    }


    @NotNull
    @Override
    public AdapterPublicacionText.PublicacionHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_publicaciones_text, viewGroup, false);
        return new AdapterPublicacionText.PublicacionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterPublicacionText.PublicacionHolder publicacionHolder, int i) {

        publicacionHolder.nombre2.setText(listPublicaciones.get(i).getUsuario());
        publicacionHolder.publicacion2.setText(listPublicaciones.get(i).getMensaje());
        publicacionHolder.comentarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
                dbDataPerfil.collection("redSocial").document(listPublicaciones.get(i).getClave()).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Intent intent = new Intent(context.getApplicationContext(), ActivityComentarios.class);
                                        intent.putExtra("clave", document.getId());
                                        context.startActivity(intent);
                                    }
                                }
                            }
                        });
            }
        });


    }


    @Override
    public int getItemCount() {
        return listPublicaciones.size();


    }

    class PublicacionHolder extends RecyclerView.ViewHolder {

        private TextView nombre2;
        private TextView publicacion2;
        private Button comentarBtn;


        public PublicacionHolder(@NonNull View itemView) {
            super(itemView);

            nombre2 = itemView.findViewById(R.id.usuarioForo);
            publicacion2 = itemView.findViewById(R.id.pregunta);
            comentarBtn = itemView.findViewById(R.id.comentar);


        }
    }

}
