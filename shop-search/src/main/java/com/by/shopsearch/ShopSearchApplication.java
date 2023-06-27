package com.by.shopsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.by", exclude = DataSourceAutoConfiguration.class)
@EnableEurekaClient
@EnableFeignClients("com.by.feign.api")
public class ShopSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopSearchApplication.class, args);
	}

}
