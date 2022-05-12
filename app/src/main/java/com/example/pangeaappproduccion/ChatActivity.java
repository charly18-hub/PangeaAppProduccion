package com.example.pangeaappproduccion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pangeaappproduccion.Adapters.AdapterChatPersonal;
import com.example.pangeaappproduccion.Listas.listPublicaciones;
import com.example.pangeaappproduccion.Util.Notifications.FCMSend;
import com.example.pangeaappproduccion.Util.UtilActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ChatActivity extends UtilActivity {

    private TextView etReceptor;
    private EditText mensaje;
    private ImageButton Enviar;
    private List<MensajeChatPersonal> listMensajesPersonal;
    private AdapterChatPersonal adapterChatPersonal;
    private RecyclerView recyclerViewChatPersonal;
    private String tokenFCM;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        establecerIdioma();
        String destinatario_recibido_chat = getIntent().getExtras().getString("destinatario");

        String usuario_recibido_chat = getIntent().getExtras().getString("usuario");


        FirebaseFirestore dbDataUserPerfilDestinatario = FirebaseFirestore.getInstance();
        dbDataUserPerfilDestinatario.collection("users").whereEqualTo("userName",destinatario_recibido_chat).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){


                                String usuarioDestinatario = documentSnapshot.getString("uid");
                                tokenFCM = documentSnapshot.getString("tokenFCM");


                                SharedPreferences.Editor editor2 = getSharedPreferences("usuario_Destinatario_id", MODE_PRIVATE).edit();
                                editor2.putString("usuario_Destinatario_id", usuarioDestinatario);
                                editor2.apply();
                            }
                        }
                    }
                });


        FirebaseFirestore dbDataUserPerfil = FirebaseFirestore.getInstance();
        dbDataUserPerfil.collection("users").whereEqualTo("emailAddress",usuario_recibido_chat).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){


                                String usuarioEmisor = documentSnapshot.getString("uid");


                                SharedPreferences.Editor editor1 = getSharedPreferences("usuario_recibido_chat1", MODE_PRIVATE).edit();
                                editor1.putString("usuario_recibido_chat1", usuarioEmisor);
                                editor1.apply();
                            }
                        }
                    }
                });




        SharedPreferences.Editor editor = getSharedPreferences("usuario_Destinatario", MODE_PRIVATE).edit();
        editor.putString("usuario_Destinatario", destinatario_recibido_chat);
        editor.apply();

        SharedPreferences preferencesusuario = getSharedPreferences("usuario_recibido_chat1", Context.MODE_PRIVATE);
        String usuario_recibido_chat1 = preferencesusuario.getString("usuario_recibido_chat1", "No existe usuario arriba");




        SharedPreferences preferencesusuarioDestino = getSharedPreferences("usuario_Destinatario_id", Context.MODE_PRIVATE);
        String usuario_Destino = preferencesusuarioDestino.getString("usuario_Destinatario_id", "No existe emisor arriba");


        etReceptor = (TextView)findViewById(R.id.receptor);
        recyclerViewChatPersonal = findViewById(R.id.reciclerChatPersonal);

        etReceptor.setText(destinatario_recibido_chat);
        mensaje=(EditText)findViewById(R.id.mensajePersonal);
        Enviar=(ImageButton)findViewById(R.id.buttonPersonal);

        Enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usuario_recibido_chat.length() == 0 || mensaje.length() == 0)
                    return;

                TimeZone timeZone = TimeZone.getTimeZone("America/Mexico_City");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                simpleDateFormat.setTimeZone(timeZone);
                String time = simpleDateFormat.format(new Date());


                MensajeChat mensajeChat = new MensajeChat();
                mensajeChat.setMessage(mensaje.getText().toString());
                mensajeChat.setIdUserEnvio(usuario_Destino);
                mensajeChat.setIsActive("true");
                mensajeChat.setDateUtc(time);
                mensajeChat.setType("received");

                FirebaseFirestore.getInstance().collection("users"+"/"+usuario_recibido_chat1+"/"+"chats"+"/"+usuario_Destino+"/"+"Messages" ).add(mensajeChat).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        DocumentReference docRef = dbDataUserPerfil.collection("users").document(destinatario_recibido_chat);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task1) {
                                if (task1.isSuccessful()) {
                                    FCMSend.pushNotification(
                                            ChatActivity.this,
                                            tokenFCM,
                                            "Nuevo Mensaje",
                                            mensaje.getText().toString()
                                    );
                                } else {
                                    Log.d(TAG, "get failed with ", task1.getException());
                                }
                            }
                        });
                    }
                });

               // FirebaseFirestore.getInstance().collection("users"+"/"+usuario_Destino+"/"+"chats"+"/"+usuario_recibido_chat1+"/"+"Messages" ).add(mensajeChat);

                SendChat();

            }
        });



        listMensajesPersonal = new ArrayList<>();
        adapterChatPersonal = new AdapterChatPersonal(listMensajesPersonal);
        recyclerViewChatPersonal.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        recyclerViewChatPersonal.setAdapter(adapterChatPersonal);
        recyclerViewChatPersonal.setHasFixedSize(true);


        FirebaseFirestore.getInstance().collection("users"+"/"+usuario_recibido_chat1+"/"+"chats"+"/"+usuario_Destino+"/"+"Messages").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d(TAG, "Error:" + error.getMessage());
                    Toast.makeText(getApplicationContext(), "Error"+ error.getMessage(), Toast.LENGTH_LONG).show();

                } else {

                    for (DocumentChange documentChange : value.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            listMensajesPersonal.add(documentChange.getDocument().toObject(MensajeChatPersonal.class));
                            adapterChatPersonal.notifyDataSetChanged();
                            recyclerViewChatPersonal.smoothScrollToPosition(listMensajesPersonal.size());
                        }
                    }
                }
            }
        });



    }

    public void SendChat(){

        SharedPreferences preferencesDestinatario = getSharedPreferences("usuario_Destinatario_id", Context.MODE_PRIVATE);
        String destinatario_recibido_chat = preferencesDestinatario.getString("usuario_Destinatario_id", "no existe destino");


        SharedPreferences preferencesusuario = getSharedPreferences("usuario_recibido_chat1", Context.MODE_PRIVATE);
        String usuario_recibido_chat = preferencesusuario.getString("usuario_recibido_chat1", "No existe emisor");



        FirebaseFirestore dbDataUserPerfil = FirebaseFirestore.getInstance();
        dbDataUserPerfil.collection("users").whereEqualTo("uid",usuario_recibido_chat).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){


                                String usuarioEmisor1 = documentSnapshot.getString("userName");

                                TimeZone timeZone = TimeZone.getTimeZone("America/Mexico_City");
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                                simpleDateFormat.setTimeZone(timeZone);
                                String time = simpleDateFormat.format(new Date());


                                MensajeChat mensajeChat = new MensajeChat();
                                mensajeChat.setMessage(mensaje.getText().toString());
                                mensajeChat.setIdUserEnvio(usuario_recibido_chat);
                                mensajeChat.setIsActive("true");
                                mensajeChat.setDateUtc(time);
                                mensajeChat.setType("received");
                                FirebaseFirestore.getInstance().collection("users"+"/"+destinatario_recibido_chat+"/"+"chats"+"/"+usuario_recibido_chat+"/"+"Messages").add(mensajeChat)
                                        .addOnCompleteListener(task1 -> {


                                        });

                             //   FirebaseFirestore.getInstance().collection("users"+"/"+usuario_recibido_chat+"/"+"chats"+"/"+destinatario_recibido_chat+"/"+"Messages").add(mensajeChat);

                            }
                        }
                    }
                });


                // Uid del usuario al que se le envia el msj
                // destinatario_recibido_chat




    }
}