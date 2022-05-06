package com.example.pangeaappproduccion.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class UtilActivity extends AppCompatActivity {
    public void establecerIdioma(String idioma){
        Locale locale = new Locale(idioma);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
    public void establecerIdioma(){
        SharedPreferences prefs = this.getSharedPreferences("usuarioRegistroNormal", Context.MODE_PRIVATE);
        String idioma = prefs.getString("idioma", "No hay nada");
        if(!idioma.equals("No hay nada")){
            establecerIdioma(idioma);
        }
    }
}
