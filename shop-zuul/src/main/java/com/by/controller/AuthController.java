package com.by.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.by.entity.ResultEntity;
import com.by.utils.JWTUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @RequestMapping("/getUserByToken")
    public ResultEntity<?> getUserByToken(@RequestHeader(name = "Authorization", required = false)
                                          String token) {

        // 1、校验
        DecodedJWT decodedJWT = JWTUtils.verify(token);

        // 2、从Token中获取用户信息
        String username = decodedJWT.getClaim("username").asString();
        String id = decodedJWT.getClaim("id").asString();
        String sex = decodedJWT.getClaim("sex").asString();
        String email = decodedJWT.getClaim("email").asString();
        String phone = decodedJWT.getClaim("phone").asString();
        String age = decodedJWT.getClaim("age").asString();
        String birthday = decodedJWT.getClaim("birthday").asString();
        String upng = decodedJWT.getClaim("upng").asString();

        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("sex", sex);
        map.put("email", email);
        map.put("phone", phone);
        map.put("age", age);
        map.put("birthday", birthday);
        map.put("id", id);
        map.put("upng", upng);

        // 3、把用户信息响应给浏览器
        return ResultEntity.success(map);
    }

}
