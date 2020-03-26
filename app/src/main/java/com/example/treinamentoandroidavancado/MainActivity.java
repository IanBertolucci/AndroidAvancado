package com.example.treinamentoandroidavancado;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnSimples, btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSimples = (Button) findViewById(R.id.btnSimples);
        btnSimples.setOnClickListener(this);

        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(this);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel("1", "canal1", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSimples:
                criarNotificacaoSimples();
                break;
            case R.id.btnCancelar:
                cancelarNotificacao(1);
                break;
        }
    }

    public void criarNotificacaoSimples(){
        int id = 1;
        String titulo = "Título da notificação";
        String texto = "Notificação simples";
        int icone = android.R.drawable.ic_dialog_info;

        Intent intent = new Intent(this, TextoActivity.class);
        intent.putExtra("txt", "Notificação aberta");
        PendingIntent p = getPendingIntent(id, intent, this);

        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(this, "1");
        notificationCompat.setSmallIcon(icone);
        notificationCompat.setContentTitle(titulo);
        notificationCompat.setContentText(texto);
        notificationCompat.setContentIntent(p);
        notificationCompat.setDefaults(Notification.DEFAULT_ALL);
        notificationCompat.setVibrate(new long[]{1000,1000,1000,1000,1000});
        notificationCompat.setAutoCancel(true);

        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        nm.notify(id, notificationCompat.build());
    }

    private PendingIntent getPendingIntent(int id, Intent intent, Context context){
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(intent.getComponent());
        stackBuilder.addNextIntent(intent);

        PendingIntent p = stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);
        return p;
    }

    public void cancelarNotificacao(int id){
        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        nm.cancel(id);
    }
}
