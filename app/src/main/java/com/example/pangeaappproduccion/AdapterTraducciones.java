package com.example.pangeaappproduccion;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pangeaappproduccion.Model.listTraducciones;
import com.example.pangeaappproduccion.ui.ActivityComentariosTraduccion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterTraducciones extends RecyclerView.Adapter<AdapterTraducciones.TraduccionesHolder> {

    private List<com.example.pangeaappproduccion.Model.listTraducciones> listTraducciones;
    private Context context;

    public AdapterTraducciones(Context context2, List<listTraducciones> listTraducciones)
    {
        this.listTraducciones = listTraducciones;
        this.context = context2;

    }


    @NonNull
    @NotNull
    @Override
    public AdapterTraducciones.TraduccionesHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_traducciones,viewGroup,false);


        return new AdapterTraducciones.TraduccionesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterTraducciones.TraduccionesHolder traduccionesHolder, int i) {

        traduccionesHolder.nombre2.setText(listTraducciones.get(i).getUsuario());
        traduccionesHolder.publicacion2.setText(listTraducciones.get(i).getTraduccion());


        traduccionesHolder.comentarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
                dbDataPerfil.collection("traducciones").whereEqualTo("traduccion", listTraducciones.get(i).getTraduccion()).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                        Toast.makeText(context.getApplicationContext(), documentSnapshot.getString("id"), Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(context.getApplicationContext(), ActivityComentariosTraduccion.class);
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
        return listTraducciones.size();


    }

    class  TraduccionesHolder extends  RecyclerView.ViewHolder{

        private TextView nombre2;
        private TextView publicacion2;
        private Button comentarBtn;


        public TraduccionesHolder(@NonNull View itemView){
            super(itemView);

            nombre2 = itemView.findViewById(R.id.usuarioForo);
            publicacion2 = itemView.findViewById(R.id.pregunta);
            comentarBtn = itemView.findViewById(R.id.comentarTraduccion);


        }
    }

}
