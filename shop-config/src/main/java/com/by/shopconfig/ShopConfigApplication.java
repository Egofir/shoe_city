package com.by.shopconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ShopConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopConfigApplication.class, args);
    }

}
