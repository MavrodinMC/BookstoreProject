package com.mavro.jwt;

import com.mavro.exceptions.KeyException;
import com.mavro.services.impl.MyAppUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

import static io.jsonwebtoken.Jwts.parserBuilder;

@Service
public class JwtProvider {

    private KeyStore keyStore;

    @Value("${jwt.expiration.time}")
    private Long jwtExpirationInMillis;

    @PostConstruct
    public void init() {

        try {
            keyStore = KeyStore.getInstance("PKCS12");
            InputStream resourceAsStream = getClass().getResourceAsStream("/bookstore.jks");
            keyStore.load(resourceAsStream, "bookstore123".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new KeyException();
        }
    }

    public String generateToken(Authentication authentication) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        MyAppUserDetails principal = (MyAppUserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(principal.getUsername())
                .claim("role", authorities)
                .signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

    public String generateTokenWithUsername(String username) {

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

    public PublicKey getPublicKey() {

        try {
            return keyStore.getCertificate("bookstore").getPublicKey();
        } catch (KeyStoreException e) {
            throw new KeyException();
        }
    }

    private PrivateKey getPrivateKey() {

        try {
            return (PrivateKey) keyStore.getKey("bookstore", "bookstore123".toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new KeyException();
        }
    }

    public boolean validateToken(String jwt) {

        try {
            parserBuilder().setSigningKey(getPublicKey()).build().parseClaimsJws(jwt);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("The token has expired");
        }

        return false;
    }


    public String getUsernameFromJwt(String token) {

        Claims claims = parserBuilder()
                .setSigningKey(getPublicKey())
                .build().parseClaimsJws(token)
                .getBody();

        return claims.getSubject();

    }

    public Long getJwtExpirationInMillis() {
        return jwtExpirationInMillis;
    }

}
