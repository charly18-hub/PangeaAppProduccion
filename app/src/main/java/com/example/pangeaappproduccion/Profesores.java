package com.example.pangeaappproduccion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class Profesores extends AppCompatActivity {

    TextView alumnos;
    Button RegistrarAlumno;
    EditText edTextPrimerNombre,edTextSegundoNombre,edTextPaterno,edTextMaterno,edtFechanNacimiento,editTextPrecioHora,editNivel,editTextEmail,edtCiudad,edtTelefono;
    RadioGroup radioGroupSex;
    RadioButton masculino, femenino,otro,noDecir;
    Spinner idiomaInteres,idiomaNativo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesores);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerEnsenarProfe);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.idiomas_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        Spinner spinner1 = (Spinner) findViewById(R.id.spinnerNativoProfe);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.idiomas_array, android.R.layout.simple_spinner_item);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter1);


        alumnos = (TextView) findViewById(R.id.AlumnosRegistro);

        alumnos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profesores.this, Registro.class);
                startActivity(intent);
            }
        });


        edTextPrimerNombre = (EditText) findViewById(R.id.edTextPrimerNombreProfe);
        edTextSegundoNombre = (EditText) findViewById(R.id.edTextSegundoNombreProfe);
        edTextMaterno = (EditText) findViewById(R.id.edTextMaternoProfe);
        edTextPaterno = (EditText) findViewById(R.id.edTextPaternoProfe);
        masculino = (RadioButton) findViewById(R.id.radioButtonMenProfe);
        femenino = (RadioButton) findViewById(R.id.radioButtonWommenProfe);
        otro = (RadioButton) findViewById(R.id.radioOtroProfe);
        idiomaInteres = (Spinner) findViewById(R.id.spinnerEnsenarProfe);
        idiomaNativo = (Spinner) findViewById(R.id.spinnerNativoProfe);
        edtCiudad = (EditText) findViewById(R.id.editTextCiudad);
        edtTelefono = (EditText) findViewById(R.id.editTextTelefono);
        editTextEmail = (EditText)findViewById(R.id.editTextTextEmailProfe);
        edtFechanNacimiento = (EditText) findViewById(R.id.edTextFechaProfe);
        editTextPrecioHora = (EditText) findViewById(R.id.editTextPrecioprofe);
        editNivel = (EditText) findViewById(R.id.editTextNivelProfe);

        idiomaNativo.setAdapter(adapter);
        idiomaNativo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                SharedPreferences.Editor editor = getSharedPreferences("idiomaNativoProfe", MODE_PRIVATE).edit();
                editor.putString("idiomaNativoProfe", (String) adapterView.getItemAtPosition(i));
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

                SharedPreferences.Editor editor = getSharedPreferences("idiomaInteresProfe", MODE_PRIVATE).edit();
                editor.putString("idiomaInteresProfe", (String) adapterView.getItemAtPosition(i));
                editor.apply();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        radioGroupSex = (RadioGroup)findViewById(R.id.rg_sex2);
        radioGroupSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (masculino.isChecked()== true){

                    String valorButton = "Hombre";

                    SharedPreferences.Editor editor = getSharedPreferences("sexoProfe", MODE_PRIVATE).edit();
                    editor.putString("sexoProfe", valorButton);
                    editor.apply();

                }
                if(femenino.isChecked()== true){

                    String valorButton = "Mujer";

                    SharedPreferences.Editor editor = getSharedPreferences("sexoProfe", MODE_PRIVATE).edit();
                    editor.putString("sexoProfe", valorButton);
                    editor.apply();

                }
                if(otro.isChecked()== true){

                    String valorButton = "Otro";

                    SharedPreferences.Editor editor = getSharedPreferences("sexoProfe", MODE_PRIVATE).edit();
                    editor.putString("sexoProfe", valorButton);
                    editor.apply();

                }

            }
        });


        RegistrarAlumno = (Button)findViewById(R.id.btnRegistrarProfe);
        RegistrarAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences prefs = getSharedPreferences("idiomaInteresProfe", MODE_PRIVATE);
                String idioma = prefs.getString("idiomaInteresProfe", "No name defined");

                SharedPreferences prefssexo = getSharedPreferences("sexoProfe", MODE_PRIVATE);
                String sexoObtenido = prefssexo.getString("sexoProfe", "No name defined");

                SharedPreferences prefsNativo = getSharedPreferences("idiomaNativoProfe", MODE_PRIVATE);
                String idiomaNativo = prefsNativo.getString("idiomaNativoProfe", "No name defined");




                RegistroProfe registroProfe = new RegistroProfe();
                registroProfe.setapellido_materno(edTextMaterno.getText().toString());
                registroProfe.setapellido_paterno(edTextPaterno.getText().toString());
                registroProfe.setfirts_name(edTextPrimerNombre.getText().toString());
                registroProfe.setlast_name(edTextSegundoNombre.getText().toString());
                registroProfe.setemail(editTextEmail.getText().toString());
                registroProfe.setpassword("");
                registroProfe.setidioma_interes(idioma);
                registroProfe.setsexo(sexoObtenido);
                registroProfe.setidioma_nativo(idiomaNativo);
                registroProfe.settelefono(edtTelefono.getText().toString());
                registroProfe.setuser("");
                registroProfe.setciudad(edtCiudad.getText().toString());
                registroProfe.setMultimedia("");
                registroProfe.setCertificado("");
                registroProfe.setCuentaBancaria("");
                registroProfe.setnivel(editNivel.getText().toString());
                registroProfe.setDisponibilidad("");
                registroProfe.setExpedidoPor("");
                registroProfe.setzonaHoraria("");
                registroProfe.setcostoHora(editTextPrecioHora.getText().toString());
                registroProfe.setidioma_certificado("");
                registroProfe.setfechaNacimiento(edtFechanNacimiento.getText().toString());

                FirebaseFirestore.getInstance().collection("users").document(editTextEmail.getText().toString()).set(registroProfe);


                Intent intent = new Intent(getApplicationContext(),SegundoActivityRegistroProfe.class);
                intent.putExtra("user_name",edTextSegundoNombre.getText().toString());
                intent.putExtra("email_register",editTextEmail.getText().toString());
                startActivity(intent);

            }
        });


    }

}