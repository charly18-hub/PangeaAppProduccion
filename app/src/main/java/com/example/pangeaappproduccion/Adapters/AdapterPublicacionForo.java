package com.example.pangeaappproduccion.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pangeaappproduccion.ui.ActivityComentariosForo;
import com.example.pangeaappproduccion.R;
import com.example.pangeaappproduccion.Listas.listForo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterPublicacionForo  extends RecyclerView.Adapter<AdapterPublicacionForo.ForoHolder> {

    private List<com.example.pangeaappproduccion.Listas.listForo> listForo;
    private Context context;

    public AdapterPublicacionForo(Context context1,List<listForo> listForo)
    {

        this.context = context1;

        this.listForo = listForo;

    }


    @NonNull
    @NotNull
    @Override
    public AdapterPublicacionForo.ForoHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_publicaciones_text,viewGroup,false);


        return new AdapterPublicacionForo.ForoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterPublicacionForo.ForoHolder foroHolder, int i) {

        foroHolder.nombre2.setText(listForo.get(i).getUsuario());
        foroHolder.publicacion2.setText(listForo.get(i).getMensaje());



        foroHolder.comentarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
                dbDataPerfil.collection("foro").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){

                                        Intent intent = new Intent(context.getApplicationContext(), ActivityComentariosForo.class);
                                        intent.putExtra("id", documentSnapshot.getString("id"));

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
        return listForo.size();


    }

    class  ForoHolder extends  RecyclerView.ViewHolder{

        private TextView nombre2;
        private TextView publicacion2;
        private Button comentarBtn;


        public ForoHolder(@NonNull View itemView){
            super(itemView);

            nombre2 = itemView.findViewById(R.id.usuarioForo);
            publicacion2 = itemView.findViewById(R.id.pregunta);
            comentarBtn = itemView.findViewById(R.id.comentar);


        }
    }

}
