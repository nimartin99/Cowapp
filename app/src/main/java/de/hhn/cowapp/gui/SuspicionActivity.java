package de.hhn.cowapp.gui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

import de.hhn.cowapp.R;

/**
 * Activity with user information what to do when having the suspicion to be infected
 *
 * @author Tabea leibl
 * @version 2020-11-17
 */
public class SuspicionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //logo of the app in the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_cowapp);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#142850")));

        setContentView(R.layout.activity_suspicion);

        TextView suspicionInfoTextView = (TextView) findViewById(R.id.textSuspicionInfo);
        Linkify.addLinks(suspicionInfoTextView, Linkify.ALL);
    }
}