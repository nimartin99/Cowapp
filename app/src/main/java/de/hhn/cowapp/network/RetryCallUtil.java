package de.hhn.cowapp.network;

import de.hhn.cowapp.network.CallbackWithRetry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Util class which specifies the behavior of a enqueue process.
 * @author Philipp Alessandrini
 * @version 2020-10-28
 */
public class RetryCallUtil {
    private static boolean connected = true;
    private static boolean responseReceived = true;

    /**
     * Allows only enqueueing if there is an active connection and a response is revived.
     * Retries this process as frequent as defined in the anonymous class.
     *
     * @param call the request which is sent to the server
     * @param callback the triggered callback after sending the request
     * @param <T> the type of the object which is sent to the server
     */
    public static <T> void enqueueWithRetry(Call<T> call, final Callback<T> callback) {
        if (connected && responseReceived) {
            responseReceived = false;
            call.enqueue(new CallbackWithRetry<T>(call) {
                @Override
                public void onResponse(Call<T> call, Response<T> response) {
                    connected = true;
                    responseReceived = true;
                    callback.onResponse(call, response);
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    connected = false;
                    responseReceived = true;
                    super.onFailure(call, t);
                    callback.onFailure(call, t);
                }
            });
        }
    }

    public static boolean isConnected() {
        return connected;
    }
}
