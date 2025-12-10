package com.example.controller;

import com.example.common.lang.Result;
import com.example.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    SysUserService sysUserService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test")
    public Result test() {
        return Result.succ(sysUserService.list());
    }

    @GetMapping("/test/pass")
    public Result pass(){
        String password = bCryptPasswordEncoder.encode("123456");
        boolean isMatches = bCryptPasswordEncoder.matches("123456",password);
        System.out.println("匹配结果："+isMatches);
        return Result.succ(password);
    }
}