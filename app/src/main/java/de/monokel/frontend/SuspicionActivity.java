package de.monokel.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Activity with user information what to do when having the suspicion to be infected
 *
 * @author Tabea leibl
 * @version 2020-10-18
 */
public class SuspicionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suspicion);

        //Back button listener
        ImageButton backButton = (ImageButton)findViewById(R.id.backButtonSuspicion);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to main screen
                Intent nextActivity = new Intent(SuspicionActivity.this,MainActivity.class);
                startActivity(nextActivity);
            }
        });
    }
}