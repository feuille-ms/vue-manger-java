package com.example.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@ConfigurationProperties(prefix = "vuemanager.jwt")
@Component
public class JwtUtils {

    private long expire;
    private String secret;
    private String header;

    //生成jwt
    public String generateToken(String username) {
        System.out.println("generateToken...");
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);

        return Jwts.builder()
                .setHeaderParam("type","JWT")
                .setSubject(username)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    //解析jwt
    public Claims getClaimByToken(String jwt) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwt)
                    .getBody();
        }catch (Exception e) {
            return null;
        }
    }

    //检查jwt是否过期
    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}
