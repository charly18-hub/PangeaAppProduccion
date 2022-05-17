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
    List<Integer> numerosUsados=new ArrayList<>();
    List<Integer> numeros=new ArrayList<>();
    ActivityPalabrasBinding binding;
    private String palabraObtenida;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityPalabrasBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        for (int i = 1; i < 11; i++) {
            numeros.add(i);
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("oracionesAcomodar").document("hmfKcV37zzMOIxVzK1fp");
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    palabraObtenida = document.getString("Palabra");
                    Handler handler = new Handler();
                    final Runnable r = () -> {
                        String[] words = palabraObtenida.split(" ");
                        int i=0;
                        for(String palabra : words) {
                            i++;
                            obtenerLugarAleatorio(palabra);
                        }
                    };
                    handler.postDelayed(r, 100);
                }
            }
        });


        binding.palabra1.setOnClickListener(view1->{
            binding.oracionCompleta.setText(binding.oracionCompleta.getText() +" "+binding.palabra1.getText());
            binding.palabra1.setEnabled(false);
        });
        binding.palabra2.setOnClickListener(view2->{
            binding.oracionCompleta.setText(binding.oracionCompleta.getText() +" "+binding.palabra2.getText());
            binding.palabra2.setEnabled(false);
        });
        binding.palabra3.setOnClickListener(view3->{
            binding.oracionCompleta.setText(binding.oracionCompleta.getText() +" "+binding.palabra3.getText());
            binding.palabra3.setEnabled(false);
        });
        binding.palabra4.setOnClickListener(view4->{
            binding.oracionCompleta.setText(binding.oracionCompleta.getText() +" "+binding.palabra4.getText());
            binding.palabra4.setEnabled(false);
        });
        binding.palabra5.setOnClickListener(view5->{
            binding.oracionCompleta.setText(binding.oracionCompleta.getText() +" "+binding.palabra5.getText());
            binding.palabra5.setEnabled(false);
        });
        binding.palabra6.setOnClickListener(view6->{
            binding.oracionCompleta.setText(binding.oracionCompleta.getText() +" "+binding.palabra6.getText());
            binding.palabra6.setEnabled(false);
        });
        binding.palabra7.setOnClickListener(view7->{
            binding.oracionCompleta.setText(binding.oracionCompleta.getText() +" "+binding.palabra7.getText());
            binding.palabra7.setEnabled(false);
        });
        binding.palabra8.setOnClickListener(view8->{
            binding.oracionCompleta.setText(binding.oracionCompleta.getText() +" "+binding.palabra8.getText());
            binding.palabra8.setEnabled(false);
        });
        binding.palabra9.setOnClickListener(view9->{
            binding.oracionCompleta.setText(binding.oracionCompleta.getText() +" "+binding.palabra9.getText());
            binding.palabra9.setEnabled(false);
        });
        binding.palabra10.setOnClickListener(view10->{
            binding.oracionCompleta.setText(binding.oracionCompleta.getText() +" "+binding.palabra10.getText());
            binding.palabra10.setEnabled(false);
        });
        binding.btnReiniciar.setOnClickListener(view11->{
        binding.oracionCompleta.setText("");
            binding.palabra1.setEnabled(true);
            binding.palabra2.setEnabled(true);
            binding.palabra3.setEnabled(true);
            binding.palabra4.setEnabled(true);
            binding.palabra5.setEnabled(true);
            binding.palabra6.setEnabled(true);
            binding.palabra7.setEnabled(true);
            binding.palabra8.setEnabled(true);
            binding.palabra9.setEnabled(true);
            binding.palabra10.setEnabled(true);
        });
        binding.btnTerminar.setOnClickListener(view12->{
            if(binding.oracionCompleta.getText().toString().trim().equals(palabraObtenida)){
                Toast.makeText(getApplicationContext(),"Ganaste!",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"Suerte para la proxima!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void obtenerLugarAleatorio(String palabra){
        if(binding.palabra1.getText().toString().equals("")){
            binding.palabra1.setText(palabra);
        }else if(binding.palabra9.getText().toString().equals("")){
            binding.palabra9.setText(palabra);
        }else if(binding.palabra10.getText().toString().equals("")){
            binding.palabra10.setText(palabra);
        }else if(binding.palabra5.getText().toString().equals("")){
            binding.palabra5.setText(palabra);
        }else if(binding.palabra4.getText().toString().equals("")){
            binding.palabra4.setText(palabra);
        }else if(binding.palabra3.getText().toString().equals("")){
            binding.palabra3.setText(palabra);
        }else if(binding.palabra7.getText().toString().equals("")){
            binding.palabra7.setText(palabra);
        }else if(binding.palabra8.getText().toString().equals("")){
            binding.palabra8.setText(palabra);
        }else if(binding.palabra2.getText().toString().equals("")){
            binding.palabra2.setText(palabra);
        }else if(binding.palabra6.getText().toString().equals("")){
            binding.palabra6.setText(palabra);
        }
    }
}