package de.monokel.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * Push notification to inform about potential infection or risk of health
 *
 * @author Tabea leibl
 * @version 2020-10-18
 */
public class PushNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_notification);
    }
}