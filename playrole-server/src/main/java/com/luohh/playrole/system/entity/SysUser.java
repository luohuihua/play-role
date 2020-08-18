package com.luohh.playrole.system.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户表实体类
 *
 * @author luohh
 */
@Data
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1597399840526L;


    /**
     * 主键
     * 主键
     * isNullAble:0
     */
    private Long id;

    /**
     * 手机号码
     * isNullAble:0
     */
    private String phone;

    /**
     * 密码
     * isNullAble:1
     */
    private String password;

    /**
     * 登录类型
     * isNullAble:0
     */
    private Integer loginType;

    /**
     * 微信唯一标记
     * isNullAble:1
     */
    private String openId;

    /**
     * 名称
     * isNullAble:1
     */
    private String nickname;

    /**
     * 头像路径
     * isNullAble:1
     */
    private String avatarUrl;

    /**
     * 0.普通会员 1.VIP会员
     * isNullAble:0,defaultVal:0
     */
    private Integer level;

    /**
     * 生日
     * isNullAble:1
     */
    private java.time.LocalDate birthday;

    /**
     * 性别(1男2女)
     * isNullAble:1,defaultVal:-1
     */
    private Integer gender;

    /**
     * 最后登录时间
     * isNullAble:0
     */
    private java.time.LocalDateTime lastLoginTime;

    /**
     * 最后登录ip
     * isNullAble:1
     */
    private String lastLoginIp;

    /**
     * 状态(1正常)
     * isNullAble:1,defaultVal:1
     */
    private Integer status;

    /**
     * 创建时间
     * isNullAble:0
     */
    private java.time.LocalDateTime createTime;

    /**
     * 修改时间
     * isNullAble:0
     */
    private java.time.LocalDateTime updateTime;
}
