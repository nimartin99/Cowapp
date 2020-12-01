package de.hhn.frontend.risklevel;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

import de.hhn.frontend.date.DateHelper;
import de.hhn.frontend.keytransfer.BeaconBackgroundService;
import de.hhn.frontend.provider.LocalSafer;

/**
 * New Risk Level Class.
 * Contains everything needed to calculate the RiskLevel value
 *
 * @author jonas
 * @version 23.11.2020
 */

public class RiskLevel {

    private static final String TAG = "RiskLevel";
    private static ArrayList<IndirectContact> indirectContactArrayList = LocalSafer.getListOfIndirectContacts();
    private static ArrayList<DirectContact> directContactArrayList = LocalSafer.getListOfDirectContacts();
    private static int newRiskLevelValue;


    /**
     * Uses all Contacts to calculate the current risk Level.
     */

    public static synchronized void calculateRiskLevel() {


        if (LocalSafer.getRiskLevel(null) != 100) {
            newRiskLevelValue = 0;
            deleteOldContacts();

            if (indirectContactArrayList.size() > 0) {

                newRiskLevelValue = 35;
                newRiskLevelValue = newRiskLevelValue + ((indirectContactArrayList.size() - 1) * 5);

            }

            if (directContactArrayList.size() > 0) {
                newRiskLevelValue = 70;
                newRiskLevelValue = newRiskLevelValue + ((directContactArrayList.size() - 1) * 5) + ((indirectContactArrayList.size() * 5));

            }

            if (newRiskLevelValue > 95) {
                newRiskLevelValue = 95;
            }

            LocalSafer.safeRiskLevel(newRiskLevelValue, null);
            Log.d(TAG, "calulated risk Level: " + newRiskLevelValue);

        }
    }


    public static synchronized void addContact(Contact contact) {


        if (contact instanceof IndirectContact) {

            indirectContactArrayList.add((IndirectContact) contact);
            LocalSafer.safeListOfIndirectContacts(indirectContactArrayList);
            Log.d(TAG, "Indirect contact added to List");

        } else if (contact instanceof DirectContact) {

            directContactArrayList.add((DirectContact) contact);
            LocalSafer.safeListOfDirectContacts(directContactArrayList);
            Log.d(TAG, "Direct contact added to List");
        }
        deleteOldContacts();

    }

    /**
     * removes Contacts if they are older than 14 days.
     */

    public static void deleteOldContacts() {
        Iterator<IndirectContact> iCIterator;
        Iterator<DirectContact> dCIterator;

        iCIterator = indirectContactArrayList.iterator();

        while (iCIterator.hasNext()) {
            IndirectContact iC = iCIterator.next();
            if (DateHelper.checkIfDateIsOld(iC.getDate())) {
                iCIterator.remove();
                Log.d(TAG, "Indirect contact was removed due to outdated date");
            }

        }

        dCIterator = directContactArrayList.iterator();

        while (dCIterator.hasNext()) {
            DirectContact dC = dCIterator.next();
            if (DateHelper.checkIfDateIsOld(dC.getDate())) {
                dCIterator.remove();
                Log.d(TAG, "Direct contact was removed due to outdated date");
            }
        }

    }

    public static void deleteAllContacts() {
        Iterator<IndirectContact> iCIterator;
        Iterator<DirectContact> dCIterator;

        iCIterator = indirectContactArrayList.iterator();

        while (iCIterator.hasNext()) {
            IndirectContact iC = iCIterator.next();
            iCIterator.remove();
            Log.d(TAG, "Indirect contact was removed");

        }

        dCIterator = directContactArrayList.iterator();

        while (dCIterator.hasNext()) {
            DirectContact dC = dCIterator.next();
            dCIterator.remove();
            Log.d(TAG, "Direct contact was removed");
        }

    }


    /**
     * this method stops BLE key exchange when the user is currently infected and starts the key exchange when the user is to infected anymore.
     */

    public static synchronized void controlKeyExchange() {

        if (LocalSafer.getRiskLevel(null) == 100) {
            //disable scanning and transmitting of the bluetoothLE key exchange
            BeaconBackgroundService application = (BeaconBackgroundService) BeaconBackgroundService.getAppContext();
            application.changeMonitoringState(false);
            BeaconBackgroundService.stopTransmittingAsBeacon();

            Log.d(TAG, "Due to a current infection the Key Exchange was stopped");


        } else if (LocalSafer.getRiskLevel(null) != 100) {
            //activate scanning and transmitting of the bluetoothLE key exchange
            BeaconBackgroundService application = (BeaconBackgroundService) BeaconBackgroundService.getAppContext();
            application.changeMonitoringState(true);
            BeaconBackgroundService.transmitAsBeacon();

            Log.d(TAG, "Due to no current infection the Key Exchange was started");
        }

    }

    /**
     * sets the current risk level to a value that represents a current infection and disables BLE key exchange
     */

    public static void reportInfection() {
        //Set risk level corresponding to infection and safe date of the infection report
        LocalSafer.safeRiskLevel(100, null);
        LocalSafer.safeDateOfLastReportedInfection(DateHelper.getCurrentDateString(), null);
        Log.d(TAG, "risk level has been set to 100!");

        //disable scanning and transmitting of the bluetoothLE key exchange
        BeaconBackgroundService application = (BeaconBackgroundService) BeaconBackgroundService.getAppContext();
        application.changeMonitoringState(false);
        BeaconBackgroundService.stopTransmittingAsBeacon();

        Log.d(TAG, "Due to a current infection the Key Exchange was stopped.");

    }

    public static void reportNegativeInfectionTestResult() {

        Log.d(TAG, "Negative Infection Test was reported.");


    }


    /**
     * check if the current infection status is still up to date.
     * updates the status if it is outdated.
     */

    public static void checkIfInfectionHasExpired() {

        if (DateHelper.checkIfDateIsOld(DateHelper.convertStringToDate(LocalSafer.getDateOfLastReportedInfection(null)))) {
            Log.d(TAG, "Infection is older than 14 days and was removed!");
            LocalSafer.safeRiskLevel(0, null);
            Log.d(TAG, "Risk level was set to 0");
            //activate scanning and transmitting of the bluetoothLE key exchange
            BeaconBackgroundService application = (BeaconBackgroundService) BeaconBackgroundService.getAppContext();
            application.changeMonitoringState(true);
            BeaconBackgroundService.transmitAsBeacon();

            Log.d(TAG, "Due to no current infection the Key Exchange was started");
            calculateRiskLevel();

        } else {
            if (!DateHelper.checkIfDateIsOld(DateHelper.convertStringToDate(LocalSafer.getDateOfLastReportedInfection(null)))) {
                Log.d(TAG, "Infection is not older than 14 days");
            }
        }
    }

}

