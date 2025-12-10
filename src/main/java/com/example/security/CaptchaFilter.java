package com.example.security;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.common.exception.CaptchaException;
import com.example.common.lang.Const;
import com.example.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CaptchaFilter extends OncePerRequestFilter {
    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Autowired
    RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws SecurityException, IOException, ServletException {
        System.out.println("CatptchaFilter_doFilterInternal......");
        String url = httpServletRequest.getRequestURI();

        if("/login".equals(url) && httpServletRequest.getMethod().equals("POST")) {
            try{
                validate(httpServletRequest);
            }catch (CaptchaException e){
                loginFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
            }

        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void validate(HttpServletRequest httpServletRequest) {
        String code = httpServletRequest.getParameter("code");
        System.out.println("页面传递的验证码为：" +code);
        String key = httpServletRequest.getParameter("token");
        System.out.println("页面传递的key为：" + key);
        if(StringUtils.isBlank(code) || StringUtils.isBlank(key)){
            throw new CaptchaException("验证码错误");
        }
        String resdis_Code = (String) redisUtil.hget(Const.CAPTCHA_KEY, key);
        System.out.println("从Redis中取出的验证码为：" + resdis_Code);
        if(!code.equals(resdis_Code)){
            throw new CaptchaException("验证码错误");
        }
        redisUtil.hdel(Const.CAPTCHA_KEY,key);

    }
}
