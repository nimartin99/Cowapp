package de.monokel.frontend.provider;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Interface for Retrofit which provides operations for client-server-communication.
 *
 * @author Philipp Alessandrini
 * @version 2020-10-17
 */
public interface RetrofitService {
    @GET("/request_key")
    Call<Key> requestKey();
}