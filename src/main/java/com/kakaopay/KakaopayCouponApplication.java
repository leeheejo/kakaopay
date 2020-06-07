package com.kakaopay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class KakaopayCouponApplication {

	public static void main(String[] args) {
		SpringApplication.run(KakaopayCouponApplication.class, args);
	}

}
