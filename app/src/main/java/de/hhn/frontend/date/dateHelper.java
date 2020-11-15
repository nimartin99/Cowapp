package de.hhn.frontend.date;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hhn.frontend.provider.LocalSafer;

/**
 * Class with all operations to help using dates
 */

public class dateHelper {

    /**
     * @param date1 earlier in time date
     * @param date2 later in time date
     * @return calculated difference in days between both mentioned dates
     */

    public static long calculateTimeIntervalBetweenTwoDays(Date date1, Date date2) {

        long diffInMillis = date2.getTime() - date1.getTime();
        return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * This method returns a date format, matching to the currently used locale setting
     */

    public static SimpleDateFormat getDateFormat() {
        // returns default Locale set by the Java Virtual Machine
        String language = Locale.getDefault().getLanguage();
        SimpleDateFormat format;

        switch (language) {
            case "de":
                format = new SimpleDateFormat("dd.MM.yyyy");
                break;
            case "en-GB":
                format = new SimpleDateFormat("dd/MM/yyyy");
                break;
            case "en-US":
                format = new SimpleDateFormat("MM/dd/yyyy");
                break;
            default:
                format = new SimpleDateFormat("dd/MM/yyyy");
                break;
        }

        return format;
    }


    public static String getCurrentDateString() {

        Date currentDate = Calendar.getInstance().getTime();

        DateFormat format = dateHelper.getDateFormat();

        return format.format(currentDate);
    }

    public static String convertDateToString(Date dateToConvert) {

        return getDateFormat().format(dateToConvert);
    }

    public static Date convertStringToDate(String stringToConvert) {

        Date date = new Date();

        SimpleDateFormat format = getDateFormat();

        try {
            date = format.parse(stringToConvert);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Jonas Log", "Parse gone Wrong @convertStringToDate");
            System.out.println("Parsing the String of getFirstStartDate to a Date went wrong!");
        }

        return date;

    }

    public static Date getCurrentDate() {

        //return Calendar.getInstance().getTime();
        Date date = new Date();
        return date;
    }


    /**
     * Methode calculates how much time passed since the first start of the app
     *
     * @return difference in days
     * @throws ParseException Signals that an error has been reached unexpectedly while parsing.
     */


    public static long getDateDiffSinceFirstUse() {

        SimpleDateFormat format = getDateFormat();

        Date firstAppStartDate = convertStringToDate(LocalSafer.getFirstStartDate());


        Date currentDate = new Date();
        long diffInMillis = currentDate.getTime() - firstAppStartDate.getTime();
        return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }



}
