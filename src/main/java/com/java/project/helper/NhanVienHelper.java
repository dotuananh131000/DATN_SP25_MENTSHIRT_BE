package com.java.project.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class NhanVienHelper {
    public static String createNhanVienHelper() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddhhmmss");
        String dateTimePart = LocalDateTime.now().format(formatter);
        String uniquePart = UUID.randomUUID().toString().substring(0,3).toUpperCase();
        return "NV" + dateTimePart + uniquePart;
    }
}
