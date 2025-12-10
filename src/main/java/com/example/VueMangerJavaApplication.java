package com.example;

import com.example.util.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtUtils.class)
public class VueMangerJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(VueMangerJavaApplication.class, args);
	}

}
