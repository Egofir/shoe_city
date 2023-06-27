package com.by.shopcar;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = "com.by")
@EnableEurekaClient
@MapperScan(basePackages = "com.by.mapper")
public class ShopCarApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopCarApplication.class, args);
	}

}
