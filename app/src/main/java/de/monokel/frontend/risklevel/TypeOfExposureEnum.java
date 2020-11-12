package de.monokel.frontend.risklevel;

/**
 * Enum zur Klassifizierung der Art wie eine Übertragung stattgefunden haben könnte.
 * Direct contact stellt einen Kontakt mit einer als infiziert gemeldeten Person da.
 * Indirect Contact stellt einen Kontakt mit einer nicht als infiziert gemeldeten Person da. *
 * Long und Short Exposure stellen dar ob es sich um lange oder kurze, bzw. entsprechend viele oder wenige Schlüssel ausgetauscht wurden.
 *
 * @author jonas
 * 26.10.2020
 */

public enum TypeOfExposureEnum {LONG_EXPOSURE_DIRECT_CONTACT, SHORT_EXPOSURE_DIRECT_CONTACT, LONG_EXPOSURE_INDIRECT_CONTACT, SHORT_EXPOSURE_INDIRECT_CONTACT, NO_CONTACT}
