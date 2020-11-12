package de.monokel.frontend.keytransfer;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.RemoteException;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.BuildConfig;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import de.monokel.frontend.MainActivity;
import de.monokel.frontend.R;
import de.monokel.frontend.Constants;

/**
 * BeaconBackgroundService that extends the Android Application so it starts when the Application is
 * first launched. From then on it will continue to scan for all BLE Beacons in the Background as a
 * foreground Service on Android 8+
 *
 * @author Nico Martin
 * @version 2020-11-02
 */
public class BeaconBackgroundService extends Application implements BootstrapNotifier, BeaconConsumer, RangeNotifier {

    private static Context context;

    private static final String TAG = "BeaconBackgroundService";
    private static RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private BeaconManager beaconManager;
    private static BeaconTransmitter beaconTransmitter;

    public static ArrayList<String> beaconids = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");
        BeaconBackgroundService.context = getApplicationContext();

        // Constants flag that disables scanning and transmitting so that development team doesn't
        // have an App on their phone that constantly uses battery
        if (Constants.SCAN_AND_TRANSMIT) {
            beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);

            // By default the AndroidBeaconLibrary will only find AltBeacons.  If you wish to make it
            // find a different type of beacon, you must specify the byte layout for that beacon's
            // advertisement with a line like below.  The example shows how to find a beacon with the
            // same byte layout as AltBeacon but with a beaconTypeCode of 0xaabb.  To find the proper
            // layout expression for other beacon types, do a web search for "setBeaconLayout"
            // including the quotes.
            beaconManager.getBeaconParsers().clear();
            beaconManager.getBeaconParsers().add(new BeaconParser().
                    setBeaconLayout("s:0-1=fd6f,p:-:-59,i:2-17,d:18-21"));

            // Uncomment the code below to use a foreground service to scan for beacons. This unlocks
            // the ability to continually scan for long periods of time in the background on Andorid 8+
            // in exchange for showing an icon at the top of the screen and a always-on notification to
            // communicate to users that your app is using resources in the background.
            Notification.Builder builder = new Notification.Builder(this);
            builder.setSmallIcon(R.drawable.ic_launcher);
            builder.setContentTitle("Scanning for Beacons");
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
            );
            builder.setContentIntent(pendingIntent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("My Notification Channel ID",
                        "My Notification Name", NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription("My Notification Channel Description");
                NotificationManager notificationManager = (NotificationManager) getSystemService(
                        Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);
                builder.setChannelId(channel.getId());
            }
            beaconManager.enableForegroundServiceScanning(builder.build(), 456);

            // For the above foreground scanning service to be useful, you need to disable
            // JobScheduler-based scans (used on Android 8+) and set a fast background scan
            // cycle that would otherwise be disallowed by the operating system.
            beaconManager.setEnableScheduledScanJobs(false);
            beaconManager.setBackgroundBetweenScanPeriod(Constants.BACKGROUND_SCAN_PERIOD);
            beaconManager.setForegroundBetweenScanPeriod(Constants.FOREGROUND_SCAN_PERIOD);


            Log.d(TAG, "setting up background monitoring for beacons and power saving");
            // wake up the app when a beacon is seen
//            Region region = new Region("backgroundRegion", null, null, null);
//            regionBootstrap = new RegionBootstrap(this, region);

            beaconManager.bind(this);
            // simply constructing this class and holding a reference to it in your custom Application
            // class will automatically cause the BeaconLibrary to save battery whenever the application
            // is not visible.  This reduces bluetooth power usage by about 60%
            backgroundPowerSaver = new BackgroundPowerSaver(this);

            /*// This code block starts beacon transmission
            Log.d(TAG, "Transmit as Exposure Notification Beacon with id1=" + Constants.id1);
            Beacon beacon = new Beacon.Builder()
                    .setId1(Constants.id1)
                    .setDataFields(Arrays.asList(new Long[]{0l}))
                    .build();

            BeaconParser beaconParser = new BeaconParser()
                    .setBeaconLayout("s:0-1=fd6f,p:0-0:-63,i:2-17,d:18-21");
            BeaconTransmitter beaconTransmitter = new
                    BeaconTransmitter(getApplicationContext(), beaconParser);
            beaconTransmitter.startAdvertising(beacon);*/
        }
    }

    public void enableMonitoring() {
        Log.d(TAG, "Scanning: ON");
        Region region = new Region("backgroundRegion",
                null, null, null);
        regionBootstrap = new RegionBootstrap(this, region);
    }

    public void disableMonitoring() {
        Log.d(TAG, "Scanning: OFF");
        if (regionBootstrap != null) {
            regionBootstrap.disable();
            regionBootstrap = null;
        }
    }

    public static void transmitAsBeacon() {
        if(Constants.SCAN_AND_TRANSMIT) {
            Log.d(TAG, "Transmit as Exposure Notification Beacon with id1=" + Constants.testid1);
            Beacon beacon = new Beacon.Builder()
                    .setId1(Constants.testid1)
                    .setDataFields(Arrays.asList(new Long[]{0l}))
                    .build();

            BeaconParser beaconParser = new BeaconParser()
                    .setBeaconLayout("s:0-1=fd6f,p:0-0:-63,i:2-17,d:18-21");
                    beaconTransmitter = new
                    BeaconTransmitter(BeaconBackgroundService.getAppContext(), beaconParser);
            beaconTransmitter.startAdvertising(beacon);
        }
    }

    public static void stopTransmittingAsBeacon() {
        Log.d(TAG, "Stop Transmitting Exposure Notification Beacon");
        beaconTransmitter.stopAdvertising();
    }

    /**
     * Callback when an Beacon enters the specified region
     *
     * @param region the specified region
     */
    @Override
    public void didEnterRegion(Region region) {
        Log.d(TAG, "didEnterRegion()");
        try {
            //Start the ranging process
            beaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            if (BuildConfig.DEBUG) Log.d(TAG, "Can't start ranging");
        }
    }

    /**
     * Callback when an Beacon exits the specified region
     *
     * @param region the specified region
     */
    @Override
    public void didExitRegion(Region region) {
        Log.d(TAG, "didExitRegion()");
        try {
            beaconManager.stopRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Callback when there is at least one beacon in the region
     *
     * @param state  the current state 1 = INSIDE, 0 = OUTSIDE
     * @param region the specified region
     */
    @Override
    public void didDetermineStateForRegion(int state, Region region) {
        Log.d(TAG, "didDetermineStateForRegion()");
        Log.d(TAG, "I have just switched from seeing/not seeing beacons: " + state);
    }

    /**
     * Callback when the ranging from didEnterRegion(Region region): beaconManager.startRangingBeaconsInRegion(region);
     * was triggered to determine data from the beacons
     *
     * @param beacons
     * @param region
     */
    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        if (beacons.size() > 0) {
            for (Beacon b : beacons) {
                String beaconid1 = String.valueOf(b.getId1());
                if (beaconid1.substring(0, 8).equals(Constants.cowappBeaconIdentifier)) {
                    String context = "Beacon found: id1=" + beaconid1;
                    Log.d(TAG, context);
                    addId(beaconid1);
                    //Comment out to send Notification
                    sendNotification(context);
                } else {
                    Log.d(TAG, "Found an unknown Beacon: "+ beaconid1);
                }
            }
        }
    }

    /**
     * Sets the rangeNotifier to the beaconManager when the beaconManager is online (Callback)
     */
    @Override
    public void onBeaconServiceConnect() {
        Log.d(TAG, "onBeaconServiceConnect()");
        beaconManager.setRangeNotifier(this);
    }

    /**
     * Sends notification from background with given context for test purposes
     *
     * @param context the given context for the notification
     */
    private void sendNotification(String context) {
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Beacon Reference Notifications",
                    "Beacon Reference Notifications", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(channel);
            builder = new Notification.Builder(this, channel.getId());
        } else {
            builder = new Notification.Builder(this);
            builder.setPriority(Notification.PRIORITY_HIGH);
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(new Intent(this, MainActivity.class));
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle("I detect a beacon");
        builder.setContentText(context);
        builder.setContentIntent(resultPendingIntent);
        notificationManager.notify(1, builder.build());
    }

    /**
     * Method that returns the application context
     *
     * @return application context
     */
    public static Context getAppContext() {
        return BeaconBackgroundService.context;
    }

    public static void addId(String id) {
        Log.d(TAG, "Added "+id);
        beaconids.add(id);
    }
}
