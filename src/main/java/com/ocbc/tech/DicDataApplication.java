package com.ocbc.tech;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ocbc.tech.mapper")
public class DicDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(DicDataApplication.class, args);
	}

}
