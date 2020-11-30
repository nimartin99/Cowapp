package de.hhn.frontend.provider;

import de.hhn.frontend.Constants;
import de.hhn.frontend.MainActivity;
import de.hhn.frontend.R;
import de.hhn.frontend.keytransfer.BeaconBackgroundService;
import de.hhn.frontend.risklevel.NewRiskLevel;

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

        if (LocalSafer.getRiskLevel(null) != 100) {
            MainActivity.requestInfectionStatus();
            NewRiskLevel.calculateRiskLevel();
        }

        // request a new key
        MainActivity.requestKey();
    }

    public static void ring() {
        fifteenMinutesBusiness();
        if (Constants.DEBUG_MODE && LocalSafer.isAlarmRingLogged(null)) {
            LocalSafer.addLogValueToDebugLog(BeaconBackgroundService.getAppContext().getString(R.string.alarm_ringed), null);
        }
    }
}
