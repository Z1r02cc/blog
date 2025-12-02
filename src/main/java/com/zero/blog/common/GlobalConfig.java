package com.zero.blog.common;


import com.zero.blog.intercepter.AdminInterceptor;
import com.zero.blog.intercepter.GloballInterceptor;
import com.zero.blog.intercepter.UserInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalConfig implements WebMvcConfigurer {
    /**
     * 全局拦截器
     * @return
     */
    @Bean
    public HandlerInterceptor getGlobalInterceptor() {
        return new GloballInterceptor();
    }

    /**
     * 让GlobalIntercepter提前加载，
     *
     * @return
     */
    @Bean
    public HandlerInterceptor getAdminInterceptor() {
        return new AdminInterceptor();
    }

    /**
     * 用户拦截器
     * @return
     */
    @Bean
    public HandlerInterceptor getUserInterceptor() {
        return new UserInterceptor();
    }




    /**
     * 添加全局拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //管理员拦截
        registry.addInterceptor(getAdminInterceptor()).addPathPatterns("/zer02/**")
                .excludePathPatterns("/zer02/login", "/zer02/logout", "/zer02/adminLogin");

        //用户拦截器
        registry.addInterceptor(getUserInterceptor()).addPathPatterns("/user/**");

        registry.addInterceptor(getGlobalInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**","/css/**","/js/**","/img/**");
    }
}
