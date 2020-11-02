package de.monokel.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import de.monokel.frontend.provider.LocalRiskLevelSafer;
import de.monokel.frontend.provider.NotificationService;

/**
 * Developer menu to test functions for the CoWApp development
 *
 * @author Tabea leibl
 * @author Philipp Alessandrini
 * @version 2020-10-28
 */
public class TestMenuActivity extends MainActivity {

    private static final int riskLevelTestValue = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_menu);

        //Back button listener
        ImageButton backButton = (ImageButton)findViewById(R.id.backButtonTest);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to main screen
                Intent nextActivity = new Intent(TestMenuActivity.this,MainActivity.class);
                startActivity(nextActivity);
            }
        });

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
                requestKey();
            }
        });

        // Risk level test button listener
        Button riskLevelTestButton = (Button)findViewById(R.id.Test3);

        riskLevelTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change risk level and update shown status
                LocalRiskLevelSafer.safeRiskLevel(riskLevelTestValue);
                MainActivity.showRiskStatus();
                MainActivity.showTrafficLightStatus();
            }
        });
    }

}