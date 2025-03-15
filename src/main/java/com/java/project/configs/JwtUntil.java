package com.java.project.configs;//package com.example.demo.config;
//
//import io.jsonwebtoken.*;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
//@Component
//public class JwtUntil {
//    @Value("${jwt.secret}")
//    private String SECRET_KEY;
//
//    @Value("${jwt.expiration}")
//    private long EXPIRATION_TIME;
//
//    public String generateToken(String userName, String role) {
//        return Jwts.builder()
//                .setSubject(userName)
//                .claim("role", role)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                .compact();
//    }
//
//    public Claims extractClaims(String token) {
//        try {
//            return Jwts.parser()
//                    .setSigningKey(SECRET_KEY)
//                    .parseClaimsJws(token)
//                    .getBody();
//        } catch (ExpiredJwtException e) {
//            throw new RuntimeException("Token has expired", e);
//        } catch (MalformedJwtException e) {
//            throw new RuntimeException("Invalid JWT token", e);
//        } catch (SignatureException e) {
//            throw new RuntimeException("Invalid JWT signature", e);
//        } catch (IllegalArgumentException e) {
//            throw new RuntimeException("Token is null or empty", e);
//        }
//    }
//
//    public String extractUserName(String token) {
//        return extractClaims(token).getSubject();
//    }
//
//    public String extractRole(String token) {
//        return extractClaims(token).get("role", String.class);
//    }
//
//    public boolean isTokenExpired(String token) {
//        return extractClaims(token).getExpiration().before(new Date());
//    }
//}
