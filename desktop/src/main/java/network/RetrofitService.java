package network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.HashMap;

/**
 * REST-Api for CoW-Desktop-App
 * @author Philipp Alessandrini
 * @version 2020-12-115
 */
public interface RetrofitService {
  /**
   * Sends the id to the db-server.
   * @param idMap id value with name
   */
  @POST("/send_id")
  Call<Void> sendId(@Body HashMap<String, String> idMap);
}
