package de.hhn.frontend.risklevel;

import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
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

public class NewRiskLevel {


    static ArrayList<IndirectContact> indirectContactArrayList = LocalSafer.getListOfIndirectContacts();
    static ArrayList<DirectContact> directContactArrayList = LocalSafer.getListOfDirectContacts();


    static int newRiskLevelValue;


    /**
     * Uses all Contacts to calculate the current risk Level.
     */

    public static synchronized void calculateRiskLevel() {


        if (LocalSafer.getRiskLevel() != 100) {
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

            LocalSafer.safeRiskLevel(newRiskLevelValue);
            Log.d("Jonas", "calulated risk Level: " + newRiskLevelValue);

        }
    }


    public static synchronized void addContact(Contact contact) {


        if (contact instanceof IndirectContact) {

            indirectContactArrayList.add((IndirectContact) contact);
            LocalSafer.safeListOfIndirectContacts(indirectContactArrayList);
            Log.d("Jonas", "Indirect contact added to List");

        } else if (contact instanceof DirectContact) {

            directContactArrayList.add((DirectContact) contact);
            LocalSafer.safeListOfDirectContacts(directContactArrayList);
            Log.d("Jonas", "Direct contact added to List");
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
            }

        }

        dCIterator = directContactArrayList.iterator();

        while (dCIterator.hasNext()) {
            DirectContact dC = dCIterator.next();
            if (DateHelper.checkIfDateIsOld(dC.getDate())) {
                dCIterator.remove();
            }
        }

    }

    /**
     * this method stops BLE key exchange when the user is currently infected and starts the key exchange when the user is to infected anymore.
     */

    public static synchronized void controlKeyExchange() {

        if (LocalSafer.getRiskLevel() == 100) {
            //disable scanning and transmitting of the bluetoothLE key exchange
            BeaconBackgroundService application = (BeaconBackgroundService) BeaconBackgroundService.getAppContext();
            application.changeMonitoringState(false);
            BeaconBackgroundService.stopTransmittingAsBeacon();

            Log.d("Jonas", "Due to a current infection the Key Exchange was stopped");


        } else if (LocalSafer.getRiskLevel() != 100) {
            //activate scanning and transmitting of the bluetoothLE key exchange
            BeaconBackgroundService application = (BeaconBackgroundService) BeaconBackgroundService.getAppContext();
            application.changeMonitoringState(true);
            BeaconBackgroundService.transmitAsBeacon();

            Log.d("Jonas", "Due to no current infection the Key Exchange was started");
        }

    }

    /**
     * sets the current risk level to a value that represents a current infection
     */

    public static void setRiskLevelToCurrentInfection() {
        LocalSafer.safeRiskLevel(100);
        LocalSafer.safeDateOfLastReportedInfection(DateHelper.getCurrentDateString());
        Log.d("Jonas", "risk level has been set to 100!");

        controlKeyExchange();
    }

    /**
     *
     */

    public static void checkIfInfectionHasExpired() {

        if (DateHelper.checkIfDateIsOld(DateHelper.convertStringToDate(LocalSafer.getDateOfLastReportedInfection()))) {
            Log.d("Jonas", "Infection is older than 14 days and was removed!");
            LocalSafer.safeRiskLevel(0);
            Log.d("Jonas", "Risk level was set to 0");
            calculateRiskLevel();

        } else {
            if (!DateHelper.checkIfDateIsOld(DateHelper.convertStringToDate(LocalSafer.getDateOfLastReportedInfection()))) {
                Log.d("Jonas", "Infection is not older than 14 days");
            }
        }
    }

}

