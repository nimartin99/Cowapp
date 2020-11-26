package de.hhn.frontend;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import de.hhn.frontend.provider.LocalSafer;

/**
 * Settings menu for CoWApp
 *
 * @author Tabea leibl
 * @version 2020-11-17
 */
public class SettingsActivity extends AppCompatActivity {

    private CheckBox keyTransmit;
    private CheckBox keySafe;
    private CheckBox alarmRing;
    private CheckBox alarmSet;
    private Button clear;
    private Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //logo of the app in the action bar
        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_cowapp);
        setContentView(R.layout.activity_settings);

        keyTransmit =  findViewById(R.id.keyTransmit);
        keySafe =  findViewById(R.id.keySafe);
        alarmRing = findViewById(R.id.alarmRing);
        alarmSet = findViewById(R.id.alarmSet);
        clear = findViewById(R.id.cleareDebugLog);
        update = findViewById(R.id.updateDebugSettings);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalSafer.clearDebugLog();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTheUpdate();
            }
        });
    }

    public void doTheUpdate() {
            LocalSafer.setIsAlarmRingLogged(alarmRing.isChecked());
            LocalSafer.setIsAlarmSetLogged(alarmSet.isChecked());
            LocalSafer.setIsKeySafeLogged(keySafe.isChecked());
            LocalSafer.setIsKeyTransmitLogged(keyTransmit.isChecked());
    }
}