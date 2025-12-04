package com.example.security;
//import com.example.until.JwtUtils;
import cn.hutool.json.JSONUtil;
import com.example.common.lang.Result;
import com.example.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse, Authentication authentication)
            throws IOException, ServletException {
        System.out.println("LoginSuccessHandler中的onAuthenticationSuccess方法......");
        httpServletResponse.setContentType("application/json;charset=utf-8");
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();

        String jwt = jwtUtils.generateToken(authentication.getName());
        System.out.println("jwt:"+jwt);
        httpServletResponse.setHeader(jwtUtils.getHeader(), jwt);

        Result result = Result.succ("???");
        outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }
}
