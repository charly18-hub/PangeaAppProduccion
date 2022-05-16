package com.example.pangeaappproduccion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.pangeaappproduccion.Adapters.AdapterPublicacion;
import com.example.pangeaappproduccion.Model.listPublicaciones;
import com.example.pangeaappproduccion.Util.UtilActivity;
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

public class PerfilBuscado extends UtilActivity {


    TextView Perfil,PaisUsuario,Nivel,Idioma,Intereses;
    ImageView FotoPerfil,FotoHead;

    private List<com.example.pangeaappproduccion.Model.listPublicaciones> listPublicaciones;
    private AdapterPublicacion adapterPublicacion;
    private RecyclerView recyclerViewPublicaciones;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_buscado);

        String perfilBuscado = getIntent().getExtras().getString("perfil");

        Toast.makeText(getApplicationContext(),perfilBuscado + " llego a la vista final", Toast.LENGTH_LONG).show();

        Perfil = (TextView)findViewById(R.id.userPerfil2);
        Perfil.setText(perfilBuscado);
        PaisUsuario = (TextView)findViewById(R.id.PaisPerfil);
        Nivel = (TextView)findViewById(R.id.nivelPerfil);
        Idioma = (TextView)findViewById(R.id.CursoPerfil);
        FotoPerfil = (ImageView)findViewById(R.id.foto_perfil2);
        FotoHead = (ImageView)findViewById(R.id.imgHeader2);
        recyclerViewPublicaciones = findViewById(R.id.recyclerViewBuscador);


        establecerIdioma();
        listPublicaciones = new ArrayList<>();
        adapterPublicacion = new AdapterPublicacion(listPublicaciones);
        recyclerViewPublicaciones.setAdapter(adapterPublicacion);
        recyclerViewPublicaciones.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewPublicaciones.setHasFixedSize(true);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("fotoPerfil").whereEqualTo("usuario",perfilBuscado).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){

                                RequestOptions requestOptions = new RequestOptions();
                                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));





                                Glide.with(getApplicationContext())
                                        .load(documentSnapshot.get("multimedia"))
                                        .apply(requestOptions)
                                        .into(FotoPerfil);





                            }
                        }
                    }
                });




        FirebaseFirestore db2 = FirebaseFirestore.getInstance();
        db2.collection("fotosHeader").whereEqualTo("usuario",perfilBuscado).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){


                                Log.d("imagen", documentSnapshot.getId()+"la imagen es"+ documentSnapshot.get("multimedia"));
                                RequestOptions requestOptions = new RequestOptions();
                                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));

                                Glide.with(getApplicationContext())
                                        .load(documentSnapshot.get("multimedia"))
                                        .apply(requestOptions)
                                        .into(FotoHead);

                            }
                        }
                    }
                });




        FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
        dbDataPerfil.collection("users").whereEqualTo("firts_name",perfilBuscado).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){


                                PaisUsuario.setText(documentSnapshot.getString("ciudad"));
                                Nivel.setText(documentSnapshot.getString("nivel"));
                                Idioma.setText(documentSnapshot.getString("idioma"));
                            }
                        }
                    }
                });


        FirebaseFirestore.getInstance().collection("redSocial").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d(TAG, "Error:" + error.getMessage());
                } else {
                    for (DocumentChange documentChange : value.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            listPublicaciones.add(documentChange.getDocument().toObject(listPublicaciones.class));
                            adapterPublicacion.notifyDataSetChanged();
                            recyclerViewPublicaciones.smoothScrollToPosition(listPublicaciones.size());
                        }
                    }
                }
            }
        });


    }
}