package de.monokel.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import de.monokel.frontend.provider.NotificationService;

/**
 * Developer menu to test functions for the CoWApp development
 *
 * @author Tabea leibl
 * @version 2020-10-28
 */
public class TestMenuActivity extends AppCompatActivity {

    private MainActivity activity;

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
                startService(pushNotificationActivity);
            }
        });
    }

}