package com.intuit.commentsService.config;

import com.intuit.commentsService.config.interceptor.ThrottleInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public ThrottleInterceptor requestHandler() {
        return new ThrottleInterceptor();
    }


    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(requestHandler()).addPathPatterns("/api/comments/*").addPathPatterns("/api/comments/*/*");;
    }

}

