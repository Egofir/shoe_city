package com.by.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.by.aop.annotation.LoginUser;
import com.by.constants.ShopConstants;
import com.by.entity.*;
import com.by.feign.api.ICarService;
import com.by.feign.api.IUserService;
import com.by.utils.JWTUtils;
import com.by.utils.PasswordUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/ssoUserController")
public class SSOUserController {

    @Resource
    private IUserService userService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private ICarService carService;

    @PostConstruct
    public void init() {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
    }


    @RequestMapping("/validUsername")
    public Map<String, Object> validUsername(@RequestParam("username") String username) {

        // 1、查询用户是否存在
        User user = userService.getUserByUsername(username);

        Map<String, Object> map = new HashMap<>();

        // 2、判断用户是否为空
        if (user != null) {
            map.put("status", "n"); // 用户名存在
            map.put("info", "用户名已存在");
        } else {
            map.put("status", "y"); // 用户名不存在
            map.put("info", "用户名可以使用");
        }

        return map;
    }

    @RequestMapping("/validEmail")
    public Map<String, Object> validEmail(@RequestParam("emailStr") String emailStr) {

        // 1、判断邮箱是否被注册
        User user = userService.getUserByEmail(emailStr);

        Map<String, Object> map = new HashMap<>();

        // 2、判断
        if (user != null) {
            map.put("status", "n");
            map.put("info", "邮箱被注册");
        } else {
            map.put("status", "y");
            map.put("info", "邮箱可以使用");
        }

        return map;
    }

    @RequestMapping("/sendEmail")
    public String sendEmail(String emailStr) {

        // 生成随机验证码
        String code = RandomStringUtils.random(6, false, true);

        // 把验证码保持进redis，并和邮箱绑定
        stringRedisTemplate.opsForValue().set(ShopConstants.SSO_REGISTER_KEY + emailStr, code,
                5, TimeUnit.MINUTES);

        // 1、创建邮箱对象
        Email email = new Email();
        email.setTitle("鞋城新用户注册");
        email.setContent("您的验证码为：" + code);
        email.setToUser(emailStr);

        // 2、调用发送邮件服务，异步形式
        rabbitTemplate.convertAndSend(ShopConstants.EMAIL_EXCHANGE, ShopConstants.EMAIL_ROUTING_KEY,
                email);

        // 3、业务处理
        return "ok";
    }

    @RequestMapping("/registerUser")
    public ResultEntity<?> registerUser(User user, String code) {

        // 1、从redis中查询验证码
        String redisCode = stringRedisTemplate.opsForValue().get(ShopConstants.SSO_REGISTER_KEY +
                user.getEmail());

        // 2、判断是否为空
        if (redisCode == null) {
            throw new ShopException(10002, "验证码已失效");
        }

        // 3、对比验证码
        if (!redisCode.equals(code)) {
            throw new ShopException(10003, "验证码不正确");
        }

        // 4、密码加密
        user.setPassword(PasswordUtils.encode(user.getPassword()));

        // 5、把新用户添加到数据库
        userService.addUser(user);

        // 6、注册成功
        return ResultEntity.success();
    }

    @RequestMapping("/getUserByUsername")
    public ResultEntity<?> getUserByUsername(String username) {

        // 1、查询用户名是否存在
        User user = userService.getUserByUsername(username);

        // 2、判断用户是否存在
        if (user == null) {
            return ResultEntity.error("该用户名未注册");
        }

        // 3、创建Map保存id和用户名
        Map<String, String> payLoadMap = new HashMap<>();
        payLoadMap.put("id", user.getId().toString());
        payLoadMap.put("username", user.getUsername());

        // 4、把id和用户名揉进token
        String token = JWTUtils.createToken(payLoadMap, 5); // token超时时间是5分钟
        System.out.println(token);

        // 5、给用户邮箱发送修改密码链接
        Email email = new Email();
        email.setTitle("鞋城用户密码修改");
        email.setContent("点击<a href = 'https://mail.qq.com?token=" + token + "'>这里</a>修改密码");
        email.setToUser(user.getEmail());

        // 6、调用邮件服务发送邮件
        rabbitTemplate.convertAndSend(ShopConstants.EMAIL_EXCHANGE, ShopConstants.EMAIL_ROUTING_KEY,
                email);

        // 7、获取用户邮箱首页地址
        String email1 = user.getEmail();
        String ehome = "mail." + email1.substring(email1.lastIndexOf("@") + 1);

        String eurl = email1.replaceAll(email1.substring(0, email1.lastIndexOf("@") - 4),
                "*****");

        Map<String, String> map = new HashMap<>();
        map.put("ehome", ehome);
        map.put("eurl", eurl);

        // 8、提示用户修改密码链接已发送至邮箱，请登录查看
        return ResultEntity.success(map);
    }

    @RequestMapping("/updateUserPassword")
    public ResultEntity<?> updateUserPassword(String token, String password) {

        // 1、判断token是否为空
        if (StringUtils.isEmpty(token)) {
            return ResultEntity.error("token不能为空");
        }

        // 2、校验token的合法性
        DecodedJWT decodedJWT = JWTUtils.checkToken(token);

        // 3、从token获取用户id
        String uid = decodedJWT.getClaim("id").asString();

        // 4、修改用户密码
        User user = new User();
        user.setId(Integer.parseInt(uid));
        user.setPassword(PasswordUtils.encode(password)); // 密码加密
        return userService.updateUser(user);
    }

    @RequestMapping("/loginUser")
    public ResultEntity<?> loginUser(@CookieValue(name = ShopConstants.ANON_ID, required = false)
                                         String anonId, String username, String password) {

        // 1、根据用户名查询对象
        User user = userService.getUserByUsername(username);

        // 2、判断用户是否为空
        if (user == null) {
            return ResultEntity.error("用户名不存在");
        }

        // 3、密码比对
        if (!PasswordUtils.checkpw(password, user.getPassword())) {
            return ResultEntity.error("用户名或密码错误");
        }

        // 4、登录成功
        Map<String, String> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("id", user.getId().toString());
        if (user.getSex() != null) {
            map.put("sex", user.getSex().toString());
        }
        if (user.getEmail() != null) {
            map.put("email", user.getEmail());
        }
        if (user.getPhone() != null) {
            map.put("phone", user.getPhone());
        }
        if (user.getAge() != null) {
            map.put("age", user.getAge().toString());
        }
        if (user.getBirthday() != null) {
            map.put("birthday", user.getBirthday().toString());

        }
        if (user.getUpng() != null) {
            map.put("upng", user.getUpng());
        }
        String token = JWTUtils.createToken(map, 60 * 24 * 7); // 用户标识

        // 合并购物车
        if (!StringUtils.isEmpty(anonId)) {
            // 获取用户在redis中的购物车数据

            // 根据匿名id查询商品id
            Set<Object> keys = redisTemplate.opsForHash().keys(anonId);

            if (keys != null && !keys.isEmpty()) {
                for (Object gid : keys) {

                    // 根据匿名id和商品id查询商品数量
                    Object count = redisTemplate.opsForHash().get(anonId, gid);

                    // 把redis中的购物车添加到MySQL
                    Car car = new Car();
                    car.setUid(user.getId());
                    car.setGid(Integer.parseInt(gid.toString()));
                    car.setCount(Integer.parseInt(count.toString()));

                    // 调用购物车服务的接口添加到MySQL中
                    carService.addCarMySQL(car);
                }
                redisTemplate.delete(anonId);
            }

        }

        // 5、响应给用户
        return ResultEntity.success(token);
    }

    @RequestMapping("/updateUserPng")
    @LoginUser
    public ResultEntity<?> updateUserPng(User user, String png) {

        if (user.getId() == null) {
            return ResultEntity.error("用户未登录");
        }

        User user1 = userService.getUserById(user.getId());
        user1.setUpng(png);

        userService.updateUser(user1);
        return ResultEntity.success();

    }

}
