package de.monokel.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Report infection screen for CoWApp
 *
 * @author Tabea leibl
 * @author Philipp Alessandrini
 * @version 2020-11-10
 */
public class ReportInfectionActivity extends AppCompatActivity {
    // tag class name for logging
    private static final String TAG = "ReportInfectionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_infection);

        //Yes button listener
        Button yesButton = (Button)findViewById(R.id.reportYesButton);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send infected key to the server
                MainActivity.reportInfection("DIRECT");
                //Go to screen to thank the user and inform about what to do now
                Intent nextActivity = new Intent(ReportInfectionActivity.this,ThankYouActivity.class);
                startActivity(nextActivity);
            }
        });

        //No button listener
        Button noButton = (Button)findViewById(R.id.reportNoButton);

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go back to main screen
                Intent nextActivity = new Intent(ReportInfectionActivity.this,MainActivity.class);
                startActivity(nextActivity);
            }
        });
    }
}