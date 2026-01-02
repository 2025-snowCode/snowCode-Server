package snowcode.snowcode.auth.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import snowcode.snowcode.auth.dto.login.CustomUserInfoDto;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    private final Key key;
    private final long accessTokenExpTime;
    private final long refreshTokenExpTime;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-expiration}") long accessTokenExpTime,
            @Value("${jwt.refresh-expiration}") long refreshTokenExpTime
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
        this.refreshTokenExpTime = refreshTokenExpTime;
    }


    /**
     * Access Token 생성
     * @param member
     * @return Access Token String
     */
    public String createAccessToken(CustomUserInfoDto member) {
        return createToken(member, accessTokenExpTime);
    }

    public String createRefreshToken(CustomUserInfoDto member) {
        return createToken(member, refreshTokenExpTime);
    }


    /**
     * JWT 생성
     * @param member
     * @param expTime
     * @return JWT String
     */
    private String createToken(CustomUserInfoDto member, long expTime) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime expiry = now.plusSeconds(expTime);

        return Jwts.builder()
                .setSubject(member.username().toString())
                .claim("provider", member.provider())
                .claim("role", member.role())
                .claim("name", member.name())
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(expiry.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Token에서 User ID 추출
     * @param token
     * @return User ID
     */
    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * JWT 검증
     * @param token
     * @return IsValidate
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    /**
     * JWT Claims 추출
     * @param accessToken
     * @return JWT Claims
     */
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
