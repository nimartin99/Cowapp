package de.hhn.frontend;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

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
                askForPermission();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTheUpdate();
            }
        });

        keyTransmit.setChecked(LocalSafer.isKeyTransmitLogged(null));
        keySafe.setChecked(LocalSafer.isKeySafeLogged(null));
        alarmRing.setChecked(LocalSafer.isAlarmRingLogged(null));
        alarmSet.setChecked(LocalSafer.isAlarmSetLogged(null));
    }

    public void doTheUpdate() {
            LocalSafer.setIsAlarmRingLogged(alarmRing.isChecked(), null);
            LocalSafer.setIsAlarmSetLogged(alarmSet.isChecked(), null);
            LocalSafer.setIsKeySafeLogged(keySafe.isChecked(), null);
            LocalSafer.setIsKeyTransmitLogged(keyTransmit.isChecked(), null);
            Toast toast = Toast.makeText(getApplicationContext(),
                getString(R.string.settings_were_saved),
                Toast.LENGTH_SHORT);
            toast.show();
    }

    public void askForPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(getString(R.string.you_are_clearing));
        builder.setMessage(getString(R.string.are_you_sure_to_clear));
        builder.setPositiveButton(getString(R.string.confirm),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LocalSafer.clearDebugLog(null);
                        Toast toast = Toast.makeText(getApplicationContext(),
                                getString(R.string.debug_log_was_cleared),
                                Toast.LENGTH_SHORT);

                        toast.show();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        getString(R.string.action_canceled),
                        Toast.LENGTH_SHORT);

                toast.show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}