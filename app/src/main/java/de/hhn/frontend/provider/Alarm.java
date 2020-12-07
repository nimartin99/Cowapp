package de.hhn.frontend.provider;

import android.util.Log;

import de.hhn.frontend.Constants;
import de.hhn.frontend.MainActivity;
import de.hhn.frontend.R;
import de.hhn.frontend.keytransfer.BeaconBackgroundService;
import de.hhn.frontend.risklevel.RiskLevel;

/**
 * This class has the method which is called once a day.
 *
 * @author Miftari, Leibl, Alessandrini, Klein
 * @version 2020-12-06
 */
public class Alarm {
    private static final String TAG = Alarm.class.getSimpleName();

    /**
     * This method is called all fifteen Minutes.
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

    public static void ring() {
        Log.d("requestLine", "Alarm: ring() was called ");
        if (LocalSafer.shouldRingAgain(null)) {
            fifteenMinutesBusiness();
            if (Constants.DEBUG_MODE && LocalSafer.isAlarmRingLogged(null)) {
                LocalSafer.addLogValueToDebugLog(BeaconBackgroundService.getAppContext().getString(R.string.alarm_ringed), null);
            }
        }
    }
}
