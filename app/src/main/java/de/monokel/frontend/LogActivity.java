package de.monokel.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Log activity for CoWApp
 *
 * @author Tabea leibl
 * @version 2020-10-18
 */
public class LogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        //Back button listener
        ImageButton backButton = (ImageButton)findViewById(R.id.backButtonLog);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to main screen
                Intent nextActivity = new Intent(LogActivity.this,MainActivity.class);
                startActivity(nextActivity);
            }
        });
    }
}