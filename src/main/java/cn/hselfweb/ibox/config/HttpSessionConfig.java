package cn.hselfweb.ibox.config;

import org.springframework.context.annotation.Bean;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;



public class HttpSessionConfig {
    @Bean
    public HttpSessionIdResolver httpSessionIdResolver(){
        return HeaderHttpSessionIdResolver.xAuthToken();
    }
}
