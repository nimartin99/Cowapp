package de.hhn.cowapp.utils;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.hhn.cowapp.R;

/**
 * Debug menu for the app developers only - for testing
 *
 * @author Mergim Miftari
 * @version 2020-11-25
 */
public class DebugActivity extends AppCompatActivity {

    private Button testActions;
    private Button debugLogSettings;
    private Button debugLog;
    private Button ownKeys;
    private Button collectedKeys;
    private Button bufferFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //logo of the app in the action bar
        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_cowapp);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#142850")));

        setContentView(R.layout.activity_debug);

        testActions = this.findViewById(R.id.testActions);
        debugLogSettings = this.findViewById(R.id.debugLogSettings);
        debugLog = this.findViewById(R.id.debugLog);
        ownKeys = this.findViewById(R.id.listOfOwnKeys);
        collectedKeys = this.findViewById(R.id.collectedKeysButton);
        bufferFile = this.findViewById(R.id.listOfBufferFileValues);

        testActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(DebugActivity.this, TestMenuActivity.class);
                startActivity(nextActivity);
            }
        });

        debugLogSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(DebugActivity.this, SettingsActivity.class);
                startActivity(nextActivity);
            }
        });

        debugLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(DebugActivity.this, DebugLog.class);
                startActivity(nextActivity);
            }
        });

        ownKeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(DebugActivity.this, OwnKeyActivity.class);
                startActivity(nextActivity);
            }
        });

        collectedKeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(DebugActivity.this, OtherKeysActivity.class);
                startActivity(nextActivity);
            }
        });

        bufferFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(DebugActivity.this, BufferFileLogActivity.class);
                startActivity(nextActivity);
            }
        });
    }
}