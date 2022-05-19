package com.inst.testprogectinst.security.jwt;

import com.inst.testprogectinst.entity.User;
import com.inst.testprogectinst.security.SecurityConst;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenProvider {

    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plusMinutes(SecurityConst.EXPIRATION_TIME);

        Map<String, Object> claimsMap = Map.of("id", user.getId().toString(),
                "login", user.getLogin(),
                "firstname", user.getFirstname(),
                "lastname", user.getLastname());

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .addClaims(claimsMap)
                .setIssuedAt(Date.valueOf(now.toLocalDate()))
                .setExpiration(Date.valueOf(expiryDate.toLocalDate()))
                .signWith(SignatureAlgorithm.HS512, SecurityConst.SECRET)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SecurityConst.SECRET)
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException |
                MalformedJwtException |
                ExpiredJwtException |
                UnsupportedJwtException |
                IllegalArgumentException exception) {
            log.error(exception.getMessage());
            return false;
        }
    }

    public String getUserId(String token) {
        return (String) Jwts.parser()
                .setSigningKey(SecurityConst.SECRET)
                .parseClaimsJws(token)
                .getBody()
                .get("id");
    }
}
