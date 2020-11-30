package de.hhn.frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
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

import java.util.Calendar;
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
import de.hhn.frontend.risklevel.NewRiskLevel;
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
 * @version 2020-11-30
 */
public class MainActivity extends AppCompatActivity {
    //TAG for Logging example: Log.d(TAG, "fine location permission granted"); -> d for debug
    protected static final String TAG = "MainActivity";

    private static MainActivity mainActivity;

    // application context that allows stating android services from static methods
    private static Context context;

    //For push notification
    public static final String CHANNEL_ID = "pushNotifications";
    private NotificationManager notificationManager;

    private Retrofit retrofit;
    private static RetrofitService retrofitService;
    private String BASE_URL = "http://10.0.2.2:3000"; // for emulated phone
    private String PHONE_URL = "http://" + Personal_Constants.OWN_IP + ":3000";

    //For the once-a-day-alarm-clock for deleting keys that are older than 3 weeks
    private PendingIntent myPendingIntent;
    private AlarmManager alarmManager;
    private BroadcastReceiver myBroadcastReceiver;
    private Calendar firingCal;

    //To display the current risk status
    private static ImageView trafficLight;
    private static TextView riskStatus;
    //To display the first use date and the elapsed time since the app is used.
    public static TextView dateDisplay;

    //TODO
    //data protection has still to be accepted
    public String prefDataProtection = "required";

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
        showTrafficLightStatus();
        showRiskStatus();

        //show current Info about days since usage.
        showDateDisplay();

        //If the app is opened for the first time the user has to accept the data protection regulations
        //TODO
        //if (firstAppStart()) {
        if (LocalSafer.isFirstAppStart(null)){
            Intent nextActivity = new Intent(MainActivity.this, DataProtectionActivity.class);
            startActivity(nextActivity);
            LocalSafer.safeFirstStartDate(DateHelper.getCurrentDateString(), null);
            requestKey();
        } else {
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
    }

    public void firstinit() {
        LocalSafer.safeFirstStartDate(DateHelper.getCurrentDateString(), this);
        requestKey();
        setAlarm();
    }

    private void setAlarm() {
        boolean alarmUp = (PendingIntent.getBroadcast(this, 0, new Intent("com.alarm.example"), PendingIntent.FLAG_NO_CREATE) != null);

        if (alarmUp == false) {
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
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO
        //if (firstAppStart()) {
        if(LocalSafer.isFirstAppStart(null)){
            Intent nextActivity = new Intent(MainActivity.this, DataProtectionActivity.class);
            startActivity(nextActivity);
            LocalSafer.safeFirstStartDate(DateHelper.getCurrentDateString(), null);
            requestKey();
        }
        else{
            //show current risk level (updated once a day)
            showTrafficLightStatus();
            showRiskStatus();
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
            menu.removeItem(it.getItemId()); //its the ID of the Test-menu for some reason
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
            case R.id.item3:
                //Go to info screen
                Intent nextActivity = new Intent(MainActivity.this, InfoMenuActivity.class);
                startActivity(nextActivity);
                return true;
            case R.id.item4:
                //Go to test menu screen
                Intent testActivity = new Intent(MainActivity.this, DebugActivity.class);
                startActivity(testActivity);
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
     * At first start of the app the user has to accept the data protection regulations before he can
     * use the app
     */
    //TODO
    public boolean firstAppStart() {
        SharedPreferences preferences = getSharedPreferences(prefDataProtection, MODE_PRIVATE);

        if (preferences.getBoolean(prefDataProtection, true)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(prefDataProtection, false);
            //TODO war davor commit
            editor.apply();
            return true;
        } else {
            return false;
        }
        //LokalSafer.isFirstAppStart();
    }

    /**
     * Request a new key from the server.
     */
    public static boolean requestKey() {
        if (LocalSafer.getRiskLevel(null) != 100) {
            Call<String> call = retrofitService.requestKey();
            RetryCallUtil.enqueueWithRetry(call, new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() == 200) {
                        // log newest key
                        Log.d(TAG, "Key: " + response.body());
                        // key is successfully requested
                        Key.setKeyRequested(true);
                        // reference requested key
                        String requestedKey = response.body();
                        // set the key
                        Key.setKey(requestedKey);
                        // safe new key
                        LocalSafer.safeOwnKey(Key.getKey(), null);
                        //Update the Transmission
                        BeaconBackgroundService.updateTransmissionBeaconKey(requestedKey);
                    } else if (response.code() == 404) {
                        Log.w(TAG, "requestKey: KEY_DOES_NOT_EXIST");
                        // key is not successfully requested
                        Key.setKeyRequested(false);
                        BeaconBackgroundService.stopTransmittingAsBeacon();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.w(TAG, Objects.requireNonNull(t.getMessage()));
                    serverResponseNotification("NO_CONNECTION_NOTIFICATION");
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
     * Report an infection by sending the current key to the server.
     */
    public static void reportInfection(final String contactType) {
        Log.d(TAG, "Sending all contact keys to the server, therefore the response may take some time...");
        Runnable runnable = new Runnable() {
            public void run() {
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
                                    // send own contacts as indirect contacts to the server
                                    reportInfection("INDIRECT");
                                    // inform user via push-up notification about the direct contact
                                    serverResponseNotification("DIRECT_CONTACT_NOTIFICATION");
                                    //calculate and safe Risklevel, update of days since last contact corresponding to the server response
                                    NewRiskLevel.addContact(new DirectContact(DateHelper.getCurrentDate()));
                                } else if (infection.getStatus().equals("INDIRECT_CONTACT")) {
                                    Log.d(TAG, "User has had indirect contact with an infected person");
                                    // inform user via push-up notification about the indirect contact
                                    serverResponseNotification("INDIRECT_CONTACT_NOTIFICATION");
                                    //calculate and safe Risklevel, update of days since last contact corresponding to the server response
                                    NewRiskLevel.addContact(new IndirectContact(DateHelper.getCurrentDate()));
                                } else {
                                    Log.w(TAG, "onResponse: NO DEFINED INFECTION_STATUS");
                                }
                            } else if (response.code() == 400) {
                                //calculate and safe Risklevel, update of days since last contact corresponding to the server response
                                // user has had no contact
                                Log.d(TAG, "User has had no contact with an infected person");
                            } else if (response.code() == 404) {
                                Log.w(TAG, "onResponse: UNDEFINED_REQUEST_BODY_VALUE");
                            }
                        }

                        @Override
                        public void onFailure(Call<RequestedObject> call, Throwable t) {
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

    // standard notification if there is no connection to the server
    private static void serverResponseNotification(String notificationType) {
        Intent responsePushNotification = new Intent(context, NotificationService.class);
        switch (notificationType) {
            case "DIRECT_CONTACT_NOTIFICATION":
                responsePushNotification.putExtra("TITLE", "Direkten Kontakt zu einer infizierten Person festgestellt");
                responsePushNotification.putExtra("TEXT", "Hier klicken für weitere Informationen.");
                responsePushNotification.putExtra("CLASS", PushNotificationActivity.class);
                responsePushNotification.putExtra("LOG", true);
                break;
            case "INDIRECT_CONTACT_NOTIFICATION":
                responsePushNotification.putExtra("TITLE", "Indirekten Kontakt zu einer infizierten Person festgestellt");
                responsePushNotification.putExtra("TEXT", "Hier klicken für weitere Informationen.");
                responsePushNotification.putExtra("CLASS", PushNotificationActivity.class);
                responsePushNotification.putExtra("LOG", true);
                break;
            case "NO_CONNECTION_NOTIFICATION":
                responsePushNotification.putExtra("TITLE", "Es konnte keine Verbindung zum Server hergestellt werden");
                responsePushNotification.putExtra("TEXT", "Versuche Verbindungsaufbau in 5 Minuten erneut...");
                responsePushNotification.putExtra("LOG", false);
                break;
            default:
                Log.w(TAG, "NO DEFINED NOTIFICATION_TYPE");
        }
        context.startService(responsePushNotification);
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
        dateDisplay.setText(DateHelper.generateStringForDateDisplay());
    }

    /**
     * method called daily to show the right traffic light status (for current health risk)
     */
    public static void showTrafficLightStatus() {
        int riskValue = LocalSafer.getRiskLevel(null);
        if (riskValue <= 33) {
            trafficLight.setImageResource(R.drawable.green_traffic_light);
        } else if (riskValue <= 70) {
            trafficLight.setImageResource(R.drawable.yellow_traffic_light);
        } else {
            trafficLight.setImageResource(R.drawable.red_traffic_light);
        }
    }

    /**
     * method called daily to show the right health risk status
     */
    public static void showRiskStatus() {
        int riskValue = LocalSafer.getRiskLevel(null);
        if (riskValue <= 33) {
                String risk = riskStatus.getResources().getString(R.string.risk_status_low)
                        + "\n \n" + riskStatus.getResources().getString(R.string.risk_level, riskValue);
                riskStatus.setText(risk);
        } else if (riskValue <= 70) {
                String risk = riskStatus.getResources().getString(R.string.risk_status_moderate)
                        + "\n \n" + riskStatus.getResources().getString(R.string.risk_level, riskValue);
                riskStatus.setText(risk);
        } else if (riskValue > 70 && riskValue < 100) {
                String risk = riskStatus.getResources().getString(R.string.risk_status_high)
                        + "\n \n" + riskStatus.getResources().getString(R.string.risk_level, riskValue);
                riskStatus.setText(risk);
        } else if (riskValue == 100) {
            String risk = "\n \n" + riskStatus.getResources().getString(R.string.current_infection);
                riskStatus.setText(risk);
        }
    }

    public static Context getContext() {
        return context;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }
}