package com.wangh7.wht.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootConfiguration
public class MyWebConfigurer implements WebMvcConfigurer {

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        //所有路径都应用拦截器，除了...
//        registry.addInterceptor(getLoginInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns("/index.html")
//                .excludePathPatterns("/api/login")
//                .excludePathPatterns("/api/register")
//                .excludePathPatterns("api/logout");
//    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true) //允许跨域使用cookie
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("POST","GET","PUT","OPTIONS","DELETE")
                .allowedHeaders("*");
    }
}
