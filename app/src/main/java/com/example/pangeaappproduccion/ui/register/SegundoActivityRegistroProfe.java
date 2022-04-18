package com.example.pangeaappproduccion.ui.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pangeaappproduccion.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SegundoActivityRegistroProfe extends AppCompatActivity {


    TextView textUser;
    EditText certificado, lenguaCertificada, expedidoPor, zonaHoraria, disponibilidad, cuentaBancaria;
    Button register;
    StorageReference mStorage = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segundo_registro_profe);


        String usuario_registrado = getIntent().getExtras().getString("user_name");
        String email_register_obtenido = getIntent().getExtras().getString("email_register");

        SharedPreferences.Editor editor = getSharedPreferences("usuario_recibido", MODE_PRIVATE).edit();
        editor.putString("usuario_register", usuario_registrado);
        editor.apply();

        SharedPreferences.Editor editor1 = getSharedPreferences("email_recibido", MODE_PRIVATE).edit();
        editor1.putString("email_register", email_register_obtenido);
        editor1.apply();



        textUser = (TextView) findViewById(R.id.textUserProfe);

        textUser.setText(usuario_registrado);
        certificado = (EditText)findViewById(R.id.editTextCertificado);
        lenguaCertificada = (EditText)findViewById(R.id.editTextLengua);
        expedidoPor = (EditText)findViewById(R.id.editTextExpedido);
        zonaHoraria = (EditText)findViewById(R.id.editTextZona);
        disponibilidad=(EditText)findViewById(R.id.editTextDisponibilidad);
        cuentaBancaria = (EditText)findViewById(R.id.editTextCuenta);





        register = (Button)findViewById(R.id.btnRegistrarProfe3);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                FirebaseFirestore db2 = FirebaseFirestore.getInstance();

                Map<String, Object> dateUpdate = new HashMap<>();
                dateUpdate.put("Certificado",certificado.getText().toString());
                dateUpdate.put("idioma_certificado",lenguaCertificada.getText().toString());
                dateUpdate.put("ExpedidoPor",expedidoPor.getText().toString());
                dateUpdate.put("zonaHoraria",zonaHoraria.getText().toString());
                dateUpdate.put("Disponibilidad",disponibilidad.getText().toString());
                dateUpdate.put("CuentaBancaria",cuentaBancaria.getText().toString());

                db2.collection("users").document(email_register_obtenido).update(dateUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(),"seActualizaron los datos",Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(),TercerActivityProfe.class);

                        intent.putExtra("usuario",usuario_registrado);
                        intent.putExtra("email_register",email_register_obtenido);

                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

                    }
                });


            }
        });

    }
}