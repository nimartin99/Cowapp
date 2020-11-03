package de.monokel.frontend.provider;

import android.content.Context;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.monokel.frontend.keytransfer.BeaconBackgroundService;

/**
 * This Class has the purpose to safe, read and manage the Notifications of the user.
 *
 * @author Mergim Miftari
 */
public class LocalNotificationSafer {
    /**
     * safes the given notification and the time of the method-call. If the parameter is null, the method deleteOldNotifications() is called.
     * @param notification The new notification as String
     */
    public synchronized static void addNotificationToSavedNotifications(String notification) {
        if (notification == null) {
            deleteOldNotifications();
        } else {
            String alreadySavedNotifications = readNotificationDataFile();
            String allNotificationsToSafe = alreadySavedNotifications + "-<>-" + notification + "----" + new Date().toString();
            try {
                FileOutputStream data = BeaconBackgroundService.getAppContext().openFileOutput("cowappnotifications.txt",
                        Context.MODE_PRIVATE);
                data.write(allNotificationsToSafe.getBytes());
                data.close();
            } catch (IOException ex) {
                System.out.println("Some Mistakes happened at addNotificationToSavedNotifications");
            }
        }
    }

    /**
     * This method clears the Notifications-Datafile.
     */
    public static void clearNotificationDataFile() {
        try {
            String nothing = "";
            FileOutputStream data = BeaconBackgroundService.getAppContext().openFileOutput("cowappnotifications.txt",
                    Context.MODE_PRIVATE);
            data.write(nothing.getBytes());
            data.close();
        } catch(IOException ex) {
            System.out.println("Some Mistakes happened at clearNotificationsDataFile()");
        }
    }

    /**
     * Reads the "cowappnotifications.txt" datafile and returns it as String.
     * If there is no "cowappnotifications.txt" datafile the return-value is an empty String.
     * @return returns the "cowappnotifications.txt" data file as String or an empty String
     */
    public static String readNotificationDataFile() {
        try {
            FileInputStream datafile = BeaconBackgroundService.getAppContext().openFileInput("cowappnotifications.txt");
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
     * Notification + "----" + new Date().toString()
     * @return A list of Strings. If there are no saved notifications, the return-value is null.
     */
    public static String[] getNotifications() {
        String notifications = readNotificationDataFile();
        if (notifications.equals("")) {
            return null;
        }
        notifications = notifications.substring(4);
        return notifications.split("-<>-");
    }

    /**
     * All Notifications older than 3 weeks are going to be deleted.
     */
    public static void deleteOldNotifications() {
        String[] notifications = getNotifications();
        String result = "";

        if (notifications != null) {
            for (String string : notifications) {
                String[] strings = string.split("----");
                Date dateOfNotification = new Date(strings[1]);
                if (!dateIsOld(dateOfNotification)) {
                    result = result + "-<>-" + string;
                }
            }

            try {
                FileOutputStream data = BeaconBackgroundService.getAppContext().openFileOutput("cowappnotifications.txt",
                        Context.MODE_PRIVATE);
                data.write(result.getBytes());
                data.close();
            } catch (IOException ex) {
                System.out.println("Some Mistakes happened at deleteOldNotifications(...)");
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
