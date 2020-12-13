package de.hhn.cowapp.utils;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hhn.cowapp.R;
import de.hhn.cowapp.datastorage.LocalSafer;

/**
 * @author Mergim Miftari
 */
public class OwnKeyActivity extends AppCompatActivity {

    private LinearLayout ownKeys;
    private static OwnKeyActivity ownKeyActivity;
    private ArrayList<TextView> valueViews;
    private  TextView template;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ownKeyActivity = this;

        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_cowapp);
        setContentView(R.layout.activity_own_key);

        ownKeys = findViewById(R.id.ownKeyLayout);
        initScrollbar();
    }

    /**
     * Creates the Scrollbar with the notifications.
     */
    private void initScrollbar() {
        template = findViewById(R.id.ownKeyText);
        ownKeys.removeView(template);

        String [] values = LocalSafer.getOwnKeys(null);

        valueViews = new ArrayList<TextView>();

        if (values != null) {
            for (String string : values) {
                TextView ownKey = new TextView(this);
                ownKey.setText(string);
                ownKey.setLayoutParams(template.getLayoutParams());
                valueViews.add(ownKey);
                ownKeys.addView(ownKey, 0);
            }
        }
    }

    /**
     * Updates the Scrollbar with the notifications.
     */
    private void updateScrollbar() {
        String [] values = LocalSafer.getOwnKeys(null);

        if (valueViews != null) {
            for (TextView textView1 : valueViews) {
                ownKeys.removeView(textView1);
            }
        }

        valueViews = new ArrayList<TextView>();

        if (values != null) {
            for (String string : values) {
                TextView ownKey = new TextView(this);
                ownKey.setText(string);
                ownKey.setLayoutParams(template.getLayoutParams());
                valueViews.add(ownKey);
                ownKeys.addView(ownKey, 0);
            }
        }
    }

    /**
     * Calles the updateScrollBar, if there is already a Scrollbar.
     */
    public static void renewTheLog() {
        if (ownKeyActivity != null) {
            ownKeyActivity.updateScrollbar();
        }
    }

    @Override
    protected void onDestroy() {
        ownKeyActivity = null;
        super.onDestroy();
    }
}