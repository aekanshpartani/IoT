package com.example.sinha.iot;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);

        Map<String, String> data = remoteMessage.getData();
        String myCustomKey = data.get("my_custom_key");
        showNotification(data.get("title"),data.get("body"));



        //Toast.makeText(this, "Daala", Toast.LENGTH_SHORT).show();

    }

    public void showNotification(String title,String message)
    {
//        Intent intent = new Intent(this, PayActivity.class);
//        PendingIntent payInent = PendingIntent.getActivity(MyMessagingService.this, 0, intent, 0);
//        Intent Laterintent = new Intent(this, PayLater.class);
//        PendingIntent payLaterInent = PendingIntent.getActivity(MyMessagingService.this, 0, Laterintent, 0);

        Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.girl);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"Notify")
//                .setContentTitle(title).setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)

//              //  .addAction(R.drawable.user,"Pay",payInent)
//                //.addAction(R.drawable.user,"Pay Later",payLaterInent)
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setPriority(NotificationManager.IMPORTANCE_HIGH)
//                .setAutoCancel(true).setContentText(message).setLargeIcon(icon)
//                .setStyle(new NotificationCompat.BigPictureStyle()
//                        .bigPicture(icon).bigLargeIcon(null));

//        NotificationCompat.Builder builder;
//        builder = new NotificationCompat.Builder(this, "Notify");
//        builder.setContentTitle(title)                            // required
//                .setSmallIcon(R.drawable.user)   // required
//                .setContentText(message) // required
//                .setStyle(new NotificationCompat.BigPictureStyle()
//                        .bigPicture(icon).setSummaryText(""))
//                .setAutoCancel(true)
//                .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
//                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
//        .setPriority(NotificationCompat.PRIORITY_HIGH)
//        ;

        Notification notification = null;
        if( message.charAt(0) == 'C' )
        {
            Intent intent = new Intent(this, PayLater.class);
            PendingIntent payInent = PendingIntent.getActivity(MyMessagingService.this, 0, intent, 0);
            notification = new NotificationCompat.Builder(this, "Notify")
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVibrate(new long[0])// heads-up
                    .addAction(R.drawable.user,"Pay",payInent)
                    .build();
        }
        else if( message.charAt(0) == 'G' )
        {
            //start activity go in
        }
        else if( message.charAt(0) == 'I' )
        {
            //start activity invalid slot
        }

        NotificationManager m = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);;
        m.notify(999,notification);
    }
}