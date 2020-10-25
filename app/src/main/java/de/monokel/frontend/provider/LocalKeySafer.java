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
     * safes the given key and the time of the method-call.
     * @param contactKey The Key of the contact
     */
    public synchronized static void addKeyPairToSavedKeyPairs(String contactKey) {
        String alreadySavedKeyPairs = readKeyPairsDataFile();
        String allKeyPairsToSafe = alreadySavedKeyPairs + "-<>-" + contactKey + "----" + new Date().toString();
        try {
            FileOutputStream data = MyApplication.getAppContext().openFileOutput("cowappkeys.txt",
                    Context.MODE_PRIVATE);
            data.write(allKeyPairsToSafe.getBytes());
            data.close();
        } catch(IOException ex) {
            System.out.println("Some Mistakes happened at addKeyPairToSavedKeyPairs(...)");
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
}
