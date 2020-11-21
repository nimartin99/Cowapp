package de.hhn.frontend;

public class Constants {
    // Debug flag for testing purposes
    public static final boolean DEBUG = true;
    // Flag for controlling background scanning and transmitting (BLE Beacon)
    // Set false if you try to run the app on a virtual device (emulator)
    public static final boolean SCAN_AND_TRANSMIT = false;
    // The time difference between device scans in ms
    public static final Long BACKGROUND_SCAN_PERIOD = 1100L;
    public static final Long FOREGROUND_SCAN_PERIOD = 1100L;

    public static final String cowappBeaconIdentifier = "a32dffb3";
    public static final String testid1 = "A32Dffb3-FFFF-ffff-090a-222222222222";
}
