package de.hhn.cowapp.gui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hhn.cowapp.R;
import de.hhn.cowapp.datastorage.LocalSafer;

/**
 * Log activity for CoWApp
 *
 * @author Tabea leibl
 * @author Mergim Miftari
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
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#142850")));
        setContentView(R.layout.activity_log);

        notificationLayout = findViewById(R.id.notificationlayout);
        initScrollbar();
    }

    /**
     * Creates the Scrollbar with the notifications.
     */
    private void initScrollbar() {
        template = findViewById(R.id.templatenotification);
        notificationLayout.removeView(template);

        String [] notifications = LocalSafer.getNotifications(null);

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

    /**
     * Updates the Scrollbar with the notifications.
     */
    private void updateScrollbar() {
        String [] notifications = LocalSafer.getNotifications(null);

        if (notificationViews != null) {
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

    /**
     * Calles the updateScrollBar, if there is already a Scrollbar.
     */
    public static void renewTheLog() {
        if (logActivity != null) {
            logActivity.updateScrollbar();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logActivity = null;
    }
}