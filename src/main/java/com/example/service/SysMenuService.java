package com.example.service;

import com.example.common.dto.SysMenuDto;
import com.example.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author teacher
 * @since 2025-11-15
 */
public interface SysMenuService extends IService<SysMenu> {
    List<SysMenuDto> getCurrentUserNav();
}
