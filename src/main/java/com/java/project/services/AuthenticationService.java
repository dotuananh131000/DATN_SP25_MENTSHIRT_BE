package com.java.project.services;

import com.java.project.configs.ENVConfig;
import com.java.project.dtos.AuthenTicationResponse;
import com.java.project.dtos.IntrospceResponse;
import com.java.project.entities.KhachHang;
import com.java.project.entities.NhanVien;
import com.java.project.repositories.KhachHangRepository;
import com.java.project.repositories.NhanVienRepository;
import com.java.project.request.AuthenticationRequest;
import com.java.project.request.IntrospecRequest;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    @NonFinal
    protected static final String SIGN_KEY = ENVConfig.getEnv("JWT_SECRET");

    NhanVienRepository nhanVienRepository;

    KhachHangRepository khachHangRepository;

    public IntrospceResponse introspce(IntrospecRequest introspecRequest) throws JOSEException, ParseException {
        String token = introspecRequest.getToken();

        JWSVerifier verifier = new MACVerifier(SIGN_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiraTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);
        signedJWT.verify(verifier);

        return IntrospceResponse.builder()
                .valid(verified && expiraTime.after(new Date()))
                .build();
    }

    public AuthenTicationResponse getAuthenTication(AuthenticationRequest authenticationRequest) {
        var nhanVien = nhanVienRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(()-> new RuntimeException("Email not found"));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), nhanVien.getMat_khau());

        if(!authenticated) {
            throw new RuntimeException("Invalid password");
        }
        return AuthenTicationResponse.builder()
                .token(generateToken(nhanVien))
                .authenTicated(authenticated)
                .build();
    }

    public String generateToken(NhanVien nhanVien) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(nhanVien.getEmail())
                .issuer("Men-TShirt")
                .issueTime(new Date())
                .claim("scope", nhanVien.getVaiTro().getMaVaiTro())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli()))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try{
            jwsObject.sign(new MACSigner(SIGN_KEY.getBytes()));
            return jwsObject.serialize();
        }catch (Exception e){
            log.error("Cannot sign JWT", e);
            throw  new RuntimeException(e);
        }

    }

    public AuthenTicationResponse getAuthenTicationCustomer(AuthenticationRequest authenticationRequest) {
        var khachHang = khachHangRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(()-> new RuntimeException("Email not found"));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), khachHang.getMat_khau());

        if(!authenticated) {
            throw new RuntimeException("Invalid password");
        }
        return AuthenTicationResponse.builder()
                .token(generateTokenCustomer(khachHang))
                .authenTicated(authenticated)
                .build();
    }

    public String generateTokenCustomer(KhachHang khachHang) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(khachHang.getEmail())
                .issuer("Men-TShirt")
                .issueTime(new Date())
                .claim("scope", "CLIENT")
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli()))
                .build();

        log.info(jwtClaimsSet.getClaim("scope").toString());

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try{
            jwsObject.sign(new MACSigner(SIGN_KEY.getBytes()));
            return jwsObject.serialize();
        }catch (Exception e){
            log.error("Cannot sign JWT", e);
            throw  new RuntimeException(e);
        }

    }

}
