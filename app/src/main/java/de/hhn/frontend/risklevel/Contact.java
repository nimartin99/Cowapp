package de.hhn.frontend.risklevel;

import java.io.Serializable;
import java.util.Date;

/**
 * Class to represent a Contact
 *
 * @author jonas
 * @version 23.11.2020
 */

public class Contact implements Serializable {

    protected Date date;


    public Contact(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

