package de.hhn.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.hhn.frontend.date.DateHelper;
import de.hhn.frontend.keytransfer.BeaconBackgroundService;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import de.hhn.frontend.provider.Alarm;
import de.hhn.frontend.provider.LocalSafer;
import de.hhn.frontend.provider.NotificationService;
import de.hhn.frontend.risklevel.DirectContact;
import de.hhn.frontend.risklevel.IndirectContact;
import de.hhn.frontend.risklevel.RiskLevel;

/**
 * Developer menu to test functions for the CoWApp development
 *
 * @author Tabea leibl
 * @author Philipp Alessandrini
 * @version 2020-11-12
 */
public class TestMenuActivity extends AppCompatActivity {

    //TAG for Logging example: Log.d(TAG, "fine location permission granted"); -> d for debug
    protected static final String TAG = "TestMenuActivity";
    private static final int riskLevelTestValue = 100;
    private static boolean clickedTransmit = true;
    private static boolean clickedScan = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_menu);

        //Push notification test button listener
        Button pushTestButton = (Button) findViewById(R.id.pushNotificationTestButton);

        pushTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //trigger push notification to test its functionality
                Intent pushNotificationActivity = new Intent(TestMenuActivity.this, NotificationService.class);
                pushNotificationActivity.putExtra("TITLE", "Mögliches Gesundheitsrisiko");
                pushNotificationActivity.putExtra("TEXT", "Hier klicken für weitere Informationen.");
                pushNotificationActivity.putExtra("CLASS", PushNotificationActivity.class);
                startService(pushNotificationActivity);
            }
        });

        // Generate key test button listener
        Button generateKeyTestButton = (Button) findViewById(R.id.keyGenerationTestButton);

        generateKeyTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // try to generate a key if the smartphone has a connection to the server
                MainActivity.requestKey();
            }
        });

        // Risk level test button listener
        Button riskLevelTestButton = (Button)findViewById(R.id.Test3);

        riskLevelTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change risk level and update shown status
                LocalSafer.safeRiskLevel(riskLevelTestValue, null);
                MainActivity.showRiskStatus();
                MainActivity.showTrafficLightStatus();
            }
        });

        // Reset button (for risk level test button) listener
        Button resetButton = (Button)findViewById(R.id.resetButton);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset risk level to 0
                LocalSafer.safeRiskLevel(0, null);
                MainActivity.showRiskStatus();
                MainActivity.showTrafficLightStatus();
            }
        });

        Button transmitOnOff = findViewById(R.id.transmit);

        transmitOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickedTransmit) {
                    BeaconBackgroundService.transmitAsBeacon();
                    clickedTransmit = false;
                } else {
                    BeaconBackgroundService.stopTransmittingAsBeacon();
                    clickedTransmit = true;
                }
            }
        });

        Button scanningOnOff = findViewById(R.id.scan);

        scanningOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeaconBackgroundService application = ((BeaconBackgroundService) BeaconBackgroundService.getAppContext());
                if(clickedScan) {
                    application.changeMonitoringState(true);
                    clickedScan = false;
                } else {
                    application.changeMonitoringState(false);
                    clickedScan = true;
                }
            }
        });

        // Request infection status test button listener
        Button requestInfectionStatusButton = (Button) findViewById(R.id.checkInfectionStatusButton);

        requestInfectionStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // try to check the infection status if the smartphone has a connection to the server
                MainActivity.requestInfectionStatus();
            }
        });

        // Log saved own keys listener
        Button logOwnKeysListener = (Button) findViewById(R.id.logOwnKeysButton);

        logOwnKeysListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // log all own keys
                Log.d(TAG, Arrays.toString(LocalSafer.getOwnKeys(null)));
            }
        });

        // delete own keys listener
        Button deleteOwnKeysListener = (Button) findViewById(R.id.deleteOwnKeysTextButton);

        deleteOwnKeysListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete all own keys
                Log.d(TAG, "own keys deleted");
                LocalSafer.clearOwnKeyPairDataFile(null);
            }
        });

        //Button to add List of contacts, used by the NewRiskLevel
        Button addIndirectContactButton = (Button) findViewById(R.id.addIndirectTestContact);
        addIndirectContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date notOutDatedDate1 = new GregorianCalendar(2020, Calendar.NOVEMBER, 22).getTime();
                RiskLevel.addContact(new IndirectContact(notOutDatedDate1));

            }
        });

        Button addDirectContactButton = (Button) findViewById(R.id.addDirectTestContact);
        addDirectContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date notOutDatedDate1 = new GregorianCalendar(2020, Calendar.NOVEMBER, 22).getTime();
                RiskLevel.addContact(new DirectContact(notOutDatedDate1));


            }
        });

        Button removeAllContactsButton = (Button) findViewById(R.id.deleteTestContacts);
        removeAllContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RiskLevel.deleteAllContacts();
            }
        });


        //Button to test the NewRiskLevel Class
        Button calculateRiskLevelOfCurrentContactListButton = (Button) findViewById(R.id.calculateRiskLevelByContacts);

        calculateRiskLevelOfCurrentContactListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RiskLevel.calculateRiskLevel();

            }
        });

        Button resetRiskLevelButton = (Button) findViewById(R.id.checkOutdatedInfectionbutton);
        resetRiskLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RiskLevel.checkIfInfectionHasExpired();
            }
        });

        Button setInfectionOutdatedButton = (Button) findViewById(R.id.setInfectionOutdated);
        setInfectionOutdatedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date outDatedInfectionDate = new GregorianCalendar(2019, Calendar.JULY, 2).getTime();
                LocalSafer.safeDateOfLastReportedInfection(DateHelper.convertDateToString(outDatedInfectionDate), null);
                Log.d("Jonas", "Date of Infection was set to outdated");
            }
        });

        //Button to test 15m Buisness
        Button activate15mBuisnessButton = (Button) findViewById(R.id.activate15MinuteBuisness);
        activate15mBuisnessButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alarm.fifteenMinutesBusiness();
                Log.d("Jonas", "FifteenMinutesBusiness was started by Button, not 15m routine");
            }
        }));

        //Button and EditText to update the foregroundNotification
        final EditText updateForegroundEditText = findViewById(R.id.updateForegroundEditText);
        Button updateForegroundButton = findViewById(R.id.updateForegroundButton);
        updateForegroundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeaconBackgroundService application = ((BeaconBackgroundService) BeaconBackgroundService.getAppContext());
                application.updateForegroundNotification(updateForegroundEditText.getText().toString());
            }
        });

        Button stopForegroundNotificationButton = findViewById(R.id.stopForegroundNotificationButton);
        stopForegroundNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeaconBackgroundService application = ((BeaconBackgroundService) BeaconBackgroundService.getAppContext());
                application.stopForegroundNotification();
            }
        });

        Button onlyBuildForeground = findViewById(R.id.onlyBuildForeground);
        onlyBuildForeground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeaconBackgroundService application = ((BeaconBackgroundService) BeaconBackgroundService.getAppContext());
                application.buildForegroundNotification("onlyBuildForeground");
            }
        });
    }
}

