package com.project.fitness.security;

import com.project.fitness.model.User;
import com.project.fitness.model.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private String jwtSecret = "gPjP2D7+O6o8QYw6qQe6QvK5S2kJvX7e2v5hKz8k8lM=";
    private Integer jwtExpiration = 172800000 ;

    public String getJwtFromHeader(HttpServletRequest httpServletRequest) {

        Enumeration<String> names = httpServletRequest.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            System.out.println(name + " = " + httpServletRequest.getHeader(name));
        }

        String beareToken = httpServletRequest.getHeader("Authorization");
        if(beareToken != null && beareToken.startsWith("Bearer ")) return beareToken.substring(7);
        return null;
    }

    public String generateJwtToken(User user, String userRole) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("roles", List.of(userRole))
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpiration))
                .signWith(Key())
                .compact();
    }

    private Key Key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwt(String jwt) {
        try {
            Jwts.parser().verifyWith((SecretKey) Key()).build().parseSignedClaims(jwt);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return true;
    }

    public String getuserId(String jwt) {
        return Jwts.parser().verifyWith((SecretKey) Key()).build().parseSignedClaims(jwt).getPayload().getSubject();
    }

    public Claims getAllClaims(String jwt) {
        return Jwts.parser().verifyWith((SecretKey) Key()).build().parseSignedClaims(jwt).getPayload();
    }
}
