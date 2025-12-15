package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.lang.Const;
import com.example.common.lang.Result;
import com.example.entity.SysRole;
import com.example.entity.SysRoleMenu;
import com.example.entity.SysUserRole;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author teacher
 * @since 2025-11-15
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends BaseController {

    @PreAuthorize("hasAuthority('sys:role:list')")
    @GetMapping("/list")
    public Result list(String name) {
        Page<SysRole> pageDate = sysRoleService.page(getPage(), new QueryWrapper<SysRole>().like(StringUtils.isNotBlank(name), "name", name));

        return Result.succ(pageDate);
    }

    @PreAuthorize("hasAuthority('sys:role:list')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long id) {
        SysRole sysRole = sysRoleService.getById(id);
        List<SysRoleMenu> roleMenus = sysRoleMenuService.list(new QueryWrapper<SysRoleMenu>().eq("role_id", id));
        List<Long> menuIds = roleMenus.stream().map(p -> p.getMenuId()).collect(Collectors.toList());
        sysRole.setMenuIds(menuIds);

        return Result.succ(sysRole);
    }

    @PreAuthorize("hasAuthority('sys:role:save')")
    @PostMapping("/save")
    public Result save(@Validated @RequestBody SysRole sysRole) {
        sysRole.setCreated(LocalDateTime.now());
        if (sysRole.getStatu() == 1){
            sysRole.setStatu(Const.STATUS_ON);
        }else{
            sysRole.setStatu(Const.STATUS_OFF);
        }
        sysRoleService.save(sysRole);
        return Result.succ(sysRole);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:role:update')")
    public Result update(@Validated @RequestBody SysRole sysRole) {
        sysRole.setUpdated(LocalDateTime.now());
        sysRoleService.updateById(sysRole);
        sysUserService.clearUserAuthorityInfoByRoleId(sysRole.getId());
        return Result.succ(sysRole);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    @Transactional
    public Result delete(@RequestBody Long[] ids) {
        sysRoleService.removeByIds(Arrays.asList(ids));
        //删除中间表
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("role_id", ids));
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().in("role_id", ids));
        Arrays.stream(ids).forEach(id -> {
            sysUserService.clearUserAuthorityInfoByRoleId(id);
        });
        return Result.succ("");
    }

    @Transactional
    @PostMapping("/perm/{roleId}")
    @PreAuthorize("hasAuthority('sys:role:perm')")
    public Result info(@PathVariable("roleId") Long roleId, @RequestBody Long[] menuIds) {
        List<SysRoleMenu> sysRoleMenus = new ArrayList<>();
        Arrays.stream(menuIds).forEach(menuId -> {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            sysRoleMenus.add(roleMenu);
        });
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("role_id", roleId));
        sysRoleMenuService.saveBatch(sysRoleMenus);
        sysUserService.clearUserAuthorityInfoByRoleId(roleId);
        return Result.succ(menuIds);
    }
}
