package com.example.pangeaappproduccion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.telecom.InCallService;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pangeaappproduccion.Util.Notifications.FCMSend;
import com.example.pangeaappproduccion.Util.UtilActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;

public class videoCall extends UtilActivity {
    private String tokenFCM;
    private String usuarioALlamar;
    private String usuarioActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);
/*
        try {
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL(""))
                     setRoom("test123")
                    .setAudioMuted(false)
                    .setVideoMuted(false)
                    .setAudioOnly(false)
                    .setWelcomePageEnabled(false)
                    .setConfigOverride("requireDisplayName", true)
                    .build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        establecerIdioma();

        int numero = ThreadLocalRandom.current().nextInt(1, 1000 + 1);
        //String NameConference = "Llamada"+numero+"2020";

        String NameConference = "pangea-test";
        Button jitsiMeet = findViewById(R.id.jitsiButton);
        EditText nameMeet = findViewById(R.id.conferenceName);
        nameMeet.setText(NameConference);




        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                jitsiMeet.performClick();
            }
        }, 500); // 1000 = 1 segundo


        jitsiMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(),"se hizo el click",Toast.LENGTH_LONG).show();
                String text = nameMeet.getText().toString();

                if(text.length()>0){
                    JitsiMeetConferenceOptions options
                            = new JitsiMeetConferenceOptions.Builder()
                            .setRoom(text)
                            .build();
                    JitsiMeetActivity.launch(videoCall.this,options);
                }

            }
        });


 */

        usuarioALlamar = getIntent().getExtras().getString("destinatario");
        usuarioActual = getIntent().getExtras().getString("usuario");


        FirebaseFirestore dbDataUserPerfilDestinatario = FirebaseFirestore.getInstance();
        dbDataUserPerfilDestinatario.collection("users").whereEqualTo("userName",usuarioALlamar).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            tokenFCM = documentSnapshot.getString("tokenFCM");
                            FCMSend.pushNotification(
                                    videoCall.this,
                                    tokenFCM,
                                    "Tienes una nueva videollamada",
                                    "El usuario "+usuarioActual+" te esta llamando"
                            );
                        }
                    }
                });


    }
}