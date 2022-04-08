package com.example.pangeaappproduccion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterPublicacion extends RecyclerView.Adapter<AdapterPublicacion.PublicacionHolder> {

    private List<listPublicaciones> listPublicaciones;
    private Context context;

    public AdapterPublicacion(List<listPublicaciones> listPublicaciones)
    {
        this.listPublicaciones = listPublicaciones;

    }


    @NonNull
    @NotNull
    @Override
    public PublicacionHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_publicaciones,viewGroup,false);


        return new PublicacionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PublicacionHolder publicacionHolder, int i) {

        publicacionHolder.nombre2.setText(listPublicaciones.get(i).getUsuario());
        publicacionHolder.publicacion2.setText(listPublicaciones.get(i).getMensaje());
        Glide.with(publicacionHolder.itemView.getContext()).load(listPublicaciones.get(i).getMultimedia()).into(publicacionHolder.imgPublicacion);



    }



    @Override
    public int getItemCount() {
        return listPublicaciones.size();


    }

    class  PublicacionHolder extends  RecyclerView.ViewHolder{

        private TextView nombre2;
        private TextView publicacion2;
        private ImageView imgPublicacion;


        public PublicacionHolder(@NonNull View itemView){
            super(itemView);

            nombre2 = itemView.findViewById(R.id.usuarioForo);
            publicacion2 = itemView.findViewById(R.id.pregunta);
            imgPublicacion = itemView.findViewById(R.id.imgPublicacion);


        }
    }

}
