package de.hhn.frontend;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import de.hhn.cowapp.datastorage.LocalSafer;

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
    public void integerTests() {
        LocalSafer.safeNotificationCounter(0, appContext);
        assertEquals(0, LocalSafer.getNotificationCounter(appContext));
        LocalSafer.safeNotificationCounter(1, appContext);
        assertEquals(1, LocalSafer.getNotificationCounter(appContext));

        LocalSafer.safeRiskLevel(0, appContext);
        assertEquals(0, LocalSafer.getRiskLevel(appContext));
        LocalSafer.safeRiskLevel(1, appContext);
        assertEquals(1, LocalSafer.getRiskLevel(appContext));
    }

    @Test
    public void keyManagementTest() {
        LocalSafer.clearKeyPairDataFile(appContext);
        LocalSafer.clearBufferFile(appContext);
        LocalSafer.addReceivedKey("00000000-0000-0000-0000-000000000000", appContext);
        assertNull(LocalSafer.getKeyPairs(appContext));
        LocalSafer.analyzeBufferFile(appContext);
        assertTrue(LocalSafer.getKeyPairs(appContext)[0].contains("0000-0000-0000-000000000000"));
        LocalSafer.clearKeyPairDataFile(appContext);
        LocalSafer.clearBufferFile(appContext);
        assertNull(LocalSafer.getKeyPairs(appContext));
        LocalSafer.analyzeBufferFile(appContext);
        assertNull(LocalSafer.getKeyPairs(appContext));
    }

    @Test
    public void ownKeyManagementTest() {
        LocalSafer.clearOwnKeyPairDataFile(appContext);
        assertNull(LocalSafer.getOwnKeys(appContext));
        LocalSafer.safeOwnKey("0000-0000-0000-000000000000", appContext);
        assertTrue(LocalSafer.getOwnKey(appContext).contains("0000-0000-0000-000000000000"));
        assertTrue(LocalSafer.getOwnKeys(appContext)[0].contains("0000-0000-0000-000000000000"));
        LocalSafer.clearOwnKeyPairDataFile(appContext);
        assertNull(LocalSafer.getOwnKeys(appContext));
    }

    @Test
    public void notificationLogTest() {
        LocalSafer.clearNotificationDataFile(appContext);
        assertNull(LocalSafer.getNotifications(appContext));
        LocalSafer.addNotificationToSavedNotifications("test", appContext);
        assertTrue(LocalSafer.getNotifications(appContext)[0].startsWith("test"));
        LocalSafer.clearNotificationDataFile(appContext);
        assertNull(LocalSafer.getNotifications(appContext));
    }

    @Test
    public void firstStartDateTest() {
        LocalSafer.safeFirstStartDate("test", appContext);
        assertTrue(LocalSafer.getFirstStartDate(appContext).equals("test"));
        LocalSafer.safeFirstStartDate("test2", appContext);
        assertTrue(LocalSafer.getFirstStartDate(appContext).equals("test2"));
        LocalSafer.safeStringAtDatafile("cowappfirstdate.txt", "", appContext);
        LocalSafer.safeFirstStartDate(new Date().toString(), appContext);
    }

    @Test
    public void isOldTest() {
        Date date = new Date();
        date.setDate(date.getDate() - 15);
        assertTrue(LocalSafer.dateIsOld(date));
        date = new Date();
        assertFalse(LocalSafer.dateIsOld(date));
    }
}