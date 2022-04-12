package com.example.pangeaappproduccion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage.ET;

public class RecuperarContrasena extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);


        EditText email;
        Button  actualizar;
        TextView mensaje;

        email = (EditText)findViewById(R.id.email_recuperar);

        mensaje =(TextView)findViewById(R.id.texto);


        actualizar = (Button)findViewById(R.id.actualizar);
        actualizar.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {

                                              FirebaseAuth.getInstance().setLanguageCode("en");
                                              FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(
                                                      new OnCompleteListener<Void>() {
                                                          @Override
                                                          public void onComplete(@NonNull @NotNull Task<Void> task) {

                                                              if(task.isSuccessful()){

                                                                  mensaje.setVisibility(View.VISIBLE);

                                                              }else {
                                                                  Toast.makeText(getApplicationContext(),"error", Toast.LENGTH_LONG).show();
                                                              }


                                                          }

                                                      }
                                              );

            }
        });


    }
}



