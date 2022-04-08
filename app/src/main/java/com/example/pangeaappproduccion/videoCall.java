package com.example.pangeaappproduccion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;

public class videoCall extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);

        try {
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL(""))
                    /*.setRoom("test123")
                    .setAudioMuted(false)
                    .setVideoMuted(false)
                    .setAudioOnly(false)*/
                    .setWelcomePageEnabled(false)
                    .setConfigOverride("requireDisplayName", true)
                    .build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


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


    }
}