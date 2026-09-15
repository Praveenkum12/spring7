package com.jimmy.portal.security.util;

import com.jimmy.portal.auth.entity.JobPortalUser;
import com.jimmy.portal.constant.ApplicationConstant;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final Environment env;

    public String generateJwtToken(Authentication authentication) {
        JobPortalUser existingUser = (JobPortalUser) authentication.getPrincipal();
        var secret = env.getProperty(ApplicationConstant.JWT_SECRET_KEY, ApplicationConstant.JWT_SECRET_DEFAULT_VALUE);
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .issuer("Job Portal")
                .setSubject("JWT Token")
                .claim("username", existingUser.getName())
                .claim("user_id", existingUser.getId())
                .claim(
                        "role",
                        existingUser.getRole().getName()
                )
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .signWith(key)
                .compact();
    }

}
