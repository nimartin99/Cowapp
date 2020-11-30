package de.hhn.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.altbeacon.beacon.BeaconManager;

import de.hhn.frontend.keytransfer.BeaconBackgroundService;

/**
 * Data protection start screen activity for CoWApp
 *
 * @author Tabea leibl
 * @version 2020-10-18
 */
public class DataProtectionActivity extends AppCompatActivity {

    private static final String TAG = "DataProtectionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_protection);
        //Accept-Button Listener
        Button acceptButton = (Button)findViewById(R.id.AcceptButton);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to main screen
                Intent nextActivity = new Intent(DataProtectionActivity.this, PermissionActivity.class);
                startActivity(nextActivity);
            }
        });
        //Decline-Button Listener
        Button declineButton = (Button)findViewById(R.id.DeclineButton);

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //App not usable
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}