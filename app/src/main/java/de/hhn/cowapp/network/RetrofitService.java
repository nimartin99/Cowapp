package de.hhn.cowapp.network;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Interface for Retrofit which provides operations for client-server-communication.
 *
 * @author Philipp Alessandrini
 * @version 2020-11-22
 */
public interface RetrofitService {
    /**
     * GET-request to get a key which is generated by the server.
     *
     * @return a unique key
     */
    @GET("/request_key")
    Call<String> requestKey();

    /**
     * POST-request to to send all contact keys to the server.
     *
     * @param keysMap contact keys which are sent to the server
     */
    @POST("/report_infection")
    Call<Void> reportInfection(@Body HashMap<String, String> keysMap);

    /**
     * POST-request to send own keys to the server which checks the infections status.
     *
     * @param ownUserKeysMap own user keys which are sent to the server
     * @return the infection status of the user
     *         possible values: "DIRECT_CONTACT", "INDIRECT_CONTACT"
     */
    @POST("/request_infection_status")
    Call<RequestedObject> requestInfectionStatus(@Body HashMap<String, String> ownUserKeysMap);
}