package com.example.config;


import com.example.security.CaptchaFilter;
import com.example.security.LoginFailureHandler;
import com.example.security.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Autowired
    LoginSuccessHandler loginSuccessHandler;

    @Autowired
    CaptchaFilter captchaFilter;

    //白名单
    private static final String[] URL_WHITELIST = {
            "/login",
            "/logout",
            "/captcha",
            "/favicon.ico",
    };

    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                //登录配置
                .formLogin()
//                    .loginProcessingUrl("/login")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)

                //禁用session
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                //配置拦截规则
                .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll()
                .anyRequest().authenticated()

                //异常处理

                //配置自定义过滤器
                .and()
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)
        ;
    }

    //测试
    @Override
    protected  void configure (AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("root")
                .password("{noop}admin")
                .roles("ADMIN");
    }
}
