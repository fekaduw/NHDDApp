package et.gov.fmoh.nhddapp.nhddapp.utils;

import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataTypeConverter {
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Nullable
    public static Date convertStringToDate(String stringToConvert) {
        try {
            return format.parse(stringToConvert);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String ConvertDateToString(Date dateToConvert) {
            return format.format(dateToConvert);
    }
}
