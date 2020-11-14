package de.hhn.frontend.risklevel;

import de.hhn.frontend.provider.LocalSafer;

/**
 * Classe with all operations needed for the risk level
 * the risk level is a value between 0 and 100 and represents the risk of a possible infection
 *
 * @author jonas
 * @version 14.11.2020
 */


public class RiskLevel {

    /**
     * Methode zur Aktualisierung des Risikolevels.
     *
     * @param newRiskLevel  ist das neue Risikolevel, sollte übergeben werden durch die Methode calculateRiskLevel.
     * @param isDailyUpdate (true/1:) Gibt an ob es sich um die tägliche Aktualisierung handelt, oder um den anderen Fall,(false/0:) dass es sich um eine Aktualisierung aufgrund einer Infektionsmeldung handelt.
     */
    public static void updateRiskLevel(int newRiskLevel, boolean isDailyUpdate) {

        checkDaysSinceLastContact();

        if (newRiskLevel < 0 || newRiskLevel > 100) {
            throw new IllegalArgumentException("Illegal Risk Level: must be an int value between 0 and 100!");

        } else if (LocalSafer.getRiskLevel() > newRiskLevel && isDailyUpdate) {

            increaseDaysSinceLastContact();

        } else if (LocalSafer.getRiskLevel() < newRiskLevel && isDailyUpdate) {
            setRiskLevel(newRiskLevel);
            resetDaysSinceLastContact();


        } else if (LocalSafer.getRiskLevel() < newRiskLevel && !isDailyUpdate) {
            setRiskLevel(newRiskLevel);
            resetDaysSinceLastContact();
        }
    }

    /**
     * smaller method with less parameters, used if the server does respond with information matching the TypeOfExposureEnum.
     *
     * @param action Type of action that could cause an infection
     */

    public static int calculateRiskLevel(TypeOfExposureEnum action) {

        int localRiskLevel = 101;

        if (action == TypeOfExposureEnum.NO_CONTACT) {
            localRiskLevel = 0;
            checkDaysSinceLastContact();
        } else if (action == TypeOfExposureEnum.INDIRECT_CONTACT) {
            localRiskLevel = 60;

        } else if (action == TypeOfExposureEnum.DIRECT_CONTACT) {
            localRiskLevel = 90;

        }

        return localRiskLevel;
    }

    /**
     * method to calculate the risk level, used when the server responds with information matching both enums: TypeOfExposureEnum and AmountOfContactsEnum
     *
     * @param action Type of action that could cause an infection
     * @param amount the amount how much a action occured with the same key  e.G.: 1 - few keys have been exchanged, few - some keys have been exchanged or some - many keys have been exchanged.
     * @return calculated value of the risk level
     */


    public static int calculateRiskLevel(TypeOfExposureEnum action, AmountOfContactsEnum amount) {

        int localRiskLevel = 101;

        if (action == TypeOfExposureEnum.SHORT_EXPOSURE_INDIRECT_CONTACT && amount == AmountOfContactsEnum.FEW) {
            localRiskLevel = 20;

        } else if (action == TypeOfExposureEnum.SHORT_EXPOSURE_INDIRECT_CONTACT && amount == AmountOfContactsEnum.SOME) {
            localRiskLevel = 30;
        } else if (action == TypeOfExposureEnum.SHORT_EXPOSURE_INDIRECT_CONTACT && amount == AmountOfContactsEnum.MANY) {
            localRiskLevel = 35;


        } else if (action == TypeOfExposureEnum.LONG_EXPOSURE_INDIRECT_CONTACT && amount == AmountOfContactsEnum.FEW) {
            localRiskLevel = 40;
        } else if (action == TypeOfExposureEnum.LONG_EXPOSURE_INDIRECT_CONTACT && amount == AmountOfContactsEnum.SOME) {
            localRiskLevel = 50;
        } else if (action == TypeOfExposureEnum.LONG_EXPOSURE_INDIRECT_CONTACT && amount == AmountOfContactsEnum.MANY) {
            localRiskLevel = 60;


        } else if (action == TypeOfExposureEnum.SHORT_EXPOSURE_DIRECT_CONTACT && amount == AmountOfContactsEnum.FEW) {
            localRiskLevel = 90;
        } else if (action == TypeOfExposureEnum.SHORT_EXPOSURE_DIRECT_CONTACT && amount == AmountOfContactsEnum.SOME) {
            localRiskLevel = 95;
        } else if (action == TypeOfExposureEnum.SHORT_EXPOSURE_DIRECT_CONTACT && amount == AmountOfContactsEnum.MANY) {
            localRiskLevel = 97;


        } else if (action == TypeOfExposureEnum.LONG_EXPOSURE_DIRECT_CONTACT && amount == AmountOfContactsEnum.FEW) {
            localRiskLevel = 96;
        } else if (action == TypeOfExposureEnum.LONG_EXPOSURE_DIRECT_CONTACT && amount == AmountOfContactsEnum.SOME) {
            localRiskLevel = 98;
        } else if (action == TypeOfExposureEnum.LONG_EXPOSURE_DIRECT_CONTACT && amount == AmountOfContactsEnum.MANY) {
            localRiskLevel = 100;
        }

        return localRiskLevel;

    }

    public static void setRiskLevel(int newRiskLevel) {
        LocalSafer.safeRiskLevel(newRiskLevel);

    }

    public static void setDaysSinceLastUpdate(int newDaysSinceLastUpdate) {
        LocalSafer.safeDaysSinceLastContact(newDaysSinceLastUpdate);
    }

    public static void increaseDaysSinceLastContact() {
        int i = LocalSafer.getDaysSinceLastContact();
        i = i + 1;
        LocalSafer.safeDaysSinceLastContact(i);
    }


    public static void resetDaysSinceLastContact() {
        LocalSafer.safeDaysSinceLastContact(0);
    }

    public static void checkDaysSinceLastContact() {
        int daysSinceLastUpdate = LocalSafer.getDaysSinceLastContact();

        if (daysSinceLastUpdate > 14) {
            LocalSafer.safeRiskLevel(0);

        }
    }

}



