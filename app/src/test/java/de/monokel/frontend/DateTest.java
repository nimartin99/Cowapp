package de.monokel.frontend;

import android.util.Log;

import org.junit.Test;
import java.util.Date;

import de.hhn.frontend.date.DateHelper;
import static org.junit.Assert.assertEquals;
public class DateTest {

    @Test
    public void calculateIntervalTest() {
        Date date1 = new Date(2020, 11, 1);
        Date date2 = new Date(2020, 11, 11);

        assertEquals(DateHelper.calculateTimeIntervalBetweenTwoDays(date1, date2), 10);
    }

    @Test
    public void convertDateFromStringToDate() {
        // get first date of current date
        Date date = DateHelper.getCurrentDate();
        //get first String of current date
        String dateString = DateHelper.getCurrentDateString();

        // get second date by converting first String
        Date date2 = DateHelper.convertStringToDate(dateString);

        //assert equals with two dates results false when both dates are on the same day, if they have different time like second, hour.
        //convert first date to a String and then back into a date, after that the date contains only Information of the used format(Day, Month, Year).
        String string2 = DateHelper.convertDateToString(date);
        date = DateHelper.convertStringToDate(string2);

        //assert equals is false if both dates are on the same day, if they have different time like second, hour
        assertEquals(date, date2);

        assertEquals(dateString, DateHelper.convertDateToString(date));


    }

    @Test
    public void dateDifferenceTest() {

        Date date1 = new Date(2020, 11, 1);

        Date date3 = new Date(2020, 11, 11);
        long dateDiffInDays = DateHelper.calculateTimeIntervalBetweenTwoDays(date1, date3);
        assertEquals(dateDiffInDays, 10);

    }
}
