package de.monokel.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import de.monokel.frontend.exceptions.KeyNotRequestedException;
import de.monokel.frontend.provider.Key;
import de.monokel.frontend.provider.RequestedObject;
import de.monokel.frontend.provider.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Main screen for CoWApp
 *
 * @author Tabea leibl
 * @author Philipp Alessandrini
 * @version 2020-10-22
 */
public class MainActivity extends AppCompatActivity {
    // tag class name for logging
    private static final String TAG = "MainActivity";

    private Retrofit retrofit;
    private RetrofitService retrofitService;
    private String BASE_URL = "http://10.0.2.2:3000"; // for emulated phone

    String prefDataProtection = "ausstehend";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
        // init retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);

        //If the app is opened for the first time the user has to accept the data protection regulations
        if(firstAppStart()){
            Intent nextActivity = new Intent(MainActivity.this,DataProtectionActivity.class);
            startActivity(nextActivity);
        } else {

            //Info button listener
            Button infoButton = (Button) findViewById(R.id.InfoButton);

            infoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Go to info screen
                    Intent nextActivity = new Intent(MainActivity.this, InfoActivity.class);
                    startActivity(nextActivity);
                }
            });

            //Settings button listener
            ImageButton settingsButton = (ImageButton) findViewById(R.id.EinstellungenButton);

            settingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Go to settings screen
                    Intent nextActivity = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(nextActivity);
                }
            });

            //LOG button listener
            Button logButton = (Button) findViewById(R.id.LOGButton);

            logButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Go to LOG screen
                    Intent nextActivity = new Intent(MainActivity.this, LogActivity.class);
                    startActivity(nextActivity);
                }
            });

            //Test menu button listener
            Button testMenuButton = (Button) findViewById(R.id.TestMenuButton);

            testMenuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // request a key
                    requestKey();
                    //Go to test menu screen
                    Intent nextActivity = new Intent(MainActivity.this, TestMenuActivity.class);
                    startActivity(nextActivity);
                }
            });

            //Report infection button listener
            Button reportInfectionButton = (Button) findViewById(R.id.InfektionMeldenButton);

            reportInfectionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Go to screen to report infection
                    Intent nextActivity = new Intent(MainActivity.this, ReportInfectionActivity.class);
                    startActivity(nextActivity);
                }
            });

            //suspicion button listener
            Button suspicionButton = (Button) findViewById(R.id.VerdachtButton);

            suspicionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Go to screen to inform what to do with infection suspicion
                    Intent nextActivity = new Intent(MainActivity.this, SuspicionActivity.class);
                    startActivity(nextActivity);
                }
            });
        }
    }

    /**
     * At first start of the app the user has to accept the data protection regulations before he can
     * use the app
     */
    public boolean firstAppStart(){
        SharedPreferences preferences = getSharedPreferences(prefDataProtection, MODE_PRIVATE);
        if(preferences.getBoolean(prefDataProtection, true)){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(prefDataProtection,false);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Request a new key from the server.
     */
    public void requestKey() {
        Call<RequestedObject> call = retrofitService.requestKey();
        call.enqueue(new Callback<RequestedObject>() {
            @Override
            public void onResponse(Call<RequestedObject> call, Response<RequestedObject> response) {
                if (response.code() == 200) {
                    RequestedObject requestedKey = response.body();
                    // set the key
                    Key.setKey(requestedKey.getKey());
                    // log key
                    Log.i(TAG, "Key: " + Key.getKey());
                } else if (response.code() == 404) {
                    Log.i(TAG, "Key doesn't exist");
                }
            }

            @Override
            public void onFailure(Call<RequestedObject> call, Throwable t) {
                Log.w(TAG, Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    /**
     * Report an infection by sending the current key to the server.
     *
     * @throws KeyNotRequestedException if this method is called before a key is requested
     */
    public void reportInfection() throws KeyNotRequestedException {
        if (Key.getKey() == null) {
            throw new KeyNotRequestedException("A key needs to be requested first");
        } else {
            HashMap<String, String> keyMap = new HashMap<>();
            keyMap.put("date", Calendar.getInstance().getTime().toString());
            keyMap.put("key", Key.getKey());

            Call<Void> call = retrofitService.reportInfection(keyMap);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200) {
                        Log.i(TAG, "Infection reported successfully");
                    } else if (response.code() == 400) {
                        Log.i(TAG, "Infection already reported");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.w(TAG, Objects.requireNonNull(t.getMessage()));
                }
            });
        }
    }
}