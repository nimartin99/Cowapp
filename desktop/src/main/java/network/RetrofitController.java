package network;

import gui.frame.MainFrame;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.HashMap;

/**
 * Controller class for Retrofit-initialization and requests
 * @author Philipp Alessandrini
 * @version 2020-12-15
 */
public class RetrofitController {
  private final static String BASE_URL = "https://cowapp.glitch.me";
  
  private static Retrofit retrofit;
  private static RetrofitService retrofitInterface;
  
  /**
   * Starts Retrofit
   */
  public static void start() {
    retrofit = new Retrofit.Builder()
                   .baseUrl(BASE_URL)
                   .addConverterFactory(GsonConverterFactory.create())
                   .build();
    
    retrofitInterface = retrofit.create(RetrofitService.class);
  }
  
  /**
   * Request for sending id to the server
   * @param id value
   * @param infection infection or non infection report
   */
  public static void sendId(String id, boolean infection) {
    // init hash map
    HashMap<String, String> idMap = new HashMap<>();
    idMap.put("id", id);
    if (infection) {
      idMap.put("infection", "TRUE");
    } else {
      idMap.put("infection", "FALSE");
    }
    // send request
    Call<Void> call = retrofitInterface.sendId(idMap);
    // handle response
    call.enqueue(new Callback<>() {
      @Override
      public void onResponse(Call<Void> call, Response<Void> response) {
        if (response.code() == 200) {
          MainFrame.transmittingSuccessful();
        } else if (response.code() == 400) {
          MainFrame.transmittingNotSuccessful();
        } else if (response.code() == 404) {
          MainFrame.transmittingNotSuccessful();
        }
      }
  
      @Override
      public void onFailure(Call<Void> call, Throwable t) {
        MainFrame.transmittingNotSuccessful();
      }
    });
  }
}