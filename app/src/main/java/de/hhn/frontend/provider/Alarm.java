package de.hhn.frontend.provider;

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
        LocalSafer.analyzeBufferFile();

        //delete all keys older then 3 weeks.
        LocalSafer.addKeyPairToSavedKeyPairs(null);
        LocalSafer.addNotificationToSavedNotifications(null);

        //update the information about the date of the first usage and the days since the app is used
        MainActivity.showDateDisplay();

        //request contacts from the server and calculates the risk level if there is no current infection
        if (LocalSafer.getRiskLevel() != 100) {
            MainActivity.requestInfectionStatus();
            RiskLevel.calculateRiskLevel();
        }

        //request a new key when the User is not infected.
        if (LocalSafer.getRiskLevel() != 100) {
            MainActivity.requestKey();
        }
    }

    public static void ring() {
        fifteenMinutesBusiness();
        if (Constants.DEBUG_MODE && LocalSafer.isAlarmRingLogged()) {
            LocalSafer.addLogValueToDebugLog(BeaconBackgroundService.getAppContext().getString(R.string.alarm_ringed));
        }
    }
}
