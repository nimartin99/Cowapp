package de.monokel.frontend;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.HashMap;

import de.monokel.frontend.provider.Key;
import de.monokel.frontend.provider.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private RetrofitService retrofitService;
    private String BASE_URL = "http://10.0.2.2:3000"; // for emulated phone

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
        // request a key
        requestKey();
    }

    /**
     * Request the key-counter value from MongoDb
     */
    private void requestKey() {
        Call<Key> call = retrofitService.requestKey();
        call.enqueue(new Callback<Key>() {
            @Override
            public void onResponse(Call<Key> call, Response<Key> response) {
                if (response.code() == 200) {
                    Key key = response.body();
                    key.increase();
                    sendKey(key.getKey());

                    Toast.makeText(MainActivity.this, "Key: " + key.getKey(),
                            Toast.LENGTH_LONG).show();

                } else if (response.code() == 404) {
                    Toast.makeText(MainActivity.this, "Key doesn't exist",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Key> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Send new key to the server
     * @param key current key for this device.
     */
    private void sendKey(String key) {
        HashMap<String, String> keyMap = new HashMap<>();
        keyMap.put("key", key);

        Call<Void> call = retrofitService.sendKey(keyMap);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(MainActivity.this,
                            "Key successfully sent", Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    Toast.makeText(MainActivity.this, "Key doesn't exist",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}