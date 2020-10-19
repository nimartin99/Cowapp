package de.monokel.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Developer menu to test functions for the CoWApp development
 *
 * @author Tabea leibl
 * @version 2020-10-18
 */
public class TestMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_menu);

        //Back button listener
        ImageButton backButton = (ImageButton)findViewById(R.id.backButtonTest);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to main screen
                Intent nextActivity = new Intent(TestMenuActivity.this,MainActivity.class);
                startActivity(nextActivity);
            }
        });
    }
}