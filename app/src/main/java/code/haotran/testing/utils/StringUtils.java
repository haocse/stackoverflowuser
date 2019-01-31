package code.haotran.testing.utils;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

public class StringUtils {
    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time*1000);
        String date = DateFormat.format("MM-dd-yyyy", cal).toString();
        return date;
    }
}
