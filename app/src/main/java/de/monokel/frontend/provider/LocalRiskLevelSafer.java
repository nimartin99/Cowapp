package de.monokel.frontend.provider;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.monokel.frontend.keytransfer.BeaconBackgroundService;

/**
 * Safer for the Risk Level and the days since last contact
 *
 * @author Miftari
 */
public class LocalRiskLevelSafer {
    /**
     * Safes the risk Level.
     * @param riskLevel risk level as int
     */
    public static void safeRiskLevel(int riskLevel) {
        try {
            FileOutputStream data = BeaconBackgroundService.getAppContext().openFileOutput("cowapprisklevel.txt",
                    Context.MODE_PRIVATE);
            data.write(String.valueOf(riskLevel).getBytes());
            data.close();
        } catch (IOException ex) {
            System.out.println("Some Mistakes happened at safeRiskLevel");
        }
    }

    /**
     * Getter for the risk level.
     * @return risk level as int.
     */
    public static int getRiskLevel() {
        try {
            FileInputStream datafile = BeaconBackgroundService.getAppContext().openFileInput("cowapprisklevel.txt");
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
            return Integer.valueOf(text);
        } catch(Exception ex) { //datafile not found
            return 0;
        }
    }

    /**
     * Safes the daysSinceLastContact.
     * @param daysSinceLastContact days Since last contact as int.
     */
    public static void safeDaysSinceLastContact(int daysSinceLastContact) {
        try {
            FileOutputStream data = BeaconBackgroundService.getAppContext().openFileOutput("cowappdaysslc.txt",
                    Context.MODE_PRIVATE);
            data.write(String.valueOf(daysSinceLastContact).getBytes());
            data.close();
        } catch (IOException ex) {
            System.out.println("Some Mistakes happened at safeDaysSinceLastContact");
        }
    }

    /**
     * Getter for the daysSinceLastContact.
     * @return the days since last Contact as int.
     */
    public static int getDaysSinceLastContact() {
        try {
            FileInputStream datafile = BeaconBackgroundService.getAppContext().openFileInput("cowappdaysslc.txt");
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
            return Integer.valueOf(text);
        } catch(Exception ex) { //datafile not found
            return 0;
        }
    }
}
