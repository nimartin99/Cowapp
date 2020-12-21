package de.hhn.cowapp.datastorage;

import android.content.Context;
import android.util.Log;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import de.hhn.cowapp.utils.BufferFileLogActivity;
import de.hhn.cowapp.utils.Constants;
import de.hhn.cowapp.utils.DebugLog;
import de.hhn.cowapp.gui.MainActivity;
import de.hhn.cowapp.utils.OtherKeysActivity;
import de.hhn.cowapp.utils.OwnKeyActivity;
import de.hhn.cowapp.R;
import de.hhn.cowapp.keytransfer.BeaconBackgroundService;
import de.hhn.cowapp.risklevel.DirectContact;
import de.hhn.cowapp.risklevel.IndirectContact;

/**
 * This is the class for persistent saving of data at the client-side.
 *
 * @author Miftari
 * @version 2020-11-30
 */
public class LocalSafer {
    private static final String TAG = "LocalSafer";

    private static final String DATAFILE01 = "cowappkeys.txt";
    private static final String DATAFILE02 = "cowappnotifications.txt";
    private static final String DATAFILE03 = "cowapprisklevel.txt";
    private static final String DATAFILE05 = "cowappfirstdate.txt";
    private static final String DATAFILE06 = "cowappownkey.txt";
    private static final String DATAFILE07 = "cowappownkeys.txt";
    private static final String DATAFILE08 = "cowapplastring.txt";
    private static final String DATAFILE09 = "cowappkeybuffer.txt";
    private static final String DATAFILE11 = "cowappnotificationcount.txt";
    private static final String DATAFILE12 = "cowappdebuglogger.txt";
    private static final String DATAFILE13 = "cowappalisalarmringlogged.txt";
    private static final String DATAFILE14 = "cowappisalarmsetlogged.txt";
    private static final String DATAFILE15 = "cowappniskeytransmitlogged.txt";
    private static final String DATAFILE16 = "cowappiskeysafelogged.txt";
    private static final String DATAFILE17 = "cowappisfirstappstart.txt";
    private static final String DATAFILE21 = "cowappindirectcontacts.txt";
    private static final String DATAFILE22 = "cowappdirectcontacts.txt";
    private static final String DATAFILE23 = "cowappdateolri.txt";

    /**
     * This methods saves a String under a datafileName.
     * If there is not such datafile, it will be created, when you call this methode.
     *
     * @param datafile The Name of the datafile.
     * @param value    The String.
     */
    public static void safeStringAtDatafile(String datafile, String value, Context context) {
        Log.d(TAG, "safeStringAtDatafile -> String: " + value + " at data file: " + datafile);
        try {
            FileOutputStream data;
            if (context == null) {
                data = BeaconBackgroundService.getAppContext().openFileOutput(datafile, Context.MODE_PRIVATE);
            } else {
                data = context.openFileOutput(datafile, Context.MODE_PRIVATE);
            }
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
    private static String readDataFile(String datafileName, Context context) {
        Log.d(TAG, "readDataFile: " + datafileName);
        try {
            FileInputStream datafile;
            if (context == null) {
                datafile = BeaconBackgroundService.getAppContext().openFileInput(datafileName);
            } else {
                datafile = context.openFileInput(datafileName);
            }
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
     * @return boolean if the date is older than 2 weeks
     */
    public static boolean dateIsOld(Date date) {
        Log.d(TAG, "dateIsOld() was called");
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
    public static String[] getValuesAsArray(String datafileName, Context context) {
        Log.d(TAG, "getValuesAsArray() was called with datafile: " + datafileName);
        String values = readDataFile(datafileName, null);

        if (values.equals("")) {
            return null;
        }
        values = values.substring(4);
        return values.split("-<>-");
    }

    /**
     * All Values older than 2 weeks are going to be deleted.
     */
    private static void deleteOldValues(String datafileName, Context context) {
        Log.d(TAG, "deleteOldValues was called with datafile " + datafileName);

        String[] values = getValuesAsArray(datafileName, context);
        String result = "";

        if (values != null) {
            for (String string : values) {
                String[] strings = string.split("----");
                Date dateOfValue = new Date(strings[1]);
                if (!dateIsOld(dateOfValue)) {
                    result = result + "-<>-" + string;
                }
            }

            safeStringAtDatafile(datafileName, result, context);
        }
    }

    /**
     * safes the given key and the time of the method-call. If the parameter is null, the method deleteOldKeyPairs() is called.
     *
     * @param contactKey The Key of the contact
     */
    public synchronized static void addKeyPairToSavedKeyPairs(String contactKey, Context context) {
        Log.d(TAG, "addKeyPairToSavedKeyPairs: " + contactKey);
        if (contactKey == null) {
            deleteOldValues(DATAFILE01, context);
        } else {
            String alreadySavedKeyPairs = readDataFile(DATAFILE01, context);
            String allKeyPairsToSafe = alreadySavedKeyPairs + "-<>-" + contactKey.substring(9) + "----" + new Date().toString();
            safeStringAtDatafile(DATAFILE01, allKeyPairsToSafe, context);

            if (Constants.DEBUG_MODE && isKeySafeLogged(context)) {
                addLogValueToDebugLog(BeaconBackgroundService.getAppContext().getString(R.string.key_was_saved) + contactKey, context);
            }
        }
        OtherKeysActivity.renewTheLog();
    }

    /**
     * This method clears the keyPairDataFile.
     */
    public static void clearKeyPairDataFile(Context context) {
        Log.d(TAG, "cleareKeyPairDataFile() was called.");
        safeStringAtDatafile(DATAFILE01, "", context);
    }

    /**
     * safes the given key.
     * @param contactKey The Key of the contact
     */
    public synchronized static String[] addKeyToBufferFile(String contactKey, Context context) {
        Log.d(TAG, "addKeyToBufferFile: " + contactKey);

        if (contactKey == null) {
            String[] result = getValuesAsArray(DATAFILE09, context);
            clearBufferFile(context);
            BufferFileLogActivity.renewTheLog();
            return result;
        } else {
            String alreadySavedKeyPairs = readDataFile(DATAFILE09, context);
            String allKeyPairsToSafe = alreadySavedKeyPairs + "-<>-" + contactKey;
            safeStringAtDatafile(DATAFILE09, allKeyPairsToSafe, context);
            BufferFileLogActivity.renewTheLog();
            return null;
        }
    }

    /**
     * This method clears the bufferFile.
     */
    public static void clearBufferFile(Context context) {
        Log.d(TAG, "cleareBufferFile() was called.");
        safeStringAtDatafile(DATAFILE09, "", context);
    }

    /**
     * This Method returns an Array of Strings.
     * The format of the Strings is the following:
     * contactKey + "----" + new Date().toString()
     *
     * @return A list of Strings. If there are no saved keys, the return-value is null.
     */
    public static String[] getKeyPairs(Context context) {
        Log.d(TAG, "getKeyPairs() was called.");
        String[] result = getValuesAsArray(DATAFILE01, context);
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
    public synchronized static void addNotificationToSavedNotifications(String notification, Context context) {
        Log.d(TAG, "addNotificationToSavedNotifications() was called with notification: " + notification);
        if (notification == null) {
            deleteOldValues(DATAFILE02, context);
        } else {
            String alreadySavedNotifications = readDataFile(DATAFILE02, context);
            String allNotificationsToSafe = alreadySavedNotifications + "-<>-" + notification + "----" + new Date().toString();

            safeStringAtDatafile(DATAFILE02, allNotificationsToSafe, context);
        }
    }

    /**
     * This method clears the Notifications-Datafile.
     */
    public static void clearNotificationDataFile(Context context) {
        Log.d(TAG, "clearNotificationDataFile() was called.");
        safeStringAtDatafile(DATAFILE02, "", context);
    }

    /**
     * This Method returns an Array of Strings (the notifications).
     * The format of the Strings is the following:
     * Notification + "----" + new Date().toString()
     *
     * @return A list of Strings. If there are no saved notifications, the return-value is null.
     */
    public static String[] getNotifications(Context context) {
        Log.d(TAG, "getNotifications was called.");
        return getValuesAsArray(DATAFILE02, context);
    }

    /**
     * Safes the risk Level and update the traffic light and risk status
     *
     * @param riskLevel risk level as int
     */
    public static void safeRiskLevel(int riskLevel, Context context) {
        Log.d(TAG, "safeRiskLevel() with riskLevel: " + riskLevel);
        safeStringAtDatafile(DATAFILE03, String.valueOf(riskLevel), context);

        if (MainActivity.getMainActivity() != null) {
            MainActivity.showTrafficLightStatus();
            MainActivity.showRiskStatus();
        }
    }

    /**
     * Getter for the risk level.
     *
     * @return risk level as int.
     */
    public static int getRiskLevel(Context context) {
        Log.d(TAG, "getRiskLevel() was called.");
        try {
            return Integer.valueOf(readDataFile(DATAFILE03, context));
        } catch (Exception ex) { //datafile not found
            return 0;
        }
    }

    /**
     * Safes the date of the first start.
     *
     * @param date first start date as string
     */
    public static void safeFirstStartDate(String date, Context context) {
        Log.d(TAG, "safeFirstStartDate() was called with " + date);
        safeStringAtDatafile(DATAFILE05, date, context);
    }

    /**
     * Getter for the date of the first start.
     *
     * @return the days since last Contact as int.
     */
    public static String getFirstStartDate(Context context) {
        Log.d(TAG, "getFirstStartDate() was called.");
        return readDataFile(DATAFILE05, context);
    }

    /**
     * Safes the own key
     *
     * @param key the own key as String
     */
    public static void safeOwnKey(String key, Context context) {
        Log.d(TAG, "safeOwnKey was called with: " + key);
        safeStringAtDatafile(DATAFILE06, key, context);
        addKeyToOwnKeys(key, context);
        OwnKeyActivity.renewTheLog();
    }


    /**
     * Getter for the own key
     *
     * @return the own key as String
     */
    public static String getOwnKey(Context context) {
        Log.d(TAG, "getOwnKey was called");
        String key = readDataFile(DATAFILE06, context);
        if (!key.isEmpty()) {
            String result = Constants.cowappBeaconIdentifier + "-" + readDataFile(DATAFILE06, context);
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
    public synchronized static void addKeyToOwnKeys(String ownKey, Context context) {
        Log.d(TAG, "addKeyToOwnKeys was called with " + ownKey);
        if (ownKey == null) {
            deleteOldValues(DATAFILE07, context);
        } else {
            String alreadySavedKeys = readDataFile(DATAFILE07, context);
            String allKeysToSafe = alreadySavedKeys + "-<>-" + ownKey + "----" + new Date().toString();
            safeStringAtDatafile(DATAFILE07, allKeysToSafe, context);
        }
    }

    /**
     * This method clears the ownKeyPairDataFile.
     */
    public static void clearOwnKeyPairDataFile(Context context) {
        Log.d(TAG, "clearOwnKeyPairDataFile() was called.");
        safeStringAtDatafile(DATAFILE07, "", context);
    }

    /**
     * This Method returns an Array of the own Strings.
     * Attention: The first eight characters are the identifier of our application.
     * To reduce the length of the string, the identifier are left out of the keys in this methode.
     *
     * @return A list of the own Strings without the first eight characters. If there are no saved keys, the return-value is null.
     */
    public static String[] getOwnKeys(Context context) {
        Log.d(TAG, "getOwnKeys() was called ");
        String[] result = getValuesAsArray(DATAFILE07, context);
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
    public synchronized static void addReceivedKey(String key, Context context) {
        Log.d(TAG, "addReceivedKey() was called " + key);
        addKeyToBufferFile(key, context);
    }

    /**
     * This method analyzes the buffer file and adds every key just one time to the key-pari datafile.
     */
    public synchronized static void analyzeBufferFile(Context context) {
        Log.d(TAG, "analyzeBufferFile() was called ");

        String[] bufferValues = addKeyToBufferFile(null, context);
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
                    addKeyPairToSavedKeyPairs(string, context);
                }
            }
        }
    }

    /**
     * Setter for the notificationCount
     *
     * @param notificationCount notificationCount as int
     */
    public static void safeNotificationCounter(int notificationCount, Context context) {
        Log.d(TAG, "safeNotificationCounter was called with: " + notificationCount);
        safeStringAtDatafile(DATAFILE11, String.valueOf(notificationCount), context);
    }

    /**
     * Getter for the NotificationCounter.
     *
     * @return the NotificationCounter as int.
     */
    public static int getNotificationCounter(Context context) {
        Log.d(TAG, "getNotificationCount()");
        try {
            return Integer.valueOf(readDataFile(DATAFILE11, context));
        } catch (Exception ex) { //datafile not found
            return 0;
        }
    }

    /**
     * safes the given LogValue and the time of the method-call. If the parameter is null, the method deleteOldNotifications() is called.
     *
     * @param logValue The new logValue as String
     */
    public synchronized static void addLogValueToDebugLog(String logValue, Context context) {
        Log.d(TAG, "addLogValueToDebugLog() was called with LogValue: " + logValue);
        if (logValue == null) {
            deleteOldValues(DATAFILE12, context);
            DebugLog.renewTheLog();
        } else {
            String alreadySavedlogValues = readDataFile(DATAFILE12, context);
            String alllogValueToSafe = alreadySavedlogValues + "-<>-" + logValue + "----" + new Date().toString();

            safeStringAtDatafile(DATAFILE12, alllogValueToSafe, context);
            DebugLog.renewTheLog();
        }
    }

    /**
     * This method clears the DebugLog-Datafile.
     */
    public static void clearDebugLog(Context context) {
        Log.d(TAG, "clearDebugLog() was called.");
        safeStringAtDatafile(DATAFILE12, "", context);
        DebugLog.renewTheLog();
    }

    /**
     * This Method returns an Array of Strings (the logValues).
     * The format of the Strings is the following:
     * logValue + "----" + new Date().toString()
     *
     * @return A list of Strings. If there are no saved logValues, the return-value is null.
     */
    public static String[] getDebugValues(Context context) {
        Log.d(TAG, "getDebugValues was called.");
        return getValuesAsArray(DATAFILE12, context);
    }

    public static void setIsAlarmRingLogged(boolean value, Context context) {
        safeStringAtDatafile(DATAFILE13, String.valueOf(value), context);
    }

    public static boolean isAlarmRingLogged(Context context) {
        String value = readDataFile(DATAFILE13, context);
        if (value.isEmpty()) {
            return true;
        }
        return Boolean.valueOf(value);
    }

    public static void setIsAlarmSetLogged(boolean value, Context context) {
        safeStringAtDatafile(DATAFILE14, String.valueOf(value), context);
    }

    public static boolean isAlarmSetLogged(Context context) {
        String value = readDataFile(DATAFILE14, context);
        if (value.isEmpty()) {
            return true;
        }
        return Boolean.valueOf(value);
    }

    public static void setIsKeyTransmitLogged(boolean value, Context context) {
        safeStringAtDatafile(DATAFILE15, String.valueOf(value), context);
    }

    public static boolean isKeyTransmitLogged(Context context) {
        String value = readDataFile(DATAFILE15, context);
        if (value.isEmpty()) {
            return true;
        }
        return Boolean.valueOf(value);
    }

    public static void setIsKeySafeLogged(boolean value, Context context) {
        safeStringAtDatafile(DATAFILE16, String.valueOf(value), context);
    }

    public static boolean isKeySafeLogged(Context context) {
        String value = readDataFile(DATAFILE16, context);
        if (value.isEmpty()) {
            return true;
        }
        return Boolean.valueOf(value);
    }

    /**
     * Safes the list containing all indirect contacts.
     * @param indirectContactArrayList list of indirect contacts
     */

    public synchronized static void safeListOfIndirectContacts(ArrayList<IndirectContact> indirectContactArrayList, Context context) {
        Log.d(TAG, "safeListOfIndirectContacts was called.");
        String listValues = "";
        for (IndirectContact contact: indirectContactArrayList) {
            listValues = listValues + "-<>-" + contact.getDate();
        }
        safeStringAtDatafile(DATAFILE21, listValues, context);
    }

    /**
     * Returns the list of the indirect contacts
     */

    public synchronized static ArrayList<IndirectContact> getListOfIndirectContacts(Context context) {
        ArrayList<IndirectContact> indirectContactArrayList = new ArrayList<>();
        String[] listvalues = getValuesAsArray(DATAFILE21, context);

        if (listvalues != null) {
            for (String date: listvalues) {
                indirectContactArrayList.add(new IndirectContact(new Date(date)));
            }
        }

        return indirectContactArrayList;
    }

    /**
     * Safes the list containing all direct contacts.
     * @param directContactArrayList list of direct contacts
     */

    public synchronized static void safeListOfDirectContacts(ArrayList<DirectContact> directContactArrayList, Context context) {
        Log.d(TAG, "safeListOfDirectContacts was called.");
        String listValues = "";
        for (DirectContact contact: directContactArrayList) {
            listValues = listValues + "-<>-" + contact.getDate();
        }
        safeStringAtDatafile(DATAFILE22, listValues, context);
    }

    /**
     * returns the list of direct contacts.
     */

    public synchronized static ArrayList<DirectContact> getListOfDirectContacts(Context context) {
        ArrayList<DirectContact> directContactArrayList = new ArrayList<>();
        String[] listvalues = getValuesAsArray(DATAFILE22, context);

        if (listvalues != null) {
            for (String date: listvalues) {
                directContactArrayList.add(new DirectContact(new Date(date)));
            }
        }

        return directContactArrayList;
    }

    /**
     * Safes the date of last reported infection.
     *
     * @param date
     */

    public static void safeDateOfLastReportedInfection(String date, Context context) {
        Log.d(TAG, "safeDateOfLastReportedInfection() was called with " + date);
        safeStringAtDatafile(DATAFILE23, date, context);
    }

    /**
     * Getter for the date of the last reported infection.
     *
     * @return the date of the last reported infection
     */
    public static String getDateOfLastReportedInfection(Context context) {
        Log.d(TAG, "getDateOfLastReportedInfection() was called.");
        return readDataFile(DATAFILE23, context);
    }

    /**
    * method to set if the terms of use have been accepted
     */
    public static void setIsFirstAppStart(boolean value, Context context) {
        safeStringAtDatafile(DATAFILE17, String.valueOf(value), context);
    }

    /**
     * method to ask whether the terms of use have been accepted or not
     * @param context the app
     * @return true if the terms of use haven't been accepted, false if they have already been accepted
     */
    public static boolean isFirstAppStart(Context context) {
        String value = readDataFile(DATAFILE17, context);
        if (value.isEmpty()) {
            return true;
        }
        return Boolean.valueOf(value);
    }

    /**
     * This method clears the clearIndirectContacts.
     */
    public static void clearIndirectContacts(Context context) {
        Log.d(TAG, "clearIndirectContacts() was called.");
        safeStringAtDatafile(DATAFILE21, "", context);
    }

    /**
     * This method clears the clearDirectContacts.
     */
    public static void clearDirectContacts(Context context) {
        Log.d(TAG, "clearDirectContacts() was called.");
        safeStringAtDatafile(DATAFILE22, "", context);
    }

    /**
     * Safes the Time of the last ring.
     *
     * @param date last ring
     */
    public static void safeLastRingTime(Date date, Context context) {
        Log.d(TAG, "safeLastRingDate() was called with " + date.toString());
        safeStringAtDatafile(DATAFILE08, date.toString(), context);
    }

    /**
     * Getter for the date of the first start.
     *
     * @return the days since last Contact as int.
     */
    public static Date getLastRingTime(Context context) {
        Log.d(TAG, "getLastRingTime() was called.");
        String date = readDataFile(DATAFILE08, context);
        if (date.isEmpty()) {
            return null;
        } else {
            return new Date(date);
        }
    }

    /**
     * Returns true if the date is older than 5 minutes
     *
     * @param date
     * @return boolean if the date is older than 5 minutes
     */
    public static boolean lastRingOlderThenFifteenMinutes(Date date) {
        Log.d(TAG, "dateIsOld() was called");
        Log.d(TAG, "dateIsOld() was called");

        boolean result = false;
        Date oldDate = date;
        oldDate.setMinutes(date.getMinutes() + 5);

        if (oldDate.before(new Date())) {
            result = true;
        }

        return result;
    }

    public static boolean shouldRingAgain(Context context) {
        Date lastRing = getLastRingTime(null);
        if (lastRing == null) {
            safeLastRingTime(new Date(), null);
            Log.d("requestLine", "LocalSafer: shouldRingAgain() was called and value is true");
            return true;
        }

        boolean value = lastRingOlderThenFifteenMinutes(lastRing);

        if (value == true) {
            safeLastRingTime(new Date(), null);
        }
        Log.d("requestLine", "LocalSafer: shouldRingAgain() was called and value is " + value);
        return value;
    }
}
