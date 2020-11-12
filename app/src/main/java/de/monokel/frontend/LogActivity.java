package de.monokel.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import de.monokel.frontend.provider.LocalSafer;

/**
 * Log activity for CoWApp
 *
 * @author Tabea leibl
 * @version 2020-10-18
 */
public class LogActivity extends AppCompatActivity {

    LinearLayout notificationLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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