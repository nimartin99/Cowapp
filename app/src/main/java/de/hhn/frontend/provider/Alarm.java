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
 * @version 2020-11-11
 */
public class Alarm {

    /**
     * This method is called all fifteen Minutes.
     */
    public static void fifteenMinutesBusiness() {
        LocalSafer.analyzeBufferFile(null);

        //delete all keys older then 3 weeks.
        LocalSafer.addKeyPairToSavedKeyPairs(null, null);
        LocalSafer.addNotificationToSavedNotifications(null, null);

        //update the information about the date of the first usage and the days since the app is used
        MainActivity.showDateDisplay();

        //check if the infection is older than 14 days and reset the infection status if that is the case
        RiskLevel.checkIfInfectionHasExpired();

        //request contacts from the server and calculates the risk level if there is no current infection

        if (LocalSafer.getRiskLevel(null) != 100) {
            MainActivity.requestInfectionStatus();
            RiskLevel.calculateRiskLevel();
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
        fifteenMinutesBusiness();
        if (Constants.DEBUG_MODE && LocalSafer.isAlarmRingLogged(null)) {
            LocalSafer.addLogValueToDebugLog(BeaconBackgroundService.getAppContext().getString(R.string.alarm_ringed), null);
        }
    }
}
