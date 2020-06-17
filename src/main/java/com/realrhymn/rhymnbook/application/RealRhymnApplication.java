package com.realrhymn.rhymnbook.application;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.realrhymn.rhymnbook")
@EnableJpaRepositories("com.realrhymn.rhymnbook.dao")
@EntityScan("com.realrhymn.rhymnbook.model")
public class RealRhymnApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(RealRhymnApplication.class, args);
	}

}
