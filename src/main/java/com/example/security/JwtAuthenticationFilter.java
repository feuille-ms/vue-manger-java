package com.example.security;

import cn.hutool.core.util.StrUtil;
import com.example.entity.SysUser;
import com.example.service.SysUserService;
import com.example.service.impl.UserDetailServiceImpl;
import com.example.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailServiceImpl userDetailService;
    private final SysUserService sysUserService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   JwtUtils jwtUtils,
                                   UserDetailServiceImpl userDetailService,
                                   SysUserService sysUserService) {
        super(authenticationManager);
        this.jwtUtils = jwtUtils;
        this.userDetailService = userDetailService;
        this.sysUserService = sysUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String jwt = request.getHeader(jwtUtils.getHeader());
        if (StrUtil.isBlankOrUndefined(jwt)) {
            chain.doFilter(request, response);
            return;
        }

        Claims claim = jwtUtils.getClaimByToken(jwt);
        if (claim == null || jwtUtils.isTokenExpired(claim)) {
            chain.doFilter(request, response);
            return;
        }

        String username = claim.getSubject();
        if (username == null) {
            chain.doFilter(request, response);
            return;
        }

        SysUser sysUser = sysUserService.getByUsername(username);
        if (sysUser == null) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(username, null,
                        userDetailService.getUserAuthority(sysUser.getId()));
        SecurityContextHolder.getContext().setAuthentication(token);
        chain.doFilter(request, response);
    }
}

