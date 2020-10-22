package de.monokel.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import org.altbeacon.beacon.BeaconManager;

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

    //TAG for Logging example: Log.d(TAG, "fine location permission granted"); -> d for debug
    protected static final String TAG = "MainActivity";

    private Retrofit retrofit;
    private RetrofitService retrofitService;
    private String BASE_URL = "http://10.0.2.2:3000"; // for emulated phone

    //Expected Permission Values
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_BACKGROUND_LOCATION = 2;

    String prefDataProtection = "ausstehend";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check bluetooth and location turned on
        verifyBluetooth();
        //Request needed permissions
        requestPermissions();

        // init retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);

        //If the app is opened for the first time the user has to accept the data protection regulations
        if (firstAppStart()) {
            Intent nextActivity = new Intent(MainActivity.this, DataProtectionActivity.class);
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
    public boolean firstAppStart() {
        SharedPreferences preferences = getSharedPreferences(prefDataProtection, MODE_PRIVATE);
        if (preferences.getBoolean(prefDataProtection, true)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(prefDataProtection, false);
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

    /**
     * Request all needed permissions based on SDK Version
     * (Permission already requested in Manifest -> double check)
     */
    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (this.checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (!this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("This app needs background location access");
                            builder.setMessage("Please grant location access so this app can detect beacons in the background.");
                            builder.setPositiveButton(android.R.string.ok, null);
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                                @TargetApi(23)
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                                            PERMISSION_REQUEST_BACKGROUND_LOCATION);
                                }

                            });
                            builder.show();
                        } else {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Functionality limited");
                            builder.setMessage("Since background location access has not been granted, this app will not be able to discover beacons in the background.  Please go to Settings -> Applications -> Permissions and grant background location access to this app.");
                            builder.setPositiveButton(android.R.string.ok, null);
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                }

                            });
                            builder.show();
                        }
                    }
                }
            } else {
                if (!this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                            PERMISSION_REQUEST_FINE_LOCATION);
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons.  Please go to Settings -> Applications -> Permissions and grant location access to this app.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }

            }
        }
    }

    /**
     * Verify if Bluetooth is turned on and if BLE is supported
     */
    private void verifyBluetooth() {
        try {
            if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Bluetooth not enabled");
                builder.setMessage("Please enable bluetooth in settings and restart this application.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //finish();
                        //System.exit(0);
                    }
                });
                builder.show();
            }
        } catch (RuntimeException e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth LE not available");
            builder.setMessage("Sorry, this device does not support Bluetooth LE.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    //finish();
                    //System.exit(0);
                }

            });
            builder.show();

        }

    }

    /**
     * Permission dialog result catch to follow further steps if not granted
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_FINE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "fine location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
            case PERMISSION_REQUEST_BACKGROUND_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "background location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since background location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }
}