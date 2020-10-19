package de.monokel.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Main screen for CoWApp
 *
 * @author Tabea leibl
 * @version 2020-10-18
 */
public class MainActivity extends AppCompatActivity {

    String prefDataProtection = "ausstehend";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
        if(firstAppStart()){
            Intent nextActivity = new Intent(MainActivity.this,DataProtectionActivity.class);
            startActivity(nextActivity);
        } else {

            //Info button listener
            Button infoButton = (Button) findViewById(R.id.InfoButton);

            infoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Go to info screen
                    Intent nextActivity = new Intent(MainActivity.this, InfoActivity.class);
                    startActivity(nextActivity);
                }
            });

            //Settings button listener
            ImageButton settingsButton = (ImageButton) findViewById(R.id.EinstellungenButton);

            settingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Go to settings screen
                    Intent nextActivity = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(nextActivity);
                }
            });

            //LOG button listener
            Button logButton = (Button) findViewById(R.id.LOGButton);

            logButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Go to LOG screen
                    Intent nextActivity = new Intent(MainActivity.this, LogActivity.class);
                    startActivity(nextActivity);
                }
            });

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
    }

    public boolean firstAppStart(){
        SharedPreferences preferences = getSharedPreferences(prefDataProtection, MODE_PRIVATE);
        if(preferences.getBoolean(prefDataProtection, true)){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(prefDataProtection,false);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }
}