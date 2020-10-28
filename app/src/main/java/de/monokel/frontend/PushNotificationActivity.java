package de.monokel.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Push notification to inform about potential infection or risk of health
 *
 * @author Tabea leibl
 * @version 2020-10-20
 */
public class PushNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_notification);

        //okay button listener
        Button okButton = (Button) findViewById(R.id.okButtonNotification);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go back to main screen
                Intent nextActivity = new Intent(PushNotificationActivity.this,MainActivity.class);
                startActivity(nextActivity);
            }
        });
    }
}