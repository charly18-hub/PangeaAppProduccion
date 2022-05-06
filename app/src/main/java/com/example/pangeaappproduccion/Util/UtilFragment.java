package com.example.pangeaappproduccion.Util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class UtilFragment extends Fragment {
    public void establecerIdioma(String idioma){
        Locale locale = new Locale(idioma);
        Locale.setDefault(locale);
        Resources resources = requireActivity().getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
    public void establecerIdioma(){
        SharedPreferences prefs = requireActivity().getSharedPreferences("usuarioRegistroNormal", Context.MODE_PRIVATE);
        String idioma = prefs.getString("idioma", "No hay nada");
        if(!idioma.equals("No hay nada")){
            establecerIdioma(idioma);
        }
    }
    public void obtenerIdiomaPreferido(@NonNull String idioma){
        if(idioma.equals("Español") || idioma.equals("Spanish")){
            idioma = "es";
        }else if(idioma.equals("English") || idioma.equals("Ingles")){
            idioma = "en";
        }
        SharedPreferences.Editor editor = requireActivity().getSharedPreferences("usuarioRegistroNormal", MODE_PRIVATE).edit();
        editor.putString("idioma", idioma);
        editor.apply();
        establecerIdioma(idioma);
    }
}
