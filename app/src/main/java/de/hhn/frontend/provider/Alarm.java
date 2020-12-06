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

        LocalSafer.analyzeBufferFile(null);

        //delete all keys older then 3 weeks.
        LocalSafer.addKeyPairToSavedKeyPairs(null, null);
        LocalSafer.addNotificationToSavedNotifications(null, null);

        //update the information about the date of the first usage and the days since the app is used
        if (MainActivity.getMainActivity() != null) {
            MainActivity.showDateDisplay();
        }

        // in case of a current infection, check if it is older than 14 days and if that is true, set the infection status from infected to not infected and start the BLE key exchange.
        if (LocalSafer.getRiskLevel(null) == 100) {
            RiskLevel.checkIfInfectionHasExpired();
        }

        //request contacts from the server and calculates the risk level if there is no current infection
        if (LocalSafer.getRiskLevel(null) != 100) {
            MainActivity.requestInfectionStatus();
        } else {
            Log.d("Alarm", "due to a current infection no contacts were requested and no risk level was calculated");
        }

        //request a new key when the User is not infected.
        if (LocalSafer.getRiskLevel(null) != 100) {
            MainActivity.requestKey();
        } else {
            Log.d("Alarm", "due to a current infection no key was requested");
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
