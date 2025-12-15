package com.example.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.dto.SysMenuDto;
import com.example.entity.SysMenu;
import com.example.entity.SysUser;
import com.example.mapper.SysMenuMapper;
import com.example.mapper.SysUserMapper;
import com.example.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author teacher
 * @since 2025-11-15
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysUserMapper sysUserMapper;

    private List<SysMenu> buildTreeMenu(List<SysMenu> Menus) {
        List<SysMenu> finalMenus = new ArrayList<>();

        for (SysMenu menu :Menus) {
            for (SysMenu e : Menus) {
                if(menu.getId() == e.getParentId()) {
                    menu.getChildren().add(e);
                }
            }
            if (menu.getParentId() == 0L){
                finalMenus.add(menu);
            }
        }
        System.out.println("finalMenus " + JSONUtil.toJsonStr(finalMenus));
        return finalMenus;
    }

    private List<SysMenuDto> convert(List<SysMenu> menuTree) {
        List<SysMenuDto> menuDtos = new ArrayList<>();

        menuTree.forEach(menu -> {
            SysMenuDto dto = new SysMenuDto();
            dto.setId(menu.getId());
            dto.setName(menu.getPerms());
            dto.setTitle(menu.getName());
            dto.setComponent(menu.getComponent());
            dto.setPath(menu.getPath());
            if (!menu.getChildren().isEmpty()) {
                dto.setChildren(convert(menu.getChildren()));
            }
            menuDtos.add(dto);
        });
        return menuDtos;
    }

    @Override
    public List<SysMenuDto> getCurrentUserNav() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysUser sysUser = sysUserService.getByUsername(username);
        List<Long> menuIds = sysUserMapper.getNavMenuIds(sysUser.getId());
        List<SysMenu> menus = this.listByIds(menuIds);
        //转换成树状结构
        List<SysMenu> menuTree = buildTreeMenu(menus);
        //实体转换成DTO
        return convert(menuTree);
    }

    @Override
    public List<SysMenu> tree() {
        List<SysMenu> menus = this.list(new QueryWrapper<SysMenu>().orderByAsc("orderNum"));
        return buildTreeMenu(menus);
    }
}
