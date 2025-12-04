package com.example.security;


import cn.hutool.json.JSONUtil;
import com.example.common.lang.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse, AuthenticationException e)
        throws IOException, ServletException{
        System.out.println("执行了LoginFailureHandler中的onAuthenticationFailure方法......");
        httpServletResponse.setContentType(("application/json;charset=UTF-8"));
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        Result result = Result.fail(e.getMessage());
        outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }
}
