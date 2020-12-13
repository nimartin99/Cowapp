package de.hhn.cowapp.network;

import android.os.Handler;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Defines when and how often there should be an retry of the request.
 *
 * @author Philipp Alessandrini
 * @version 2020-10-28
 */
public abstract class CallbackWithRetry<T> implements Callback<T> {
    // delay between each retry of 5 minutes
    private static final long RETRY_DELAY = 300000; // [ms]
    private static final String TAG = CallbackWithRetry.class.getSimpleName();

    private final Call<T> call;

    /**
     * Constructor of this class which sets the call reference.
     *
     * @param call the sent request-object
     */
    public CallbackWithRetry(Call<T> call) {
        this.call = call;
    }

    @Override
    public void onFailure(final Call<T> call, Throwable t) {
        if (!RetryCallUtil.isConnected()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "Retrying to connect...");
                    retry(call);
                }
            }, RETRY_DELAY);
        }
    }

    /**
     * Reties the call.
     *
     * @param call specific call which is going to retry
     */
    private void retry(Call<T> call) {
        this.call.clone().enqueue(this);
    }
}