package de.monokel.frontend;

import org.junit.Test;

import de.hhn.frontend.MainActivity;
import de.hhn.frontend.utils.ResponseState;

import static org.junit.Assert.assertEquals;

/**
 * Tests if the expected server responses are happen.
 * NOTE: This test expects a successful connection to the serer.
 *
 * @author Philipp Alessandrini
 * @version 2020-11-30
 */
public class RequestConditionTest {
    @Test
    public void testRequestInfectionCondition() throws InterruptedException {
        // request the infection status
        MainActivity.requestInfectionStatus();
        // give the server some time to respond
        Thread.sleep(500);
        // there should be no responded user keys because there are none requested
        assertEquals("NO_USER_KEYS", ResponseState.getLastResponseState());
    }

    @Test
    public void testReportInfectionCondition() throws InterruptedException {
        // report an infection
        MainActivity.reportInfection("DIRECT_CONTACT");
        // give the server some time to response
        Thread.sleep(500);
        // the emulated android environment shouldn't has any contacts registered
        assertEquals("NO_REGISTERED_CONTACTS", ResponseState.getLastResponseState());
    }
}