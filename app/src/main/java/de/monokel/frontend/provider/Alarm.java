package de.monokel.frontend.provider;

/**
 * This class has the method which is called once a day.
 *
 * @author Miftari
 */
public class Alarm {

    /**
     * This method is called once a day.
     */
    public static void dailyBusiness() {
        //delete all keys older then 3 weeks.
        LocalKeySafer.addKeyPairToSavedKeyPairs(null);
    }
}
