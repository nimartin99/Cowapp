package de.monokel.frontend.provider;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import de.monokel.frontend.MainActivity;
import de.monokel.frontend.PushNotificationActivity;
import de.monokel.frontend.R;

public class NotificationService extends Service {

    final int notificationId = 1;
    //visibility of the push notification
    private NotificationManagerCompat notificationManagerCom;

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        notificationManagerCom = NotificationManagerCompat.from(this);
        displayNotification("Mögliches Gesundheitsrisiko", "Hier klicken für weitere Informationen.");
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    private void displayNotification(String title, String text){
        // Creates an explicit intent for the push activity screen of the CoWApp (activity is called when tapping the notification)
        Intent pushIntent = new Intent(this, PushNotificationActivity.class);
        PendingIntent pushPendingIntent = PendingIntent.getActivity(this, 0, pushIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //build push notification itself
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title) //title of notification
                .setContentText(text) //message shown in the notification
                .setContentIntent(pushPendingIntent) //sets the intent to react on a tap on the notification
                .setAutoCancel(true) //automatically removes the notification when tapped on
                .setPriority(NotificationCompat.PRIORITY_HIGH); //high priority for heads-up notification for android < 8.0

        NotificationManager notManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notManager.notify(notificationId, builder.build());
    }
}