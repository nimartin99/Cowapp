package de.monokel.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Info activity for CoWApp
 *
 * @author Tabea leibl
 * @version 2020-10-18
 */
public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //Back button listener
        ImageButton backButton = (ImageButton)findViewById(R.id.backButtonInfo);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to main screen
                Intent nextActivity = new Intent(InfoActivity.this,MainActivity.class);
                startActivity(nextActivity);
            }
        });
    }
}