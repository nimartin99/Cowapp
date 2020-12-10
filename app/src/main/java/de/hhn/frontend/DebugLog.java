package de.hhn.frontend;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hhn.frontend.provider.LocalSafer;

/**
 * @author Mergim Miftari
 */
public class DebugLog extends AppCompatActivity {


    private LinearLayout debugValueLayout;
    private static DebugLog logActivity;
    private ArrayList<TextView> valueViews;
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

        setContentView(R.layout.activity_debug_log);

        debugValueLayout = findViewById(R.id.debugValueLayout);
        initScrollbar();
    }

    /**
     * Creates the Scrollbar with the notifications.
     */
    private void initScrollbar() {
        template = findViewById(R.id.templatenotification02);
        debugValueLayout.removeView(template);

        String [] values = LocalSafer.getDebugValues(null);

        valueViews = new ArrayList<TextView>();

        if (values != null) {
            for (String string : values) {
                TextView notification = new TextView(this);
                notification.setText(string);
                notification.setLayoutParams(template.getLayoutParams());
                valueViews.add(notification);
                debugValueLayout.addView(notification, 0);
            }
        }
    }

    /**
     * Updates the Scrollbar with the notifications.
     */
    private void updateScrollbar() {
        String [] notifications = LocalSafer.getDebugValues(null);

        if (valueViews != null) {
            for (TextView textView1 : valueViews) {
                debugValueLayout.removeView(textView1);
            }
        }

        valueViews = new ArrayList<TextView>();

        if (notifications != null) {
            for (String string : notifications) {
                TextView notification = new TextView(this);
                notification.setText(string);
                notification.setLayoutParams(template.getLayoutParams());
                valueViews.add(notification);
                debugValueLayout.addView(notification, 0);
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
        logActivity = null;
        super.onDestroy();
    }
}