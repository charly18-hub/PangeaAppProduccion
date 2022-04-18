package com.example.pangeaappproduccion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pangeaappproduccion.Listas.listTraducciones;

public class AdapterComentariosTraduccion extends RecyclerView.Adapter<AdapterComentariosTraduccion.ComentariosTraduccionHolder> {


    private List<com.example.pangeaappproduccion.Listas.listTraducciones> listTraducciones;
    private Context context;

    public AdapterComentariosTraduccion(List<listTraducciones> listTraducciones)
    {

        this.listTraducciones = listTraducciones;

    }


    @NonNull
    @NotNull
    @Override
    public AdapterComentariosTraduccion.ComentariosTraduccionHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comentario_traduccion,viewGroup,false);


        return new AdapterComentariosTraduccion.ComentariosTraduccionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterComentariosTraduccion.ComentariosTraduccionHolder comentariosTraduccionHolder, int i) {

        comentariosTraduccionHolder.nombreComentario.setText(listTraducciones.get(i).getUsuario());
        comentariosTraduccionHolder.publicacionComentario.setText(listTraducciones.get(i).getTraduccion());






    }



    @Override
    public int getItemCount() {
        return listTraducciones.size();


    }

    class  ComentariosTraduccionHolder extends  RecyclerView.ViewHolder{

        private TextView nombreComentario;
        private TextView publicacionComentario;


        public ComentariosTraduccionHolder(@NonNull View itemView){
            super(itemView);

            nombreComentario = itemView.findViewById(R.id.nombreComentarioTraduccion);
            publicacionComentario = itemView.findViewById(R.id.comentarioTraduccion);


        }
    }


}
