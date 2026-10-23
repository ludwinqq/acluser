package bo.quispe.edwin.infrastructure.config;
/* 
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import bo.quispe.edwin.infrastructure.persistence.acluser.auth.JwtAuthorizationGlobalFilter;

@Configuration
public class FilterConfig {

    private final JwtAuthorizationGlobalFilter jwtAuthorizationGlobalFilter;

    public FilterConfig(JwtAuthorizationGlobalFilter jwtAuthorizationGlobalFilter) {
        this.jwtAuthorizationGlobalFilter = jwtAuthorizationGlobalFilter;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthorizationGlobalFilter> loggingFilter(){
        FilterRegistrationBean<JwtAuthorizationGlobalFilter> registrationBean = new FilterRegistrationBean<>();
        
        registrationBean.setFilter(jwtAuthorizationGlobalFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1); 
        
        return registrationBean;
    }
}*/
