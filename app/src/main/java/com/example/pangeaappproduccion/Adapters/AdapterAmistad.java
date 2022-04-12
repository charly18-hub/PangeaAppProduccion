package com.example.pangeaappproduccion.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pangeaappproduccion.R;
import com.example.pangeaappproduccion.SolicitudesList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterAmistad  extends RecyclerView.Adapter<AdapterAmistad.AmistadHolder> {

    private List<SolicitudesList> listSolicitudes;

    private Context context;
    private AdapterAmistad.ItemClickListener itemClickListener;

    public AdapterAmistad(List<SolicitudesList> listSolicitudes, AdapterAmistad.ItemClickListener itemClickListene1) {
        this.listSolicitudes = listSolicitudes;
        this.itemClickListener = itemClickListene1;

    }


    @NonNull
    @NotNull
    @Override
    public AdapterAmistad.AmistadHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_amistades, viewGroup, false);


        return new AdapterAmistad.AmistadHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterAmistad.AmistadHolder amistadHolder, int i) {

        amistadHolder.nombre2.setText(listSolicitudes.get(i).getUsuario());
        Glide.with(amistadHolder.itemView.getContext()).load(listSolicitudes.get(i).getMultimedia()).into(amistadHolder.imgPublicacion);
        amistadHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(listSolicitudes.get(i));
            }
        });



    }


    @Override
    public int getItemCount() {
        return listSolicitudes.size();


    }


    public interface ItemClickListener {

        void onItemClick(SolicitudesList listSolicitudes);

    }


    class AmistadHolder extends RecyclerView.ViewHolder {

        private TextView nombre2;
        private ImageView imgPublicacion;


        public AmistadHolder(@NonNull View itemView) {
            super(itemView);

            nombre2 = itemView.findViewById(R.id.nombreSolicitud);
            imgPublicacion = itemView.findViewById(R.id.fotoPerfil);


        }
    }

}
