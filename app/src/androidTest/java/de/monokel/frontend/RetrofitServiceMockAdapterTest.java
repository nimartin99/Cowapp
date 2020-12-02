package de.monokel.frontend;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import de.hhn.frontend.provider.RequestedObject;
import de.hhn.frontend.provider.RetrofitService;
import de.hhn.frontend.utils.ResponseState;
import de.monokel.frontend.mockserverdummy.MockRetrofitService;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the API through testing if the expected response object is called.
 *
 * @author Philipp Alessandrini
 * @version 2020-12-01
 */
public class RetrofitServiceMockAdapterTest {
    private MockRetrofit mockRetrofit;
    private Retrofit retrofit;

    @Before
    public void setAppContext() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://test.com")
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NetworkBehavior behavior = NetworkBehavior.create();
        behavior.setFailurePercent(0);

        mockRetrofit = new MockRetrofit.Builder(retrofit)
                .networkBehavior(behavior)
                .build();
    }

    @Test
    public void testRequestKey() throws Exception {
        BehaviorDelegate<RetrofitService> delegate = mockRetrofit.create(RetrofitService.class);
        RetrofitService mockRetrofitService = new MockRetrofitService(delegate);
        // test request and response
        Call<String> key = mockRetrofitService.requestKey();
        Response<String> retrofitResponse = key.execute();
        // asserting values
        assertTrue(retrofitResponse.isSuccessful());
        assertEquals("0123-4567-89ab-cdef01234567", retrofitResponse.body());
        assertEquals("KEY_SUCCESSFULLY_REQUESTED", ResponseState.getLastResponseState());
    }

    @Test
    public void testRequestInfectionStatus() throws Exception {
        BehaviorDelegate<RetrofitService> delegate = mockRetrofit.create(RetrofitService.class);
        RetrofitService mockRetrofitService = new MockRetrofitService(delegate);
        // init example key
        HashMap<String, String> exampleKeyMap = new HashMap<>();
        exampleKeyMap.put("key", "0000-0000-0000-000000000000");
        // test request and response
        Call<RequestedObject> infection = mockRetrofitService.requestInfectionStatus(exampleKeyMap);
        Response<RequestedObject> retrofitResponse = infection.execute();
        // asserting values
        assertTrue(retrofitResponse.isSuccessful());
        assertEquals("DIRECT_CONTACT", retrofitResponse.body().getStatus());
        assertEquals("3", retrofitResponse.body().getContactNbr());
        assertEquals("120", retrofitResponse.body().getLastInfectionTime());
        assertEquals("DIRECT_CONTACT", ResponseState.getLastResponseState());
    }
}
