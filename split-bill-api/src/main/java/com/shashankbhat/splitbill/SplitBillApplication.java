package com.shashankbhat.splitbill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.*;

@SpringBootApplication
public class SplitBillApplication {

	public static void main(String[] args) {
		SpringApplication.run(SplitBillApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

}
