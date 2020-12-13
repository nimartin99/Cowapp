package de.hhn.cowapp.notificationservice;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import de.hhn.cowapp.gui.LogActivity;
import de.hhn.cowapp.gui.MainActivity;
import de.hhn.cowapp.R;
import de.hhn.cowapp.datastorage.LocalSafer;

/**
 * Notification service for push-notifications
 *
 * @author Tabea leibl
 * @author Philipp Alessandrini
 * @version 2020-10-28
 */
public class NotificationService extends Service {

    final int notificationId = 1;
    private static final int NO_CONNECTION_NOTIFICATION_INTERVAL = 10;
    //visibility of the push notification
    private NotificationManagerCompat notificationManagerCom;

    private String notificationTitle;
    private String notificationText;
    private Class notificationClass;

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        notificationTitle = intent.getStringExtra("TITLE");
        notificationText = intent.getStringExtra("TEXT");
        boolean shouldBeLogged = intent.getBooleanExtra("LOG", true);
        try {
            notificationClass = (Class<Activity>)intent.getSerializableExtra("CLASS");
        } catch (Exception e) {
            notificationClass = null;
        }

        notificationManagerCom = NotificationManagerCompat.from(this);
        displayNotification(notificationTitle, notificationText, notificationClass, shouldBeLogged);
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    private void displayNotification(String title, String text, Class intentClass, boolean shouldBeLogged){
        if (shouldBeLogged) {
            LocalSafer.addNotificationToSavedNotifications(title + " - " + text, null);
            LogActivity.renewTheLog();
        }
        Intent pushIntent;
        PendingIntent pushPendingIntent;
        // check if the notification leads to another activity
        if (intentClass != null) {
            // Creates an explicit intent for the push activity screen of the CoWApp (activity is called when tapping the notification)
            pushIntent = new Intent(this, intentClass);
            pushPendingIntent = PendingIntent.getActivity(this, 0, pushIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            pushPendingIntent = null;
        }

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

    public static int getNoConnectionNotificationInterval() {
        return NO_CONNECTION_NOTIFICATION_INTERVAL;
    }
}