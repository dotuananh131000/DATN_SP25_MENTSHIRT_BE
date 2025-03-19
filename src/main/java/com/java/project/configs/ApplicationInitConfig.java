package com.java.project.configs;

import com.java.project.entities.NhanVien;
import com.java.project.entities.VaiTro;
import com.java.project.repositories.NhanVienRepository;
import com.java.project.repositories.VaiTroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationInitConfig {

    private static final Logger log = LoggerFactory.getLogger(ApplicationInitConfig.class);
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    VaiTroRepository vaiTroRepository;
    @Bean
    ApplicationRunner applicationRunner(NhanVienRepository nhanVienRepository){
        return args -> {
        VaiTro vaiTro = vaiTroRepository.findById(1)
                .orElseThrow(()-> new EntityNotFoundException("Không tìm thâ vai trò"));

            if(nhanVienRepository.findByEmail("admin@gmail.com").isEmpty()){
                NhanVien nhanVien = new NhanVien();
                nhanVien.setMaNhanVien("admin");
                nhanVien.setSoDienThoai("0377595723");
                nhanVien.setTenDangNhap("admin");
                nhanVien.setTenNhanVien("Admin");
                nhanVien.setEmail("admin@gmail.com");
                nhanVien.setMat_khau(passwordEncoder.encode("Admin123@"));
                nhanVien.setVaiTro(vaiTro);
                nhanVien.setTrangThai(1);
                nhanVien.setAvatarUrl("gfgfgfg");
                nhanVienRepository.save(nhanVien);
                log.warn("Admin has been create");
            }
        };
    }
}
