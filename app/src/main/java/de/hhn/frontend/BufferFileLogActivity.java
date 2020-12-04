package de.hhn.frontend;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

import de.hhn.frontend.provider.LocalSafer;

public class BufferFileLogActivity extends AppCompatActivity {

    private LinearLayout bufferLayout;
    private static BufferFileLogActivity bufferFileLogActivity;
    private ArrayList<TextView> valueViews;
    private TextView template;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bufferFileLogActivity = this;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_cowapp);
        setContentView(R.layout.activity_buffer_file_log);

        bufferLayout = findViewById(R.id.bufferLayOut);
        initScrollbar();
    }

    /**
     * Creates the Scrollbar with the notifications.
     */
    private void initScrollbar() {
        template = findViewById(R.id.bufferText);
        bufferLayout.removeView(template);

        String[] values = LocalSafer.getValuesAsArray("cowappkeybuffer.txt", null);

        valueViews = new ArrayList<TextView>();

        if (values != null) {
            HashSet<String> valueList = new HashSet<>();

            for (String string : values) {
                valueList.add(string);
            }

            for (String string : valueList) {
                int amountsOfThis = 0;

                for (String string2 : values) {
                    if (string2.equals(string)) {
                        amountsOfThis++;
                    }
                }

                TextView keyvalue = new TextView(this);
                keyvalue.setText(string + " is saved " + amountsOfThis + " times.");
                keyvalue.setLayoutParams(template.getLayoutParams());
                valueViews.add(keyvalue);
                bufferLayout.addView(keyvalue, 0);
            }
        }
    }

    /**
     * Updates the Scrollbar with the notifications.
     */
    private void updateScrollbar() {
        String[] values = LocalSafer.getValuesAsArray("cowappkeybuffer.txt", null);

        if (valueViews != null) {
            for (TextView textView1 : valueViews) {
                bufferLayout.removeView(textView1);
            }
        }

        valueViews = new ArrayList<TextView>();

        if (values != null) {
            HashSet<String> valueList = new HashSet<>();

            for (String string : values) {
                valueList.add(string);
            }

            for (String string : valueList) {
                int amountsOfThis = 0;

                for (String string2 : values) {
                    if (string2.equals(string)) {
                        amountsOfThis++;
                    }
                }

                TextView keyvalue = new TextView(this);
                keyvalue.setText(string + " is saved " + amountsOfThis + " times.");
                keyvalue.setLayoutParams(template.getLayoutParams());
                valueViews.add(keyvalue);
                bufferLayout.addView(keyvalue, 0);
            }
        }
    }

    /**
     * Calles the updateScrollBar, if there is already a Scrollbar.
     */
    public static void renewTheLog() {
        if (bufferFileLogActivity != null) {
            bufferFileLogActivity.updateScrollbar();
        }
    }

    @Override
    protected void onDestroy() {
        bufferFileLogActivity = null;
        super.onDestroy();
    }
}
