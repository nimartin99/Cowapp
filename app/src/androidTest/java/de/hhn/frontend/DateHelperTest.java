package de.hhn.frontend;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import de.hhn.cowapp.utils.DateHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Class to Test the methods of the DateHelper class
 *
 * @author jonas
 * @version 07.12.2020
 */


public class DateHelperTest {

    Date outDatedTestDate = new GregorianCalendar(2020, Calendar.NOVEMBER, 4).getTime();
    Date notOutDatedTestDate = DateHelper.getCurrentDate();

    @Test
    public void calculateIntervalTest() {
        Date date1 = new Date(2020, 11, 1);
        Date date2 = new Date(2020, 11, 11);

        assertEquals(DateHelper.calculateTimeIntervalBetweenTwoDays(date1, date2), 10);
    }

    @Test
    public void convertDateFromStringToDate() {
        // get date of current date
        Date currentDateByDateHelper = DateHelper.getCurrentDate();
        // get the current date as a String
        String currentDateAsSringByDateHelper = DateHelper.getCurrentDateString();

        // get second date by converting first String
        Date date2 = DateHelper.convertStringToDate(currentDateAsSringByDateHelper);

        // assert equals with two dates results false when both dates are on the same day, if they have different time like second, hour.
        // convert first date to a String and then back into a date, after that the date contains only Information of the used format(Day, Month, Year).
        String string2 = DateHelper.convertDateToString(currentDateByDateHelper);
        currentDateByDateHelper = DateHelper.convertStringToDate(string2);

        // assert equals is false if both dates are on the same day, if they have different time like second, hour
        assertEquals(currentDateByDateHelper, date2);

        assertEquals(currentDateAsSringByDateHelper, DateHelper.convertDateToString(currentDateByDateHelper));
    }

    @Test
    public void dateDifferenceTest() {

        Date date1 = new Date(2020, 11, 1);

        Date date3 = new Date(2020, 11, 11);
        long dateDiffInDays = DateHelper.calculateTimeIntervalBetweenTwoDays(date1, date3);
        assertEquals(dateDiffInDays, 10);
    }

    @Test
    public void dateIsOldTest() {
        assertFalse(DateHelper.checkIfDateIsOld(notOutDatedTestDate));
        assertTrue(DateHelper.checkIfDateIsOld((outDatedTestDate)));
    }
}