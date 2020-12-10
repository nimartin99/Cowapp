package de.hhn.frontend;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hhn.frontend.provider.LocalSafer;

/**
 * @author Mergim Miftari
 */
public class OtherKeysActivity extends AppCompatActivity {

    private LinearLayout otherKeys;
    private static OtherKeysActivity otherKeyActivity;
    private ArrayList<TextView> valueViews;
    private  TextView template;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        otherKeyActivity = this;

        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_cowapp);
        setContentView(R.layout.activity_other_keys);

        otherKeys = findViewById(R.id.collectedKeysList);
        initScrollbar();
    }

    /**
     * Creates the Scrollbar with the notifications.
     */
    private void initScrollbar() {
        template = findViewById(R.id.collectedKeysText);
        otherKeys.removeView(template);

        String [] values = LocalSafer.getKeyPairs(null);

        valueViews = new ArrayList<TextView>();

        if (values != null) {
            for (String string : values) {
                TextView ownKey = new TextView(this);
                ownKey.setText(string);
                ownKey.setLayoutParams(template.getLayoutParams());
                valueViews.add(ownKey);
                otherKeys.addView(ownKey, 0);
            }
        }
    }

    /**
     * Updates the Scrollbar with the notifications.
     */
    private void updateScrollbar() {
        String [] values = LocalSafer.getKeyPairs(null);

        if (valueViews != null) {
            for (TextView textView1 : valueViews) {
                otherKeys.removeView(textView1);
            }
        }

        valueViews = new ArrayList<TextView>();

        if (values != null) {
            for (String string : values) {
                TextView ownKey = new TextView(this);
                ownKey.setText(string);
                ownKey.setLayoutParams(template.getLayoutParams());
                valueViews.add(ownKey);
                otherKeys.addView(ownKey, 0);
            }
        }
    }

    /**
     * Calles the updateScrollBar, if there is already a Scrollbar.
     */
    public static void renewTheLog() {
        if (otherKeyActivity != null) {
            otherKeyActivity.updateScrollbar();
        }
    }

    @Override
    protected void onDestroy() {
        otherKeyActivity = null;
        super.onDestroy();
    }
}