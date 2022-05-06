package com.example.pangeaappproduccion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;


public class EnviarSolicitud extends AppCompatActivity {

    TextView nombreSolicitud;
    Context context;
    ImageView fotoPerfil;
    Button sendSolicitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_solicitud);

        SharedPreferences preferences = getSharedPreferences("accesos", Context.MODE_PRIVATE);
        String email_perfil = preferences.getString("email", "No name defined");



        FirebaseFirestore dbDataPerfil1 = FirebaseFirestore.getInstance();
        dbDataPerfil1.collection("users").whereEqualTo("email",email_perfil).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){


                                String user = documentSnapshot.getString("user");


                                SharedPreferences.Editor editor = getSharedPreferences("user_referente", MODE_PRIVATE).edit();
                                editor.putString("user_referente", user);
                                editor.apply();



                            }
                        }
                    }
                });







        String solicitud_enviar = getIntent().getExtras().getString("perfil_a_enviar");
        Toast.makeText(getApplicationContext(), solicitud_enviar, Toast.LENGTH_LONG).show();

        fotoPerfil = (ImageView) findViewById(R.id.fotoPerfil);



        FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
        dbDataPerfil.collection("users").whereEqualTo("usuario",solicitud_enviar).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){


                                String email = documentSnapshot.getString("email");


                                SharedPreferences.Editor editor = getSharedPreferences("email", MODE_PRIVATE).edit();
                                editor.putString("email", email);
                                editor.apply();
                            }
                        }
                    }
                });


        SharedPreferences preferencesIdioma = getSharedPreferences("email", Context.MODE_PRIVATE);
        String emailObtenido = preferencesIdioma.getString("email", "No existe idioma");

        Toast.makeText(getApplicationContext(),emailObtenido+" email obtenido",Toast.LENGTH_LONG).show();




        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("fotoPerfil").whereEqualTo("usuario", emailObtenido).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {


                                Log.d("imagen", documentSnapshot.getId() + "la imagen es" + documentSnapshot.get("multimedia"));
                                RequestOptions requestOptions = new RequestOptions();
                                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));

                                Glide.with(getApplicationContext())
                                        .load(documentSnapshot.get("multimedia"))
                                        .apply(requestOptions)
                                        .into(fotoPerfil);

                            }
                        }
                    }
                });

        nombreSolicitud = (TextView) findViewById(R.id.nombreSolicitud);
        nombreSolicitud.setText(solicitud_enviar);


        SharedPreferences preferences1 = getSharedPreferences("user_referente", MODE_PRIVATE);
        String usuario_post_final = preferences1.getString("user_referente", "No name defined");





        sendSolicitud = (Button) findViewById(R.id.enviarSolicitud);

        sendSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SolicitudesList solicitudesList = new SolicitudesList();
                solicitudesList.setEstatus("enviada");
                solicitudesList.setUserName(usuario_post_final);
                solicitudesList.getUserName();


                FirebaseFirestore.getInstance().collection("solicitudes" + "/" + solicitud_enviar + "/" + "solicitudes" ).document(usuario_post_final).set(solicitudesList);

                sendSolicitudBidireccional();


            }
        });


    }

    public void sendSolicitudBidireccional(){

        String solicitud_enviar = getIntent().getExtras().getString("perfil_a_enviar");
        Toast.makeText(getApplicationContext(), solicitud_enviar, Toast.LENGTH_LONG).show();

        SharedPreferences preferences1 = getSharedPreferences("user_referente", MODE_PRIVATE);
        String usuario_post_final = preferences1.getString("user_referente", "No name defined");


        SolicitudesList solicitudesList2 = new SolicitudesList();
        solicitudesList2.setEstatus("enviada");
        solicitudesList2.setUserName(solicitud_enviar);
        solicitudesList2.getUserName();

        FirebaseFirestore.getInstance().collection("solicitudes" + "/" + usuario_post_final + "/" + "solicitudes").document(solicitud_enviar).set(solicitudesList2);


    }
}