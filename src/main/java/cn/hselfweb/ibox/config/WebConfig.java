package cn.hselfweb.ibox.config;

import cn.hselfweb.ibox.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter{

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截的路径
        String [] addPathPatterns = {
                "/users/**",
                "/foods/**",
                "/geticeboxinfo/**"
        };

        //不拦截的路径
        String [] excludePathPatterns = {
                "/login/**",
                "/validations/**"
        };

        registry.addInterceptor(new LoginInterceptor()).addPathPatterns(addPathPatterns).excludePathPatterns();

    }
}
