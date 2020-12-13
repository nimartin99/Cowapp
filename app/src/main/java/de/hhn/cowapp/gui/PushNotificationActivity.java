package de.hhn.cowapp.gui;

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
 * Push notification to inform about potential infection or risk of health
 *
 * @author Tabea leibl
 * @version 2020-12-09
 */
public class PushNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //logo of the app in the action bar
        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_cowapp);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#142850")));

        setContentView(R.layout.activity_push_notification);

        //okay button listener
        Button okButton = (Button) findViewById(R.id.okButtonNotification);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go back to main screen
                Intent nextActivity = new Intent(PushNotificationActivity.this, MainActivity.class);
                startActivity(nextActivity);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}