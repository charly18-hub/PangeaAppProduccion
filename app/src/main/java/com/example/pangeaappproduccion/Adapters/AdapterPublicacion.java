package com.example.pangeaappproduccion.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pangeaappproduccion.R;
import com.example.pangeaappproduccion.Listas.listPublicaciones;
import com.example.pangeaappproduccion.ui.ActivityComentarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterPublicacion extends RecyclerView.Adapter<AdapterPublicacion.PublicacionHolder> {

    private List<com.example.pangeaappproduccion.Listas.listPublicaciones> listPublicaciones;
    private Context context;

    public AdapterPublicacion(List<listPublicaciones> listPublicaciones) {
        this.listPublicaciones = listPublicaciones;

    }


    @NonNull
    @NotNull
    @Override
    public PublicacionHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_publicaciones, viewGroup, false);
        return new PublicacionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PublicacionHolder publicacionHolder, int i) {

        publicacionHolder.nombre2.setText(listPublicaciones.get(i).getUsuario());
        publicacionHolder.publicacion2.setText(listPublicaciones.get(i).getMensaje());
        if (listPublicaciones.get(i).getStatus().equals("0")) {
            publicacionHolder.imgPublicacion.setVisibility(View.GONE);
            publicacionHolder.btnComentario.setOnClickListener(view -> {
                FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
                dbDataPerfil.collection("redSocial").document(listPublicaciones.get(i).getClave()).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Intent intent = new Intent(publicacionHolder.imgPublicacion.getContext().getApplicationContext(), ActivityComentarios.class);

                                        intent.putExtra("clave", document.getId());

                                        publicacionHolder.imgPublicacion.getContext().startActivity(intent);
                                    }
                                }
                            }
                        });
            });
        } else if (listPublicaciones.get(i).getStatus().equals("1")) {
            Glide.with(publicacionHolder.itemView.getContext()).load(listPublicaciones.get(i).getMultimedia()).into(publicacionHolder.imgPublicacion);
            publicacionHolder.btnComentario.setOnClickListener(view -> {
                FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
                dbDataPerfil.collection("redSocial").document(listPublicaciones.get(i).getClave()).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Intent intent = new Intent(publicacionHolder.imgPublicacion.getContext().getApplicationContext(), ActivityComentarios.class);

                                        intent.putExtra("clave", document.getId());

                                        intent.putExtra("multimedia", 1);
                                        publicacionHolder.imgPublicacion.getContext().startActivity(intent);
                                    }
                                }
                            }
                        });
            });
        } else if (listPublicaciones.get(i).getStatus().equals("2")) {
            publicacionHolder.audioLayout.setVisibility(View.VISIBLE);
            publicacionHolder.publicacionLayout.setVisibility(View.GONE);
            publicacionHolder.imgPublicacion.setImageResource(R.drawable.audio);
            Glide.with(publicacionHolder.itemView.getContext())
                    .load("https://www.nicepng.com/png/detail/7-75606_play-button-png-image-instagram.png")
                    .into(publicacionHolder.imgPublicacion);
            publicacionHolder.reproducir.setOnClickListener(view -> {
                MediaPlayer mediaPlayer;
                Uri myUri = Uri.parse(listPublicaciones.get(i).getMultimedia());
                mediaPlayer = new MediaPlayer();
                try {
                    // mediaPlayer.setDataSource(String.valueOf(myUri));
                    mediaPlayer.setDataSource(publicacionHolder.imgPublicacion.getContext(), myUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
            });
            publicacionHolder.btnComentario.setOnClickListener(view -> {
                FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
                dbDataPerfil.collection("redSocial").document(listPublicaciones.get(i).getClave()).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Intent intent = new Intent(publicacionHolder.imgPublicacion.getContext().getApplicationContext(), ActivityComentarios.class);

                                        intent.putExtra("clave", document.getId());

                                        intent.putExtra("multimedia", 2);
                                        publicacionHolder.imgPublicacion.getContext().startActivity(intent);
                                    }
                                }
                            }
                        });
            });
        }
    }


    @Override
    public int getItemCount() {
        return listPublicaciones.size();


    }

    class PublicacionHolder extends RecyclerView.ViewHolder {

        private TextView nombre2;
        private TextView publicacion2;
        private ImageView imgPublicacion;
        private LinearLayout audioLayout, publicacionLayout;
        private Button reproducir;
        private Button btnComentario;


        public PublicacionHolder(@NonNull View itemView) {
            super(itemView);

            nombre2 = itemView.findViewById(R.id.usuarioForo);
            publicacion2 = itemView.findViewById(R.id.pregunta);
            imgPublicacion = itemView.findViewById(R.id.imgPublicacion);
            audioLayout = itemView.findViewById(R.id.audioLayout);
            publicacionLayout = itemView.findViewById(R.id.publicacionLayout);
            reproducir = itemView.findViewById(R.id.button77);
            btnComentario = itemView.findViewById(R.id.btnComentario);


        }
    }

}
