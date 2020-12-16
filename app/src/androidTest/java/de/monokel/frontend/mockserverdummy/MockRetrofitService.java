package de.monokel.frontend.mockserverdummy;

import java.util.HashMap;

import de.hhn.cowapp.network.RequestedObject;
import de.hhn.cowapp.network.RetrofitService;
import de.hhn.cowapp.network.ResponseState;
import retrofit2.Call;
import retrofit2.mock.BehaviorDelegate;

/**
 * Mock class which uses example response values to test the API.
 *
 * @author Philipp Alessandrini
 * @version 2020-12-16
 */
public class MockRetrofitService implements RetrofitService {
    private final BehaviorDelegate<RetrofitService> delegate;

    public MockRetrofitService(BehaviorDelegate<RetrofitService> service) {
        this.delegate = service;
    }

    @Override
    public Call<String> requestKey() {
        String requestedKey = "0123-4567-89ab-cdef01234567";
        ResponseState.setLastResponseState(ResponseState.State.KEY_SUCCESSFULLY_REQUESTED);

        return delegate.returningResponse(requestedKey).requestKey();
    }

    @Override
    public Call<Void> reportInfection(HashMap<String, String> keysMap) {
        return null;
    }

    @Override
    public Call<RequestedObject> requestInfectionStatus(HashMap<String, String> ownUserKeysMap) {
        RequestedObject requestedObject = new RequestedObject();
        requestedObject.setStatus("DIRECT_CONTACT");
        requestedObject.setContactNbr("3");
        requestedObject.setLastInfectionTime("120");
        ResponseState.setLastResponseState(ResponseState.State.DIRECT_CONTACT);

        return delegate.returningResponse(requestedObject).requestInfectionStatus(ownUserKeysMap);
    }

    @Override
    public Call<Void> verifyId(HashMap<String, String> idMap) {
        return null;
    }
}
