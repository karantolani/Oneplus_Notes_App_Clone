package com.example.notes;

import android.text.format.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String formatDate(String date) throws ParseException {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        Date d = sdf.parse(date);
        Date currentDate = new Date();

        String hours = (String) DateFormat.format("HH", d);
        String minutes = (String) DateFormat.format("mm", d);
        String year = (String) DateFormat.format("yyyy", d);
        String month = (String) DateFormat.format("MMMM", d);
        String day = (String) DateFormat.format("dd", d);


        int hh = Integer.parseInt(hours);

        String meridien="";
        if (hh < 12)
            meridien = "am";
        else
            meridien = "pm";

        hh %= 12;

        if (hh == 0)
            hours = "12";
        else
            hours = String.valueOf(hh);


        String currentYear = (String) DateFormat.format("yyyy", currentDate);
        if(currentYear.equals(year))
            year = "";


        return month+" "+day+(year.length() == 0?", ":(" "+year+", "))+hours+":"+minutes+" "+meridien;

    }
}
