package com.order.service.util;

import com.order.service.constants.ApiConstant;
import com.order.service.dto.TokenResDto;
import com.order.service.exception.GenericException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@Log4j2
public class JwtTokenProvider {
    private static final Integer REFRESH_EXPIRY_IN_MINUTES = 60;
    private static final Integer ACCESS_EXPIRY_IN_MINUTES = 10;
    private static final String JWT_SECRET = "b2857b9a29cf4fc990ec58e7f23f98cf7577e36b1cd74ecda34ceb575bc5bec2";

    public TokenResDto createToken(String userId) {
        String tokenId = UUID.randomUUID().toString();
        return TokenResDto.builder().accessToken(generateAccessToken(userId, tokenId)).refreshToken(generateRefreshToken(userId, tokenId)).build();
    }

    private String generateAccessToken(String userId, String tokenId) {
        Map<String, String> claims = new HashMap<>();
        claims.put(ApiConstant.TOKEN_TYPE, ApiConstant.ACCESS_TOKEN);
        claims.put(ApiConstant.TOKEN_ID, tokenId);
        claims.put(ApiConstant.USER_ID, userId);
        Date currentDate = new Date();
        return Jwts.builder()
                .claims(claims)
                .subject(userId)
                .issuedAt(currentDate)
                .expiration(new Date(currentDate.getTime() + TimeUnit.MINUTES.toMillis(ACCESS_EXPIRY_IN_MINUTES)))
                .id(tokenId)
                .issuer(ApiConstant.TOKEN_ISSUER)
                .signWith(key())
                .compact();
    }

    private String generateRefreshToken(String userId, String tokenId) {
        Map<String, String> claims = new HashMap<>();
        claims.put(ApiConstant.TOKEN_TYPE, ApiConstant.REFRESH_TOKEN);
        claims.put(ApiConstant.TOKEN_ID, tokenId);
        claims.put(ApiConstant.USER_ID, userId);
        Date currentDate = new Date();
        return Jwts.builder()
                .claims(claims)
                .subject(userId)
                .issuedAt(currentDate)
                .expiration(new Date(currentDate.getTime() + TimeUnit.MINUTES.toMillis(REFRESH_EXPIRY_IN_MINUTES)))
                .id(tokenId)
                .issuer(ApiConstant.TOKEN_ISSUER)
                .signWith(key())
                .compact();
    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_SECRET));
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUserId(String token) {
        return extractAllClaims(token).getSubject();
    }


    public String getJwtTokenId(String token) {
        return extractAllClaims(token).getId();
    }

    public Boolean isAccessToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get(ApiConstant.TOKEN_TYPE).equals(ApiConstant.ACCESS_TOKEN);
    }

    public Boolean isRefreshToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get(ApiConstant.TOKEN_TYPE).equals(ApiConstant.REFRESH_TOKEN);
    }

    public Boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            log.error(e);
            throw new GenericException(HttpStatus.FORBIDDEN.value(), "Invalid or expired jwt token");
        }
    }
}
