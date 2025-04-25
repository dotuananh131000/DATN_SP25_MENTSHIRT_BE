package com.java.project.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class KhachHangHelper {
    public static String createNhanVienHelper() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddhhmmss");
        String dateTimePart = LocalDateTime.now().format(formatter);
        String uniquePart = UUID.randomUUID().toString().substring(0,3).toUpperCase();
        return "KH" + dateTimePart + uniquePart;
    }

    public static String createTenDangNhapHelper() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyddhhmmss");
        String dateTimePart = LocalDateTime.now().format(formatter);
        String uniquePart = UUID.randomUUID().toString().substring(0,2).toUpperCase();
        return "Client" + dateTimePart + uniquePart;
    }


}
