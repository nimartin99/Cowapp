package de.monokel.frontend.provider;

import de.monokel.frontend.MainActivity;

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

        //update current risk status (traffic light and risk status title) on main screen
        //MainActivity.showTrafficLightStatus();
        //MainActivity.showRiskStatus();
    }
}
