package de.hhn.frontend;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hhn.frontend.provider.LocalSafer;

/**
 * Log activity for CoWApp
 *
 * @author Tabea leibl
 * @version 2020-11-17
 */
public class LogActivity extends AppCompatActivity {

    LinearLayout notificationLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //logo of the app in the action bar
        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_cowapp);
        setContentView(R.layout.activity_log);

        notificationLayout = findViewById(R.id.notificationlayout);
        initScrollbar();
    }


    private void initScrollbar() {
        TextView textView = findViewById(R.id.templatenotification);
        notificationLayout.removeView(textView);
        String[] notifications = LocalSafer.getNotifications();

        if (notifications != null) {
            for (String string : notifications) {
                TextView notification = new TextView(this);
                notification.setText(string);
                notification.setLayoutParams(textView.getLayoutParams());
                notificationLayout.addView(notification, 0);
            }
        }

    }
}