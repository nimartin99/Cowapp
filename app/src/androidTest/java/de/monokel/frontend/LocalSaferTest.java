package de.monokel.frontend;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Before;
import org.junit.Test;

import de.hhn.frontend.provider.LocalSafer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test-class for the LocalSafer-Class
 *
 * @author Miftari
 */
public class LocalSaferTest {

    Context appContext;

    @Before
    public void setAppContext() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void settingsTest() {
        LocalSafer.setIsAlarmRingLogged(false, appContext);
        LocalSafer.setIsAlarmSetLogged(false, appContext);
        LocalSafer.setIsKeySafeLogged(false, appContext);
        LocalSafer.setIsKeyTransmitLogged(false, appContext);
        LocalSafer.setIsFirstAppStart(false, appContext);

        assertFalse(LocalSafer.isAlarmRingLogged(appContext));
        assertFalse(LocalSafer.isAlarmSetLogged(appContext));
        assertFalse(LocalSafer.isKeySafeLogged(appContext));
        assertFalse(LocalSafer.isKeyTransmitLogged(appContext));
        assertFalse(LocalSafer.isFirstAppStart(appContext));

        LocalSafer.setIsAlarmRingLogged(true, appContext);
        LocalSafer.setIsAlarmSetLogged(true, appContext);
        LocalSafer.setIsKeySafeLogged(true, appContext);
        LocalSafer.setIsKeyTransmitLogged(true, appContext);
        LocalSafer.setIsFirstAppStart(true, appContext);

        assertTrue(LocalSafer.isAlarmRingLogged(appContext));
        assertTrue(LocalSafer.isAlarmSetLogged(appContext));
        assertTrue(LocalSafer.isKeySafeLogged(appContext));
        assertTrue(LocalSafer.isKeyTransmitLogged(appContext));
        assertTrue(LocalSafer.isFirstAppStart(appContext));
    }

    @Test
    public void debugLogTest() {
        LocalSafer.clearDebugLog(appContext);
        assertNull(LocalSafer.getDebugValues(appContext));
        LocalSafer.addLogValueToDebugLog("test", appContext);
        assertTrue(LocalSafer.getDebugValues(appContext)[0].startsWith("test"));
        LocalSafer.clearDebugLog(appContext);
        assertNull(LocalSafer.getDebugValues(appContext));
    }

    @Test
    public void notificationCountTest() {
        LocalSafer.safeNotificationCounter(0, appContext);
        assertEquals(0, LocalSafer.getNotificationCounter(appContext));
        LocalSafer.safeNotificationCounter(1, appContext);
        assertEquals(1, LocalSafer.getNotificationCounter(appContext));
    }
}