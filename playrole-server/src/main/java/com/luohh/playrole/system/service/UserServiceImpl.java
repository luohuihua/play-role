package com.luohh.playrole.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.luohh.playrole.exception.ApiServiceException;
import com.luohh.playrole.system.entity.SysUser;
import com.luohh.playrole.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public SysUser login(String phone, String password, Integer loginType, String raw, String ip) throws ApiServiceException {
        QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
        sysUserQueryWrapper.eq("phone", phone);
        SysUser sysUser = sysUserMapper.selectOne(sysUserQueryWrapper);
        return sysUser;
    }

    @Override
    public SysUser thirdPartLogin(Integer loginType, String ip, String raw) throws ApiServiceException {
        SysUser sysUser = new SysUser();
        sysUser.setNickname("罗辉华");
        return sysUser;
    }
}
