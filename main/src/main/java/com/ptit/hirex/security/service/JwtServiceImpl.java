package com.ptit.hirex.security.service;

import com.ptit.data.base.RedisToken;
import com.ptit.data.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.expiry-time-secs}")
    private long expiryTimeSecs;

    @Value("${jwt.access-key}")
    private String accessKey;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = extractRolesFromUserDetails(user);
        claims.put("roles", roles);
        String token = generateToken(claims, user);
        RedisToken redisToken = RedisToken.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .roles(roles)
                .loginTime(LocalDateTime.now().toString())
                .expireTime(LocalDateTime.now().plusSeconds(expiryTimeSecs).toString())
                .build();
        saveTokenInRedis(token, redisToken, expiryTimeSecs);
        return token;
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && isTokenInRedisValid(token));
    }

    private String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (expiryTimeSecs * 1000)))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessKey));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private List<String> extractRolesFromUserDetails(UserDetails userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    private boolean isTokenInRedisValid(String token) {
        RedisToken tokenStatus = (RedisToken) redisTemplate.opsForValue().get(token);
        return tokenStatus != null;
    }

    private void saveTokenInRedis(String token, RedisToken redisToken, long expirationTimeInSeconds) {
        redisTemplate.opsForValue().set(token, redisToken, expirationTimeInSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void invalidateToken(String token) {
        redisTemplate.delete(token);
    }
}
