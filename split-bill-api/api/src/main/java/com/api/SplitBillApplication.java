package com.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(scanBasePackages = { "com.shashankbhat", "com.common", "com.api"  })
@EntityScan(value = {"com.shashankbhat", "com.common", "com.api" })
@EnableJpaRepositories(basePackages = {"com.shashankbhat", "com.common", "com.api" })
public class SplitBillApplication {

	public static void main(String[] args) {
		SpringApplication.run(SplitBillApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

}
