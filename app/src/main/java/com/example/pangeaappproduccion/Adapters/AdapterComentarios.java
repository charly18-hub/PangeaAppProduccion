package com.example.pangeaappproduccion.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pangeaappproduccion.R;
import com.example.pangeaappproduccion.Listas.listPublicaciones;

public class AdapterComentarios extends RecyclerView.Adapter<AdapterComentarios.ComentariosHolder> {

    private List<com.example.pangeaappproduccion.Listas.listPublicaciones> listPublicaciones;
    private Context context;

    public AdapterComentarios(List<listPublicaciones> listPublicaciones)
    {

        this.listPublicaciones = listPublicaciones;

    }


    @NonNull
    @NotNull
    @Override
    public AdapterComentarios.ComentariosHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comentarios,viewGroup,false);


        return new AdapterComentarios.ComentariosHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterComentarios.ComentariosHolder comentariosHolder, int i) {

        comentariosHolder.nombreComentario.setText(listPublicaciones.get(i).getUsuario());
        comentariosHolder.publicacionComentario.setText(listPublicaciones.get(i).getMensaje());






    }



    @Override
    public int getItemCount() {
        return listPublicaciones.size();


    }

    class  ComentariosHolder extends  RecyclerView.ViewHolder{

        private TextView nombreComentario;
        private TextView publicacionComentario;


        public ComentariosHolder(@NonNull View itemView){
            super(itemView);

            nombreComentario = itemView.findViewById(R.id.nombreComentario);
            publicacionComentario = itemView.findViewById(R.id.comentario);


        }
    }

}