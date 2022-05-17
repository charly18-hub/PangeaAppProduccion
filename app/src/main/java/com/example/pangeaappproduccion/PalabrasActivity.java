package com.example.pangeaappproduccion;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.pangeaappproduccion.databinding.ActivityPalabrasBinding;
import com.example.pangeaappproduccion.databinding.ActivityPerfilAmigosBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PalabrasActivity extends AppCompatActivity {
    List<Integer> numeros=new ArrayList<>();
    ActivityPalabrasBinding binding;
    private String palabraObtenida;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityPalabrasBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        numeros.add(0);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("oracionesAcomodar").document("hmfKcV37zzMOIxVzK1fp");
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    palabraObtenida = document.getString("Palabra");
                    Handler handler = new Handler();
                    final Runnable r = new Runnable() {
                        public void run() {
                            String[] words = palabraObtenida.split(" ");
                            int i=0;
                            for(String palabra : words) {
                                i++;
                                obtenerLugarAleatorio(palabra);
                            }
                        }
                    };
                    handler.postDelayed(r, 100);
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });


        binding.palabra1.setOnClickListener(view1->{
            binding.oracionCompleta.setText(binding.oracionCompleta.getText() +" "+binding.palabra1.getText());
        });
        binding.palabra2.setOnClickListener(view2->{
            binding.oracionCompleta.setText(binding.oracionCompleta.getText() +" "+binding.palabra2.getText());
        });
        binding.palabra3.setOnClickListener(view3->{
            binding.oracionCompleta.setText(binding.oracionCompleta.getText() +" "+binding.palabra3.getText());
        });
        binding.palabra4.setOnClickListener(view4->{
            binding.oracionCompleta.setText(binding.oracionCompleta.getText() +" "+binding.palabra4.getText());
        });
        binding.palabra5.setOnClickListener(view5->{
            binding.oracionCompleta.setText(binding.oracionCompleta.getText() +" "+binding.palabra5.getText());
        });
        binding.palabra6.setOnClickListener(view6->{
            binding.oracionCompleta.setText(binding.oracionCompleta.getText() +" "+binding.palabra6.getText());
        });
        binding.palabra7.setOnClickListener(view7->{
            binding.oracionCompleta.setText(binding.oracionCompleta.getText() +" "+binding.palabra7.getText());
        });
        binding.palabra8.setOnClickListener(view8->{
            binding.oracionCompleta.setText(binding.oracionCompleta.getText() +" "+binding.palabra8.getText());
        });
        binding.palabra9.setOnClickListener(view9->{
            binding.oracionCompleta.setText(binding.oracionCompleta.getText() +" "+binding.palabra9.getText());
        });
        binding.palabra10.setOnClickListener(view10->{
            binding.oracionCompleta.setText(binding.oracionCompleta.getText() +" "+binding.palabra10.getText());
        });
        binding.btnReiniciar.setOnClickListener(view11-> binding.oracionCompleta.setText(""));
        binding.btnTerminar.setOnClickListener(view12->{
            if(binding.oracionCompleta.getText().toString().equals(palabraObtenida)){
                Toast.makeText(getApplicationContext(),"Ganaste!",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"Suerte para la proxima!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void obtenerLugarAleatorio(String palabra){
        int numeroAleatorio = obtenerNumeroAleatorio();
        if(numeroAleatorio==1 && binding.palabra1.getText().toString().equals("")){
            binding.palabra1.setText(palabra);
        }else if(numeroAleatorio==2 && binding.palabra2.getText().toString().equals("")){
            binding.palabra2.setText(palabra);
        }else if(numeroAleatorio==3 && binding.palabra3.getText().toString().equals("")){
            binding.palabra3.setText(palabra);
        }else if(numeroAleatorio==4 && binding.palabra4.getText().toString().equals("")){
            binding.palabra4.setText(palabra);
        }else if(numeroAleatorio==5 && binding.palabra5.getText().toString().equals("")){
            binding.palabra5.setText(palabra);
        }else if(numeroAleatorio==6 && binding.palabra6.getText().toString().equals("")){
            binding.palabra6.setText(palabra);
        }else if(numeroAleatorio==7 && binding.palabra7.getText().toString().equals("")){
            binding.palabra7.setText(palabra);
        }else if(numeroAleatorio==8 && binding.palabra8.getText().toString().equals("")){
            binding.palabra8.setText(palabra);
        }else if(numeroAleatorio==9 && binding.palabra9.getText().toString().equals("")){
            binding.palabra9.setText(palabra);
        }else if(numeroAleatorio==10 && binding.palabra10.getText().toString().equals("")){
            binding.palabra10.setText(palabra);
        }else if(numeroAleatorio==0){
            obtenerNumeroAleatorio();
        }
    }

    public int obtenerNumeroAleatorio(){
        int numeroAleatorio = 0;
        boolean huboNumero=false;
        numeroAleatorio = (int)Math.floor(Math.random()*(10-1+1)+1);
        for (Integer numero:numeros) {
            if(numero==numeroAleatorio){
                huboNumero=true;
            }else{
                huboNumero=false;
            }
        }
        if(huboNumero){
            numeroAleatorio=0;
        }else{
            numeros.add(numeroAleatorio);
        }
        return numeroAleatorio;
    }
}