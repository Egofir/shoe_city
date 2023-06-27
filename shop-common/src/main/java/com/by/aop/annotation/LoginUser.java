package com.by.aop.annotation;

import java.lang.annotation.*;

@Documented // 生成API文档
@Target({ElementType.METHOD}) // 注解可以添加到哪个元素上
@Retention(RetentionPolicy.RUNTIME) // 注解的作用域
public @interface LoginUser {
}
