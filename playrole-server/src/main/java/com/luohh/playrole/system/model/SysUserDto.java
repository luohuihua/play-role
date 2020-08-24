package com.luohh.playrole.system.model;

import com.luohh.playrole.system.entity.SysUser;
import lombok.Data;

@Data
public class SysUserDto extends SysUser {
    /**
     * token
     */
    private String accessToken;
}
