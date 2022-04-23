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

import com.example.pangeaappproduccion.Model.Comentario;
import com.example.pangeaappproduccion.R;
import com.example.pangeaappproduccion.Listas.listPublicaciones;

public class AdapterComentarios extends RecyclerView.Adapter<AdapterComentarios.ComentariosHolder> {

    private List<listPublicaciones> listPublicaciones;
    private List<Comentario> listPublicacionesMultimedia;
    private String multimedia;
    private Context context;

    public AdapterComentarios(List<listPublicaciones> listPublicaciones){
        this.listPublicaciones = listPublicaciones;
    }
    public AdapterComentarios(List<Comentario> listPublicacionesMultimedia,String multimedia){
        this.listPublicacionesMultimedia = listPublicacionesMultimedia;
        this.multimedia = multimedia;
    }

    @NotNull
    @Override
    public AdapterComentarios.ComentariosHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comentarios,viewGroup,false);
        return new AdapterComentarios.ComentariosHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterComentarios.ComentariosHolder comentariosHolder, int i) {

        if(multimedia!=null){
            if(multimedia.equals("audio")||multimedia.equals("imagen")){
                comentariosHolder.nombreComentario.setText(listPublicacionesMultimedia.get(i).getUsuario());
                comentariosHolder.publicacionComentario.setText(listPublicacionesMultimedia.get(i).getComentario());
            }
        }else{
            comentariosHolder.nombreComentario.setText(listPublicaciones.get(i).getUsuario());
            comentariosHolder.publicacionComentario.setText(listPublicaciones.get(i).getComentario());
        }
    }



    @Override
    public int getItemCount() {
        if(multimedia!=null){
            return listPublicacionesMultimedia.size();
        }else{
            return listPublicaciones.size();
        }
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