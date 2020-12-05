package de.hhn.frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import de.hhn.frontend.keytransfer.BeaconBackgroundService;
import de.hhn.frontend.date.DateHelper;
import de.hhn.frontend.provider.Alarm;
import de.hhn.frontend.provider.Key;
import de.hhn.frontend.provider.LocalSafer;
import de.hhn.frontend.provider.NotificationService;
import de.hhn.frontend.provider.RequestedObject;
import de.hhn.frontend.provider.RetrofitService;
import de.hhn.frontend.risklevel.DirectContact;
import de.hhn.frontend.risklevel.IndirectContact;
import de.hhn.frontend.risklevel.RiskLevel;
import de.hhn.frontend.utils.ResponseState;
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
 * @author Jonas Klein
 * @version 2020-12-04
 */
public class MainActivity extends AppCompatActivity {
    //TAG for Logging example: Log.d(TAG, "fine location permission granted"); -> d for debug
    protected static final String TAG = "CoWAppMainActivity";

    private static MainActivity mainActivity;

    // application context that allows stating android services from static methods
    private static Context context;

    //For push notification
    public static final String CHANNEL_ID = "pushNotifications";
    private NotificationManager notificationManager;

    private Retrofit retrofit;
    private static RetrofitService retrofitService;
    private String BASE_URL = "https://cowapp.glitch.me";

    //For the once-a-day-alarm-clock for deleting keys that are older than 3 weeks
    private PendingIntent myPendingIntent;
    private AlarmManager alarmManager;
    private BroadcastReceiver myBroadcastReceiver;
    private Calendar firingCal;

    //To display the current risk status
    private static ImageView trafficLight;
    private static TextView riskStatus;
    private static TextView risklevelStatus;
    //To display the first use date and the elapsed time since the app is used.
    public static TextView dateDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = this;

        //logo of the app in the action bar
        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_cowapp);
        setContentView(R.layout.activity_main);

        //traffic light image view and risk status text view
        this.trafficLight = (ImageView) this.findViewById(R.id.trafficLightView);
        this.riskStatus = (TextView) this.findViewById(R.id.RiskView);
        this.risklevelStatus = (TextView) this.findViewById(R.id.RisklevelView);
        this.dateDisplay = (TextView) this.findViewById(R.id.DateDisplay);

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
        RiskLevel.calculateRiskLevel();

        //If the app is opened for the first time the user has to accept the data protection regulations
        if (LocalSafer.isFirstAppStart(null)){
            if (LocalSafer.getFirstStartDate(null) == null || LocalSafer.getFirstStartDate(null).isEmpty()) {
                LocalSafer.safeFirstStartDate(DateHelper.getCurrentDateString(), this);
            }
            Intent nextActivity = new Intent(MainActivity.this, DataProtectionActivity.class);
            startActivity(nextActivity);
        } else {
            //method to initialize buttons due to infection status of the user
            initButtons();

            //info risk calculation button listener
            ImageButton infoRiskCalcButton = (ImageButton) findViewById(R.id.infoRiskCalcButton);

            infoRiskCalcButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Go to screen to inform how the risk level is calculated
                    Intent nextActivity = new Intent(MainActivity.this, RiskLevelInfoActivity.class);
                    startActivity(nextActivity);
                }
            });
            setAlarm();
        }
        //show current Info about days since usage.
        showDateDisplay();
        if(Constants.SCAN_AND_TRANSMIT) {
            checkIfBluetoothIsEnabled();
            checkIfGPSIsEnabled();
        }
    }

    @Override
    public void onBackPressed() {

    }

    /**
     * First initialization of the application. The date the user starts the app for the first time is saved, the
     * client requests his first key and the alarm is set.
     */
    public void firstinit() {
        //TODO get best place to save first app start date
        if (LocalSafer.getFirstStartDate(null) == null || LocalSafer.getFirstStartDate(null).isEmpty()) {
            LocalSafer.safeFirstStartDate(DateHelper.getCurrentDateString(), this);
        }
        requestKey();
        setAlarm();
    }

    private void setAlarm() {
        boolean alarmUp = (PendingIntent.getBroadcast(this, 0, new Intent("com.alarm.example"), PendingIntent.FLAG_NO_CREATE) != null);

        if (alarmUp == false) {
            LocalSafer.shouldRingAgain(null);
            Log.d("requestLine", "Main Activity: Alarm was set");
            Log.i(TAG, "onCreate: Alarm is set");
            //Register AlarmManager Broadcast receive.
            firingCal = Calendar.getInstance();
            firingCal.set(Calendar.HOUR, 0); // alarm hour
            firingCal.set(Calendar.MINUTE, 15); // alarm minute
            firingCal.set(Calendar.SECOND, 0); // and alarm second
            long intendedTime = firingCal.getTimeInMillis();

            registerMyAlarmBroadcast();

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, intendedTime, (15 * 60 * 1000), myPendingIntent);

            if (Constants.DEBUG_MODE && LocalSafer.isAlarmSetLogged(null)) {
                LocalSafer.addLogValueToDebugLog(getString(R.string.alarm_set), null);
            }
        } else {
            Log.i(TAG, "onCreate: Alarm was already set. No resetting necessary");
            Log.d("requestLine", "Main Activity: Alarm was not set, because allready setted");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Constants.SCAN_AND_TRANSMIT) {
            checkIfBluetoothIsEnabled();
            checkIfGPSIsEnabled();
        }
        if(LocalSafer.isFirstAppStart(null)){
            Intent nextActivity = new Intent(MainActivity.this, DataProtectionActivity.class);
            startActivity(nextActivity);
        }
        else{
            //show current risk level (updated once a day)
            RiskLevel.calculateRiskLevel();
            //initialize buttons due to infection status of the user
            initButtons();
        }
    }

    /**
     * Creates the dropdown menu of the main screen
     *
     * @param menu the created menu
     * @return true so the menu is shown
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);

        if (!Constants.DEBUG_MODE) {
            MenuItem it = menu.getItem(2);
            menu.removeItem(it.getItemId()); //ID of the test menu button because of its place in the dropdown menu
        }
        return true;
    }

    /**
     * adds on click listeners to the dropdown menu
     *
     * @param item the item on which the user has clicked
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                //Go to LOG screen
                Intent nextActivityItem1 = new Intent(MainActivity.this, LogActivity.class);
                startActivity(nextActivityItem1);
                return true;
            case R.id.item2:
                //Go to info screen
                Intent nextActivity = new Intent(MainActivity.this, InfoMenuActivity.class);
                startActivity(nextActivity);
                return true;
            case R.id.item3:
                //Go to test menu screen
                Intent testActivity = new Intent(MainActivity.this, DebugActivity.class);
                startActivity(testActivity);
                return true;
            case R.id.item4:
                //ask the user if he really wants to report himself negative
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setTitle(getString(R.string.head_report_negative));
                builder.setMessage(getString(R.string.text_report_negative));
                //approval button
                builder.setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //report yourself negative and reset risk level
                                RiskLevel.reportNegativeInfectionTestResult();
                                //update buttons if there was a current infection
                                initButtons();
                                //pop up dialog to inform the user that the negative report was successful
                                AlertDialog.Builder builder = new AlertDialog.Builder(getMainActivity());
                                builder.setCancelable(true);
                                builder.setTitle(getString(R.string.head_report_successful));
                                builder.setMessage(getString(R.string.text_report_successful));
                                builder.setPositiveButton(getString(R.string.ok_button),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //pop up disappears
                                            }
                                        });
                                AlertDialog thankYouDialog = builder.create();
                                thankYouDialog.show();
                            }
                        });
                //button to exit the app
                builder.setNegativeButton(getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //just shows the main screen again

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method supports the once-a-day-alarm-clock for deleting keys older then 3 weeks.
     */
    private void registerMyAlarmBroadcast() {
        //This is the call back function(BroadcastReceiver) which will be call when your
        //alarm time will reached.
        myBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Alarm.ring();
            }
        };

        registerReceiver(myBroadcastReceiver, new IntentFilter("com.alarm.example"));
        myPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent("com.alarm.example"), 0);
        alarmManager = (AlarmManager) (this.getSystemService(Context.ALARM_SERVICE));
    }

    /**
     * Request a new key from the server.
     */
    public static boolean requestKey() {
        Log.d("requestLine", "MainActivity: requestKey() was called. ");
        if (LocalSafer.getRiskLevel(null) != 100) {
            Call<String> call = retrofitService.requestKey();
            RetryCallUtil.enqueueWithRetry(call, new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() == 200) {
                        // log newest key
                        Log.d(TAG, "Key: " + response.body());
                        // set last response state
                        ResponseState.setLastResponseState(ResponseState.State.KEY_SUCCESSFULLY_REQUESTED);
                        // key is successfully requested
                        Key.setKeyRequested(true);
                        // reference requested key
                        String requestedKey = response.body();
                        // set the key
                        Key.setKey(requestedKey);
                        // safe new key
                        LocalSafer.safeOwnKey(Key.getKey(), null);
                        //Update the Transmission
                        BeaconBackgroundService application = ((BeaconBackgroundService) BeaconBackgroundService.getAppContext());
                        application.updateTransmissionBeaconKey(requestedKey);
                    } else if (response.code() == 404) {
                        Log.w(TAG, "requestKey: KEY_DOES_NOT_EXIST");
                        // set last response state
                        ResponseState.setLastResponseState(ResponseState.State.NO_EXISTING_KEY);
                        // key is not successfully requested
                        Key.setKeyRequested(false);
                        BeaconBackgroundService application = ((BeaconBackgroundService) BeaconBackgroundService.getAppContext());
                        application.stopTransmittingAsBeacon();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.w(TAG, Objects.requireNonNull(t.getMessage()));
                    serverResponseNotification("NO_CONNECTION_NOTIFICATION");
                    // set last response state
                    ResponseState.setLastResponseState(ResponseState.State.NO_CONNECTION);
                    // key is not successfully requested
                    Key.setKeyRequested(false);
                }
            });

            return Key.isKeyRequested();
        } else {
            return false;
        }
    }

    /**
     * Method called when the user clicks the report infection or negative test result button.
     * A dialog pops up which asks for approval to report or not.
     * If an infection has been reported a dialog pops up to thank the user for the report
     */
    public void reportApproval() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        final int riskValue = LocalSafer.getRiskLevel(null);
        if(riskValue == 100){ //user wants to report negative test result
            builder.setTitle(getString(R.string.head_report_negative));
            builder.setMessage(getString(R.string.text_report_negative));
        }
        else{
            builder.setTitle(getString(R.string.head_report_infection));
            builder.setMessage(getString(R.string.text_report_infection));
        }
        //approval button
        builder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(riskValue == 100){ //report negative test result
                            //report yourself negative and reset risk level
                            RiskLevel.reportNegativeInfectionTestResult();
                            //update buttons
                            initButtons();
                            //pop up dialog to inform the user that the negative report was successful
                            AlertDialog.Builder builder = new AlertDialog.Builder(getMainActivity());
                            builder.setCancelable(true);
                            builder.setTitle(getString(R.string.head_report_successful));
                            builder.setMessage(getString(R.string.text_report_successful));
                            builder.setPositiveButton(getString(R.string.ok_button),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //pop up disappears
                                        }
                                    });
                            AlertDialog thankYouDialog = builder.create();
                            thankYouDialog.show();
                        }
                        else{ //report infection
                            //send infected key to the server
                            reportInfection("DIRECT");
                            //set the risk level corresponding to the infection
                            RiskLevel.reportInfection();
                            //update buttons
                            initButtons();
                            //pop up dialog to thank the user and inform about what to do now
                            AlertDialog.Builder builder = new AlertDialog.Builder(getMainActivity());
                            builder.setCancelable(true);
                            builder.setTitle(getString(R.string.head_thank_you));
                            builder.setMessage(getString(R.string.text_thank_you));
                            builder.setPositiveButton(getString(R.string.ok_button),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //pop up disappears
                                        }
                                    });
                            AlertDialog thankYouDialog = builder.create();
                            thankYouDialog.show();
                        }
                    }
                });
        //button to stop the report
        builder.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //pop up disappears
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /**
     * Report an infection by sending the current key to the server.
     */
    public static void reportInfection(final String contactType) {
        Log.d(TAG, "Sending all contact keys to the server, therefore the response may take some time...");

        Runnable runnable = new Runnable() {
            public void run() {
                LocalSafer.analyzeBufferFile(null);
                // check if infected user has had contacts
                if (LocalSafer.getKeyPairs(null) != null) {
                    // get all contact keys
                    HashMap<String, String> keyMap = new HashMap<>();
                    StringBuilder contactDate = new StringBuilder();
                    StringBuilder contactKey = new StringBuilder();
                    for (int i = 0; i < LocalSafer.getKeyPairs(null).length; i++) {
                        // don't append "|" on the fist circle
                        if (i == 0) {
                            contactDate.append(LocalSafer.getKeyPairs(null)[i].split("----")[1]);
                            contactKey.append(LocalSafer.getKeyPairs(null)[i].split("----")[0]);
                        } else {
                            contactDate.append("|").append(LocalSafer.getKeyPairs(null)[i].split("----")[1]);
                            contactKey.append("|").append(LocalSafer.getKeyPairs(null)[i].split("----")[0]);
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
                                // set last response state
                                ResponseState.setLastResponseState(ResponseState.State.CONTACTS_SUCCESSFULLY_REPORTED);
                            } else if (response.code() == 400) {
                                Log.w(TAG, "reportInfection: NO DEFINED CONTACT_TYPE");
                                // set last response state
                                ResponseState.setLastResponseState(ResponseState.State.NO_DEFINED_CONTACT_TYPE);
                            } else if (response.code() == 404) {
                                Log.w(TAG, "reportInfection: UNDEFINED_REQUEST_BODY_VALUE");
                                // set last response state
                                ResponseState.setLastResponseState(ResponseState.State.UNDEFINED_REQUEST_BODY_VALUE);
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.w(TAG, Objects.requireNonNull(t.getMessage()));
                            serverResponseNotification("NO_CONNECTION_NOTIFICATION");
                            // set last response state
                            ResponseState.setLastResponseState(ResponseState.State.NO_CONNECTION);
                        }
                    });
                } else {
                    Log.d(TAG, "User doesn't have contacts registered");
                    // set last response state
                    ResponseState.setLastResponseState(ResponseState.State.NO_REGISTERED_CONTACTS);
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
                if (LocalSafer.getOwnKeys(null) != null) {
                    // get all own keys
                    HashMap<String, String> ownKeysMap = new HashMap<>();
                    StringBuilder contactDate = new StringBuilder();
                    StringBuilder contactKey = new StringBuilder();
                    for (int i = 0; i < LocalSafer.getOwnKeys(null).length; i++) {
                        // don't append "|" on the fist circle
                        if (i == 0) {
                            contactDate.append(LocalSafer.getOwnKeys(null)[i].split("----")[1]);
                            contactKey.append(LocalSafer.getOwnKeys(null)[i].split("----")[0]);
                        } else {
                            contactDate.append("|").append(LocalSafer.getOwnKeys(null)[i].split("----")[1]);
                            contactKey.append("|").append(LocalSafer.getOwnKeys(null)[i].split("----")[0]);
                        }
                    }
                    ownKeysMap.put("userDate", contactDate.toString());
                    ownKeysMap.put("userKey", contactKey.toString());

                    // send user keys to the server
                    Call<RequestedObject> call = retrofitService.requestInfectionStatus(ownKeysMap);
                    RetryCallUtil.enqueueWithRetry(call, new Callback<RequestedObject>() {
                        @Override
                        public void onResponse(Call<RequestedObject> call, Response<RequestedObject> response) {
                            if (response.code() == 200) {
                                // get infection status
                                RequestedObject infection = response.body();
                                if (infection.getStatus().equals("DIRECT_CONTACT")) {
                                    Log.d(TAG, "User has had direct contact with an infected person");
                                    // set last response state
                                    ResponseState.setLastResponseState(ResponseState.State.DIRECT_CONTACT);
                                    // send own contacts as indirect contacts to the server
                                    reportInfection("INDIRECT");
                                    // inform user via push-up notification about the direct contact
                                    serverResponseNotification("DIRECT_CONTACT_NOTIFICATION");
                                    //calculate and safe Risklevel, update of days since last contact corresponding to the server response
                                    int numberOfInfection= Integer.parseInt(infection.getContactNbr());
                                    for (int i = 0; i != numberOfInfection; i++){
                                        RiskLevel.addContact(new DirectContact(DateHelper.getCurrentDate()));
                                    }
                                } else if (infection.getStatus().equals("INDIRECT_CONTACT")) {
                                    Log.d(TAG, "User has had indirect contact with an infected person");
                                    // set last response state
                                    ResponseState.setLastResponseState(ResponseState.State.INDIRECT_CONTACT);
                                    // inform user via push-up notification about the indirect contact
                                    serverResponseNotification("INDIRECT_CONTACT_NOTIFICATION");
                                    //calculate and safe Risklevel, update of days since last contact corresponding to the server response
                                    int numberOfInfection= Integer.parseInt(infection.getContactNbr());
                                    for (int i = 0; i != numberOfInfection; i++) {
                                        RiskLevel.addContact(new IndirectContact(DateHelper.getCurrentDate()));
                                    }
                                } else {
                                    Log.w(TAG, "onResponse: NO DEFINED INFECTION_STATUS");
                                    // set last response state
                                    ResponseState.setLastResponseState(ResponseState.State.NO_DEFINED_INFECTION_STATUS);
                                }
                            } else if (response.code() == 400) {
                                // user has had no contact
                                Log.d(TAG, "User has had no contact with an infected person");
                                // set last response state
                                ResponseState.setLastResponseState(ResponseState.State.NO_CONTACT);
                            } else if (response.code() == 404) {
                                Log.w(TAG, "onResponse: UNDEFINED_REQUEST_BODY_VALUE");
                                // set last response state
                                ResponseState.setLastResponseState(ResponseState.State.UNDEFINED_REQUEST_BODY_VALUE);
                            }
                        }

                        @Override
                        public void onFailure(Call<RequestedObject> call, Throwable t) {
                            Log.w(TAG, Objects.requireNonNull(t.getMessage()));
                            serverResponseNotification("NO_CONNECTION_NOTIFICATION");
                            // set last response state
                            ResponseState.setLastResponseState(ResponseState.State.NO_CONNECTION);
                        }
                    });
                } else {
                    Log.d(TAG, "User has no keys");
                    // set last response state
                    ResponseState.setLastResponseState(ResponseState.State.NO_USER_KEYS);
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    // standard notification if there is no connection to the server
    private static void serverResponseNotification(String notificationType) {
        switch (notificationType) {
            case "DIRECT_CONTACT_NOTIFICATION":
                Intent directContactPushNotification = new Intent(context, NotificationService.class);
                directContactPushNotification.putExtra("TITLE", "Direct contact with an infected person detected");
                directContactPushNotification.putExtra("TEXT", "Tap for further information.");
                directContactPushNotification.putExtra("CLASS", PushNotificationActivity.class);
                directContactPushNotification.putExtra("LOG", true);
                context.startService(directContactPushNotification);
                break;
            case "INDIRECT_CONTACT_NOTIFICATION":
                Intent indirectContactPushNotification = new Intent(context, NotificationService.class);
                indirectContactPushNotification.putExtra("TITLE", "Indirect contact with an infected person detected");
                indirectContactPushNotification.putExtra("TEXT", "Tap for further information.");
                indirectContactPushNotification.putExtra("CLASS", PushNotificationActivity.class);
                indirectContactPushNotification.putExtra("LOG", true);
                context.startService(indirectContactPushNotification);
                break;
            case "NO_CONNECTION_NOTIFICATION":
                int noConnectionNotificationCounter = LocalSafer.getNotificationCounter(null);
                Log.d(TAG, "noConnectionNotificationCounter: " + noConnectionNotificationCounter);
                // only show this notification every x times a NO_CONNECTION_NOTIFICATION appears
                if ((noConnectionNotificationCounter
                        % NotificationService.getNoConnectionNotificationInterval()) == 0) {
                    Intent noConnectionPushNotification = new Intent(context, NotificationService.class);
                    noConnectionPushNotification.putExtra("TITLE", "Currently no connection to the server");
                    noConnectionPushNotification.putExtra("TEXT", "Trying to connect again in 5 minutes...");
                    noConnectionPushNotification.putExtra("LOG", false);
                    context.startService(noConnectionPushNotification);
                }
                LocalSafer.safeNotificationCounter((noConnectionNotificationCounter + 1)
                        % NotificationService.getNoConnectionNotificationInterval(), null);
                break;
            default:
                Log.w(TAG, "NO DEFINED NOTIFICATION_TYPE");
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
     * Sets the content of the date display on the mainscreen of the app
     */
    public static void showDateDisplay() {
        if(dateDisplay != null) {
            dateDisplay.setText(DateHelper.generateStringForDateDisplay());
        }
    }

    /**
     * method called daily to show the right traffic light status (for current health risk)
     */
    public static void showTrafficLightStatus() {
        if (trafficLight != null) {
            int riskValue = LocalSafer.getRiskLevel(null);
            if (riskValue <= 33) {
                trafficLight.setImageResource(R.drawable.green_traffic_light);
            } else if (riskValue <= 70) {
                trafficLight.setImageResource(R.drawable.yellow_traffic_light);
            } else {
                trafficLight.setImageResource(R.drawable.red_traffic_light);
            }
        }
    }

    /**
     * method called daily to show the right health risk status
     */
    public static void showRiskStatus() {
        int riskValue = LocalSafer.getRiskLevel(null);
        if (riskValue <= 33) {
                String risk = riskStatus.getResources().getString(R.string.risk_status_low);
                riskStatus.setText(risk);
                String risklevel = riskStatus.getResources().getString(R.string.risk_level, riskValue);
                risklevelStatus.setText(risklevel);
        } else if (riskValue <= 70) {
                String risk = riskStatus.getResources().getString(R.string.risk_status_moderate);
                riskStatus.setText(risk);
                String risklevel = riskStatus.getResources().getString(R.string.risk_level, riskValue);
                risklevelStatus.setText(risklevel);
        } else if (riskValue > 70 && riskValue < 100) {
                String risk = riskStatus.getResources().getString(R.string.risk_status_high);
                riskStatus.setText(risk);
                String risklevel = riskStatus.getResources().getString(R.string.risk_level, riskValue);
                risklevelStatus.setText(risklevel);
        } else if (riskValue == 100) {
                String risk = "\n \n" + riskStatus.getResources().getString(R.string.current_infection);
                riskStatus.setText(risk);
                String risklevel = "";
                risklevelStatus.setText(risklevel);
        }
    }

    /**
     * Method called to initialize the buttons depending on the infection status of the user.
     * If the user has no known infection the buttons are to report yourself infected and inform yourself what to
     * do if you have a suspicion to be infected.
     * If the user has a current infection the buttons change to a button for further information and one to report
     * yourself negative.
     */
    public void initButtons(){
        int riskValue = LocalSafer.getRiskLevel(null);
        //set buttons
        Button reportInfectionButton = (Button) findViewById(R.id.InfektionMeldenButton);
        Button suspicionButton = (Button) findViewById(R.id.VerdachtButton);
        //set button text and listener
        if(riskValue == 100){ //the user has a current infection
            //change report infection button to further information button and suspicion button to negative test result
            reportInfectionButton.setText(R.string.report_infection_button_ci);
            suspicionButton.setText(R.string.suspicion_button_ci);
            //Report infection button listener
            reportInfectionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Go to screen for further information what to do if there is a current infection
                    Intent nextActivity = new Intent(MainActivity.this, InfectionActivity.class);
                    startActivity(nextActivity);
                }
            });

            //suspicion button listener
            suspicionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //opens pop up dialog to ask the user if he really wants to report a negative test result to update the infection status
                    reportApproval();
                }
            });
        }
        else{  //normal app usage
            //normal button texts (report infection and suspicion button)
            reportInfectionButton.setText(R.string.report_infection_button);
            suspicionButton.setText(R.string.suspicion_button);
            //Report infection button listener
            reportInfectionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //opens pop up dialog to ask the user if he really wants to report a infection
                    reportApproval();
                }
            });

            //suspicion button listener
            suspicionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Go to screen to inform what to do with infection suspicion
                    Intent nextActivity = new Intent(MainActivity.this, SuspicionActivity.class);
                    startActivity(nextActivity);
                }
            });
        }
    }

    /**
     * Check if Bluetooth is enabled and BLE is supported
     */
    private void checkIfBluetoothIsEnabled() {
        try {
            if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.check_bluetooth_enabled_title));
                builder.setMessage(getString(R.string.check_bluetooth_enabled_message))
                    .setCancelable(false)
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                            }
                        })
                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
            }
        } catch (RuntimeException e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.ble_available_title));
            builder.setMessage(getString(R.string.ble_available_message));
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
     * Check if GPS is enabled
     */
    public void checkIfGPSIsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if ( !manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.check_gps_enabled_title));
            builder.setMessage(getString(R.string.check_gps_enabled_message))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }

    }

    public static Context getContext() {
        return context;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainActivity = null;
    }
}