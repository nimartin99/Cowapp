package de.hhn.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import de.hhn.frontend.keytransfer.BeaconBackgroundService;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

import de.hhn.frontend.provider.LocalSafer;
import de.hhn.frontend.provider.NotificationService;

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
    private static final int riskLevelTestValue = 50;
    private static boolean clickedTransmit = true;
    private static boolean clickedScan = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_menu);

        //Push notification test button listener
        Button pushTestButton = (Button)findViewById(R.id.Test1);

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
        Button generateKeyTestButton = (Button)findViewById(R.id.Test2);

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
                LocalSafer.safeRiskLevel(riskLevelTestValue);
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
                LocalSafer.safeRiskLevel(0);
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
                    application.enableMonitoring();
                    clickedScan = false;
                } else {
                    application.disableMonitoring();
                    clickedScan = true;
                }
            }
        });

        // Request infection status test button listener
        Button requestInfectionStatusButton = (Button)findViewById(R.id.Test4);

        requestInfectionStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // try to check the infection status if the smartphone has a connection to the server
                MainActivity.requestInfectionStatus();
            }
        });

        // Log saved own keys listener
        Button logOwnKeysListener = (Button)findViewById(R.id.Test5);

        logOwnKeysListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // log all own keys
                Log.d(TAG, Arrays.toString(LocalSafer.getOwnKeys()));
            }
        });

        // delete own keys listener
        Button deleteOwnKeysListener = (Button)findViewById(R.id.Test6);

        deleteOwnKeysListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete all own keys
                Log.d(TAG, "own keys deleted");
                LocalSafer.clearOwnKeyPairDataFile();
            }
        });
    }
}