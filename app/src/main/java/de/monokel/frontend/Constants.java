package de.monokel.frontend;

public class Constants {
    // Debug flag for testing purposes
    public static final boolean DEBUG = true;
    // Flag for controlling background scanning and transmitting (BLE Beacon)
    // Set false if you try to run the app on a virtual device (emulator)
    public static final boolean SCAN_AND_TRANSMIT = false;
    // The time difference between device scans in ms
    public static final Long BACKGROUND_SCAN_PERIOD = 1100L;
    public static final Long FOREGROUND_SCAN_PERIOD = 1100L;
    public static final String id1 = "01234567-0506-0708-090a-222222222222";
    // 000000000000
    // 111111111111
    // 222222222222
}
