package de.monokel.frontend.provider;

public class Alarm {

    public static void dailyBusiness() {
        //delete all keys older then 3 weeks.
        LocalKeySafer.addKeyPairToSavedKeyPairs(null);
    }
}
