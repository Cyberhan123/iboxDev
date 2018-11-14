package cn.hselfweb.ibox.config;

import cn.hselfweb.ibox.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //需要拦截的路径
        String[] addPathPatterns = {
                "/api/**"
        };
        //不需要拦截的路径
        String[] excludePathPatterns = {};

        registry.addInterceptor(new LoginInterceptor()).addPathPatterns(addPathPatterns).excludePathPatterns(excludePathPatterns);
    }
}
