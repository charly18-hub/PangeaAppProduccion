package com.example.pangeaappproduccion.ui.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pangeaappproduccion.MainActivity;
import com.example.pangeaappproduccion.Model.Registro.Accounts;
import com.example.pangeaappproduccion.Model.Registro.ImagenPerfil;
import com.example.pangeaappproduccion.Model.Registro.Interests;
import com.example.pangeaappproduccion.Model.Registro.Language;
import com.example.pangeaappproduccion.Model.Registro.RegistroRedesSociales;
import com.example.pangeaappproduccion.Profesores;
import com.example.pangeaappproduccion.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Registro extends AppCompatActivity {


    TextView profesores;
    Button RegistrarAlumno;
    EditText edTextPrimerNombre, pass, edTextSegundoNombre, edTextPaterno, edTextMaterno, edtMotivosAprendizaje, editTextIntereses, editTextEmail, edtCiudad, edtTelefono;
    RadioGroup radioGroupSex;
    RadioButton masculino, femenino, otro, noDecir;
    Spinner idiomaInteres, idiomaNativo, spinnerMotivosAprendizaje;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.idiomas_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter_motivos = ArrayAdapter.createFromResource(this,
                R.array.motivos_rray, android.R.layout.simple_spinner_item);
        profesores = (TextView) findViewById(R.id.profesores);
        profesores.setOnClickListener(view -> {
            Intent intent = new Intent(Registro.this, Profesores.class);
            startActivity(intent);
        });
        pass = findViewById(R.id.editTextPassword);
        edTextPrimerNombre = findViewById(R.id.edTextPrimerNombreProfe);
        edTextSegundoNombre = findViewById(R.id.edTextSegundoNombreProfe);
        editTextIntereses = findViewById(R.id.editTextIntereses);
        editTextEmail = findViewById(R.id.editTextEmail);
        edTextMaterno = findViewById(R.id.edTextMaternoProfe);
        edTextPaterno = findViewById(R.id.edTextPaternoProfe);
        //edtMotivosAprendizaje =findViewById(R.id.edtMotivosAprendizaje);
        masculino = findViewById(R.id.radioHomre);
        femenino = findViewById(R.id.radioMujer);
        otro = findViewById(R.id.radioOtro);
        noDecir = findViewById(R.id.sinDato);
        idiomaInteres = findViewById(R.id.idiomaInteres);
        idiomaNativo = findViewById(R.id.spinnerIdiomas);
        spinnerMotivosAprendizaje = findViewById(R.id.spinnerMotivosAprendizaje);
        edtCiudad = findViewById(R.id.edtCiudad);
        edtTelefono = findViewById(R.id.edtTelefono);

        idiomaNativo.setAdapter(adapter);
        idiomaNativo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences.Editor editor = getSharedPreferences("idiomaNativo", MODE_PRIVATE).edit();
                editor.putString("idiomaNativo", (String) adapterView.getItemAtPosition(i));
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerMotivosAprendizaje.setAdapter(adapter_motivos);
        spinnerMotivosAprendizaje.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                SharedPreferences.Editor editor = getSharedPreferences("motivo_aprender", MODE_PRIVATE).edit();
                editor.putString("motivo", (String) adapterView.getItemAtPosition(i));
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        idiomaInteres.setAdapter(adapter);
        idiomaInteres.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                SharedPreferences.Editor editor = getSharedPreferences("idiomaInteres", MODE_PRIVATE).edit();
                editor.putString("idiomaInteres", (String) adapterView.getItemAtPosition(i));
                editor.apply();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        radioGroupSex = (RadioGroup) findViewById(R.id.rg_sex);
        radioGroupSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (masculino.isChecked() == true) {

                    String valorButton = "Hombre";

                    SharedPreferences.Editor editor = getSharedPreferences("sexo", MODE_PRIVATE).edit();
                    editor.putString("sexo", valorButton);
                    editor.apply();

                }
                if (femenino.isChecked() == true) {

                    String valorButton = "Mujer";

                    SharedPreferences.Editor editor = getSharedPreferences("sexo", MODE_PRIVATE).edit();
                    editor.putString("sexo", valorButton);
                    editor.apply();

                }
                if (otro.isChecked() == true) {

                    String valorButton = "Otro";

                    SharedPreferences.Editor editor = getSharedPreferences("sexo", MODE_PRIVATE).edit();
                    editor.putString("sexo", valorButton);
                    editor.apply();

                }
                if (noDecir.isChecked() == true) {

                    String valorButton = "Sin Dato";

                    SharedPreferences.Editor editor = getSharedPreferences("sexo", MODE_PRIVATE).edit();
                    editor.putString("sexo", valorButton);
                    editor.apply();

                }
            }
        });


        RegistrarAlumno = (Button) findViewById(R.id.btnRegistrarProfe3);
        RegistrarAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences prefs = getSharedPreferences("idiomaInteres", MODE_PRIVATE);
                String idioma = prefs.getString("idiomaInteres", "No name defined");

                SharedPreferences prefssexo = getSharedPreferences("sexo", MODE_PRIVATE);
                String sexoObtenido = prefssexo.getString("sexo", "No name defined");

                SharedPreferences prefsNativo = getSharedPreferences("idiomaNativo", MODE_PRIVATE);
                String idiomaNativo = prefsNativo.getString("idiomaNativo", "No name defined");

                SharedPreferences prefsMotivo = getSharedPreferences("motivo_aprender", MODE_PRIVATE);
                String motivo = prefsMotivo.getString("motivo", "No name defined");


                Bundle extras = getIntent().getExtras();
                String uid;


                SharedPreferences.Editor editor = getSharedPreferences("correo", MODE_PRIVATE).edit();
                editor.putString("correo", editTextEmail.getText().toString());
                editor.apply();

                if (extras != null) {
                    uid = extras.getString("uid", "");

                    if (!uid.equals("")) {

                        RegistroRedesSociales usuario = new RegistroRedesSociales();
                        usuario.setLastName(edTextPaterno.getText().toString() + edTextMaterno.getText().toString());
                        usuario.setFirstName(edTextPrimerNombre.getText().toString());
                        usuario.setInterests(new Interests(editTextIntereses.getText().toString()));
                        usuario.setEmailAddress(editTextEmail.getText().toString());
                        usuario.setLanguage(new Language("" + idioma, idiomaNativo));
                        usuario.setGender(sexoObtenido);
                        usuario.setTelephoneNumber(edtTelefono.getText().toString());
                        usuario.setCountryResidence(edtCiudad.getText().toString());
                        usuario.setUserName("");
                        usuario.setAccounts(new Accounts("" + uid, "", "", ""));

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("fotoPerfil").document(uid)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        ImagenPerfil imagen = new ImagenPerfil();
                                        imagen.setUsuario(editTextEmail.getText().toString());
                                        FirebaseFirestore.getInstance().collection("fotoPerfil").document(editTextEmail.getText().toString()).set(imagen);

                                        db.collection("users").
                                                document(uid).set(usuario)
                                                .addOnSuccessListener(avoide -> {
                                                    Intent intent = new Intent(getApplicationContext(), SegundoActivityRegistro.class);
                                                    intent.putExtra("uid", uid);
                                                    startActivity(intent);
                                                })
                                                .addOnFailureListener(e -> Log.w("Error!!!!", "Error updating document", e));

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });

                    }
                } else {
                    RegistroRedesSociales usuario = new RegistroRedesSociales();
                    usuario.setLastName(edTextPaterno.getText().toString() + edTextMaterno.getText().toString());
                    usuario.setFirstName(edTextPrimerNombre.getText().toString());
                    usuario.setInterests(new Interests(editTextIntereses.getText().toString()));
                    usuario.setEmailAddress(editTextEmail.getText().toString());
                    usuario.setLanguage(new Language("" + idioma, idiomaNativo));
                    usuario.setGender(sexoObtenido);
                    usuario.setTelephoneNumber(edtTelefono.getText().toString());
                    usuario.setCountryResidence(edtCiudad.getText().toString());
                    usuario.setUserName("");
                    usuario.setAccounts(new Accounts("", "", "", ""));


                    ImagenPerfil imagen = new ImagenPerfil();
                    imagen.setUsuario(editTextEmail.getText().toString());
                    FirebaseFirestore.getInstance().collection("fotoPerfil").document(editTextEmail.getText().toString()).set(imagen);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String id = UUID.randomUUID().toString().toUpperCase();
                    FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(editTextEmail.getText().toString(), pass.getText().toString())
                            .addOnCompleteListener(Registro.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("BIEN!!!!", "createUserWithEmail:success");
                                        FirebaseFirestore.getInstance().collection("users").document(id).set(usuario).addOnCompleteListener(task1 -> {
                                            Intent intent = new Intent(getApplicationContext(), SegundoActivityRegistro.class);
                                            intent.putExtra("id", id);
                                            startActivity(intent);
                                        });
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("ERROR!!!!", "createUserWithEmail:failure", task.getException());
                                    }
                                }
                            });
                }
            }
        });


    }

}


