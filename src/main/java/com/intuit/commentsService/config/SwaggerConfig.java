package com.intuit.commentsService.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.OAS_30)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.intuit.commentsService.rest.controller"))
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(apiInfo());
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title(")
//                .description("API documentation for the Comments Service")
//                .version("1.0")
//                .build();
//    }

//    @Bean
//    public OpenAPI apiInfo() {
//        return new OpenAPI();
//    }
//
//    @Bean
//    public GroupedOpenApi httpApi() {
//        return GroupedOpenApi.builder()
//                .group("http")
//                .pathsToMatch("/**")
//                .build();
//    }

//    @Bean
//    public OpenAPI openAPI() {
//        return new OpenAPI().info(new Info().title("SpringDoc example")
//                .description("SpringDoc application")
//                .version("v0.0.1"));
//    }


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.intuit.commentsService"))
                .paths(PathSelectors.any())
                .build();
    }
}

