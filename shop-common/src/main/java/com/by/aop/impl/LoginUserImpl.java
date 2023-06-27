package com.by.aop.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.by.aop.annotation.LoginUser;
import com.by.entity.User;
import com.by.utils.JWTUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
@Aspect // 标识这是一个切面
public class LoginUserImpl {

    @Resource
    private HttpServletRequest request;

    // 在加了loginUser注解的方法之前执行
    @Around("@annotation(loginUser)")
    public Object loginUser(ProceedingJoinPoint point, LoginUser loginUser) throws Exception {

        User user; // 封装的登录对象

        // 1、获取token
        String token = request.getHeader("Authorization");

        // 如果token从请求头中没有取到，检查地址栏是否存在
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter("token");
        }

        Object[] args = point.getArgs(); // 因为方法有多个形参

        if (!StringUtils.isEmpty(token)) {

            // 2、解析token
            DecodedJWT verify = JWTUtils.verify(token);

            // 3、从token中获取uid
            String id = verify.getClaim("id").asString();

            // 4、把uid封装到User对象中
            user = new User();
            user.setId(Integer.parseInt(id));

            // 5、传递到目标方法的形参（因为要修改目标方法的参数，所以只能用环绕完成）

            //  循环遍历数组，找到user的参数
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null && args[i].getClass() == User.class) {
                    args[i] = user;
                    break; // 替换完成就马上返回
                }
            }
        }

        try {
            // 放行
            return point.proceed(args);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

}
