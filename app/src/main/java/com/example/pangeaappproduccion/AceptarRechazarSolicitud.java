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

public class AceptarRechazarSolicitud extends AppCompatActivity {
    TextView nombreSolicitud;
    Context context;
    ImageView fotoPerfil;
    Button aceptarSolicitud,rechazarSolicitud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aceptar_rechazar_solicitud);


        SharedPreferences preferences = getSharedPreferences("accesos", Context.MODE_PRIVATE);
        String email_perfil = preferences.getString("email", "No name defined");


        String solicitud_enviar = getIntent().getExtras().getString("perfil_a_enviar");
        Toast.makeText(getApplicationContext(), solicitud_enviar, Toast.LENGTH_LONG).show();

        SharedPreferences.Editor editor = getSharedPreferences("recibe_solicitud", MODE_PRIVATE).edit();
        editor.putString("recibe_solicitud", solicitud_enviar);
        editor.apply();



        fotoPerfil = (ImageView) findViewById(R.id.fotoPerfil);


        FirebaseFirestore dbDataPerfil1 = FirebaseFirestore.getInstance();
        dbDataPerfil1.collection("users").whereEqualTo("email",email_perfil).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){


                                String user = documentSnapshot.getString("user");

                                SharedPreferences.Editor editor = getSharedPreferences("user_solicitud", MODE_PRIVATE).edit();
                                editor.putString("user_solicitud", user);
                                editor.apply();
                            }
                        }
                    }
                });

        SharedPreferences preferencesUser = getSharedPreferences("user_solicitud", Context.MODE_PRIVATE);
        String userObtenido = preferencesUser.getString("user_solicitud", "No existe idioma");


        FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
        dbDataPerfil.collection("users").whereEqualTo("usuario",solicitud_enviar).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){


                                String email = documentSnapshot.getString("email");

                                Toast.makeText(getApplicationContext(),email+" email obtenido",Toast.LENGTH_LONG).show();

                                SharedPreferences.Editor editor = getSharedPreferences("email", MODE_PRIVATE).edit();
                                editor.putString("email", email);
                                editor.apply();
                            }
                        }
                    }
                });


        SharedPreferences preferencesIdioma = getSharedPreferences("email", Context.MODE_PRIVATE);
        String emailObtenido = preferencesIdioma.getString("email", "No existe idioma");




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


        aceptarSolicitud = (Button) findViewById(R.id.aceptarSolicitud);

        aceptarSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences preferencesUserRecibe = getSharedPreferences("recibe_solicitud", Context.MODE_PRIVATE);
                String userRecibe = preferencesUserRecibe.getString("recibe_solicitud", "No existe quien recibe");

                SharedPreferences preferencesUser = getSharedPreferences("user_solicitud", Context.MODE_PRIVATE);
                String userEnvia = preferencesUser.getString("user_solicitud", "No existe quien envia");


                Toast.makeText(getApplicationContext(),"solicitud Aceptada",Toast.LENGTH_LONG).show();

                SolicitudesList solicitudesList = new SolicitudesList();
                solicitudesList.setEstatus("acepata");
                solicitudesList.setUsuario(userEnvia);
                solicitudesList.getUsuario();


                FirebaseFirestore.getInstance().collection("solicitudes" + "/" + solicitud_enviar + "/" + "aceptadas" ).document(userEnvia).set(solicitudesList);
                FirebaseFirestore.getInstance().collection("solicitudes" + "/" +solicitud_enviar + "/" + "solicitudes").document(userEnvia).delete();


                AceptedSolicitud();

            }
        });

        rechazarSolicitud = (Button) findViewById(R.id.rechazarSolicitud);

        rechazarSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences preferencesUserRecibe = getSharedPreferences("recibe_solicitud", Context.MODE_PRIVATE);
                String userRecibe = preferencesUserRecibe.getString("recibe_solicitud", "No existe quien recibe");

                SharedPreferences preferencesUser = getSharedPreferences("user_solicitud", Context.MODE_PRIVATE);
                String userEnvia = preferencesUser.getString("user_solicitud", "No existe quien envia");


                Toast.makeText(getApplicationContext(),"solicitud rechazadas",Toast.LENGTH_LONG).show();

                SolicitudesList solicitudesList = new SolicitudesList();
                solicitudesList.setEstatus("rechazadas");
                solicitudesList.setUsuario(userEnvia);
                solicitudesList.getUsuario();


                FirebaseFirestore.getInstance().collection("solicitudes" + "/" + solicitud_enviar + "/" + "rechazadas" ).document(userEnvia).set(solicitudesList);
                FirebaseFirestore.getInstance().collection("solicitudes" + "/" +solicitud_enviar + "/" + "solicitudes").document(userEnvia).delete();


                rechasedSolicitud();
            }
        });
    }



    public void rechasedSolicitud(){




        SharedPreferences preferencesUserRecibe = getSharedPreferences("recibe_solicitud", Context.MODE_PRIVATE);
        String userRecibe = preferencesUserRecibe.getString("recibe_solicitud", "No existe quien recibe");


        SharedPreferences preferencesUser = getSharedPreferences("user_solicitud", Context.MODE_PRIVATE);
        String userEnvia = preferencesUser.getString("user_solicitud", "No existe quien envia");


        SolicitudesList solicitudesList2 = new SolicitudesList();
        solicitudesList2.setEstatus("rechazada");
        solicitudesList2.setUsuario(userRecibe);
        solicitudesList2.getUsuario();

        FirebaseFirestore.getInstance().collection("solicitudes" + "/" + userEnvia + "/" + "rechazadas").document(userRecibe).set(solicitudesList2);
        FirebaseFirestore.getInstance().collection("solicitudes" + "/" +userEnvia + "/" + "solicitudes").document(userRecibe).delete();


    }


    public void AceptedSolicitud(){



        SharedPreferences preferencesUserRecibe = getSharedPreferences("recibe_solicitud", Context.MODE_PRIVATE);
        String userRecibe = preferencesUserRecibe.getString("recibe_solicitud", "No existe quien recibe");


        SharedPreferences preferencesUser = getSharedPreferences("user_solicitud", Context.MODE_PRIVATE);
        String userEnvia = preferencesUser.getString("user_solicitud", "No existe quien envia");


        SolicitudesList solicitudesList2 = new SolicitudesList();
        solicitudesList2.setEstatus("aceptada");
        solicitudesList2.setUsuario(userRecibe);
        solicitudesList2.getUsuario();

        FirebaseFirestore.getInstance().collection("solicitudes" + "/" + userEnvia + "/" + "aceptadas").document(userRecibe).set(solicitudesList2);
        FirebaseFirestore.getInstance().collection("solicitudes" + "/" +userEnvia + "/" + "solicitudes").document(userRecibe).delete();


    }
}