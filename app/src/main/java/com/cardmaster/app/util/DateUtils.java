package com.cardmaster.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    private static final SimpleDateFormat DAY_LABEL_FORMAT = new SimpleDateFormat("dd/MM", Locale.getDefault());

    public static String formatDate(long millis) {
        if (millis <= 0) return "";
        return DATE_FORMAT.format(new Date(millis));
    }

    public static String formatTime(long millis) {
        if (millis <= 0) return "";
        return TIME_FORMAT.format(new Date(millis));
    }

    public static String formatDateTime(long millis) {
        if (millis <= 0) return "";
        return DATE_TIME_FORMAT.format(new Date(millis));
    }

    public static String formatDayLabel(long millis) {
        if (millis <= 0) return "";
        return DAY_LABEL_FORMAT.format(new Date(millis));
    }

    public static String formatDuration(long seconds) {
        if (seconds < 60) {
            return seconds + " giây";
        }
        long minutes = seconds / 60;
        long remainingSec = seconds % 60;
        if (minutes < 60) {
            return minutes + " phút " + (remainingSec > 0 ? remainingSec + " giây" : "");
        }
        long hours = minutes / 60;
        long remainingMin = minutes % 60;
        return hours + " giờ " + (remainingMin > 0 ? remainingMin + " phút" : "");
    }
}
