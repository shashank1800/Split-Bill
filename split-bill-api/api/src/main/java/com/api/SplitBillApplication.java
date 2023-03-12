package com.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.api", "com.common", "com.data"})
@EntityScan(value = {"com.api", "com.common", "com.data"})
@EnableJpaRepositories(basePackages = {"com.api", "com.common", "com.data"})
public class SplitBillApplication {

    public static void main(String[] args) {
        SpringApplication.run(SplitBillApplication.class, args);
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//    }

}
