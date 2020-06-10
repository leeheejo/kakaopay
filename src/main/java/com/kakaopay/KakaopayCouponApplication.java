package com.kakaopay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableAspectJAutoProxy
@SpringBootApplication
@EnableScheduling
public class KakaopayCouponApplication {

	public static void main(String[] args) {
		SpringApplication.run(KakaopayCouponApplication.class, args);
	}

}
