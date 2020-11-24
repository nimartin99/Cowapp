package de.hhn.frontend.provider;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import de.hhn.frontend.Constants;
import de.hhn.frontend.keytransfer.BeaconBackgroundService;

/**
 * This is the class for persistent saving of data at the client-side.
 *
 * @author Miftari
 * @version Nov 2020
 */
public class LocalSafer {
    private static final String TAG = "LocalSafer";

    private static String DATAFILE01 = "cowappkeys.txt";
    private static String DATAFILE02 = "cowappnotifications.txt";
    private static String DATAFILE03 = "cowapprisklevel.txt";
    private static String DATAFILE04 = "cowappdaysslc.txt";
    private static String DATAFILE05 = "cowappfirstdate.txt";
    private static String DATAFILE06 = "cowappownkey.txt";
    private static String DATAFILE07 = "cowappownkeys.txt";
    private static String DATAFILE09 = "cowappkeybuffer.txt";

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
     * Safes the risk Level.
     *
     * @param riskLevel risk level as int
     */
    public static void safeRiskLevel(int riskLevel) {
        Log.d(TAG, "safeRiskLevel() with riskLevel: " + riskLevel);
        safeStringAtDatafile(DATAFILE03, String.valueOf(riskLevel));
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
                addKeyPairToSavedKeyPairs(string);
            }
        }
    }
}
