package com.example.pangeaappproduccion.ui.register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pangeaappproduccion.FotoPerfil;
import com.example.pangeaappproduccion.MainActivity;
import com.example.pangeaappproduccion.R;
import com.example.pangeaappproduccion.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SegundoActivityRegistro extends AppCompatActivity {


    TextView textUser;
    EditText  UserPerfil;
    Button register;
    ImageView imageView;
    private static final int GALLERY_PICKER =1;
    String correo;
    StorageReference mStorage = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segundo_registro);



        String usuario_registrado = getIntent().getExtras().getString("user_name");
        String email_register_obtenido = getIntent().getExtras().getString("email_register");

        SharedPreferences.Editor editor = getSharedPreferences("usuario_recibido", MODE_PRIVATE).edit();
        editor.putString("usuario_register", usuario_registrado);
        editor.apply();

        SharedPreferences.Editor editor1 = getSharedPreferences("email_recibido", MODE_PRIVATE).edit();
        editor1.putString("email_register", email_register_obtenido);
        editor1.apply();


        textUser = (TextView) findViewById(R.id.editTextTextPersonName3);

        textUser.setText(usuario_registrado);

        UserPerfil = (EditText)findViewById(R.id.editTextTextPersonName3);
        imageView = findViewById(R.id.imageView3);


        SharedPreferences prefs = getSharedPreferences("correo", MODE_PRIVATE);
        correo = prefs.getString("correo", "");


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentImagen = new Intent(Intent.ACTION_PICK);
                intentImagen.setType("image/*");
                startActivityForResult(intentImagen,GALLERY_PICKER);

            }
        });

        register = (Button)findViewById(R.id.btnRegistrarProfe3);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle extras = getIntent().getExtras();
                String uid;
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                if (extras != null) {
                    uid = extras.getString("uid","");
                    if(!uid.equals("")){

                        db.collection("fotoPerfil").whereEqualTo("usuario",correo).get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){

                                                FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                                                db2.collection("users").document(uid).update("userName",UserPerfil.getText().toString()).addOnSuccessListener(unused -> {
                                                    Toast.makeText(getApplicationContext(),"Se actualizo el nombre de usuario",Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(SegundoActivityRegistro.this, MainActivity.class);
                                                    startActivity(intent);
                                                }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show());

                                            }
                                        }
                                    }
                                });



                    }
                }else{
                    db.collection("fotoPerfil").whereEqualTo("usuario",email_register_obtenido).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()){

                                            String imgPerfil = (String) documentSnapshot.get("multimedia");
                                            Toast.makeText(getApplicationContext(),imgPerfil,Toast.LENGTH_LONG).show();



                                            FirebaseFirestore db2 = FirebaseFirestore.getInstance();

                                            Map<String, Object> dateUpdate = new HashMap<>();
                                            dateUpdate.put("multimedia",imgPerfil);
                                            dateUpdate.put("usuario",textUser.getText().toString());
                                            dateUpdate.put("user",textUser.getText().toString());

                                            db2.collection("users").document(email_register_obtenido).update(dateUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(getApplicationContext(),"se Actualizaron los datos",Toast.LENGTH_LONG).show();

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull @NotNull Exception e) {
                                                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

                                                }
                                            });


                                        }
                                    }
                                }
                            });
                }


            }
        });



    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == GALLERY_PICKER &&  resultCode == RESULT_OK) {
            Toast.makeText(getApplicationContext(),"SUBIENDO, NO CIERRE LA APLICACION!",Toast.LENGTH_LONG).show();

            Uri uri = data.getData();
            imageView.setImageURI(uri);


            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setCustomMetadata("descripcion","Esta es una Prueba")
                    .setCustomMetadata("usuario",correo)
                    .build();

            StorageReference filePath = mStorage.child("fotosPerfil").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uiriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uiriTask.isSuccessful());
                    Uri dowloadUri = uiriTask.getResult();


                    FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                    Map<String, Object> fotoPerfil = new HashMap<>();
                    fotoPerfil.put("multimedia",dowloadUri.toString());
                    fotoPerfil.put("usuario",correo);
                    db2.collection("fotoPerfil").document(correo).update(fotoPerfil).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Se actualizo la imagen de perfil",Toast.LENGTH_LONG).show();
                            Bundle extras = getIntent().getExtras();
                            String uid;
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            if (extras != null) {
                                uid = extras.getString("uid","");
                                if(!uid.equals("")) {
                                    db2.collection("users").document(uid).update("profilePicture",dowloadUri.toString()).addOnSuccessListener(unusedd -> {
                                    }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show());
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {

                        }
                    });



                }











            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("FileManager","Error en uploadImg ==>"+e);
                }
            });
        }


    }

}