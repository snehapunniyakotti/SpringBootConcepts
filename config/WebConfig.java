//package com.demo.authentication.config;
//
//import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.multipart.MultipartResolver;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//
//@Configuration
//@EnableWebMvc
//public class WebConfig extends WebMvcConfigurerAdapter {
//
//    
//    @Bean
//    public MultipartResolver multipartResolver() {
//        CommonsMultipartResolver  multipartResolver = new CommonsMultipartResolver();
//        multipartResolver.setMaxUploadSize(500000000);
//        return multipartResolver;
//    }
//}
