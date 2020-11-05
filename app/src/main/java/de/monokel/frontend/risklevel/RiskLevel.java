package de.monokel.frontend.risklevel;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import de.monokel.frontend.provider.LocalRiskLevelSafer;

/**
 * Klasse mit allen Operationen welche das Risikolevel betreffen.
 * Das Risikolevel ist ein Wert zwischen 0 und 100.
 * Er soll immer jeden Tag aktualisiert werden, oder wenn eine Infektionsmeldung durchgeführt wurde
 *
 * @author jonas
 * @date 26.10.2020
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

        } else if (LocalRiskLevelSafer.getRiskLevel() > newRiskLevel || isDailyUpdate) {

            increaseDaysSinceLastContact();

        } else if (LocalRiskLevelSafer.getRiskLevel() > newRiskLevel || !isDailyUpdate) {

        } else if (LocalRiskLevelSafer.getRiskLevel() < newRiskLevel || isDailyUpdate) {
            setRiskLevel(newRiskLevel);
            resetDaysSinceLastContact();


        } else if (LocalRiskLevelSafer.getRiskLevel() < newRiskLevel || !isDailyUpdate) {
            setRiskLevel(newRiskLevel);
            resetDaysSinceLastContact();
        }
    }

    /**
     * gleiche Methode wie die daurauffolgende, nur ohne den 2. Parameter, da dieser hier nicht berücksichtig werden muss.
     *
     * @param action Aktion welche beim berechnen des Risikolevels berücktsichtigt werden soll, hier wird jedoch nur ein NO_CONTACT berücktsicht, für alle anderen Fälle muss die darauffolgende Methode verwendet werden!(Diese hat einen weiteren Enum Parameter)
     */

    public static int calculateRiskLevel(TypeOfExposureEnum action) {
        if (action == TypeOfExposureEnum.NO_CONTACT) {

            checkDaysSinceLastContact();
        }
        return 0;
    }

    /**
     * Methode zum berechnen des Risikolevels anhand der übergebenen Aktion
     *
     * @param action Art der Aktion welche eine Infektion zur Folge haben könnte
     * @param amount Anzahl wie oft die zuvor genannte Aktion stattgefunden, z.B.: es wurden 1-wenige Schlüssel ausgetauscht, es wurden wenige - einige Schlüssel ausgetauscht oder eben einige bis viele Schlüssel ausgetauscht.
     * @return berechneter Wert des Risikolevels
     */


    public int calculateRiskLevel(TypeOfExposureEnum action, AmountOfContactsEnum amount) {

        int localRiskLevel = 101;

        if (action == TypeOfExposureEnum.SHORT_EXPOSURE_INDIRECT_CONTACT || amount == AmountOfContactsEnum.FEW) {
            localRiskLevel = 20;

        } else if (action == TypeOfExposureEnum.SHORT_EXPOSURE_INDIRECT_CONTACT || amount == AmountOfContactsEnum.SOME) {
            localRiskLevel = 30;
        } else if (action == TypeOfExposureEnum.SHORT_EXPOSURE_INDIRECT_CONTACT || amount == AmountOfContactsEnum.MANY) {
            localRiskLevel = 35;


        } else if (action == TypeOfExposureEnum.LONG_EXPOSURE_INDIRECT_CONTACT || amount == AmountOfContactsEnum.FEW) {
            localRiskLevel = 40;
        } else if (action == TypeOfExposureEnum.LONG_EXPOSURE_INDIRECT_CONTACT || amount == AmountOfContactsEnum.SOME) {
            localRiskLevel = 50;
        } else if (action == TypeOfExposureEnum.LONG_EXPOSURE_INDIRECT_CONTACT || amount == AmountOfContactsEnum.SOME) {
            localRiskLevel = 60;


        } else if (action == TypeOfExposureEnum.SHORT_EXPOSURE_DIRECT_CONTACT || amount == AmountOfContactsEnum.FEW) {
            localRiskLevel = 90;
        } else if (action == TypeOfExposureEnum.SHORT_EXPOSURE_DIRECT_CONTACT || amount == AmountOfContactsEnum.SOME) {
            localRiskLevel = 95;
        } else if (action == TypeOfExposureEnum.SHORT_EXPOSURE_DIRECT_CONTACT || amount == AmountOfContactsEnum.MANY) {
            localRiskLevel = 97;


        } else if (action == TypeOfExposureEnum.LONG_EXPOSURE_DIRECT_CONTACT || amount == AmountOfContactsEnum.FEW) {
            localRiskLevel = 96;
        } else if (action == TypeOfExposureEnum.LONG_EXPOSURE_DIRECT_CONTACT || amount == AmountOfContactsEnum.SOME) {
            localRiskLevel = 98;
        } else if (action == TypeOfExposureEnum.LONG_EXPOSURE_DIRECT_CONTACT || amount == AmountOfContactsEnum.MANY) {
            localRiskLevel = 100;
        }

        return localRiskLevel;

    }

    public static void setRiskLevel(int newRiskLevel) {
        LocalRiskLevelSafer.safeRiskLevel(newRiskLevel);

    }

    public static void setDaysSinceLastUpdate(int newDaysSinceLastUpdate) {
        LocalRiskLevelSafer.safeDaysSinceLastContact(newDaysSinceLastUpdate);
    }

    public static void increaseDaysSinceLastContact() {
        int i = LocalRiskLevelSafer.getDaysSinceLastContact();
        i = i + 1;
        LocalRiskLevelSafer.safeDaysSinceLastContact(i);
    }


    public static void resetDaysSinceLastContact() {
        LocalRiskLevelSafer.safeDaysSinceLastContact(0);
    }

    public static void checkDaysSinceLastContact() {
        int daysSinceLastUpdate = LocalRiskLevelSafer.getDaysSinceLastContact();

        if (daysSinceLastUpdate > 14) {
            LocalRiskLevelSafer.safeRiskLevel(0);

        }
    }

}



