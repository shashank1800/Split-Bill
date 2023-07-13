package com.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(getApiInfo())
                .host("192.168.0.103:8080")
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.api.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "JWT", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }

    public ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("Split Bill")
                .description("Split Bill is a convenient tool for organizing and managing shared expenses and bills " +
                        "within a group. Whether you're living with roommates and need to split the rent and other " +
                        "living expenses, planning a trip with friends, or enjoying social activities with coworkers, " +
                        "Split Bill can help you track and manage all kinds of group expenses, such as food, groceries, " +
                        "furniture rentals, movie tickets, travel costs, and more. With Split Bill, you can easily keep " +
                        "track of who owes what and ensure that everyone pays their fair share, making it a hassle-free " +
                        "way to manage group expenses..")
                .licenseUrl("shashankbhat1800@gmail.com")
                .version("1.0")
                .build();
    }

}
