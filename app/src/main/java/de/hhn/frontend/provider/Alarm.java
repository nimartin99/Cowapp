package de.hhn.frontend.provider;

import de.hhn.frontend.MainActivity;
import de.hhn.frontend.risklevel.RiskLevel;
import de.hhn.frontend.risklevel.TypeOfExposureEnum;

/**
 * This class has the method which is called once a day.
 *
 * @author Miftari, Leibl, Alessandrini
 * @version 2020-11-11
 */
public class Alarm {

    /**
     * This method is called once a day.
     */
    public static void dailyBusiness() {
        //delete all keys older then 3 weeks.
        LocalSafer.addKeyPairToSavedKeyPairs(null);
        LocalSafer.addNotificationToSavedNotifications(null);

        //update the information about the date of the first usage and the days since the app is used
        // MainActivity.showDaysSinceUse();

        // check if user has had direct or indirect contact
        MainActivity.requestInfectionStatus();

        //calculate and update the riskLevel
        RiskLevel.updateRiskLevel(RiskLevel.calculateRiskLevel(TypeOfExposureEnum.NO_CONTACT), true);

        //update current risk status (traffic light and risk status title) on main screen
        MainActivity.showTrafficLightStatus();
        MainActivity.showRiskStatus();
    }

    /**
     * This method is called all fifteen Minutes.
     */
    public static void fifteenMinutesBusiness() {
        // request a new key
        MainActivity.requestKey();
    }

    /**
     * This method is called all five Minutes.
     */
    public static void fiveMinutesBusiness() {
    }

    public static void ring() {
        fiveMinutesBusiness();
        int i = LocalSafer.getAlarmCounter();
        i++;

        if ((i % 3) == 0) {
            fifteenMinutesBusiness();
        }

        if (i == 288) {
            dailyBusiness();
            i = 0;
        }
        LocalSafer.safeAlarmCounter(i);
    }
}
