package com.by.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.by.constants.ShopConstants;
import com.by.entity.ShopException;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;

public class JWTUtils {

    /**
     * 生成token
     * @param payload 设置给token的数据
     * @return token
     */
    public static String createToken(Map<String, String> payload, Integer time) {

        // 1、创建JWTBuilder
        JWTCreator.Builder builder = JWT.create();

        // 2、设置Token有效时间
        Calendar calendar = Calendar.getInstance();
        if (time == null) {
            calendar.add(Calendar.MINUTE, 30); // 超时时间30分钟
        } else {
            calendar.add(Calendar.MINUTE, time);
        }

        // 3、设置负载（用户数据可以放在负载）
        Set<Map.Entry<String, String>> entries = payload.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            builder.withClaim(entry.getKey(), entry.getValue());
        }

        return builder
                .withExpiresAt(calendar.getTime()) // 过期时间
                .sign(Algorithm.HMAC256(ShopConstants.JWT_SIGN));
    }

    // 校验token
    public static DecodedJWT verify(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(ShopConstants.JWT_SIGN)).build();
        return jwtVerifier.verify(token);
    }

    // 校验token是否合法
    public static DecodedJWT checkToken(String token) throws ShopException {
        try {
            return verify(token);
        } catch (SignatureVerificationException e) {
            throw new ShopException(10007, "签名不一致异常");
        } catch (TokenExpiredException e) {
            throw new ShopException(10008, "令牌过期异常");
        } catch (Exception e) {
            throw new ShopException(10009, "token认证失败");
        }
    }

    // 获取token中的数据
    public static String getWithClaim(String token, String key) {
        try {
            DecodedJWT verify = verify(token);
            return verify.getClaim(key).toString();
        } catch (Exception e) {
            throw new ShopException(10010, "签名异常");
        }
    }
}
