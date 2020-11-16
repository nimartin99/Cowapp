package de.hhn.frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.altbeacon.beacon.BeaconManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import de.hhn.frontend.provider.Alarm;
import de.hhn.frontend.provider.Key;
import de.hhn.frontend.provider.LocalSafer;
import de.hhn.frontend.provider.NotificationService;
import de.hhn.frontend.provider.RequestedObject;
import de.hhn.frontend.provider.RetrofitService;
import de.hhn.frontend.utils.RetryCallUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Main screen for CoWApp
 *
 * @author Tabea leibl
 * @author Philipp Alessandrini
 * @author Mergim Miftari
 * @author Nico Martin
 * @author Jonas
 * @version 2020-11-16
 */
public class MainActivity extends AppCompatActivity {

    //TAG for Logging example: Log.d(TAG, "fine location permission granted"); -> d for debug
    protected static final String TAG = "MainActivity";

    // application context that allows stating android services from static methods
    private static Context context;

    //For push notification
    public static final String CHANNEL_ID = "pushNotifications";
    private NotificationManager notificationManager;

    private Retrofit retrofit;
    private static RetrofitService retrofitService;
    private String BASE_URL = "http://10.0.2.2:3000"; // for emulated phone
    private String PHONE_URL = "http://" + Personal_Constants.OWN_IP + ":3000";

    //Expected Permission Values
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_BACKGROUND_LOCATION = 2;

    //For the once-a-day-alarm-clock for deleting keys that are older than 3 weeks
    private PendingIntent myPendingIntent;
    private AlarmManager alarmManager;
    private BroadcastReceiver myBroadcastReceiver;
    private Calendar firingCal;

    //To display the current risk status
    private static ImageView trafficLight;
    private static TextView riskStatus;
    private static TextView daysSinceFirstUseTextview;


    String prefDataProtection = "ausstehend";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //traffic light image view and risk status text view
        this.trafficLight = (ImageView) this.findViewById(R.id.trafficLightView);
        this.riskStatus = (TextView) this.findViewById(R.id.RiskView);
        this.daysSinceFirstUseTextview = (TextView) this.findViewById(R.id.ViewDaysUse);

        //Check bluetooth and location turned on
        if(Constants.SCAN_AND_TRANSMIT) {
            verifyBluetooth();
        }
        //Request needed permissions
        requestPermissions();

        // init retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);

        // get context for using context in static methods
        context = this.getApplicationContext();

        //Create channel for push up notifications
        createNotificationChannel();

        //show current risk level (updated once a day)
        showTrafficLightStatus();
        showRiskStatus();

        //show current Info about days since usage.
        //showDaysSinceUse();


        //If the app is opened for the first time the user has to accept the data protection regulations
        if (firstAppStart()) {
            Intent nextActivity = new Intent(MainActivity.this, DataProtectionActivity.class);
            startActivity(nextActivity);
        } else {
            //Test menu button listener
            Button testMenuButton = (Button) findViewById(R.id.TestMenuButton);

            testMenuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Go to test menu screen
                    Intent nextActivity = new Intent(MainActivity.this, TestMenuActivity.class);
                    startActivity(nextActivity);
                }
            });

            //Report infection button listener
            Button reportInfectionButton = (Button) findViewById(R.id.InfektionMeldenButton);

            reportInfectionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Go to screen to report infection
                    Intent nextActivity = new Intent(MainActivity.this, ReportInfectionActivity.class);
                    startActivity(nextActivity);
                }
            });

            //suspicion button listener
            Button suspicionButton = (Button) findViewById(R.id.VerdachtButton);

            suspicionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Go to screen to inform what to do with infection suspicion
                    Intent nextActivity = new Intent(MainActivity.this, SuspicionActivity.class);
                    startActivity(nextActivity);
                }
            });
        }

        //Register AlarmManager Broadcast receive.
        firingCal = Calendar.getInstance();
        firingCal.set(Calendar.HOUR, 0); // alarm hour
        firingCal.set(Calendar.MINUTE, 5); // alarm minute
        firingCal.set(Calendar.SECOND, 0); // and alarm second
        long intendedTime = firingCal.getTimeInMillis();

        registerMyAlarmBroadcast();

        alarmManager.setRepeating( AlarmManager.RTC_WAKEUP, intendedTime , (5 * 60 * 1000), myPendingIntent );

    }

    /**
     * Creates the dropdown menu of the main screen
     * @param menu the created menu
     * @return true so the menu is shown
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }

    /**
     * adds on click listeners to the dropdown menu
     * @param item the item on which the user has clicked
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.item1:
                //Go to LOG screen
                Intent nextActivityItem1 = new Intent(MainActivity.this, LogActivity.class);
                startActivity(nextActivityItem1);
                return true;
            case R.id.item2:
                //Go to settings screen
                Intent nextActivityItem2 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(nextActivityItem2);
                return true;
            case R.id.item3:
                //Go to info screen
                Intent nextActivity = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(nextActivity);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method supports the once-a-day-alarm-clock for deleting keys older then 3 weeks.
     */
    private void registerMyAlarmBroadcast()
    {
        //This is the call back function(BroadcastReceiver) which will be call when your
        //alarm time will reached.
        myBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Alarm.ring();
            }
        };

        registerReceiver(myBroadcastReceiver, new IntentFilter("com.alarm.example") );
        myPendingIntent = PendingIntent.getBroadcast( this, 0, new Intent("com.alarm.example"),0 );
        alarmManager = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));
    }

    /**
     * At first start of the app the user has to accept the data protection regulations before he can
     * use the app
     */
    public boolean firstAppStart() {
        SharedPreferences preferences = getSharedPreferences(prefDataProtection, MODE_PRIVATE);
        //generate and save the Date of the first app Start, maybe this code should be relocated.
        LocalSafer.safeFirstStartDate(getCurrentDate());

        requestKey();

        if (preferences.getBoolean(prefDataProtection, true)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(prefDataProtection, false);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Request a new key from the server.
     */
    public static void requestKey() {
        Call<RequestedObject> call = retrofitService.requestKey();
        RetryCallUtil.enqueueWithRetry(call, new Callback<RequestedObject>() {
            @Override
            public void onResponse(Call<RequestedObject> call, Response<RequestedObject> response) {
                if (response.code() == 200) {
                    RequestedObject requestedKey = response.body();
                    // set the key
                    Key.setKey(Key.increaseKey(requestedKey.getKey()));
                    // send new key to the db
                    sendKey();
                } else if (response.code() == 404) {
                    Log.w(TAG, "requestKey: KEY_DOES_NOT_EXIST");
                }
            }

            @Override
            public void onFailure(Call<RequestedObject> call, Throwable t) {
                Log.w(TAG, Objects.requireNonNull(t.getMessage()));
                serverResponseNotification("NO_CONNECTION_NOTIFICATION");
            }
        });
    }

    /**
     * Report an infection by sending the current key to the server.
     */
    public static void reportInfection(final String contactType) {
        Log.d(TAG, "Sending all contact keys to the server, therefore the response may take some time...");
        Runnable runnable = new Runnable() {
            public void run() {
                // check if infected user has had contacts
                if (LocalSafer.getKeyPairs() != null) {
                    // get all contact keys
                    HashMap<String, String> keyMap = new HashMap<>();
                    StringBuilder contactDate = new StringBuilder();
                    StringBuilder contactKey = new StringBuilder();
                    for (int i = 0; i < LocalSafer.getKeyPairs().length; i++) {
                        // don't append "|" on the fist circle
                        if (i == 0) {
                            contactDate.append(LocalSafer.getKeyPairs()[i].split("----")[1]);
                            contactKey.append(LocalSafer.getKeyPairs()[i].split("----")[0]);
                        } else {
                            contactDate.append("|").append(LocalSafer.getKeyPairs()[i].split("----")[1]);
                            contactKey.append("|").append(LocalSafer.getKeyPairs()[i].split("----")[0]);
                        }
                    }
                    keyMap.put("contactType", contactType);
                    keyMap.put("contactDate", contactDate.toString());
                    keyMap.put("contactKey", contactKey.toString());

                    // send contact keys to the server
                    Call<Void> call = retrofitService.reportInfection(keyMap);
                    RetryCallUtil.enqueueWithRetry(call, new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 200) {
                                Log.d(TAG, "Contacts are successfully reported");
                            } else if (response.code() == 400) {
                                Log.w(TAG, "reportInfection: NO DEFINED CONTACT_TYPE");
                            } else if (response.code() == 404) {
                                Log.w(TAG, "reportInfection: UNDEFINED_REQUEST_BODY_VALUE");
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.w(TAG, Objects.requireNonNull(t.getMessage()));
                            serverResponseNotification("NO_CONNECTION_NOTIFICATION");
                        }
                    });
                } else {
                    Log.d(TAG, "User doesn't have contacts registered");
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * Request the infection status of the user from the server.
     */
    public static void requestInfectionStatus() {
        Log.d(TAG, "Sending all own keys to the server, therefore the response may take some time...");
        Runnable runnable = new Runnable() {
            public void run() {
                if (LocalSafer.getOwnKeys() != null) {
                    // get all own keys
                    HashMap<String, String> ownKeysMap = new HashMap<>();
                    StringBuilder contactDate = new StringBuilder();
                    StringBuilder contactKey = new StringBuilder();
                    for (int i = 0; i < LocalSafer.getOwnKeys().length; i++) {
                        // don't append "|" on the fist circle
                        if (i == 0) {
                            contactDate.append(LocalSafer.getOwnKeys()[i].split("----")[1]);
                            contactKey.append(LocalSafer.getOwnKeys()[i].split("----")[0]);
                        } else {
                            contactDate.append("|").append(LocalSafer.getOwnKeys()[i].split("----")[1]);
                            contactKey.append("|").append(LocalSafer.getOwnKeys()[i].split("----")[0]);
                        }
                    }
                    ownKeysMap.put("userDate", contactDate.toString());
                    ownKeysMap.put("userKey", contactKey.toString());

                    // send user keys to the server
                    Call<String> call = retrofitService.requestInfectionStatus(ownKeysMap);
                    RetryCallUtil.enqueueWithRetry(call, new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.code() == 200) {
                                // get infection status
                                String infectionStatus = response.body();
                                if (infectionStatus.equals("DIRECT_CONTACT")) {
                                    Log.d(TAG, "User has had direct contact with an infected person");
                                    // send own contacts as indirect contacts to the server
                                    reportInfection("INDIRECT");
                                    // inform user via push-up notification about the direct contact
                                    serverResponseNotification("DIRECT_CONTACT_NOTIFICATION");
                                } else if (infectionStatus.equals("INDIRECT_CONTACT")) {
                                    Log.d(TAG, "User has had indirect contact with an infected person");
                                    // inform user via push-up notification about the indirect contact
                                    serverResponseNotification("INDIRECT_CONTACT_NOTIFICATION");
                                } else {
                                    Log.w(TAG, "onResponse: NO DEFINED INFECTION_STATUS");
                                }
                            } else if (response.code() == 400) {
                                // user has had no contact
                                Log.d(TAG, "User has had no contact with an infected person");
                            } else if (response.code() == 404) {
                                Log.w(TAG, "onResponse: UNDEFINED_REQUEST_BODY_VALUE");
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.w(TAG, Objects.requireNonNull(t.getMessage()));
                            serverResponseNotification("NO_CONNECTION_NOTIFICATION");
                        }
                    });
                } else {
                    Log.d(TAG, "User has no keys");
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    // Send the key to inform the database about the new key
    private static void sendKey() {
        // prepare users key for report
        HashMap<String, String> sendKeyMap = new HashMap<>();
        sendKeyMap.put("key", Key.getKey());

        // send values to the server
        Call<Void> call = retrofitService.sendKey(sendKeyMap);
        RetryCallUtil.enqueueWithRetry(call, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    // log newest key
                    Log.d(TAG, "Key: " + Key.getKey());
                    // safe new key
                    LocalSafer.safeOwnKey(Key.getKey());
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.w(TAG, Objects.requireNonNull(t.getMessage()));
                serverResponseNotification("NO_CONNECTION_NOTIFICATION");
            }
        });
    }

    // standard notification if there is no connection to the server
    private static void serverResponseNotification(String notificationType) {
        Intent responsePushNotification = new Intent(context, NotificationService.class);
        switch (notificationType) {
            case "DIRECT_CONTACT_NOTIFICATION":
                responsePushNotification.putExtra("TITLE", "Direkten Kontakt zu einer infizierten Person festgestellt");
                responsePushNotification.putExtra("TEXT", "Hier klicken für weitere Informationen.");
                responsePushNotification.putExtra("CLASS", PushNotificationActivity.class);
                break;
            case "INDIRECT_CONTACT_NOTIFICATION":
                responsePushNotification.putExtra("TITLE", "Indirekten Kontakt zu einer infizierten Person festgestellt");
                responsePushNotification.putExtra("TEXT", "Hier klicken für weitere Informationen.");
                responsePushNotification.putExtra("CLASS", PushNotificationActivity.class);
                break;
            case "NO_CONNECTION_NOTIFICATION":
                responsePushNotification.putExtra("TITLE", "Es konnte keine Verbindung zum Server hergestellt werden");
                responsePushNotification.putExtra("TEXT", "Versuche Verbindungsaufbau in 5 Minuten erneut...");
                break;
            default:
                Log.w(TAG, "NO DEFINED NOTIFICATION_TYPE");
        }
        context.startService(responsePushNotification);
    }

    /**
     * Request all needed permissions based on SDK Version
     * (Permission already requested in Manifest -> double check)
     */
    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (this.checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (!this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("This app needs background location access");
                            builder.setMessage("Please grant location access so this app can detect beacons in the background.");
                            builder.setPositiveButton(android.R.string.ok, null);
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                                @TargetApi(23)
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                                            PERMISSION_REQUEST_BACKGROUND_LOCATION);
                                }

                            });
                            builder.show();
                        } else {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Functionality limited");
                            builder.setMessage("Since background location access has not been granted, this app will not be able to discover beacons in the background.  Please go to Settings -> Applications -> Permissions and grant background location access to this app.");
                            builder.setPositiveButton(android.R.string.ok, null);
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                }

                            });
                            builder.show();
                        }
                    }
                }
            } else {
                if (!this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                            PERMISSION_REQUEST_FINE_LOCATION);
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons.  Please go to Settings -> Applications -> Permissions and grant location access to this app.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }

            }
        }
    }

    /**
     * Verify if Bluetooth is turned on and if BLE is supported
     */
    private void verifyBluetooth() {
        try {
            if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Bluetooth not enabled");
                builder.setMessage("Please enable bluetooth in settings and restart this application.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //finish();
                        //System.exit(0);
                    }
                });
                builder.show();
            }
        } catch (RuntimeException e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth LE not available");
            builder.setMessage("Sorry, this device does not support Bluetooth LE.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    //finish();
                    //System.exit(0);
                }

            });
            builder.show();

        }

    }

    /**
     * Permission dialog result catch to follow further steps if not granted
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_FINE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "fine location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
            case PERMISSION_REQUEST_BACKGROUND_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "background location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since background location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    /**
     * create channel for the notification to be delivered as heads-up notification
     */
    private void createNotificationChannel() {
        // Create the NotificationChannel (only on API 26+ because
        // the NotificationChannel class is new and not in the support library)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH; //high priority for heads-up notifications for android 8.0 and higher
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }





    /**
     * Getter of the current date when this method is used
     *
     * @return
     */

    public static String getCurrentDate() {

        Date currentDate = Calendar.getInstance().getTime();
        String formattedDateString = DateFormat.getDateInstance().format(currentDate);

        //Log.d("Jonas Log", currentDate.toString());
        //Log.d("Jonas Log", formattedDateString);

        return formattedDateString;
    }

    /**
     * Methode berechnet den Abstand zwischen dem heutigen und dem Datum des ersten Starts der App
     *
     * @return
     * @throws ParseException
     */


    public static long getDateDiffSinceFirstUse() {


        Date firstAppStartDate = null;
        try {
            firstAppStartDate = new SimpleDateFormat("MMMM dd, yyyy").parse(LocalSafer.getFirstStartDate());
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Jonas Log", "Parse gone Wrong");
        }

        Date currentDate = new Date();

        long diffInMillis = currentDate.getTime() - firstAppStartDate.getTime();
        long dateDiffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

        return dateDiffInDays;
    }


    public static String generateStringDaysSince() {
        String daysSinceText = "days of usage";
        String language = Locale.getDefault().getLanguage();
        if(language == "de") {
            daysSinceText = ("Seit dem " + LocalSafer.getFirstStartDate() + " helfen Sie, seit " + getDateDiffSinceFirstUse() + " Tagen, Corona einzudämmen.");
        }
        else{
            daysSinceText = ("Since " + LocalSafer.getFirstStartDate() + " you are helping for " + getDateDiffSinceFirstUse() + " days to fight Corona.");
        }
        return daysSinceText;
    }

    public static void showDaysSinceUse() {
        daysSinceFirstUseTextview.setText(generateStringDaysSince());
    }


    /**
     * method called daily to show the right traffic light status (for current health risk)
     */
    public static void showTrafficLightStatus() {
        int riskValue = LocalSafer.getRiskLevel();
        if(riskValue <= 33) {
            trafficLight.setImageResource(R.drawable.green_traffic_light);
        }
        else if(riskValue <=70) {
            trafficLight.setImageResource(R.drawable.yellow_traffic_light);
        }
        else {
            trafficLight.setImageResource(R.drawable.red_traffic_light);
        }
    }

    /**
     * method called daily to show the right health risk status
     */
    public static void showRiskStatus(){
        String language = Locale.getDefault().getLanguage();
        int riskValue = LocalSafer.getRiskLevel();
        if(riskValue <= 33) {
            if(language == "de") {
                riskStatus.setText(riskValue + ": Geringes Risiko");
            }
            else{
                riskStatus.setText(riskValue + ": Low Risk");
            }
        }
        else if(riskValue <=70) {
            if(language == "de") {
                riskStatus.setText(riskValue + ": Moderates Risiko");
            }
            else{
                riskStatus.setText(riskValue + ": Moderate Risk");
            }
        }
        else {
            if(language == "de") {
                riskStatus.setText(riskValue + ": Hohes Risiko");
            }
            else{
                riskStatus.setText(riskValue + ": High Risk");
            }
        }
    }

    public static Context getContext(){
        return context;
    }
}