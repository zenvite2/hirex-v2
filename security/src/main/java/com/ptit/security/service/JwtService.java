package com.ptit.security.service;

import com.ptit.hirex.constant.RedisPrefixKeyConstant;
import com.ptit.hirex.enums.TokenTypeEnum;
import com.ptit.security.config.UserDetailsImpl;
import com.ptit.security.util.Util;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
//    private final JedisPool jedisPool;
    private final UserDetailsService userDetailsService;

    @Value("${spring.jwt.access.secret}")
    private String jwtAccessSecret;

    @Value("${spring.jwt.access.expire}")
    private Long jwtAccessExpiration; // milliseconds unit

    @Value("${spring.jwt.refresh.secret}")
    private String jwtRefreshSecret;

    @Value("${spring.jwt.refresh.expire}")
    private Long jwtRefreshExpiration; // milliseconds unit

    public String generateAccessToken(@NotNull Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        return generateAccessToken(user.getAccount().getId());
    }

    /***
     * Generate JWT access token by account id and save to redis
     * @param accountId
     * @return
     */
    public String generateAccessToken(String accountId) {
        SecretKey secretKey = getSigningKey(TokenTypeEnum.ACCESS_TOKEN);
        Date now = new Date();
        Date expDate = new Date(now.getTime() + jwtAccessExpiration);

        String accessToken = Jwts.builder()
                .subject(accountId)
                .signWith(secretKey)
                .issuedAt(now)
                .expiration(expDate)
                .compact();
//
//        try (Jedis jedis = jedisPool.getResource()) {
//            String key = Util.generateRedisKey(RedisPrefixKeyConstant.TOKEN, accessToken);
//            jedis.set(key, accountId);
//            jedis.expire(key, jwtAccessExpiration / 1000);
//        } catch (Exception e) {
//            log.error("Save access token to redis failed: " + e.getMessage());
//        }

        return accessToken;
    }

    public String generateRefreshToken(@NotNull Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        return generateRefreshToken(user.getAccount().getId());
    }

    /***
     * Generate JWT refresh token by account id and save to redis
     * @param accountId
     * @return
     */
    public String generateRefreshToken(String accountId) {
        SecretKey secretKey = getSigningKey(TokenTypeEnum.REFRESH_TOKEN);
        Date now = new Date();
        Date expDate = new Date(now.getTime() + jwtRefreshExpiration);

        String refreshToken = Jwts.builder()
                .subject(accountId)
                .signWith(secretKey)
                .issuedAt(now)
                .expiration(expDate)
                .compact();

//        try (Jedis jedis = jedisPool.getResource()) {
//            String key = Util.generateRedisKey(RedisPrefixKeyConstant.REFRESH_TOKEN, refreshToken);
//            jedis.set(key, accountId);
//            jedis.expire(key, jwtRefreshExpiration / 1000);
//        } catch (Exception e) {
//            log.error("Save refresh token to redis failed: ", e);
//        }

        return refreshToken;
    }

//    public Optional<Jws<Claims>> validateAccessToken(String token) {
//        try (Jedis jedis = jedisPool.getResource()) {
//            String userIdJwtRedis = jedis.get(Util.generateRedisKey(RedisPrefixKeyConstant.TOKEN, token));
//
//            SecretKey secretKey = getSigningKey(TokenTypeEnum.ACCESS_TOKEN);
//
//            Jws<Claims> jws = Jwts.parser()
//                    .verifyWith(secretKey)
//                    .build()
//                    .parseSignedClaims(token);
//
//            String userId = jws.getPayload().getSubject();
//            if (!Util.isNullOrEmpty(userIdJwtRedis) && !Util.isNullOrEmpty(userId) && userIdJwtRedis.equals(userId)) {
//                return Optional.of(jws);
//            }
//        } catch (ExpiredJwtException e) {
//            log.error("Request to parse expired JWT: {} failed: {}", token, e.getMessage());
//        } catch (UnsupportedJwtException e) {
//            log.error("Request to parse unsupported JWT: {} failed: {}", token, e.getMessage());
//        } catch (MalformedJwtException e) {
//            log.error("Request to parse invalid JWT: {} failed: {}", token, e.getMessage());
//        } catch (SignatureException e) {
//            log.error("Request to parse JWT with invalid signature: {} failed: {}", token, e.getMessage());
//        } catch (IllegalArgumentException e) {
//            log.error("Request to parse empty or null JWT: {} failed: {}", token, e.getMessage());
//        }
//        return Optional.empty();
//    }

//    public String getUserIdFromRefreshToken(String token) {
//        try (Jedis jedis = jedisPool.getResource()) {
//            return jedis.get(Util.generateRedisKey(RedisPrefixKeyConstant.REFRESH_TOKEN, token));
//        } catch (Exception e) {
//            log.error("Get user id by jwt from redis failed: ", e);
//            return null;
//        }
//    }

    private SecretKey getSigningKey(TokenTypeEnum tokenType) {
        switch (tokenType) {
            case ACCESS_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.jwtAccessSecret));
            }
            case REFRESH_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.jwtRefreshSecret));
            }
            default -> throw new RuntimeException();
        }
    }
}
