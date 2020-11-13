package de.hhn.frontend.provider;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Interface for Retrofit which provides operations for client-server-communication.
 *
 * @author Philipp Alessandrini
 * @version 2020-11-10
 */
public interface RetrofitService {
    /**
     * GET-request to get a key which is generated by the server.
     *
     * @return a unique key
     */
    @GET("/request_key")
    Call<RequestedObject> requestKey();

    /**
     * POST-request to to send all contact keys to the server.
     *
     * @param keysMap contact keys which are sent to the server
     */
    @POST("/report_infection")
    Call<Void> reportInfection(@Body HashMap<String, String> keysMap);

    /**
     * POST-request to inform the server about the newest key to allow unique keys for all users.
     *
     * @param sendKeyMap the generated user key
     */
    @POST("/send_key")
    Call<Void> sendKey(@Body HashMap<String, String> sendKeyMap);

    /**
     * POST-request to send own keys to the server which checks the infections status.
     *
     * @param ownUserKeysMap own user keys which are sent to the server
     * @return the infection status of the user
     *         possible values: "DIRECT_CONTACT", "INDIRECT_CONTACT"
     */
    @POST("/request_infection_status")
    Call<String> requestInfectionStatus(@Body HashMap<String, String> ownUserKeysMap);
}