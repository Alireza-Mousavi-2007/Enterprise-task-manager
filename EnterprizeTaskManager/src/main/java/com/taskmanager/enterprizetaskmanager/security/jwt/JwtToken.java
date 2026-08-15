package com.taskmanager.enterprizetaskmanager.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JwtToken {

    @Value("${jwt.security}")
    private String secretKey;
    private Algorithm algorithm;

    @PostConstruct
    public void init() {
        this.algorithm = Algorithm.HMAC256(secretKey);
    }


    public String tokenMaker(String user, List<String> authorities) {
        return JWT.create()
                .withSubject(user)
                .withClaim("authorities", authorities)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 5))
                .sign(algorithm);
    }

    public DecodedJWT tokenVerifier(String token) {
        var verifiedToken = JWT.require(algorithm).build();
        return verifiedToken.verify(token);
    }
}
