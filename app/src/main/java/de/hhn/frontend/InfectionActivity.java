package de.hhn.frontend;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

/**
 * Information for currently infected people using the CoWApp
 *
 * @author Tabea leibl
 * @version 2020-11-22
 */
public class InfectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //logo of the app in the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_cowapp);
        setContentView(R.layout.activity_infection);
        TextView generalInfoTextView = (TextView) findViewById(R.id.TextGeneralInfo);
        Linkify.addLinks(generalInfoTextView, Linkify.ALL);
    }
}