package com.hasan.storyvibrance.Utility;

import java.util.Date;

public class TimeUtils {

    // Method to convert timestamp to a human-readable format
    public static String getTimeAgo(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - timestamp;

        long seconds = timeDifference / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long years = days / 365;

        if (years > 0) {
            return years + (years == 1 ? " year ago" : " years ago");
        } else if (days > 0) {
            return days + (days == 1 ? " day ago" : " days ago");
        } else if (hours > 0) {
            return hours + (hours == 1 ? " hour ago" : " hours ago");
        } else if (minutes > 0) {
            return minutes + (minutes == 1 ? " minute ago" : " minutes ago");
        } else {
            return seconds + (seconds == 1 ? " second ago" : " seconds ago");
        }
    }
}

