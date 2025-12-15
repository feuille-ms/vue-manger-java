package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.common.dto.SysMenuDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author teacher
 * @since 2025-11-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 父菜单ID，一级菜单为0
     */
    @NotNull(message = "上级菜单不能为空")
    private Long parentId;

    @NotNull(message = "菜单名称不能为空")
    private String name;

    /**
     * 菜单URL
     */
    private String path;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    private String perms;

    private String component;

    /**
     * 类型     0：目录   1：菜单   2：按钮
     */
    @NotNull(message = "菜单类型不能为空")
    private Integer type;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序
     */
    @TableField("orderNum")
    private Integer orderNum;

    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();


}
