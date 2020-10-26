package de.monokel.frontend.provider;

import android.content.Context;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This Class has the purpose to safe, read and manage the keys of the contact-persons.
 *
 * @author Mergim Miftari
 */
public class LocalKeySafer {
    /**
     * safes the given key and the time of the method-call. If the parameter is null, the method deleteOldKeyPairs() is called.
     * @param contactKey The Key of the contact
     */
    public synchronized static void addKeyPairToSavedKeyPairs(String contactKey) {
        if (contactKey == null) {
            deleteOldKeyPairs();
        } else {
            String alreadySavedKeyPairs = readKeyPairsDataFile();
            String allKeyPairsToSafe = alreadySavedKeyPairs + "-<>-" + contactKey + "----" + new Date().toString();
            try {
                FileOutputStream data = MyApplication.getAppContext().openFileOutput("cowappkeys.txt",
                        Context.MODE_PRIVATE);
                data.write(allKeyPairsToSafe.getBytes());
                data.close();
            } catch (IOException ex) {
                System.out.println("Some Mistakes happened at addKeyPairToSavedKeyPairs(...)");
            }
        }
    }

    /**
     * This method clears the keyPairDataFile.
     */
    public static void clearKeyPairDataFile() {
        try {
            String nothing = "";
            FileOutputStream data = MyApplication.getAppContext().openFileOutput("cowappkeys.txt",
                    Context.MODE_PRIVATE);
            data.write(nothing.getBytes());
            data.close();
        } catch(IOException ex) {
            System.out.println("Some Mistakes happened at clearKeyPairDataFile()");
        }
    }

    /**
     * Reads the cowappkey.txt datafile and returns it as String.
     * If there is no cowappkeys.txt datafile the return-value is an empty String.
     * @return returns the cowappkeys.txt data file as String or an empty String
     */
    public static String readKeyPairsDataFile() {
        try {
            FileInputStream datafile = MyApplication.getAppContext().openFileInput("cowappkeys.txt");
            List<Byte> data = new ArrayList<Byte>();

            while(true) {
                int b = datafile.read();
                if(b == -1) {
                    break; // end of the datafile.
                } else {
                    data.add((byte) b);
                }
            }
            // Bytes to words
            byte[] bytes = new byte[data.size()];

            for(int i = 0; i < bytes.length; i++) {
                bytes[i] = data.get(i);
            }

            String text = new String(bytes);
            return text;
        } catch(Exception ex) { //datafile not found
            return "";
        }
    }
    /**
     * This Method returns an Array of Strings.
     * The format of the Strings is the following:
     * contactKey + "----" + new Date().toString()
     * @return A list of Strings. If there are no saved keys, the return-value is null.
     */
    public static String[] getKeyPairs() {
        String keyPairs = readKeyPairsDataFile();
        if (keyPairs.equals("")) {
            return null;
        }
        keyPairs = keyPairs.substring(4);
        return keyPairs.split("-<>-");
    }

    /**
     * All Keys older than 3 weeks are going to be deleted.
     */
    public static void deleteOldKeyPairs() {
        String[] keyPairs = getKeyPairs();
        String result = "";

        if (keyPairs != null) {
            for (String string : keyPairs) {
                String[] strings = string.split("----");
                Date dateOfKey = new Date(strings[1]);
                if (!dateIsOld(dateOfKey)) {
                    result = result + "-<>-" + string;
                }
            }

            try {
                FileOutputStream data = MyApplication.getAppContext().openFileOutput("cowappkeys.txt",
                        Context.MODE_PRIVATE);
                data.write(result.getBytes());
                data.close();
            } catch (IOException ex) {
                System.out.println("Some Mistakes happened at deleteOldKeyPairs(...)");
            }
        }
    }

    /**
     * Returns true if the date is older than 3 weeks.
     * @param date
     * @return
     */
    private static boolean dateIsOld(Date date) {
        boolean result = false;
        Date currentDate = new Date();

        int currentMonth = currentDate.getMonth();
        int currentDay = currentDate.getDay();
        int oldMonth = date.getMonth();
        int oldDay = date.getDay();

        if (currentMonth != oldMonth) {
            int days = 0;

            switch (oldMonth) {
                case 1:
                case 7:
                case 3:
                case 5:
                case 8:
                case 10:
                case 12:
                    days = 31;
                    break;

                case 4:
                case 6:
                case 9:
                case 11:
                    days = 30;
                    break;

                case 2:
                    days = 28;
                    break;
            }

            if (((days - oldDay) + currentDay) > 21) {
                result = true;
            }
        }
        return result;
    }
}
