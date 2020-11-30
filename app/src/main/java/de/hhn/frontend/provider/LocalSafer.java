package de.hhn.frontend.provider;

import android.content.Context;
import android.util.Log;

import androidx.annotation.StringRes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import de.hhn.frontend.Constants;
import de.hhn.frontend.DebugLog;
import de.hhn.frontend.MainActivity;
import de.hhn.frontend.R;
import de.hhn.frontend.keytransfer.BeaconBackgroundService;
import de.hhn.frontend.risklevel.DirectContact;
import de.hhn.frontend.risklevel.IndirectContact;

/**
 * This is the class for persistent saving of data at the client-side.
 *
 * @author Miftari, Klein
 * @version Nov 2020
 */
public class LocalSafer {
    private static final String TAG = "LocalSafer";

    private static final String DATAFILE01 = "cowappkeys.txt";
    private static final String DATAFILE02 = "cowappnotifications.txt";
    private static final String DATAFILE03 = "cowapprisklevel.txt";
    private static final String DATAFILE04 = "cowappdaysslc.txt";
    private static final String DATAFILE05 = "cowappfirstdate.txt";
    private static final String DATAFILE06 = "cowappownkey.txt";
    private static final String DATAFILE07 = "cowappownkeys.txt";
    private static final String DATAFILE08 = "cowappindirectcontacts.txt";
    private static final String DATAFILE09 = "cowappkeybuffer.txt";
    private static final String DATAFILE10 = "cowappdirectcontacts.txt";
    private static final String DATAFILE11 = "cowappnotificationcount.txt";
    private static final String DATAFILE12 = "cowappdebuglogger.txt";
    private static final String DATAFILE13 = "cowappalisalarmringlogged.txt";
    private static final String DATAFILE14 = "cowappisalarmsetlogged.txt";
    private static final String DATAFILE15 = "cowappniskeytransmitlogged.txt";
    private static final String DATAFILE16 = "cowappiskeysafelogged.txt";
    private static final String DATAFILE21 = "cowappindirectcontacts.txt";
    private static final String DATAFILE22 = "cowappdirectcontacts.txt";
    private static final String DATAFILE23 = "cowappdateolri";
    /**
     * This methods saves a String under a datafileName.
     * If there is not such datafile, it will be created, when you call this methode.
     *
     * @param datafile The Name of the datafile.
     * @param value    The String.
     */
    public static void safeStringAtDatafile(String datafile, String value) {
        Log.d(TAG, "safeStringAtDatafile -> String: " + value + " at data file: " + datafile);
        try {
            FileOutputStream data = BeaconBackgroundService.getAppContext().openFileOutput(datafile,
                    Context.MODE_PRIVATE);
            data.write(value.getBytes());
            data.close();
        } catch (IOException ex) {
            System.out.println("Some Mistakes happened at safeStringAtDatafile(...)");
        }
    }

    /**
     * Returns the value of a Datafile. If there is no such datafile, the returnvalue is an empty string.
     *
     * @param datafileName the name of the datafile.
     * @return the value of the datafile.
     */
    private static String readDataFile(String datafileName) {
        Log.d(TAG, "readDataFile: " + datafileName);
        try {
            FileInputStream datafile = BeaconBackgroundService.getAppContext().openFileInput(datafileName);
            List<Byte> data = new ArrayList<Byte>();

            while (true) {
                int b = datafile.read();
                if (b == -1) {
                    break; // end of the datafile.
                } else {
                    data.add((byte) b);
                }
            }
            // Bytes to words
            byte[] bytes = new byte[data.size()];

            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = data.get(i);
            }

            String text = new String(bytes);
            return text;
        } catch (Exception ex) { //datafile not found
            return "";
        }
    }

    /**
     * Returns true if the date is older than 2 weeks.
     *
     * @param date
     * @return
     */
    private static boolean dateIsOld(Date date) {
        Log.d(TAG, "dateIsOld() was called");

        boolean result = false;
        Date oldDate = date;
        oldDate.setDate(date.getDate() + 14);

        if (oldDate.before(new Date())) {
            result = true;
        }

        return result;
    }

    /**
     * This Method returns an Array of Strings.
     * The format of the Strings is the following:
     * value + "----" + new Date().toString()
     *
     * @return A list of Strings. If there are no saved keys, the return-value is null.
     */
    private static String[] getValuesAsArray(String datafileName) {
        Log.d(TAG, "getValuesAsArray() was called with datafile: " + datafileName);
        String values = readDataFile(datafileName);
        if (values.equals("")) {
            return null;
        }
        values = values.substring(4);
        return values.split("-<>-");
    }

    /**
     * All Values older than 2 weeks are going to be deleted.
     */
    private static void deleteOldValues(String datafileName) {
        Log.d(TAG, "deleteOldValues was called with datafile " + datafileName);

        String[] values = getValuesAsArray(datafileName);
        String result = "";

        if (values != null) {
            for (String string : values) {
                String[] strings = string.split("----");
                Date dateOfValue = new Date(strings[1]);
                if (!dateIsOld(dateOfValue)) {
                    result = result + "-<>-" + string;
                }
            }

            safeStringAtDatafile(datafileName, result);
        }
    }

    /**
     * safes the given key and the time of the method-call. If the parameter is null, the method deleteOldKeyPairs() is called.
     *
     * @param contactKey The Key of the contact
     */
    public synchronized static void addKeyPairToSavedKeyPairs(String contactKey) {
        Log.d(TAG, "addKeyPairToSavedKeyPairs: " + contactKey);
        if (contactKey == null) {
            deleteOldValues(DATAFILE01);
        } else {
            String alreadySavedKeyPairs = readDataFile(DATAFILE01);
            String allKeyPairsToSafe = alreadySavedKeyPairs + "-<>-" + contactKey.substring(9) + "----" + new Date().toString();
            safeStringAtDatafile(DATAFILE01, allKeyPairsToSafe);

            if (Constants.DEBUG_MODE && isKeySafeLogged()) {
                addLogValueToDebugLog(BeaconBackgroundService.getAppContext().getString(R.string.key_was_saved) + contactKey);
            }
        }
    }

    /**
     * This method clears the keyPairDataFile.
     */
    public static void clearKeyPairDataFile() {
        Log.d(TAG, "cleareKeyPairDataFile() was called.");
        safeStringAtDatafile(DATAFILE01, "");
    }

    /**
     * safes the given key.
     * @param contactKey The Key of the contact
     */
    public synchronized static String[] addKeyToBufferFile(String contactKey) {
        Log.d(TAG, "addKeyToBufferFile: " + contactKey);

        if (contactKey == null) {
            String[] result = getValuesAsArray(DATAFILE09);
            clearBufferFile();
            return result;
        } else {
            String alreadySavedKeyPairs = readDataFile(DATAFILE09);
            String allKeyPairsToSafe = alreadySavedKeyPairs + "-<>-" + contactKey;
            safeStringAtDatafile(DATAFILE09, allKeyPairsToSafe);
            return null;
        }
    }

    /**
     * This method clears the bufferFile.
     */
    public static void clearBufferFile() {
        Log.d(TAG, "cleareBufferFile() was called.");
        safeStringAtDatafile(DATAFILE09, "");
    }

    /**
     * This Method returns an Array of Strings.
     * The format of the Strings is the following:
     * contactKey + "----" + new Date().toString()
     *
     * @return A list of Strings. If there are no saved keys, the return-value is null.
     */
    public static String[] getKeyPairs() {
        Log.d(TAG, "getKeyPairs() was called.");
        String[] result = getValuesAsArray(DATAFILE01);
        if (result != null) {
            return result;
        } else {
            return null;
        }
    }

    /**
     * safes the given notification and the time of the method-call. If the parameter is null, the method deleteOldNotifications() is called.
     *
     * @param notification The new notification as String
     */
    public synchronized static void addNotificationToSavedNotifications(String notification) {
        Log.d(TAG, "addNotificationToSavedNotifications() was called with notification: " + notification);
        if (notification == null) {
            deleteOldValues(DATAFILE02);
        } else {
            String alreadySavedNotifications = readDataFile(DATAFILE02);
            String allNotificationsToSafe = alreadySavedNotifications + "-<>-" + notification + "----" + new Date().toString();

            safeStringAtDatafile(DATAFILE02, allNotificationsToSafe);
        }
    }

    /**
     * This method clears the Notifications-Datafile.
     */
    public static void clearNotificationDataFile() {
        Log.d(TAG, "clearNotificationDataFile() was called.");
        safeStringAtDatafile(DATAFILE02, "");
    }

    /**
     * This Method returns an Array of Strings (the notifications).
     * The format of the Strings is the following:
     * Notification + "----" + new Date().toString()
     *
     * @return A list of Strings. If there are no saved notifications, the return-value is null.
     */
    public static String[] getNotifications() {
        Log.d(TAG, "getNotifications was called.");
        return getValuesAsArray(DATAFILE02);
    }

    /**
     * Safes the risk Level and update the traffic light and risk status
     *
     * @param riskLevel risk level as int
     */
    public static void safeRiskLevel(int riskLevel) {
        Log.d(TAG, "safeRiskLevel() with riskLevel: " + riskLevel);
        safeStringAtDatafile(DATAFILE03, String.valueOf(riskLevel));
        MainActivity.showTrafficLightStatus();
        MainActivity.showRiskStatus();
    }

    /**
     * Getter for the risk level.
     *
     * @return risk level as int.
     */
    public static int getRiskLevel() {
        Log.d(TAG, "getRiskLevel() was called.");
        try {
            return Integer.valueOf(readDataFile(DATAFILE03));
        } catch (Exception ex) { //datafile not found
            return 0;
        }
    }

    /**
     * Safes the daysSinceLastContact.
     *
     * @param daysSinceLastContact days Since last contact as int.
     */
    public static void safeDaysSinceLastContact(int daysSinceLastContact) {
        Log.d(TAG, "safeDaysSinceLastContact was called with: " + daysSinceLastContact);
        safeStringAtDatafile(DATAFILE04, String.valueOf(daysSinceLastContact));
    }

    /**
     * Getter for the daysSinceLastContact.
     *
     * @return the days since last Contact as int.
     */
    public static int getDaysSinceLastContact() {
        Log.d(TAG, "getDaysSinceLastContact()");
        try {
            return Integer.valueOf(readDataFile(DATAFILE04));
        } catch (Exception ex) { //datafile not found
            return 0;
        }
    }

    /**
     * Safes the date of the first start.
     *
     * @param date days Since last contact as int.
     */
    public static void safeFirstStartDate(String date) {
        Log.d(TAG, "safeFirstStartDate() was called with " + date);
        safeStringAtDatafile(DATAFILE05, date);
    }

    /**
     * Getter for the date of the first start.
     *
     * @return the days since last Contact as int.
     */
    public static String getFirstStartDate() {
        Log.d(TAG, "getFirstStartDate() was called.");
        return readDataFile(DATAFILE05);
    }

    /**
     * Safes the own key
     *
     * @param key the own key as String
     */
    public static void safeOwnKey(String key) {
        Log.d(TAG, "safeOwnKey was called with: " + key);
        safeStringAtDatafile(DATAFILE06, key);
        addKeyToOwnKeys(key);
    }


    /**
     * Getter for the own key
     *
     * @return the own key as String
     */
    public static String getOwnKey() {
        Log.d(TAG, "getOwnKey was called");
        String key = readDataFile(DATAFILE06);
        if (!key.isEmpty()) {
            String result = Constants.cowappBeaconIdentifier + "-" + readDataFile(DATAFILE06);
            return result;
        } else {
            return "";
        }
    }

    /**
     * safes the given key and the time of the method-call. If the parameter is null, the method deleteOldKeyPairs() is called.
     *
     * @param ownKey The Key of the contact
     */
    public synchronized static void addKeyToOwnKeys(String ownKey) {
        Log.d(TAG, "addKeyToOwnKeys was called with " + ownKey);
        if (ownKey == null) {
            deleteOldValues(DATAFILE07);
        } else {
            String alreadySavedKeys = readDataFile(DATAFILE07);
            String allKeysToSafe = alreadySavedKeys + "-<>-" + ownKey + "----" + new Date().toString();
            safeStringAtDatafile(DATAFILE07, allKeysToSafe);
        }
    }

    /**
     * This method clears the ownKeyPairDataFile.
     */
    public static void clearOwnKeyPairDataFile() {
        Log.d(TAG, "clearOwnKeyPairDataFile() was called.");
        safeStringAtDatafile(DATAFILE07, "");
    }

    /**
     * This Method returns an Array of the own Strings.
     * Attention: The first eight characters are the identifier of our application.
     * To reduce the length of the string, the identifier are left out of the keys in this methode.
     *
     * @return A list of the own Strings without the first eight characters. If there are no saved keys, the return-value is null.
     */
    public static String[] getOwnKeys() {
        Log.d(TAG, "getOwnKeys() was called ");
        String[] result = getValuesAsArray(DATAFILE07);
        if (result != null) {
            return result;
        } else {
            return null;
        }
    }

    /**
     * This method adds the received Key to an buffer-datafile, so we can work with it.
     *
     * @param key the received key
     */
    public synchronized static void addReceivedKey(String key) {
        Log.d(TAG, "addReceivedKey() was called " + key);
        addKeyToBufferFile(key);
    }

    /**
     * This method analyzes the buffer file and adds every key just one time to the key-pari datafile.
     */
    public synchronized static void analyzeBufferFile() {
        Log.d(TAG, "analyzeBufferFile() was called ");

        String[] bufferValues = addKeyToBufferFile(null);
        HashSet<String> strings = new HashSet<>();

        if (bufferValues != null) {
            for (String string : bufferValues) {
                strings.add(string);
            }

            for (String string : strings) {
                int factor = 0;

                for (String value : bufferValues) {
                    if (string.equals(value)) {
                        factor++;
                    }
                }

                factor = (factor / 20);
                if (factor == 0) { factor = 1; }

                for (int i = 0; i != factor; i++) {
                    addKeyPairToSavedKeyPairs(string);
                }
            }
        }
    }

    public synchronized static void safeIndirectContacts(String[] indirectContacts) {
        if (indirectContacts == null) {
            safeStringAtDatafile(DATAFILE08, "");
        } else {
            String value = "-<>-";
            for (String string: indirectContacts) {
                value = value + string + "-<>-";
            }
            safeStringAtDatafile(DATAFILE08, value);
        }
    }

    public synchronized static void safeDirectContacts(String[] directContacts) {
        if (directContacts == null) {
            safeStringAtDatafile(DATAFILE10, "");
        } else {
            String value = "-<>-";
            for (String string: directContacts) {
                value = value + string + "-<>-";
            }
            safeStringAtDatafile(DATAFILE10, value);
        }
    }

    public synchronized static String[] getIndirectContacts() {
        return getValuesAsArray(DATAFILE08);
    }

    public synchronized static String[] getdirectContacts() {
        return getValuesAsArray(DATAFILE10);
    }

    /**
     * Setter for the notificationCount
     *
     * @param notificationCount notificationCount as int
     */
    public static void safeNotificationCounter(int notificationCount) {
        Log.d(TAG, "safeNotificationCounter was called with: " + notificationCount);
        safeStringAtDatafile(DATAFILE11, String.valueOf(notificationCount));
    }

    /**
     * Getter for the NotificationCounter.
     *
     * @return the NotificationCounter as int.
     */
    public static int getNotificationCounter() {
        Log.d(TAG, "getNotificationCount()");
        try {
            return Integer.valueOf(readDataFile(DATAFILE11));
        } catch (Exception ex) { //datafile not found
            return 0;
        }
    }

    /**
     * safes the given LogValue and the time of the method-call. If the parameter is null, the method deleteOldNotifications() is called.
     *
     * @param logValue The new logValue as String
     */
    public synchronized static void addLogValueToDebugLog(String logValue) {
        Log.d(TAG, "addLogValueToDebugLog() was called with LogValue: " + logValue);
        if (logValue == null) {
            deleteOldValues(DATAFILE12);
            DebugLog.renewTheLog();
        } else {
            String alreadySavedlogValues = readDataFile(DATAFILE12);
            String alllogValueToSafe = alreadySavedlogValues + "-<>-" + logValue + "----" + new Date().toString();

            safeStringAtDatafile(DATAFILE12, alllogValueToSafe);
            DebugLog.renewTheLog();
        }
    }

    /**
     * This method clears the DebugLog-Datafile.
     */
    public static void clearDebugLog() {
        Log.d(TAG, "clearDebugLog() was called.");
        safeStringAtDatafile(DATAFILE12, "");
        DebugLog.renewTheLog();
    }

    /**
     * This Method returns an Array of Strings (the logValues).
     * The format of the Strings is the following:
     * logValue + "----" + new Date().toString()
     *
     * @return A list of Strings. If there are no saved logValues, the return-value is null.
     */
    public static String[] getDebugValues() {
        Log.d(TAG, "getDebugValues was called.");
        return getValuesAsArray(DATAFILE12);
    }

    public static void setIsAlarmRingLogged(boolean value) {
        safeStringAtDatafile(DATAFILE13, String.valueOf(value));
    }

    public static boolean isAlarmRingLogged() {
        try {
            return Boolean.valueOf(readDataFile(DATAFILE13));
        } catch (Exception e) {
            return false;
        }
    }

    public static void setIsAlarmSetLogged(boolean value) {
        safeStringAtDatafile(DATAFILE14, String.valueOf(value));
    }

    public static boolean isAlarmSetLogged() {
        try {
            return Boolean.valueOf(readDataFile(DATAFILE14));
        } catch (Exception e) {
            return false;
        }
    }

    public static void setIsKeyTransmitLogged(boolean value) {
        safeStringAtDatafile(DATAFILE15, String.valueOf(value));
    }

    public static boolean isKeyTransmitLogged() {
        try {
            return Boolean.valueOf(readDataFile(DATAFILE15));
        } catch (Exception e) {
            return false;
        }
    }

    public static void setIsKeySafeLogged(boolean value) {
        safeStringAtDatafile(DATAFILE16, String.valueOf(value));
    }

    public static boolean isKeySafeLogged() {
        try {
            return Boolean.valueOf(readDataFile(DATAFILE16));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Safes the list containing all indirect contacts.
     *
     * @param indirectContactArrayList list of indirect contacts
     */

    public synchronized static void safeListOfIndirectContacts(ArrayList<IndirectContact> indirectContactArrayList) {
        try {
            FileOutputStream fos = BeaconBackgroundService.getAppContext().openFileOutput(DATAFILE21, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(indirectContactArrayList);
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Returns the list of the indirect contacts
     */

    public synchronized static ArrayList<IndirectContact> getListOfIndirectContacts() {

        ArrayList<IndirectContact> indirectContactArrayList = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(DATAFILE21);
            ObjectInputStream ois = new ObjectInputStream(fis);

            indirectContactArrayList = (ArrayList) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();

        }

        return indirectContactArrayList;
    }

    /**
     * Safes the list containing all direct contacts.
     * @param directContactArrayList list of direct contacts
     */

    public synchronized static void safeListOfDirectContacts(ArrayList<DirectContact> directContactArrayList) {
        try {

            FileOutputStream fos = BeaconBackgroundService.getAppContext().openFileOutput(DATAFILE22, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(directContactArrayList);
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * returns the list of direct contacts.
     */

    public synchronized static ArrayList<DirectContact> getListOfDirectContacts() {

        ArrayList<DirectContact> directContactArrayList = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(DATAFILE22);
            ObjectInputStream ois = new ObjectInputStream(fis);

            directContactArrayList = (ArrayList) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();

        }

        return directContactArrayList;
    }

    /**
     * Safes the date of last reported infection.
     *
     * @param date
     */
    public static void safeDateOfLastReportedInfection(String date) {
        Log.d(TAG, "safeDateOfLastReportedInfection() was called with " + date);
        safeStringAtDatafile(DATAFILE23, date);
    }

    /**
     * Getter for the date of the last reported infection.
     *
     * @return
     */
    public static String getDateOfLastReportedInfection() {
        Log.d(TAG, "getDateOfLastReportedInfection() was called.");
        return readDataFile(DATAFILE23);
    }
}
