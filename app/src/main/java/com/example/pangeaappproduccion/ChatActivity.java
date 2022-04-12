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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ChatActivity extends AppCompatActivity {

    private TextView etReceptor;
    private EditText mensaje;
    private ImageButton Enviar;
    private List<MensajeChatPersonal> listMensajesPersonal;
    private AdapterChatPersonal adapterChatPersonal;
    private RecyclerView recyclerViewChatPersonal;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        String destinatario_recibido_chat = getIntent().getExtras().getString("destinaraio");
        String usuario_recibido_chat = getIntent().getExtras().getString("usuario");


        FirebaseFirestore dbDataUserPerfil = FirebaseFirestore.getInstance();
        dbDataUserPerfil.collection("users").whereEqualTo("email",usuario_recibido_chat).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){


                                String usuarioEmisor = documentSnapshot.getString("usuario");
                                Toast.makeText(getApplicationContext(),"obtenido"+usuarioEmisor, Toast.LENGTH_LONG).show();


                                SharedPreferences.Editor editor1 = getSharedPreferences("usuario_recibido_chat1", MODE_PRIVATE).edit();
                                editor1.putString("usuario_recibido_chat1", usuarioEmisor);
                                editor1.apply();
                            }
                        }
                    }
                });




        SharedPreferences.Editor editor = getSharedPreferences("destinatario_recibido_chat", MODE_PRIVATE).edit();
        editor.putString("destinatario_recibido_chat", destinatario_recibido_chat);
        editor.apply();

        SharedPreferences preferencesusuario = getSharedPreferences("usuario_recibido_chat1", Context.MODE_PRIVATE);
        String usuario_recibido_chat1 = preferencesusuario.getString("usuario_recibido_chat1", "No existe idioma");

        Toast.makeText(getApplicationContext(),"guardado"+usuario_recibido_chat1, Toast.LENGTH_LONG).show();



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

                MensajeChat mensajeChat = new MensajeChat();
                mensajeChat.setMessage(mensaje.getText().toString());
                mensajeChat.setName(usuario_recibido_chat1);
                FirebaseFirestore.getInstance().collection("chat"+"/"+destinatario_recibido_chat+"/"+usuario_recibido_chat1 ).add(mensajeChat);

                SendChat();

            }
        });



        listMensajesPersonal = new ArrayList<>();
        adapterChatPersonal = new AdapterChatPersonal(listMensajesPersonal);
        recyclerViewChatPersonal.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        recyclerViewChatPersonal.setAdapter(adapterChatPersonal);
        recyclerViewChatPersonal.setHasFixedSize(true);


        FirebaseFirestore.getInstance().collection("chat"+"/"+usuario_recibido_chat1+"/"+destinatario_recibido_chat).addSnapshotListener(new EventListener<QuerySnapshot>() {

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


        FirebaseFirestore.getInstance().collection("chat"+"/"+destinatario_recibido_chat+"/"+usuario_recibido_chat1).addSnapshotListener(new EventListener<QuerySnapshot>() {

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

        SharedPreferences preferencesDestinatario = getSharedPreferences("destinatario_recibido_chat", Context.MODE_PRIVATE);
        String destinatario_recibido_chat = preferencesDestinatario.getString("destinatario_recibido_chat", "no existe destino");

        SharedPreferences preferencesusuario = getSharedPreferences("usuario_recibido_chat1", Context.MODE_PRIVATE);
        String usuario_recibido_chat = preferencesusuario.getString("usuario_recibido_chat1", "No existe emisor");



        MensajeChat mensajeChat = new MensajeChat();
        mensajeChat.setMessage(mensaje.getText().toString());
        mensajeChat.setName(destinatario_recibido_chat);
        FirebaseFirestore.getInstance().collection("chat"+"/"+usuario_recibido_chat+"/"+destinatario_recibido_chat).add(mensajeChat);


    }
}