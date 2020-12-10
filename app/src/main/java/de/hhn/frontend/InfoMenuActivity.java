package de.hhn.frontend;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Info menu for CoWApp with buttons to choose
 *
 * @author Tabea leibl
 * @version 2020-11-22
 */
public class InfoMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //logo of the app in the action bar
        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_cowapp);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#142850")));
        setContentView(R.layout.activity_info_menu);

        //General information button listener
        Button generalInfoButton = (Button) findViewById(R.id.generalInfoButton);

        generalInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to screen for general information
                Intent nextActivity = new Intent(InfoMenuActivity.this, GeneralInfoActivity.class);
                startActivity(nextActivity);
            }
        });

        //Current infection info button listener
        Button infectionInfoButton = (Button) findViewById(R.id.infectionInfoButton);

        infectionInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to screen for information for current infection
                Intent nextActivity = new Intent(InfoMenuActivity.this, InfectionActivity.class);
                startActivity(nextActivity);
            }
        });

        //Risk level calculation info button listener
        Button riskLevelInfoButton = (Button) findViewById(R.id.riskLevelInfoButton);

        riskLevelInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to screen for information about the risk level calculation
                Intent nextActivity = new Intent(InfoMenuActivity.this, RiskLevelInfoActivity.class);
                startActivity(nextActivity);
            }
        });

        Button faqButton = (Button) findViewById(R.id.faqButton);

        faqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to screen for information about the risk level calculation
                Intent nextActivity = new Intent(InfoMenuActivity.this, FaqActivity.class);
                startActivity(nextActivity);
            }
        });
    }

}