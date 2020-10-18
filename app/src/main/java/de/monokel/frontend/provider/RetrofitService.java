package de.monokel.frontend.provider;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Interface for Retrofit which provides operations for client-server-communication.
 *
 * @author Philipp Alessandrini
 * @version 2020-10-17
 */
public interface RetrofitService {
    @GET("/request_key")
    Call<Key> requestKey();

    @POST("/send_key")
    Call<Void> sendKey(@Body HashMap<String, String> key);
}