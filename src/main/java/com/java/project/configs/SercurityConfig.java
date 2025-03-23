package com.java.project.configs;//package com.example.demo.config;

import lombok.experimental.NonFinal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import javax.crypto.spec.SecretKeySpec;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SercurityConfig {

    // Danh sách các endpoint công khai, không yêu cầu xác thực
    private final String [] PUBLIC_ENPOINTS = {"/api/auth/token", "/api/auth/introspect"
            ,"/api/auth/token/khach-hang", "/api/auth/introspect/khach-hang"
            , "/san-pham-chi-tiet/thuoc-tinh", "/san-pham-chi-tiet/chi-tiet"
            , "/san-pham", "/api/thuong-hieu", "/api/chat-lieu", "/api/co-ao"
            , "/api/kich-thuoc", "/api/mau-sac", "/api/tay-ao", "/api/xuat-xu"
            , "/api/khach-hang/myAccount", "/phieu-giam-gia"};

    @NonFinal
    protected static final String SIGN_KEY = ENVConfig.getEnv("JWT_SECRET");

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOriginPatterns(List.of("*")); // Cho phép tất cả nguồn )
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")); // Các method HTTP được phép
                    config.setAllowedHeaders(List.of("Authorization", "Content-Type")); // Cho phép gửi token
                    config.setAllowCredentials(true); // Cho phép gửi cookie, token
                    return config;
                }))
                .csrf(AbstractHttpConfigurer::disable) // Tắt CSRF để tránh chặn request từ trình duyệt
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/api/**").permitAll() // Xử lý preflight requests
                        .requestMatchers( PUBLIC_ENPOINTS).permitAll() // Các API public
                        .requestMatchers("/api/**").hasAuthority("ROLE_ADMIN") // Chỉ Admin mới truy cập
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );

        return httpSecurity.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
        //Convert từ SCOPE_ sang ROLE_
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return converter;
    }

    @Bean
    JwtDecoder jwtDecoder() {

        // Tạo khóa bí mật (SecretKeySpec) từ chuỗi SIGN_KEY và chỉ định thuật toán HS512
        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGN_KEY.getBytes(), "HS512");

        // Trả về một đối tượng JwtDecoder sử dụng NimbusJwtDecoder với khóa bí mật và thuật toán HMAC-SHA512
        return NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }
}
