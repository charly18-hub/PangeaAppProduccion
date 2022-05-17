package com.example.pangeaappproduccion.Util.Notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class RecibidorNotificaciones extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String tipo= intent.getStringExtra("llamada");
        if(tipo.equals("Rechazar")){
            Toast.makeText(context,"Has Rechazado la llamada",Toast.LENGTH_SHORT).show();
        }else if (tipo.equals("Aceptar")){
            Toast.makeText(context,"Has Aceptado la llamada",Toast.LENGTH_SHORT).show();
        }
    }
}
