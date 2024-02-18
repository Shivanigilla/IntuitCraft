//package com.intuit.commentsService.config;//package com.intuit.commentsService.config;//package com.intuit.commentsService.config;//package com.intuit.commentsService.config;
////////
////////
////////import org.springframework.context.annotation.Bean;
////////import org.springframework.context.annotation.Configuration;
////////import springfox.documentation.builders.PathSelectors;
////////import springfox.documentation.builders.RequestHandlerSelectors;
////////import springfox.documentation.spi.DocumentationType;
////////import springfox.documentation.spring.web.plugins.Docket;
////////
////////@Configuration
////////public class SwaggerConfig {
////////
//////////    @Bean
//////////    public Docket api() {
//////////        return new Docket(DocumentationType.OAS_30)
//////////                .select()
//////////                .apis(RequestHandlerSelectors.basePackage("com.intuit.commentsService.rest.controller"))
//////////                .paths(PathSelectors.any())
//////////                .build()
//////////                .apiInfo(apiInfo());
//////////    }
//////////
//////////    private ApiInfo apiInfo() {
//////////        return new ApiInfoBuilder()
//////////                .title(")
//////////                .description("API documentation for the Comments Service")
//////////                .version("1.0")
//////////                .build();
//////////    }
////////
//////////    @Bean
//////////    public OpenAPI apiInfo() {
//////////        return new OpenAPI();
//////////    }
//////////
//////////    @Bean
//////////    public GroupedOpenApi httpApi() {
//////////        return GroupedOpenApi.builder()
//////////                .group("http")
//////////                .pathsToMatch("/**")
//////////                .build();
//////////    }
////////
//////////    @Bean
//////////    public OpenAPI openAPI() {
//////////        return new OpenAPI().info(new Info().title("SpringDoc example")
//////////                .description("SpringDoc application")
//////////                .version("v0.0.1"));
//////////    }
////////
////////
////////    @Bean
////////    public Docket api() {
////////        return new Docket(DocumentationType.SWAGGER_2)
////////                .select()
////////                .apis(RequestHandlerSelectors.basePackage("com.intuit.commentsService"))
////////                .paths(PathSelectors.any())
////////                .build();
////////    }
////////}
////////
//////
//////import com.mangofactory.swagger.models.dto.ApiInfo;
//////import com.mangofactory.swagger.plugin.EnableSwagger;
//////import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
//////import org.springframework.context.annotation.Bean;
//////import org.springframework.context.annotation.Configuration;
//////import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//////
//////@Configuration
//////@EnableSwagger
//////public class SwaggerConfig {
//////
//////    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//////        registry.addResourceHandler("swagger-ui.html")
//////                .addResourceLocations("classpath:/META-INF/resources/");
//////
//////        registry.addResourceHandler("/webjars/**")
//////                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//////    }
//////
//////    @Bean
//////    public SwaggerSpringMvcPlugin customImplementation() {
//////        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig)
//////                .apiInfo(apiInfo())
//////                .includePatterns(".*");
//////    }
//////
//////    private ApiInfo apiInfo() {
//////        ApiInfo apiInfo = new ApiInfo(
//////                "Your API Documentation",
//////                "Your API description.",
//////                "Your API terms of service",
//////                "Your API Contact Email",
//////                "Your API License Type",
//////                "Your API License URL"
//////        );
//////        return apiInfo;
//////    }
//////}
//////
////
////import io.swagger.annotations.ApiOperation;
////import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import springfox.documentation.builders.ApiInfoBuilder;
////import springfox.documentation.builders.PathSelectors;
////import springfox.documentation.builders.RequestHandlerSelectors;
////import springfox.documentation.service.ApiInfo;
////import springfox.documentation.spi.DocumentationType;
////import springfox.documentation.spring.web.plugins.Docket;
////
////@Configuration
////@ConditionalOnProperty(value = "springfox.documentation.enabled", havingValue = "true", matchIfMissing = true)
////public class SwaggerConfig {
////    @Bean
////    public Docket createRestApi() {
////        return new Docket(DocumentationType.SWAGGER_2)
////                .apiInfo(apiInfo())
////                .select()
////                .apis(RequestHandlerSelectors.basePackage("com.intuit.commentsService"))
////                .paths(PathSelectors.any())
////                .build();
////    }
////
////    private ApiInfo apiInfo() {
////        return new ApiInfoBuilder()
////                .title("Comments Service")
////                .description("Comments Service")
//////                .contact(new Contact("码农小胖哥", "https://felord.cn", "dax@felord.cn"))
////                .version("1.0.0")
////                .build();
////    }
////}
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//
//@Configuration
//public class SwaggerConfig {
//
//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.intuit.commentsService")) // Update with your controller package
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(apiInfo());
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("Comments Service API")
//                .description("API documentation for the Comments Service")
//                .version("1.0")
//                .build();
//    }
//}
//
