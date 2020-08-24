package com.luohh.playrole.system.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.luohh.playrole.exception.ApiServiceException;
import com.luohh.playrole.system.entity.SysUser;
import com.luohh.playrole.system.mapper.SysUserMapper;
import com.luohh.playrole.system.model.SysUserDto;
import com.luohh.playrole.util.RedisUtil;
import com.luohh.playrole.util.ToolsUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public SysUser login(String phone, String password, Integer loginType, String raw, String ip) throws ApiServiceException {
        QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
        sysUserQueryWrapper.eq("phone", phone);
        SysUser sysUser = sysUserMapper.selectOne(sysUserQueryWrapper);
        String accessToken = JWT.create().withAudience(sysUser.getId().toString()).sign(Algorithm.HMAC256(sysUser.getPassword()));
        SysUserDto sysUserDto = new SysUserDto();
        BeanUtils.copyProperties(sysUser, sysUserDto);
        sysUserDto.setAccessToken(accessToken);
        redisUtil.set(accessToken, sysUserDto, 60 * 60 * 24 * 2);//2天
        return sysUserDto;
    }

    @Override
    public SysUser thirdPartLogin(Integer loginType, String ip, String raw) throws ApiServiceException {
        SysUser sysUser = new SysUser();
        sysUser.setNickname("罗辉华");
        return sysUser;
    }
}
