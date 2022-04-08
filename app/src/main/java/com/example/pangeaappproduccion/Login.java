package com.example.pangeaappproduccion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;


public class Login extends AppCompatActivity {

    EditText editTextTextPersonName,editTextTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        editTextTextPersonName=(EditText)findViewById(R.id.editTextTextPersonName);
        editTextTextPassword = (EditText)findViewById(R.id.editTextTextPassword);



        Button login = (Button)findViewById(R.id.buttonLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                db2.collection("users").whereEqualTo("email",editTextTextPersonName.getText().toString()).whereEqualTo("password",editTextTextPassword.getText().toString()).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                                if(task.isSuccessful()){
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){

                                        SharedPreferences.Editor editor = getSharedPreferences("accesos", MODE_PRIVATE).edit();
                                        editor.putString("email", editTextTextPersonName.getText().toString());
                                        editor.putString("password", editTextTextPersonName.getText().toString());

                                        editor.apply();

                                        Intent intent = new Intent(Login.this,MainActivity.class);
                                        startActivity(intent);

                                    }
                                }

                                if(task.isCanceled()){

                                    Toast.makeText(getApplicationContext(),"Verifique Usuario y Contraseña",Toast.LENGTH_LONG).show();

                                }

                            }

                        });



            }
        });


        Button registro = (Button)findViewById(R.id.buttonRegistro);
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Registro.class);
                startActivity(intent);

            }
        });
    }
}