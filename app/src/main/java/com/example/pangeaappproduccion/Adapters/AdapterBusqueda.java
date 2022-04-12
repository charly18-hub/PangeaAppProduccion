package com.example.pangeaappproduccion.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pangeaappproduccion.BuscadorList;
import com.example.pangeaappproduccion.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterBusqueda extends RecyclerView.Adapter<AdapterBusqueda.BusquedaHolder> {

    private List<BuscadorList> listBuscador;
    private Context context;
    private ItemClickListener itemClickListener;

    public AdapterBusqueda(List<BuscadorList> listBuscador, ItemClickListener itemClickListene1 )
    {
        this.listBuscador = listBuscador;
        this.itemClickListener = itemClickListene1;

    }


    @NonNull
    @NotNull
    @Override
    public AdapterBusqueda.BusquedaHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_buscador,viewGroup,false);


        return new AdapterBusqueda.BusquedaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterBusqueda.BusquedaHolder busquedaHolder, int i) {

        busquedaHolder.nombre2.setText(listBuscador.get(i).getUsuario());
        Glide.with(busquedaHolder.itemView.getContext()).load(listBuscador.get(i).getMultimedia()).into(busquedaHolder.imgPublicacion);
        busquedaHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(listBuscador.get(i));
            }
        });


    }



    @Override
    public int getItemCount() {
        return listBuscador.size();


    }


    public interface  ItemClickListener{

        void  onItemClick(BuscadorList buscadorList);

    }



    class  BusquedaHolder extends  RecyclerView.ViewHolder{

        private TextView nombre2;
        private ImageView imgPublicacion;


        public BusquedaHolder(@NonNull View itemView){
            super(itemView);

            nombre2 = itemView.findViewById(R.id.NombreUser);
            imgPublicacion = itemView.findViewById(R.id.BuscarPerfil);


        }
    }

}