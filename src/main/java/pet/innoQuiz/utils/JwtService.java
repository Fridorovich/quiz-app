package pet.innoQuiz.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pet.innoQuiz.model.dto.JwtDto;
import pet.innoQuiz.model.entity.User;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService {

    private static final Logger LOGGER = LogManager.getLogger(JwtService.class);
    @Value("8074658237c236e39e96e909ac1abb25a3e1773b100096ad6877c439cd452c17")
    private String jwtSecret;

    // Основной метод генерации токена
    public JwtDto generateAuthToken(User user) {
        JwtDto jwtDto = new JwtDto();
        jwtDto.setToken(generateJwtToken(user));
        jwtDto.setRefreshToken(generateRefreshToken(user));
        return jwtDto;
    }

    // Метод для обновления токена
    public JwtDto refreshBaseToken(User user, String refreshToken) {
        JwtDto jwtDto = new JwtDto();
        jwtDto.setToken(generateJwtToken(user));
        jwtDto.setRefreshToken(refreshToken);
        return jwtDto;
    }

    // Генерация JWT токена
    private String generateJwtToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("profileId", user.getProfile().getId());

        Date expirationDate = Date.from(
                LocalDateTime.now()
                        .plusYears(10)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
        );

        return Jwts.builder()
                .subject(user.getEmail())
                .claims(claims)
                .expiration(expirationDate)
                .signWith(getSigningKey())
                .compact();
    }

    // Генерация refresh токена
    private String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("profileId", user.getProfile().getId());

        Date expirationDate = Date.from(
                LocalDateTime.now()
                        .plusDays(1)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
        );

        return Jwts.builder()
                .subject(user.getEmail())
                .claims(claims)
                .expiration(expirationDate)
                .signWith(getSigningKey())
                .compact();
    }

    public String getEmailFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public Long getUserIdFromToken(String token) {
        return getClaimsFromToken(token).get("userId", Long.class);
    }

    public Long getProfileIdFromToken(String token) {
        return getClaimsFromToken(token).get("profileId", Long.class);
    }

    public boolean validateJwtToken(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            LOGGER.error("Expired JWT token", expEx);
        } catch (UnsupportedJwtException unsEx) {
            LOGGER.error("Unsupported JWT token", unsEx);
        } catch (MalformedJwtException malEx) {
            LOGGER.error("Invalid JWT token", malEx);
        } catch (SecurityException secEx) {
            LOGGER.error("Security exception", secEx);
        } catch (Exception e) {
            LOGGER.error("Invalid token", e);
        }
        return false;
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}