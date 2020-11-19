package de.hhn.frontend;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hhn.frontend.provider.LocalSafer;

/**
 * Log activity for CoWApp
 *
 * @author Tabea leibl
 * @version 2020-11-17
 */
public class LogActivity extends AppCompatActivity {

    private LinearLayout notificationLayout;
    private static LogActivity logActivity;
    private ArrayList<TextView> notificationViews;
    private  TextView template;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //logo of the app in the action bar
        logActivity = this;
        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_cowapp);
        setContentView(R.layout.activity_log);

        notificationLayout = findViewById(R.id.notificationlayout);
        initScrollbar();
    }


    private void initScrollbar() {
        TextView textView = findViewById(R.id.templatenotification);
        if (textView != null) {
            template = textView;
        }

        try {
            notificationLayout.removeView(textView);
        } catch (Exception e) {
            return;
        }

        String [] notifications = LocalSafer.getNotifications();

        if (notificationViews != null && !notificationViews.isEmpty()) {
            for (TextView textView1 : notificationViews) {
                notificationLayout.removeView(textView1);
            }
        }

        notificationViews = new ArrayList<TextView>();

        if (notifications != null) {
            for (String string : notifications) {
                TextView notification = new TextView(this);
                notification.setText(string);
                notification.setLayoutParams(template.getLayoutParams());
                notificationViews.add(notification);
                notificationLayout.addView(notification, 0);
            }
        }

    }

    public static void renewTheLog() {
        try {
            logActivity.initScrollbar();
        } catch (Exception e) {
            return;
        }
    }
}