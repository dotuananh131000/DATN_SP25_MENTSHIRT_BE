package com.java.project.services;

import com.java.project.entities.HoaDon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private SimpMessagingTemplate template;

    public void notifyNewOrder(HoaDon hoaDon) {
        String notificationMessage = "Đơn hàng mới" + hoaDon.getMaHoaDon() + "đã được tạo!";

        template.convertAndSend("/topic/new-hoaDon", notificationMessage);
    }
}
