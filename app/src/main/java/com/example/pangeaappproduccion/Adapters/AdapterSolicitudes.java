package com.example.pangeaappproduccion.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pangeaappproduccion.R;
import com.example.pangeaappproduccion.SolicitudesList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterSolicitudes extends RecyclerView.Adapter<AdapterSolicitudes.SolicitudesHolder> {

    private List<SolicitudesList> listSolicitudes;

    private Context context;
    private AdapterSolicitudes.ItemClickListener itemClickListener;




    public AdapterSolicitudes(List<SolicitudesList> listSolicitudes, AdapterSolicitudes.ItemClickListener itemClickListene1 )
    {
        this.listSolicitudes = listSolicitudes;
        this.itemClickListener = itemClickListene1;
        // this.context = context1;

    }


    @NonNull
    @NotNull
    @Override
    public AdapterSolicitudes.SolicitudesHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_solicitudes,viewGroup,false);


        return new AdapterSolicitudes.SolicitudesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterSolicitudes.SolicitudesHolder solicitudesHolder, int i) {

        Context context1 = solicitudesHolder.itemView.getContext();

        solicitudesHolder.nombre2.setText(listSolicitudes.get(i).getUserName());
        Glide.with(solicitudesHolder.itemView.getContext()).load(listSolicitudes.get(i).getProfilePicture()).into(solicitudesHolder.imgPublicacion);
        solicitudesHolder.itemView.setOnClickListener(new View.OnClickListener() {
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


    public interface  ItemClickListener{

        void  onItemClick(SolicitudesList listSolicitudes);

    }



    class  SolicitudesHolder extends  RecyclerView.ViewHolder{

        private TextView nombre2;
        private ImageView imgPublicacion;
        private Button enviarSolicitud;


        public SolicitudesHolder(@NonNull View itemView){
            super(itemView);

            nombre2 = itemView.findViewById(R.id.nombreSolicitud);
            imgPublicacion = itemView.findViewById(R.id.fotoPerfil);
            enviarSolicitud = itemView.findViewById(R.id.enviarSolicitud);


        }
    }

}