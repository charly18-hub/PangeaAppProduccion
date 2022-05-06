package com.example.pangeaappproduccion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pangeaappproduccion.Util.UtilActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;






//En este activity hay que agregar manualmente tanto el le evaluacion como les preguntas de cada una de las evaluaciones para que funcione




public class ActivityResponderEvaluacion extends UtilActivity {

    TextView Respuesta1,Respuesta2,Respuesta3,Pregunta;
    Button EnviarRespuesta;
    EditText EditRespuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String idPregunta = getIntent().getExtras().getString("id");
        Toast.makeText(getApplicationContext(), "dato al responder " + idPregunta, Toast.LENGTH_LONG).show();



        establecerIdioma();

        FirebaseFirestore dbDataPerfilType = FirebaseFirestore.getInstance();


        dbDataPerfilType.collection("Evaluaciones").document("TG5G3jev3T8pUXEuU8L8").collection("Evaluacion1").whereEqualTo("id", idPregunta).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                String TypeObtenida = documentSnapshot.getString("type");

                                Toast.makeText(getApplicationContext(), "llego el tipo es " + TypeObtenida, Toast.LENGTH_LONG).show();




                                if(TypeObtenida.equals("TF")){


                                    setContentView(R.layout.activity_responder_evaluacion);


                                    Respuesta1 = (TextView) findViewById(R.id.false2);
                                    Respuesta2 = (TextView) findViewById(R.id.true2);
                                    Pregunta = (TextView) findViewById(R.id.pregunta);


                                    FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
                                    dbDataPerfil.collection("Evaluaciones").document("TG5G3jev3T8pUXEuU8L8").collection("Evaluacion1").whereEqualTo("id", idPregunta).get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                                            String preguntaObtenida = documentSnapshot.getString("pregunta");

                                                            if(preguntaObtenida != ""){

                                                                dbDataPerfil.collection("Evaluaciones") .document("TG5G3jev3T8pUXEuU8L8").collection("Evaluacion1").document("QRnn1izIwyekivjSjiab").collection("opciones").whereEqualTo("id", idPregunta).get()
                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                                                                                if(task.isSuccessful()) {
                                                                                    for(QueryDocumentSnapshot documentSnapshot1 : task.getResult()) {

                                                                                        Pregunta.setText(documentSnapshot.getString("pregunta"));
                                                                                        Respuesta1.setText(documentSnapshot1.getString("respuesta1"));
                                                                                        Respuesta2.setText(documentSnapshot1.getString("respuesta2"));

                                                                                    }
                                                                                }


                                                                            }
                                                                        });

                                                            }
                                                        }
                                                    }
                                                }
                                            });


                                    Respuesta1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            dbDataPerfil.collection("Evaluaciones").document("TG5G3jev3T8pUXEuU8L8").collection("Evaluacion1").document("QRnn1izIwyekivjSjiab").collection("respuestaCorrecta").whereEqualTo("respuesta1", Respuesta1.getText()).get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                                                            if(task.isSuccessful()) {

                                                                if(!task.getResult().isEmpty()){

                                                                    Toast.makeText(getApplication(), "seleccionaste la respuesta correcta", Toast.LENGTH_SHORT).show();

                                                                    AlertDialog alertDialog = new AlertDialog.Builder(ActivityResponderEvaluacion.this)
                                                                            //set icon
                                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                                            //set title
                                                                            .setTitle("Respuesta Correcta")
                                                                            //set message
                                                                            .setMessage("¡Muy bien obtuviste la respuesta correcta!")
                                                                            //set positive button
                                                                            .setPositiveButton("Siguiente Pregunta", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                    //set what would happen when positive button is clicked
                                                                                    finish();
                                                                                }
                                                                            })
                                                                            //set negative button
                                                                            .setNegativeButton("Regresar Al Panel", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                    //set what should happen when negative button is clicked
                                                                                    Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                                                                                }
                                                                            })
                                                                            .show();


                                                                } else{

                                                                    AlertDialog alertDialog = new AlertDialog.Builder(ActivityResponderEvaluacion.this)
                                                                            //set icon
                                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                                            //set title
                                                                            .setTitle("Respuesta Incorrecta")
                                                                            //set message
                                                                            .setMessage("¡Intenta Nuevamente!")

                                                                            //set negative button
                                                                            .setNegativeButton("Regresar Al Panel", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                    //set what should happen when negative button is clicked
                                                                                    Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                                                                                }
                                                                            })
                                                                            .show();
                                                                }

                                                            }else{
                                                                Toast.makeText(getApplication(), "intenta nuevamente", Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull @NotNull Exception e) {
                                                    Toast.makeText(getApplication(), "intenta nuevamente", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        }
                                    });


                                    Respuesta2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(getApplication(), Respuesta2.getText(), Toast.LENGTH_SHORT).show();
                                            dbDataPerfil.collection("Evaluaciones").document("TG5G3jev3T8pUXEuU8L8").collection("Evaluacion1").document("QRnn1izIwyekivjSjiab").collection("respuestaCorrecta").whereEqualTo("respuesta1", Respuesta2.getText()).get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                                                            if(task.isSuccessful()) {

                                                                if(!task.getResult().isEmpty()){

                                                                    Toast.makeText(getApplication(), "seleccionaste la respuesta correcta", Toast.LENGTH_SHORT).show();


                                                                    AlertDialog alertDialog = new AlertDialog.Builder(ActivityResponderEvaluacion.this)
                                                                            //set icon
                                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                                            //set title
                                                                            .setTitle("Respuesta Correcta")
                                                                            //set message
                                                                            .setMessage("¡Muy bien obtuviste la respuesta correcta!")
                                                                            //set positive button
                                                                            .setPositiveButton("Siguiente Pregunta", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                    //set what would happen when positive button is clicked
                                                                                    finish();
                                                                                }
                                                                            })
                                                                            //set negative button
                                                                            .setNegativeButton("Regresar Al Panel", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                    //set what should happen when negative button is clicked
                                                                                    Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                                                                                }
                                                                            })
                                                                            .show();


                                                                } else{


                                                                    AlertDialog alertDialog = new AlertDialog.Builder(ActivityResponderEvaluacion.this)
                                                                            //set icon
                                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                                            //set title
                                                                            .setTitle("Respuesta Incorrecta")
                                                                            //set message
                                                                            .setMessage("¡Intenta Nuevamente!")

                                                                            //set negative button
                                                                            .setNegativeButton("Regresar Al Panel", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                    //set what should happen when negative button is clicked
                                                                                    Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                                                                                }
                                                                            })
                                                                            .show();


                                                                }

                                                            }else{
                                                                Toast.makeText(getApplication(), "intenta nuevamente", Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull @NotNull Exception e) {
                                                    Toast.makeText(getApplication(), "intenta nuevamente", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        }
                                    });

                                }


                                if(TypeObtenida.equals("CR")){

                                    setContentView(R.layout.activity_responder_evaluacion_tipo2);



                                    Pregunta = (TextView) findViewById(R.id.pregunta2);


                                    FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
                                    dbDataPerfil.collection("Evaluaciones").document("TG5G3jev3T8pUXEuU8L8").collection("Evaluacion1").whereEqualTo("id", idPregunta).get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {



                                                            Pregunta.setText(documentSnapshot.getString("pregunta"));

                                                        }
                                                    }


                                                }
                                            });


                                    EditRespuesta = (EditText)findViewById(R.id.frase2);
                                    EnviarRespuesta=(Button)findViewById(R.id.respuesta);
                                    EnviarRespuesta.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            dbDataPerfil.collection("Evaluaciones").document("TG5G3jev3T8pUXEuU8L8").collection("Evaluacion1").document("gxjqGdrdRBd4McHSYJlM").collection("respuestaCorrecta").whereEqualTo("respuesta", EditRespuesta.getText().toString()).get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                                                            if(task.isSuccessful()) {

                                                                if(!task.getResult().isEmpty()){

                                                                    Toast.makeText(getApplication(), "la es respuesta correcta", Toast.LENGTH_SHORT).show();

                                                                    AlertDialog alertDialog = new AlertDialog.Builder(ActivityResponderEvaluacion.this)
                                                                            //set icon
                                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                                            //set title
                                                                            .setTitle("Respuesta Correcta")
                                                                            //set message
                                                                            .setMessage("¡Muy bien obtuviste la respuesta correcta!")
                                                                            //set positive button
                                                                            .setPositiveButton("Siguiente Pregunta", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                    //set what would happen when positive button is clicked
                                                                                    finish();
                                                                                }
                                                                            })
                                                                            //set negative button
                                                                            .setNegativeButton("Regresar Al Panel", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                    //set what should happen when negative button is clicked
                                                                                    Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                                                                                }
                                                                            })
                                                                            .show();
                                                                } else{
                                                                    AlertDialog alertDialog = new AlertDialog.Builder(ActivityResponderEvaluacion.this)
                                                                            //set icon
                                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                                            //set title
                                                                            .setTitle("Respuesta Incorrecta")
                                                                            //set message
                                                                            .setMessage("¡Intenta Nuevamente!")

                                                                            //set negative button
                                                                            .setNegativeButton("Regresar Al Panel", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                    //set what should happen when negative button is clicked
                                                                                    Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                                                                                }
                                                                            })
                                                                            .show();
                                                                }

                                                            }else{
                                                                Toast.makeText(getApplication(), "intenta nuevamente", Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull @NotNull Exception e) {
                                                    Toast.makeText(getApplication(), "intenta nuevamente", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        }
                                    });
                                }


                                if(TypeObtenida.equals("OM")){

                                    setContentView(R.layout.activity_responder_evaluacion_tipo3);

                                    Respuesta1 = (TextView) findViewById(R.id.opcion1);
                                    Respuesta2 = (TextView) findViewById(R.id.opcion2);
                                    Respuesta3 = (TextView) findViewById(R.id.opcion3);
                                    Pregunta = (TextView) findViewById(R.id.pregunta3);


                                    FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
                                    dbDataPerfil.collection("Evaluaciones").document("TG5G3jev3T8pUXEuU8L8").collection("Evaluacion1").whereEqualTo("id", idPregunta).get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                                            String preguntaObtenida = documentSnapshot.getString("pregunta");

                                                            if(preguntaObtenida != ""){

                                                                dbDataPerfil.collection("Evaluaciones").document("TG5G3jev3T8pUXEuU8L8").collection("Evaluacion1").document("Ro0vzKpsloCvvQJqzhhm").collection("opciones").whereEqualTo("id", idPregunta).get()
                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                                                                                if(task.isSuccessful()) {
                                                                                    for(QueryDocumentSnapshot documentSnapshot1 : task.getResult()) {

                                                                                        Pregunta.setText(documentSnapshot.getString("pregunta"));
                                                                                        Respuesta1.setText(documentSnapshot1.getString("respuesta1"));
                                                                                        Respuesta2.setText(documentSnapshot1.getString("respuesta2"));
                                                                                        Respuesta3.setText(documentSnapshot1.getString("respuesta3"));

                                                                                    }
                                                                                }


                                                                            }
                                                                        });

                                                            }
                                                        }
                                                    }
                                                }
                                            });


                                    Respuesta1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            dbDataPerfil.collection("Evaluaciones").document("TG5G3jev3T8pUXEuU8L8").collection("Evaluacion1").document("Ro0vzKpsloCvvQJqzhhm").collection("respuestaCorrecta").whereEqualTo("respuestaCorrecta", Respuesta1.getText()).get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                                                            if(task.isSuccessful()) {

                                                                if(!task.getResult().isEmpty()){

                                                                    Toast.makeText(getApplication(), "seleccionaste la respuesta correcta", Toast.LENGTH_SHORT).show();

                                                                    AlertDialog alertDialog = new AlertDialog.Builder(ActivityResponderEvaluacion.this)
                                                                            //set icon
                                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                                            //set title
                                                                            .setTitle("Respuesta Correcta")
                                                                            //set message
                                                                            .setMessage("¡Muy bien obtuviste la respuesta correcta!")
                                                                            //set positive button
                                                                            .setPositiveButton("Siguiente Pregunta", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                    //set what would happen when positive button is clicked
                                                                                    finish();
                                                                                }
                                                                            })
                                                                            //set negative button
                                                                            .setNegativeButton("Regresar Al Panel", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                    //set what should happen when negative button is clicked
                                                                                    Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                                                                                }
                                                                            })
                                                                            .show();


                                                                } else{

                                                                    AlertDialog alertDialog = new AlertDialog.Builder(ActivityResponderEvaluacion.this)
                                                                            //set icon
                                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                                            //set title
                                                                            .setTitle("Respuesta Incorrecta")
                                                                            //set message
                                                                            .setMessage("¡Intenta Nuevamente!")

                                                                            //set negative button
                                                                            .setNegativeButton("Regresar Al Panel", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                    //set what should happen when negative button is clicked
                                                                                    Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                                                                                }
                                                                            })
                                                                            .show();
                                                                }

                                                            }else{
                                                                Toast.makeText(getApplication(), "intenta nuevamente", Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull @NotNull Exception e) {
                                                    Toast.makeText(getApplication(), "intenta nuevamente", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        }
                                    });


                                    Respuesta2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(getApplication(), Respuesta2.getText(), Toast.LENGTH_SHORT).show();
                                            dbDataPerfil.collection("Evaluaciones").document("TG5G3jev3T8pUXEuU8L8").collection("Evaluacion1").document("Ro0vzKpsloCvvQJqzhhm").collection("respuestaCorrecta").whereEqualTo("respuestaCorrecta", Respuesta2.getText()).get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                                                            if(task.isSuccessful()) {

                                                                if(!task.getResult().isEmpty()){

                                                                    Toast.makeText(getApplication(), "seleccionaste la respuesta correcta", Toast.LENGTH_SHORT).show();


                                                                    AlertDialog alertDialog = new AlertDialog.Builder(ActivityResponderEvaluacion.this)
                                                                            //set icon
                                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                                            //set title
                                                                            .setTitle("Respuesta Correcta")
                                                                            //set message
                                                                            .setMessage("¡Muy bien obtuviste la respuesta correcta!")
                                                                            //set positive button
                                                                            .setPositiveButton("Siguiente Pregunta", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                    //set what would happen when positive button is clicked
                                                                                    finish();
                                                                                }
                                                                            })
                                                                            //set negative button
                                                                            .setNegativeButton("Regresar Al Panel", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                    //set what should happen when negative button is clicked
                                                                                    Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                                                                                }
                                                                            })
                                                                            .show();


                                                                } else{


                                                                    AlertDialog alertDialog = new AlertDialog.Builder(ActivityResponderEvaluacion.this)
                                                                            //set icon
                                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                                            //set title
                                                                            .setTitle("Respuesta Incorrecta")
                                                                            //set message
                                                                            .setMessage("¡Intenta Nuevamente!")

                                                                            //set negative button
                                                                            .setNegativeButton("Regresar Al Panel", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                    //set what should happen when negative button is clicked
                                                                                    Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                                                                                }
                                                                            })
                                                                            .show();


                                                                }

                                                            }else{
                                                                Toast.makeText(getApplication(), "intenta nuevamente", Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull @NotNull Exception e) {
                                                    Toast.makeText(getApplication(), "intenta nuevamente", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        }
                                    });

                                    Respuesta3.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(getApplication(), Respuesta3.getText(), Toast.LENGTH_SHORT).show();
                                            dbDataPerfil.collection("Evaluaciones").document("TG5G3jev3T8pUXEuU8L8").collection("Evaluacion1").document("Ro0vzKpsloCvvQJqzhhm").collection("respuestaCorrecta").whereEqualTo("respuestaCorrecta", Respuesta3.getText()).get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                                                            if(task.isSuccessful()) {

                                                                if(!task.getResult().isEmpty()){

                                                                    Toast.makeText(getApplication(), "seleccionaste la respuesta correcta", Toast.LENGTH_SHORT).show();


                                                                    AlertDialog alertDialog = new AlertDialog.Builder(ActivityResponderEvaluacion.this)
                                                                            //set icon
                                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                                            //set title
                                                                            .setTitle("Respuesta Correcta")
                                                                            //set message
                                                                            .setMessage("¡Muy bien obtuviste la respuesta correcta!")
                                                                            //set positive button
                                                                            .setPositiveButton("Siguiente Pregunta", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                    //set what would happen when positive button is clicked
                                                                                    finish();
                                                                                }
                                                                            })
                                                                            //set negative button
                                                                            .setNegativeButton("Regresar Al Panel", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                    //set what should happen when negative button is clicked
                                                                                    Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                                                                                }
                                                                            })
                                                                            .show();


                                                                } else{


                                                                    AlertDialog alertDialog = new AlertDialog.Builder(ActivityResponderEvaluacion.this)
                                                                            //set icon
                                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                                            //set title
                                                                            .setTitle("Respuesta Incorrecta")
                                                                            //set message
                                                                            .setMessage("¡Intenta Nuevamente!")

                                                                            //set negative button
                                                                            .setNegativeButton("Regresar Al Panel", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                    //set what should happen when negative button is clicked
                                                                                    Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                                                                                }
                                                                            })
                                                                            .show();


                                                                }

                                                            }else{
                                                                Toast.makeText(getApplication(), "intenta nuevamente", Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull @NotNull Exception e) {
                                                    Toast.makeText(getApplication(), "intenta nuevamente", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        }
                                    });

                                }
                            }
                        }
                    }
                });





        FirebaseFirestore dbDataPerfilType2 = FirebaseFirestore.getInstance();


        dbDataPerfilType2.collection("Evaluaciones").document("3nDweB5nuWBtfLnkm9xi").collection("Evaluacion2").whereEqualTo("id", idPregunta).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                String TypeObtenida = documentSnapshot.getString("type");

                                Toast.makeText(getApplicationContext(), "llego el tipo es " + TypeObtenida, Toast.LENGTH_LONG).show();


                                if (TypeObtenida.equals("CR")) {

                                    setContentView(R.layout.activity_responder_evaluacion_tipo2);


                                    Pregunta = (TextView) findViewById(R.id.pregunta2);


                                    FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
                                    dbDataPerfil.collection("Evaluaciones").document("3nDweB5nuWBtfLnkm9xi").collection("Evaluacion2").whereEqualTo("id", idPregunta).get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {


                                                            Pregunta.setText(documentSnapshot.getString("pregunta"));

                                                        }
                                                    }


                                                }
                                            });


                                    EditRespuesta = (EditText) findViewById(R.id.frase2);
                                    EnviarRespuesta = (Button) findViewById(R.id.respuesta);
                                    EnviarRespuesta.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            dbDataPerfil.collection("Evaluaciones").document("3nDweB5nuWBtfLnkm9xi").collection("Evaluacion2").document("HqiQQVK9v6nI3GSDspyG").collection("respuestaCorrecta").whereEqualTo("respuesta", EditRespuesta.getText().toString()).get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                                                            if (task.isSuccessful()) {

                                                                if (!task.getResult().isEmpty()) {

                                                                    Toast.makeText(getApplication(), "la es respuesta correcta", Toast.LENGTH_SHORT).show();

                                                                    AlertDialog alertDialog = new AlertDialog.Builder(ActivityResponderEvaluacion.this)
                                                                            //set icon
                                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                                            //set title
                                                                            .setTitle("Respuesta Correcta")
                                                                            //set message
                                                                            .setMessage("¡Muy bien obtuviste la respuesta correcta!")
                                                                            //set positive button
                                                                            .setPositiveButton("Siguiente Pregunta", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                    //set what would happen when positive button is clicked
                                                                                    finish();
                                                                                }
                                                                            })
                                                                            //set negative button
                                                                            .setNegativeButton("Regresar Al Panel", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                    //set what should happen when negative button is clicked
                                                                                    Toast.makeText(getApplicationContext(), "Nothing Happened", Toast.LENGTH_LONG).show();
                                                                                }
                                                                            })
                                                                            .show();
                                                                } else {
                                                                    AlertDialog alertDialog = new AlertDialog.Builder(ActivityResponderEvaluacion.this)
                                                                            //set icon
                                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                                            //set title
                                                                            .setTitle("Respuesta Incorrecta")
                                                                            //set message
                                                                            .setMessage("¡Intenta Nuevamente!")

                                                                            //set negative button
                                                                            .setNegativeButton("Regresar Al Panel", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                    //set what should happen when negative button is clicked
                                                                                    Toast.makeText(getApplicationContext(), "Nothing Happened", Toast.LENGTH_LONG).show();
                                                                                }
                                                                            })
                                                                            .show();
                                                                }

                                                            } else {
                                                                Toast.makeText(getApplication(), "intenta nuevamente", Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull @NotNull Exception e) {
                                                    Toast.makeText(getApplication(), "intenta nuevamente", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        }
                    }
                });

    }
}