package com.luohh.playrole.system.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.luohh.playrole.exception.ApiServiceException;
import com.luohh.playrole.system.entity.SysUser;
import com.luohh.playrole.system.mapper.SysUserMapper;
import com.luohh.playrole.system.model.SysUserDto;
import com.luohh.playrole.util.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 登录设置
     *
     * @param sysUser
     * @return
     */
    private SysUserDto loginSysUserDto(SysUser sysUser) {
        String accessToken = JWT.create().withAudience(sysUser.getId().toString()).sign(Algorithm.HMAC256(sysUser.getPassword()));
        SysUserDto sysUserDto = new SysUserDto();
        BeanUtils.copyProperties(sysUser, sysUserDto);
        sysUserDto.setAccessToken(accessToken);
        sysUserDto.setPassword(null);
        redisUtil.set(accessToken, sysUserDto, 60 * 60 * 24 * 2);//2天
        return sysUserDto;
    }


    @Override
    public SysUser login(String phone, String password, String loginType) throws ApiServiceException {
        QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
        sysUserQueryWrapper.eq("phone", phone);
        SysUser sysUser = sysUserMapper.selectOne(sysUserQueryWrapper);
        return loginSysUserDto(sysUser);
    }

    @Override
    public boolean checkLogin(String accessToken) {
        Object user = redisUtil.get(accessToken);
        if (user == null) {
            return false;
        }
        return true;
    }

    @Override
    public SysUser thirdPartLogin(String openId, String loginType, String nickName, Integer gender, String avatarUrl) throws ApiServiceException {
        QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
        sysUserQueryWrapper.eq("open_id", openId);
        SysUser sysUser = sysUserMapper.selectOne(sysUserQueryWrapper);
        //存在则直接返回
        if (sysUser != null)
            return loginSysUserDto(sysUser);
        //不存在则注册新增
        sysUser = new SysUser();
        sysUser.setOpenId(openId);
        sysUser.setLoginType(loginType);
        sysUser.setNickName(nickName);
        sysUser.setGender(gender);
        sysUser.setAvatarUrl(avatarUrl);
        sysUser.setLastLoginTime(LocalDateTime.now());
        sysUser.setCreateTime(LocalDateTime.now());
        sysUser.setUpdateTime(LocalDateTime.now());
        sysUser.setPassword(openId);
        sysUserMapper.insert(sysUser);
        return loginSysUserDto(sysUser);
    }


}
