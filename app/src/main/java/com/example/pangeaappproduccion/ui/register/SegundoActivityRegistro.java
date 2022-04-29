package com.example.pangeaappproduccion.ui.register;

import static androidx.constraintlayout.widget.Constraints.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pangeaappproduccion.FotoPerfil;
import com.example.pangeaappproduccion.Listas.listPublicaciones;
import com.example.pangeaappproduccion.MainActivity;
import com.example.pangeaappproduccion.Model.Registro.ImagenPerfil;
import com.example.pangeaappproduccion.R;
import com.example.pangeaappproduccion.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    EditText UserPerfil;
    Button register;
    ImageView imageView;
    private static final int GALLERY_PICKER = 1;
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

        UserPerfil = (EditText) findViewById(R.id.editTextTextPersonName3);
        imageView = findViewById(R.id.imageView3);


        SharedPreferences prefs = getSharedPreferences("correo", MODE_PRIVATE);
        correo = prefs.getString("correo", "");


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentImagen = new Intent(Intent.ACTION_PICK);
                intentImagen.setType("image/*");
                startActivityForResult(intentImagen, GALLERY_PICKER);

            }
        });

        register = (Button) findViewById(R.id.btnRegistrarProfe3);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (formularioValido()) {

                    Bundle extras = getIntent().getExtras();
                    String uid, id;
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    if (extras != null) {

                        uid = extras.getString("uid", "");
                        id = extras.getString("id", "");


                        if (!uid.equals("")) {

                            DocumentReference docRef = db.collection("users").document(uid);
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                                            db2.collection("users").document(uid).update("userName", UserPerfil.getText().toString()).addOnSuccessListener(unused -> {
                                                Toast.makeText(getApplicationContext(), "Se actualizo el nombre de usuario", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(SegundoActivityRegistro.this, MainActivity.class);
                                                startActivity(intent);
                                            }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show());
                                        } else {
                                            Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });


                        } else if (!id.equals("")) {
                            db.collection("users").whereEqualTo("emailAddress", correo).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                    FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                                                    db2.collection("users").document(id).update("userName", UserPerfil.getText().toString()).addOnSuccessListener(unused -> {
                                                        Toast.makeText(getApplicationContext(), "Se actualizo el nombre de usuario", Toast.LENGTH_LONG).show();
                                                        Intent intent = new Intent(SegundoActivityRegistro.this, MainActivity.class);
                                                        startActivity(intent);
                                                    }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show());
                                                }
                                            }
                                        }
                                    });
                        }
                    }

                }

            }
        });


    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == GALLERY_PICKER && resultCode == RESULT_OK) {

            Toast.makeText(getApplicationContext(), "SUBIENDO, NO CIERRE LA APLICACION!", Toast.LENGTH_LONG).show();
            Bundle extras = getIntent().getExtras();
            String uid, id;
            if (extras != null) {
                uid = extras.getString("uid", "");
                id = extras.getString("id", "");
                if (!uid.equals("")) {

                    Uri uri = data.getData();
                    imageView.setImageURI(uri);
                    StorageReference filePath = mStorage.child("fotoPerfil").child(uri.getLastPathSegment());
                    filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uiriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uiriTask.isSuccessful()) ;
                            Uri dowloadUri = uiriTask.getResult();
                            FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                            Map<String, Object> fotoPerfil = new HashMap<>();
                            fotoPerfil.put("multimedia", dowloadUri.toString());
                            fotoPerfil.put("usuario", uid);
                            db2.collection("fotoPerfil").document(uid).update(fotoPerfil).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(), "Se actualizo la imagen de perfil", Toast.LENGTH_LONG).show();
                                    db2.collection("users").document(uid).update("profilePicture", dowloadUri.toString()).addOnSuccessListener(unusedd -> {
                                    }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show());
                                }
                            }).addOnFailureListener(e -> {
                            });
                        }
                    }).addOnFailureListener(e -> Log.e("FileManager", "Error en uploadImg ==>" + e));
                } else if (!id.equals("")) {
                    Uri uri = data.getData();
                    imageView.setImageURI(uri);
                    StorageReference filePath = mStorage.child("fotoPerfil").child(uri.getLastPathSegment());
                    filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "Se actualizo la imagen de perfil", Toast.LENGTH_LONG).show();
                            Task<Uri> uiriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uiriTask.isSuccessful()) ;
                            Uri dowloadUri = uiriTask.getResult();
                            ImagenPerfil imagen = new ImagenPerfil();
                            imagen.setMultimedia(dowloadUri.toString());
                            imagen.setUsuario(id);
                            FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                            FirebaseFirestore.getInstance().collection("fotoPerfil").document(id)
                                    .set(imagen)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            db2.collection("users").document(id).update("profilePicture", dowloadUri.toString()).addOnSuccessListener(unusedd -> {
                                            }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }).addOnFailureListener(e -> Log.e("FileManager", "Error en uploadImg ==>" + e));
                }
            }
        }


    }

    private boolean formularioValido() {
        boolean valido = false;
        if (TextUtils.isEmpty(textUser.getText().toString())) {
            Toast.makeText(this, "Debes de escribir tu nombre de usuario!", Toast.LENGTH_SHORT).show();
        } else {
            valido = true;
        }
        return valido;
    }

    public static boolean validarCorreo(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
