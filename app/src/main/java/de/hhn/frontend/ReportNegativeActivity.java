package de.hhn.frontend;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.hhn.frontend.provider.LocalSafer;
import de.hhn.frontend.risklevel.RiskLevel;

/**
 * Report infection screen for CoWApp
 *
 * @author Tabea leibl
 * @version 2020-12-01
 */
public class ReportNegativeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //logo of the app in the action bar
        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_cowapp);
        setContentView(R.layout.activity_report_negative);

        //Yes button listener
        Button yesButton = (Button)findViewById(R.id.reportNegYesButton);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //report yourself negative
                //TODO Ticket 73
                //reset risk level
                LocalSafer.safeRiskLevel(0, null);
                //Go back to main screen
                Intent nextActivity = new Intent(ReportNegativeActivity.this,MainActivity.class);
                startActivity(nextActivity);
            }
        });

        //No button listener
        Button noButton = (Button)findViewById(R.id.reportNegNoButton);

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go back to main screen
                Intent nextActivity = new Intent(ReportNegativeActivity.this,MainActivity.class);
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