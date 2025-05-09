package com.java.project.controllers;

import com.java.project.services.VNPaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/payment")
public class VNPaymentControlller {

    VNPaymentService vnPaymentService;

    @PostMapping("/create-payment-url/{orderId}")
    public ResponseEntity<?> createPaymentUrl(@PathVariable String orderId) {
        try {
            String paymentUrl = vnPaymentService.generatePaymentUrl(orderId);
            return ResponseEntity.ok(paymentUrl);
        } catch (UnsupportedEncodingException e) {
            return ResponseEntity.status(500).body("Lỗi mã hóa URL: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/handle-response")
    public ResponseEntity<?> handleVNPayResponse(@RequestParam Map<String, String> params) {
        // Xử lý logic phản hồi từ VNPay (VD: xác nhận thanh toán, lưu trạng thái đơn hàng, ...)
        String result = "Đã nhận phản hồi từ VNPay. Chi tiết: " + params.toString();
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/vnpay-callback")
    public ResponseEntity<String> handleVNPayCallback(@RequestParam Map<String, String> payload) {
        try {
            if (!payload.containsKey("vnp_TxnRef") || !payload.containsKey("vnp_ResponseCode")) {
                String htmlResponse = vnPaymentService.generateHtml("Dữ liệu không hợp lệ",
                        "Thiếu thông tin cần thiết trong callback.",
                        "Vui lòng kiểm tra và thử lại.");
                return new ResponseEntity<>(htmlResponse, HttpStatus.BAD_REQUEST);
            }

            String response = vnPaymentService.handleVnpayCallback(payload);

            if ("Giao dịch thành công".equals(response)) {
                String htmlResponse = vnPaymentService.generateHtml("Thanh toán thành công",
                        "Cảm ơn bạn đã thanh toán.",
                        "Đơn hàng của bạn đã được thanh toán thành công.");
                return new ResponseEntity<>(htmlResponse, HttpStatus.OK);
            } else {
                String vnpResponseCode = payload.get("vnp_ResponseCode");
                String htmlResponse = vnPaymentService.generateHtml("Thanh toán thất bại",
                        "Giao dịch không thành công.",
                        "Mã lỗi: " + vnpResponseCode + "<br/>" +
                                "Xin lỗi, có lỗi xảy ra trong quá trình thanh toán. Vui lòng thử lại sau.");
                return new ResponseEntity<>(htmlResponse, HttpStatus.OK);
            }
        } catch (Exception e) {
            String htmlResponse = vnPaymentService.generateHtml("Có lỗi xảy ra",
                    e.getMessage(),
                    "Vui lòng liên hệ với chúng tôi để biết thêm chi tiết.");
            return new ResponseEntity<>(htmlResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
