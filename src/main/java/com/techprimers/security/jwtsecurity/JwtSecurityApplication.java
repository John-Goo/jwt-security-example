package com.techprimers.security.jwtsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@EnableDiscoveryClient
public class JwtSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtSecurityApplication.class, args);
		System.out.println("http://localhost:8082/doc.html");
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		// 单个数据大小
		//factory.setMaxFileSize("1024000KB");
		// 总上传数据大小
		//factory.setMaxRequestSize("1024000KB");
		return factory.createMultipartConfig();
	}
}
