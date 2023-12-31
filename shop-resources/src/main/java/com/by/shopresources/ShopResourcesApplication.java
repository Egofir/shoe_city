package com.by.shopresources;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

@SpringBootApplication(scanBasePackages = "com.by")

// 配置FastDFS两个注解
@Import(FdfsClientConfig.class)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class ShopResourcesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopResourcesApplication.class, args);
	}

}
