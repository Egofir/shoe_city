package com.by.shopgoods;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = "com.by")
@EnableEurekaClient
@MapperScan(basePackages = "com.by.mapper")
public class ShopGoodsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopGoodsApplication.class, args);
	}

}
