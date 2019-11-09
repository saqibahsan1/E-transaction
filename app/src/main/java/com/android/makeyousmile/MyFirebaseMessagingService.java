package com.android.makeyousmile;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.android.makeyousmile.ui.Activities.MainActivity;
import com.android.makeyousmile.ui.Utility.Utils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onNewToken(@NonNull String s) {
        Utils.getInstance().setDefaults("token",s,getApplicationContext());
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(getApplicationContext(),remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }



    public static void showNotification(Context context, String title, String body) {
        Intent notificationIntent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 2, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificatioMng = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String CHANNEL_ID = "com.android.makeyousmile";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, title, importance);
            mChannel.setDescription(body);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificatioMng.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.image_icon)
                .setContentTitle(title)
                .setContentText(body)
                .setChannelId(CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent).build();

        builder.setAutoCancel(true);
        builder.build().flags |= Notification.FLAG_AUTO_CANCEL;

        Random r = new Random();
        int i1 = r.nextInt(80 - 65) + 65;

        notificatioMng.notify(i1, builder.build());
    }

}
