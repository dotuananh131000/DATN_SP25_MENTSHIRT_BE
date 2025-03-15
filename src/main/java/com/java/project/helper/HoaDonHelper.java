package com.java.project.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class HoaDonHelper {
    public static String createHoaDonHelper() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddhhmm");
        String dateTimePart = LocalDateTime.now().format(formatter);
        String uniquePart = UUID.randomUUID().toString().substring(0,3).toUpperCase();
        return "HD" + dateTimePart + uniquePart;
    }
}
