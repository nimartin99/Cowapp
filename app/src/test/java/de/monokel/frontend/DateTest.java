package de.monokel.frontend;

import android.util.Log;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.hhn.frontend.MainActivity;
import de.hhn.frontend.date.dateHelper;
import de.hhn.frontend.provider.LocalSafer;

import static org.junit.Assert.assertEquals;

public class DateTest {

    @Test
    public void calculateIntervalTest() {
        Date date1 = new Date(2020, 11, 1);
        Date date2 = new Date(2020, 11, 11);

        assertEquals(dateHelper.calculateTimeIntervalBetweenTwoDays(date1, date2), 10);
    }

    @Test
    public void convertDateFromStringToDate() {
        // get first date of current date
        Date date = dateHelper.getCurrentDate();
        //get first String of current date
        String dateString = dateHelper.getCurrentDateString();

        // get second date by converting first String
        Date date2 = dateHelper.convertStringToDate(dateString);

        //assert equals is false if both dates are on the same day, if they have different time like second, hour.
        //convert first date to a String and then back into a date, after that the date contains only Information of the used format(Day, Month, Year).
        String string2 = dateHelper.convertDateToString(date);
        date = dateHelper.convertStringToDate(string2);

        //assert equals is false if both dates are on the same day, if they have different time like second, hour
        assertEquals(date, date2);

        assertEquals(dateString, dateHelper.convertDateToString(date));


    }

    @Test
    public void dateDifferenceTest() {

        Date date1 = new Date(2020, 11, 1);

        Date date3 = new Date(2020, 11, 11);


        //long diffInMillis = date3.getTime() - date1.getTime();
        //long dateDiffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

        long dateDiffInDays = dateHelper.calculateTimeIntervalBetweenTwoDays(date1, date3);
        assertEquals(dateDiffInDays, 10);

    }
}
