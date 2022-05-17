package com.example.pangeaappproduccion.Util.Notifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.pangeaappproduccion.ChatActivity;
import com.example.pangeaappproduccion.MainActivity;
import com.example.pangeaappproduccion.R;
import com.example.pangeaappproduccion.SplashScreen;
import com.example.pangeaappproduccion.databinding.FragmentLlamadasBinding;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class PushNotificationService extends FirebaseMessagingService {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("NewApi")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        String title=message.getNotification().getTitle();
        String text=message.getNotification().getBody();
        String CHANNEL_ID="MESSAGE";

        if(title.equals("Nuevo Mensaje")){
            NotificationChannel channel= new NotificationChannel(
                    CHANNEL_ID,
                    "Message Notification",
                    NotificationManager.IMPORTANCE_HIGH);

            getSystemService(NotificationManager.class).createNotificationChannel(channel);

            Intent activityIntent = new Intent(this, FragmentLlamadasBinding.class);
            PendingIntent contentIntent= PendingIntent.getActivity(this,
                    0,activityIntent,0);

            Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentIntent(contentIntent)
                    .setOnlyAlertOnce(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true);
            NotificationManagerCompat.from(this).notify(1,notification.build());

        }else{

            NotificationChannel channel= new NotificationChannel(
                    CHANNEL_ID,
                    "Message Notification",
                    NotificationManager.IMPORTANCE_HIGH);
            getSystemService(NotificationManager.class).createNotificationChannel(channel);

            Intent activityIntent = new Intent(this, MainActivity.class);
            PendingIntent contentIntent= PendingIntent.getActivity(this,
                    0,activityIntent,0);



            Intent broadCastIntent = new Intent(this,RecibidorNotificaciones.class);
            broadCastIntent.putExtra("llamada","Aceptar");
            PendingIntent actionIntent= PendingIntent.getBroadcast(this,
                    0,broadCastIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Action action = new Notification.Action.Builder(
                    Icon.createWithResource(this, R.mipmap.ic_launcher),
                    "Aceptar",
                    actionIntent).build();




            Intent broadCastIntent2 = new Intent(this,RecibidorNotificaciones.class);
            broadCastIntent2.putExtra("llamada","Rechazar");
            PendingIntent actionIntent2= PendingIntent.getBroadcast(this,
                    1,broadCastIntent2,PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Action action2 = new Notification.Action.Builder(
                    Icon.createWithResource(this, R.mipmap.ic_launcher),
                    "Rechazar",
                    actionIntent2).build();

            Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentIntent(contentIntent)
                    .setOnlyAlertOnce(true)
                    .addAction(action)
                    .addAction(action2)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true);
            NotificationManagerCompat.from(this).notify(1,notification.build());

        }

        super.onMessageReceived(message);
    }

}
