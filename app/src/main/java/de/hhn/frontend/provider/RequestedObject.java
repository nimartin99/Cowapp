package de.hhn.frontend.provider;

/**
 * Class for holding requested values from Retrofit.
 *
 * @author Philipp Alessandrini
 * @version 2020-12-01
 */
public class RequestedObject {
    private String status;
    private String contactNbr;
    private String lastInfectionTime;

    public String getStatus() {
        return status;
    }

    public String getContactNbr() {
        return contactNbr;
    }

    public String getLastInfectionTime() {
        return lastInfectionTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setContactNbr(String contactNbr) {
        this.contactNbr = contactNbr;
    }

    public void setLastInfectionTime(String lastInfectionTime) {
        this.lastInfectionTime = lastInfectionTime;
    }
}