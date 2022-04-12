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
import com.example.pangeaappproduccion.listForo;

public class AdapterComentarioForo extends RecyclerView.Adapter<AdapterComentarioForo.ComentariosForoHolder> {

    private List<com.example.pangeaappproduccion.listForo> listForo;
    private Context context;

    public AdapterComentarioForo(List<listForo> listForo)
    {

        this.listForo = listForo;

    }


    @NonNull
    @NotNull
    @Override
    public AdapterComentarioForo.ComentariosForoHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comentario_foro,viewGroup,false);


        return new AdapterComentarioForo.ComentariosForoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterComentarioForo.ComentariosForoHolder comentariosForoHolder, int i) {

        comentariosForoHolder.nombreComentario.setText(listForo.get(i).getUsuario());
        comentariosForoHolder.publicacionComentario.setText(listForo.get(i).getMensaje());






    }



    @Override
    public int getItemCount() {
        return listForo.size();


    }

    class  ComentariosForoHolder extends  RecyclerView.ViewHolder{

        private TextView nombreComentario;
        private TextView publicacionComentario;


        public ComentariosForoHolder(@NonNull View itemView){
            super(itemView);

            nombreComentario = itemView.findViewById(R.id.nombreComentario);
            publicacionComentario = itemView.findViewById(R.id.comentario);


        }
    }

}