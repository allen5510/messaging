package com.example.demo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.controller.DTO.UserDTO;
import com.example.demo.exception.TokenException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenProcessor {
    private static final String SERIALNO = "serialno";
    private static final String ACCOUNT = "account";

    private static final long EXPIRE_DATE = 24 * 60 * 60 * 1000 *999 ;

    private static final String TOKEN_SECRET = "DeepenLin77543DeepenLin77543"; // token 密鑰

    public static String generateTokenAndThrow(UserDTO user) throws TokenException {
        try {
            Date date = new Date(System.currentTimeMillis()+EXPIRE_DATE); //過期時間
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET); //密鑰及加密算法
            //設置頭部訊息、類型、簽名用的算法
            Map<String,Object> header = new HashMap<>();
            header.put("typ","JWT");
            header.put("alg","HS256");
            // 訊息存入token，生成簽名
            String token = JWT.create()
                    .withHeader(header)
                    .withClaim(SERIALNO, user.getId())
                    .withClaim(ACCOUNT, user.getAccount())
                    .withExpiresAt(date)
                    .sign(algorithm);
            return token;
        } catch (Exception e) {
            throw new TokenException("Token generation failed:"+ e.getMessage());
        }
    }

    public static long parseUserIdAndThrow(String token) throws TokenException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            @SuppressWarnings("unused")
            DecodedJWT jwt = verifier.verify(token);
            long userId = jwt.getClaim(SERIALNO).asLong();
            String account = jwt.getClaim(ACCOUNT).asString();
            return userId;
        } catch (IllegalArgumentException e) {
            throw new TokenException("Token verification failed:"+ e.getMessage());
        } catch (JWTVerificationException e) {
            throw new TokenException("Token verification failed:"+ e.getMessage());
        }
    }
}
