package ru.semen.springcourse.FirstSecurityApp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {

    @Value("${jwt_secret}")//внедрение ключа из файла(application.properties)
    private String secret;

    public String generateToken(String username) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

        return JWT.create()//создаем jwt токен
                .withSubject("User details")//данные пользователя
                .withClaim("username", username)//пара ключ значение
                .withIssuedAt(new Date())//текушее время создание токена
                .withIssuer("semen")//кто выдал токен(название приложение)
                .withExpiresAt(expirationDate)//срок годности токена
                .sign(Algorithm.HMAC256(secret));//генерация секретной ключа
    }

    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("semen")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }
}
