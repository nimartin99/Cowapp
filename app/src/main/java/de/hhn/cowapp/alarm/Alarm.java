package de.hhn.cowapp.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.Calendar;

import de.hhn.cowapp.utils.Constants;
import de.hhn.cowapp.gui.MainActivity;
import de.hhn.cowapp.R;
import de.hhn.cowapp.keytransfer.BeaconBackgroundService;
import de.hhn.cowapp.datastorage.LocalSafer;
import de.hhn.cowapp.risklevel.RiskLevel;

/**
 * This class provides an Alarm which is called about all 15 minutes to trigger action,
 * which are necessary for the Application to work.
 *
 * @author Miftari, Leibl, Alessandrini, Klein
 * @version 2020-12-06
 */
public class Alarm {
    private static final String TAG = Alarm.class.getSimpleName();
    private static PendingIntent myPendingIntent;
    private static AlarmManager alarmManager;
    private static BroadcastReceiver myBroadcastReceiver;
    private static Calendar firingCal;

    /**
     * This method is called around all fifteen Minutes.
     */
    public static void fifteenMinutesBusiness() {
        Log.d(TAG, "Alarm: fifteenMinutesBusiness() was called ");

        //analyze the buffer file
        LocalSafer.analyzeBufferFile(null);

        //delete all keys older then 3 weeks.
        LocalSafer.addKeyPairToSavedKeyPairs(null, null);
        LocalSafer.addNotificationToSavedNotifications(null, null);

        //update the information about the date of the first usage and the days since the app is used
        if (MainActivity.getMainActivity() != null) {
            MainActivity.showDateDisplay();
        }

        if (LocalSafer.getRiskLevel(null) == 100) {
            RiskLevel.checkIfInfectionHasExpired();
            Log.d("Alarm", "due to a current infection no contacts were requested and no risk level was calculated");
            Log.d("Alarm", "due to a current infection no key was requested");
        } else {
            MainActivity.requestInfectionStatus();
            MainActivity.requestKey();
        }
    }

    /**
     * This method is called when the alarm rings.
     */
    public static void ring() {
        Log.d("requestLine", "Alarm: ring() was called ");
        if (LocalSafer.shouldRingAgain(null)) {
            fifteenMinutesBusiness();
            if (Constants.DEBUG_MODE && LocalSafer.isAlarmRingLogged(null)) {
                LocalSafer.addLogValueToDebugLog(BeaconBackgroundService.getAppContext().getString(R.string.alarm_ringed), null);
            }
        }
    }

    /**
     * This method creates the alarm, if it is not already created.
     */
    public static void setAlarm() {
        boolean alarmUp = (PendingIntent.getBroadcast(BeaconBackgroundService.getAppContext(), 0, new Intent("com.alarm.example"), PendingIntent.FLAG_NO_CREATE) != null);

        if (alarmUp == false) {
            LocalSafer.shouldRingAgain(null);
            Log.d("requestLine", "Alarm: Alarm was set");
            Log.i(TAG, "onCreate: Alarm is set");

            firingCal = Calendar.getInstance();
            firingCal.set(Calendar.HOUR, 0);
            firingCal.set(Calendar.MINUTE, 15);
            firingCal.set(Calendar.SECOND, 0);
            long intendedTime = firingCal.getTimeInMillis();

            registerMyAlarmBroadcast();

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, intendedTime, (15 * 60 * 1000), myPendingIntent);

            if (Constants.DEBUG_MODE && LocalSafer.isAlarmSetLogged(null)) { //for debugging
                LocalSafer.addLogValueToDebugLog(BeaconBackgroundService.getAppContext().getString(R.string.alarm_set), null);
            }
        } else {
            Log.i(TAG, "onCreate: Alarm was already set. No resetting necessary");
            Log.d("requestLine", "Alarm: Alarm was not set, because already set.");
        }
    }

    /**
     * This method registers the AlarmBroadcast and defines what should happen when the alarm is called.
     */
    private static void registerMyAlarmBroadcast() {

        myBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Alarm.ring(); //Action to happen, when the alarm is called.
            }
        };

        BeaconBackgroundService.getAppContext().registerReceiver(myBroadcastReceiver, new IntentFilter("com.alarm.example"));
        myPendingIntent = PendingIntent.getBroadcast(BeaconBackgroundService.getAppContext(), 0, new Intent("com.alarm.example"), 0);
        alarmManager = (AlarmManager) (BeaconBackgroundService.getAppContext().getSystemService(Context.ALARM_SERVICE));
    }
}
