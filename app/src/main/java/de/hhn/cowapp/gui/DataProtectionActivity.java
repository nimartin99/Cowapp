package de.hhn.cowapp.gui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.hhn.cowapp.R;
import de.hhn.cowapp.datastorage.LocalSafer;

/**
 * Data protection start screen activity for CoWApp
 *
 * @author Tabea leibl
 * @version 2020-12-09
 */
public class DataProtectionActivity extends AppCompatActivity {

    protected static final String TAG = "DataProtectionActivity";

    public static MainActivity main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //logo of the app in the action bar
        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_cowapp);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#142850")));

        setContentView(R.layout.activity_data_protection);

        //Accept-Button Listener
        Button acceptButton = (Button)findViewById(R.id.AcceptButton);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //terms of use have been accepted - set boolean of first app start to false
                LocalSafer.setIsFirstAppStart(false, null);
                MainActivity.getMainActivity().firstInit();
                //Go to main screen
                Intent nextActivity = new Intent(DataProtectionActivity.this, PermissionActivity.class);
                startActivity(nextActivity);
                finish();
            }
        });
        //Decline-Button Listener
        Button declineButton = (Button)findViewById(R.id.DeclineButton);

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Data protection has been declined - info that it has to be accepted to use the app
                deniedDataProtectionNotification();
            }
        });
    }

    public void deniedDataProtectionNotification() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(getString(R.string.decline_info_title));
        builder.setMessage(getString(R.string.decline_info_message));
        //ok button
        builder.setPositiveButton(getString(R.string.decline_info_ok_button),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     //just shows the data protection screen again
                    }
                });
        //button to exit the app
        builder.setNegativeButton(getString(R.string.exit_app),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //app is finished, user is on the home screen again
                finishAffinity();
                main.finish();
                System.exit(0);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onBackPressed() {
    }
}