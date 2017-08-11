package fr.inria.spirals.sensorscollect.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public static final String toString(final Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    private DateUtils() {

    }

}
