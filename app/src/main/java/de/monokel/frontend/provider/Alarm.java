package de.monokel.frontend.provider;

import de.monokel.frontend.MainActivity;
import de.monokel.frontend.risklevel.AmountOfContactsEnum;
import de.monokel.frontend.risklevel.RiskLevel;
import de.monokel.frontend.risklevel.TypeOfExposureEnum;

/**
 * This class has the method which is called once a day.
 *
 * @author Miftari, Leibl
 * @version 2020-11-02
 */
public class Alarm {

    /**
     * This method is called once a day.
     */
    public static void dailyBusiness() {
        //delete all keys older then 3 weeks.
        LocalKeySafer.addKeyPairToSavedKeyPairs(null);
        LocalNotificationSafer.addNotificationToSavedNotifications(null);

        //update the information about the date of the first usage and the days since the app is used
        MainActivity.showDaysSinceUse();

        //calculate and update the riskLevel
        RiskLevel.updateRiskLevel(RiskLevel.calculateRiskLevel(TypeOfExposureEnum.NO_CONTACT), true);

        //update current risk status (traffic light and risk status title) on main screen
        MainActivity.showTrafficLightStatus();
        MainActivity.showRiskStatus();


    }
}
