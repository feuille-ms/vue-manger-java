package com.example.controller;


import cn.hutool.core.map.MapUtil;
import com.example.common.dto.SysMenuDto;
import com.example.common.lang.Result;
import com.example.entity.SysUser;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author teacher
 * @since 2025-11-15
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends BaseController {

    @GetMapping("nav")
    public Result nav(Principal principal) {
        SysUser sysUser = sysUserService.getByUsername(principal.getName());
        String authorityInfo = sysUserService.getUserAuthorityInfo(sysUser.getId());
        String [] authorityInfoArray = StringUtils.tokenizeToStringArray(authorityInfo, ",");
        List<SysMenuDto> navs = sysMenuService.getCurrentUserNav();
        return Result.succ(MapUtil.builder()
                .put("authoritys", authorityInfoArray)
                .put("nav", navs)
                .map());

    }
}
