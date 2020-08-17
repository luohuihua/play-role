package com.luohh.playrole.system.service;

import com.luohh.playrole.exception.ApiServiceException;
import com.luohh.playrole.system.entity.SysUser;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {
    @Override
    public SysUser login(String phone, String password, Integer loginType, String raw, String ip) throws ApiServiceException {
        SysUser sysUser = new SysUser();
        sysUser.setNickname("罗辉华");
        return sysUser;
    }

    @Override
    public SysUser thirdPartLogin(Integer loginType, String ip, String raw) throws ApiServiceException {
        SysUser sysUser = new SysUser();
        sysUser.setNickname("罗辉华");
        return sysUser;
    }
}
