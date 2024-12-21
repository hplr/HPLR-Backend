package org.hplr.library.infrastructure.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hplr.library.exception.HPLRAccessDeniedException;
import org.hplr.user.core.model.vo.AdministratorRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;
import java.util.UUID;

@Component
@Slf4j
@Getter
public class AccessValidator {

    @Value("${jwt.secret}")
    private String secret;

    public Boolean validateUserAccess(HttpServletRequest request, UUID userId){
        String token = getAuthHeader(request);
        Claims claims = getAllClaims(token);
        UUID requestingUserId = UUID.fromString(String.valueOf(claims.get("id")));
        return validateToken(claims) && userId.equals(requestingUserId);

    }
    public Boolean validateAdministratorAccess(HttpServletRequest request, AdministratorRole administratorRole){
        String token = getAuthHeader(request);
        Claims claims = getAllClaims(token);
        return(claims.get("permissions").toString().contains(administratorRole.name()));
    }

    public UUID getUserId(HttpServletRequest request){
        String token = getAuthHeader(request);
        Claims claims = getAllClaims(token);
        validateToken(claims);
        return UUID.fromString(String.valueOf(claims.get("id")));
    }


    private static Boolean validateToken(Claims claims){
        long epochTime = Long.parseLong(String.valueOf(claims.get("exp")));
        LocalDateTime expTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(epochTime),
                        TimeZone.getDefault().toZoneId());
        return expTime.isAfter(LocalDateTime.now());

    }


    private static String getAuthHeader(HttpServletRequest request) {
        if(request.getHeader("Authorization") == null){
            throw new HPLRAccessDeniedException("No token provided");
        }
        return request.getHeaders("Authorization").nextElement();
    }

    private Claims getAllClaims(String token) {
        if(token.contains("bearer:")){
            token=token.substring(7);
        }

        Claims claims;
        try{
            claims = Jwts.parserBuilder().setSigningKey(secret.getBytes()).build().parseClaimsJws(token).getBody();
        }
        catch (ExpiredJwtException e){
            throw new HPLRAccessDeniedException(e.getMessage());
        }
        return claims;
    }
}
