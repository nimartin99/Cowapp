package de.hhn.frontend.provider;

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

        // check if user has had direct or indirect contact and calculate and update the riskLevel
        MainActivity.requestInfectionStatus();

        //update current risk status (traffic light and risk status title) on main screen
        MainActivity.showTrafficLightStatus();
        MainActivity.showRiskStatus();

        // request a new key
        MainActivity.requestKey();
    }

    public static void ring() {
        fifteenMinutesBusiness();
        if (LocalSafer.isAlarmRingLogged()) {
            LocalSafer.addLogValueToDebugLog(BeaconBackgroundService.getAppContext().getString(R.string.alarm_ringed));
        }
    }
}
