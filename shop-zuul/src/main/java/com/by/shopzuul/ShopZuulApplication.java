package com.by.shopzuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication(scanBasePackages = "com.by", exclude = DataSourceAutoConfiguration.class)
@EnableEurekaClient
@EnableZuulProxy
public class ShopZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopZuulApplication.class, args);
	}

}
