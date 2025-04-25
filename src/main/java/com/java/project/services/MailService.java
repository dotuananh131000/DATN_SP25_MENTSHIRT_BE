package com.java.project.services;

import com.java.project.dtos.HoaDonPhuongThucThanhToanResponse;
import com.java.project.entities.HoaDon;
import com.java.project.entities.HoaDonPhuongThucThanhToan;
import com.java.project.repositories.HoaDonPhuongThucThanhToanRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MailService {
    private final JavaMailSender javaMailSender;

    @Autowired
    HoaDonPhuongThucThanhToanRepository hoaDonPhuongThucThanhToanRepository;

    public MailService(JavaMailSender javaMailSender, HoaDonPhuongThucThanhToanRepository hoaDonPhuongThucThanhToanRepository) {
        this.javaMailSender = javaMailSender;
        this.hoaDonPhuongThucThanhToanRepository = hoaDonPhuongThucThanhToanRepository;
    }

    @Async // Chạy bất đồng bộ
    public void sendHtmlMail(String to, String subject, String htmlBody) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.err.println("Lỗi khi gửi email: " + e.getMessage());
        }
    }

    @Async
    public void sendNewPasswordMail(String username, String to, String newPassword) {
        String subject = "🔐 Mật Khẩu Mới Của Bạn";
        String htmlBody = "<p>Xin chào <strong>" + username + "</strong>,</p>"
                + "<p>Hệ thống đã tạo mật khẩu mới cho tài khoản của bạn:</p>"
                + "<p><strong style=\"color:red; font-size:18px;\">" + newPassword + "</strong></p>"
                + "<p>Vui lòng sử dụng mật khẩu này để đăng nhập và đổi mật khẩu ngay lập tức.</p>"
                + "<p>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng liên hệ với bộ phận hỗ trợ.</p>"
                + "<p>Trân trọng,</p>";

        sendHtmlMail(to, subject, htmlBody);
    }

    @Async
    public void sendVoucherDeletionMail(String username, String to, String voucherCode, String deletedAt) {
        String subject = "⚠️ Thông báo: Voucher của bạn đã bị xóa";
        String htmlBody = "<p>Xin chào <strong>" + username + "</strong>,</p>"
                + "<p>Hệ thống xin thông báo rằng voucher của bạn với mã: <strong style=\"color:red;\">" + voucherCode + "</strong> đã bị xóa vào lúc <strong>" + deletedAt + "</strong>.</p>"
                + "<p>Chúng tôi thành thật xin lỗi vì sự bất tiện này.</p>"
                + "<p>Nếu bạn có bất kỳ câu hỏi nào, vui lòng liên hệ với bộ phận hỗ trợ.</p>"
                + "<p>Trân trọng,</p>"
                + "<p><strong>Đội ngũ hỗ trợ</strong></p>";

        sendHtmlMail(to, subject, htmlBody);
    }

    @Async
    public void sendVoucherInactiveMail(String username, String to, String voucherCode, String deletedAt) {
        String subject = "⚠️ Thông báo: Voucher của bạn đã bị khóa";
        String htmlBody = "<p>Xin chào <strong>" + username + "</strong>,</p>"
                + "<p>Hệ thống xin thông báo rằng voucher của bạn với mã: <strong style=\"color:red;\">" + voucherCode + "</strong> đã bị xóa vào lúc <strong>" + deletedAt + "</strong>.</p>"
                + "<p>Chúng tôi thành thật xin lỗi vì sự bất tiện này.</p>"
                + "<p>Nếu bạn có bất kỳ câu hỏi nào, vui lòng liên hệ với bộ phận hỗ trợ.</p>"
                + "<p>Trân trọng,</p>"
                + "<p><strong>Đội ngũ hỗ trợ</strong></p>";

        sendHtmlMail(to, subject, htmlBody);
    }

    @Async
    public void sendVoucherActiveMail(String username, String to, String voucherCode, String activatedAt) {
        String subject = "️ Thông báo: Voucher của bạn đã được mở lại";
        String htmlBody = "<p>Xin chào <strong>" + username + "</strong>,</p>"
                + "<p>Hệ thống xin thông báo rằng voucher của bạn với mã: <strong style=\"color:red;\">" + voucherCode + "</strong> đã được mở lại vào lúc <strong>" + activatedAt + "</strong>.</p>"
                + "<p>Chúng tôi hy vọng rằng các thay đổi này sẽ giúp bạn có trải nghiệm tốt hơn.</p>"
                + "<p>Nếu bạn có bất kỳ câu hỏi nào, vui lòng liên hệ với bộ phận hỗ trợ.</p>"
                + "<p>Trân trọng,</p>"
                + "<p><strong>Đội ngũ hỗ trợ</strong></p>";

        sendHtmlMail(to, subject, htmlBody);
    }

    @Async
    public void sendVoucherUpdateMail(String username, String to, String voucherCode, String tenVoucher,
                                      String giaTriGiam, String thoiGianApDung, String thoiGianHetHan,
                                      String trangThai) {
        String subject = "🔔 Thông báo: Voucher của bạn đã được cập nhật";

        String htmlBody = "<p>Xin chào <strong>" + username + "</strong>,</p>"
                + "<p>Voucher của bạn với mã <strong style=\"color:red;\">" + voucherCode + "</strong> đã được cập nhật thành công. Dưới đây là thông tin mới:</p>"
                + "<ul>"
                + "<li><strong>Tên voucher:</strong> " + tenVoucher + "</li>"
                + "<li><strong>Giảm giá:</strong> " + giaTriGiam + "</li>"
                + "<li><strong>Thời gian áp dụng:</strong> " + thoiGianApDung + "</li>"
                + "<li><strong>Thời gian hết hạn:</strong> " + thoiGianHetHan + "</li>"
                + "<li><strong>Trạng thái:</strong> " + trangThai + "</li>"
                + "</ul>"
                + "<p>Chúng tôi hy vọng rằng các thay đổi này sẽ giúp bạn có trải nghiệm tốt hơn.</p>"
                + "<p>Nếu bạn có bất kỳ câu hỏi nào, vui lòng liên hệ với bộ phận hỗ trợ.</p>"
                + "<p>Trân trọng,</p>"
                + "<p><strong>Đội ngũ hỗ trợ</strong></p>";

        sendHtmlMail(to, subject, htmlBody);
    }

    @Async
    public void sendVoucherReceivedMail(String username, String to, String voucherCode, String tenVoucher,
                                        String giaTriGiam, String thoiGianApDung, String thoiGianHetHan) {
        String subject = "🎉 Bạn vừa nhận được một voucher mới!";

        String htmlBody = "<p>Xin chào <strong>" + username + "</strong>,</p>"
                + "<p>Chúc mừng! Bạn vừa nhận được một voucher mới từ hệ thống:</p>"
                + "<ul>"
                + "<li><strong>Mã voucher:</strong> <span style=\"color:red;\">" + voucherCode + "</span></li>"
                + "<li><strong>Tên voucher:</strong> " + tenVoucher + "</li>"
                + "<li><strong>Giá trị giảm:</strong> " + giaTriGiam + "</li>"
                + "<li><strong>Thời gian áp dụng:</strong> " + thoiGianApDung + "</li>"
                + "<li><strong>Thời gian hết hạn:</strong> " + thoiGianHetHan + "</li>"
                + "</ul>"
                + "<p>Hãy sử dụng voucher này trước khi hết hạn để nhận ưu đãi nhé!</p>"
                + "<p>Nếu bạn có bất kỳ câu hỏi nào, vui lòng liên hệ với bộ phận hỗ trợ.</p>"
                + "<p>Trân trọng,</p>"
                + "<p><strong>Đội ngũ hỗ trợ</strong></p>";

        sendHtmlMail(to, subject, htmlBody);
    }

    @Async
    public void sendBillStatus(String username,String to, String orderCode, String status) {
        String subject = "🎉 Đơn hàng của bạn đã được cập nhật!";

        String htmlBody = "<p>Xin chào <strong>" + username + "</strong>,</p>"
                + "<p>Chúng tôi xin thông báo rằng <strong>đơn hàng " + orderCode + "</strong> của bạn vừa được <strong>cập nhật trạng thái</strong>.</p>"
                + "<p><strong>Trạng thái mới:</strong> <span style='color: #28a745;'>" + status + "</span></p>"
                + "<p>Vui lòng kiểm tra chi tiết đơn hàng của bạn tại trang web của chúng tôi hoặc liên hệ bộ phận chăm sóc khách hàng nếu cần hỗ trợ thêm.</p>"
                + "<hr/>"
                + "<p>📞 Hotline: 0396798513</p>"
                + "<p>🌐 Website: <a href='http://localhost:5173/'>Men-TShirt.com</a></p>"
                + "<p>Cảm ơn bạn đã mua sắm tại <strong>Men T-shirt</strong>!</p>";

        sendHtmlMail(to,subject, htmlBody);
    }

    @Async
    public void totaled (HoaDon hoaDon) {

        List<HoaDonPhuongThucThanhToanResponse> hoaDonPhuongThucThanhToanList =
                hoaDonPhuongThucThanhToanRepository.getAllByIdHD(hoaDon.getId());

        BigDecimal tongTienThanhToan = hoaDonPhuongThucThanhToanList.stream()
                .map(HoaDonPhuongThucThanhToanResponse::getSoTienThanhToan)
                .reduce(BigDecimal.ZERO, BigDecimal::add);



        String subject = "🎉 Đơn hàng của bạn đã được thanh toán !";

        String htmlBody = "<p>Xin chào <strong>" + hoaDon.getHoTenNguoiNhan() + "</strong>,</p>"
                + "<p>Chúng tôi xin thông báo rằng <strong>đơn hàng " + hoaDon.getMaHoaDon() + "</strong> của bạn đã được <strong>thanh toán</strong>.</p>"
                + "<p><strong>Với số tiền:</strong> <span style='color: #28a745;'>" + tongTienThanhToan + "</span></p>"
                + "<p>Vui lòng kiểm tra chi tiết đơn hàng của bạn tại trang web của chúng tôi hoặc liên hệ bộ phận chăm sóc khách hàng nếu cần hỗ trợ thêm.</p>"
                + "<hr/>"
                + "<p>📞 Hotline: 0396798513</p>"
                + "<p>🌐 Website: <a http://localhost:5173/>Men-TShirt.com</a></p>"
                + "<p>Cảm ơn bạn đã mua sắm tại <strong>Men T-shirt</strong>!</p>";

        sendHtmlMail(hoaDon.getEmail(),subject, htmlBody);
    }
}
