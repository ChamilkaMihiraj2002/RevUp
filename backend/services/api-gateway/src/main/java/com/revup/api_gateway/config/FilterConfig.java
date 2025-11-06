package com.revup.api_gateway.config;

import com.revup.api_gateway.filter.FirebaseAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<FirebaseAuthenticationFilter> firebaseAuthFilter(
            FirebaseAuthenticationFilter filter) {
        FilterRegistrationBean<FirebaseAuthenticationFilter> registrationBean = 
            new FilterRegistrationBean<>();
        
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
        
        return registrationBean;
    }
}
