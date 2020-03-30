package com.example.treinamentoandroidavancado;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification() != null) {
            Log.i("notificação", String.valueOf(remoteMessage.getNotification().getBody()));
            enviarNotificacao(remoteMessage.getNotification().getBody(), remoteMessage.getData());
        }

        if (remoteMessage.getData().size() != 0) {
            Log.i("dados", String.valueOf(remoteMessage.getData()));
        }
    }

    private void enviarNotificacao(String msg, Map<String, String> dados){
        Intent intent = new Intent(this, FcmActivity.class);

        Bundle bundle = new Bundle();
        for (String key : dados.keySet()){
            String valor = dados.get(key).toString();
            bundle.putString(key, valor);
        }

        intent.putExtras(bundle);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Firebase CM")
                .setContentText(msg)
                .setAutoCancel(true)
                .setSound(som)
                .setContentIntent(pendingIntent)
                .setChannelId("channel1");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }


}
