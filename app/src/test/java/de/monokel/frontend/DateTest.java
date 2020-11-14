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

import static org.junit.Assert.assertEquals;

public class DateTest {

    @Test
    public void currentDateTest() {
        System.out.println(MainActivity.getCurrentDateString());
    }

    @Test
    public void dateDifferenceTest() {

        Date date1 = new Date(2020, 11, 1);

        Date date3 = new Date(2020, 11, 11);


        long diffInMillis = date3.getTime() - date1.getTime();
        long dateDiffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

        assertEquals(dateDiffInDays, 10);


    }

    @Test
    public void calculateIntervalTest() {
        Date date1 = new Date(2020, 11, 1);
        Date date2 = new Date(2020, 11, 11);

        assertEquals(dateHelper.calculateTimeIntervalBetweenTwoDays(date1, date2), 10);
    }

    @Test
    public void convertDateFromStringToDate() {

    }


    public static void main(String[] args) {

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

        String dateString = MainActivity.getCurrentDateString();
        System.out.println("1. Ausgabe: ");
        System.out.println(dateString);

        Date dateCal = Calendar.getInstance().getTime();
        System.out.println("2. Ausgabe: ");
        System.out.println(dateCal);


        // richtige formatierung von Date -> String
        System.out.println("3. Ausgabe: ");
        String strDate = format.format(dateCal);
        System.out.println(strDate);

        Date date2 = MainActivity.getCurrentDate();
        System.out.println("4. Ausgabe: ");
        System.out.println(date2);


        //Date date = new Date();
        Date date = date2;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Jonas", "Parse gone wrong in Test class!");
            System.out.println("Parse gone wrong!!");
        }
        System.out.println("5. Ausgabe: ");
        System.out.println(date.toString());


    }
}
