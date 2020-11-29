package de.hhn.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.Button;

public class DebugActivity extends AppCompatActivity {

    private Button testActions;
    private Button debugLogSettings;
    private Button debugLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        testActions = this.findViewById(R.id.testActions);
        debugLogSettings = this.findViewById(R.id.debugLogSettings);
        debugLog = this.findViewById(R.id.debugLog);

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
    }
}