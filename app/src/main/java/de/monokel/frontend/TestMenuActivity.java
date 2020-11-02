package de.monokel.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import de.monokel.frontend.keytransfer.BeaconBackgroundService;

/**
 * Developer menu to test functions for the CoWApp development
 *
 * @author Tabea leibl
 * @version 2020-10-18
 */
public class TestMenuActivity extends AppCompatActivity{

    protected static final String TAG = "TestMenuActivity";
    private Button rangingTestButton;
    BeaconBackgroundService beaconBackgroundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_menu);


        beaconBackgroundService = (BeaconBackgroundService) this.getApplicationContext();
        Context context = getApplicationContext();
        CharSequence text = "BeaconBackgroundService created";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();


        //Back button listener
        ImageButton backButton = (ImageButton)findViewById(R.id.backButtonTest);
        rangingTestButton = findViewById(R.id.rangingTestButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to main screen
                Intent nextActivity = new Intent(TestMenuActivity.this, MainActivity.class);
                startActivity(nextActivity);
            }
        });
    }
}