package com.example.pangeaappproduccion;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pangeaappproduccion.Adapters.AdapterBusqueda;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Buscador extends AppCompatActivity {


    private List<com.example.pangeaappproduccion.BuscadorList> listBuscador;
    private AdapterBusqueda adapterBusqueda;
    private RecyclerView recyclerViewbusquedas;
    ImageButton BusquedaBtn,SolicitudesBtn;
    EditText Perfil;
    TextView Solicitudes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscador);

        recyclerViewbusquedas = findViewById(R.id.recyclerBuscador);
        listBuscador = new ArrayList<>();
        adapterBusqueda = new AdapterBusqueda(listBuscador, new AdapterBusqueda.ItemClickListener() {
            @Override
            public void onItemClick(BuscadorList buscadorList) {
                Toast.makeText(getApplicationContext(),buscadorList.getUsuario(),Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(),PerfilBuscado.class);
                intent.putExtra("perfil", buscadorList.getUsuario());
                startActivity(intent);
            }
        });
        recyclerViewbusquedas.setAdapter(adapterBusqueda);
        recyclerViewbusquedas.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewbusquedas.setHasFixedSize(true);
        Perfil = (EditText)findViewById(R.id.EdTextBuscador);



        BusquedaBtn = (ImageButton)findViewById(R.id.btnFind);
        BusquedaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("fotoPerfil").whereEqualTo("usuario",Perfil.getText().toString()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.d(TAG, "Error:" + error.getMessage());
                        } else {
                            for (DocumentChange documentChange : value.getDocumentChanges()) {
                                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                    listBuscador.add(documentChange.getDocument().toObject(BuscadorList.class));
                                    adapterBusqueda.notifyDataSetChanged();
                                    recyclerViewbusquedas.smoothScrollToPosition(listBuscador.size());


                                }
                            }
                        }
                    }
                });

            }
        });

        SolicitudesBtn = (ImageButton)findViewById(R.id.btnSolicitudes);
        SolicitudesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inten = new Intent(getApplicationContext(), ActivitySolicitudes.class);
                startActivity(inten);
            }
        });



    }
}