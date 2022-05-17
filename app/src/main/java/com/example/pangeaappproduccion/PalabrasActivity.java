package com.example.pangeaappproduccion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.pangeaappproduccion.databinding.ActivityPalabrasBinding;
import com.example.pangeaappproduccion.databinding.ActivityPerfilAmigosBinding;

import java.util.ArrayList;
import java.util.List;

public class PalabrasActivity extends AppCompatActivity {
    List<Integer> numeros=new ArrayList<>();
    ActivityPalabrasBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityPalabrasBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        numeros.add(0);

        String palabraObtenida = "Hi how are you welcome to Tutorialspoint";
        String[] words = palabraObtenida.split(" ");
        int i=0;
        for(String palabra : words) {
            i++;
            obtenerLugarAleatorio(palabra);
        }
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
            if(binding.oracionCompleta.getText().toString().equals(palabraObtenida));
        });
    }

    public void obtenerLugarAleatorio(String palabra){
        int numeroAleatorio = obtenerNumeroAleatorio();
        if(numeroAleatorio==1 && binding.palabra1.getText().toString().equals("")){
            binding.palabra1.setText(palabra);
        }else if(numeroAleatorio==2 && binding.palabra1.getText().toString().equals("")){
            binding.palabra2.setText(palabra);
        }else if(numeroAleatorio==3 && binding.palabra1.getText().toString().equals("")){
            binding.palabra3.setText(palabra);
        }else if(numeroAleatorio==4 && binding.palabra1.getText().toString().equals("")){
            binding.palabra4.setText(palabra);
        }else if(numeroAleatorio==5 && binding.palabra1.getText().toString().equals("")){
            binding.palabra5.setText(palabra);
        }else if(numeroAleatorio==6 && binding.palabra1.getText().toString().equals("")){
            binding.palabra6.setText(palabra);
        }else if(numeroAleatorio==7 && binding.palabra1.getText().toString().equals("")){
            binding.palabra7.setText(palabra);
        }else if(numeroAleatorio==8 && binding.palabra1.getText().toString().equals("")){
            binding.palabra8.setText(palabra);
        }else if(numeroAleatorio==9 && binding.palabra1.getText().toString().equals("")){
            binding.palabra9.setText(palabra);
        }else if(numeroAleatorio==10 && binding.palabra1.getText().toString().equals("")){
            binding.palabra10.setText(palabra);
        }else{

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
            obtenerNumeroAleatorio();
        }else{
            numeros.add(numeroAleatorio);
        }
        return numeroAleatorio;
    }
}