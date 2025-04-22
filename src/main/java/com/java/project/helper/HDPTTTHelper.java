package com.java.project.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class HDPTTTHelper {
    public static String createHDPTTTHelper() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddhhmmss");
        String dateTimePart = LocalDateTime.now().format(formatter);
        String uniquePart = UUID.randomUUID().toString().substring(0,3).toUpperCase();
        return "TT" + dateTimePart + uniquePart;
    }
}
