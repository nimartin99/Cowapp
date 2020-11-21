package de.hhn.frontend;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Activity to thank the user for informing about his or her infection
 *
 * @author Tabea leibl
 * @version 2020-11-17
 */
public class ThankYouActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //logo of the app in the action bar
        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_cowapp);
        setContentView(R.layout.activity_thank_you);

        //suspicion button listener
        Button okButton = (Button) findViewById(R.id.okButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go back to main screen
                Intent nextActivity = new Intent(ThankYouActivity.this,MainActivity.class);
                startActivity(nextActivity);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}