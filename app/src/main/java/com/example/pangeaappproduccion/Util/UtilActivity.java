package com.example.pangeaappproduccion.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class UtilActivity extends AppCompatActivity {
    public void establecerIdioma(){
        SharedPreferences prefs = this.getSharedPreferences("usuarioRegistroNormal", Context.MODE_PRIVATE);
        String lenguaje = prefs.getString("idioma", "No hay nada");
        if(!lenguaje.equals("No hay nada")){
            Locale locale = new Locale(lenguaje);
            Locale.setDefault(locale);
            Resources resources = this.getResources();
            Configuration config = resources.getConfiguration();
            config.setLocale(locale);
            resources.updateConfiguration(config, resources.getDisplayMetrics());
        }
    }
}
