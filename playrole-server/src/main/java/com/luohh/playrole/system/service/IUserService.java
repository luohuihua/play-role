package com.luohh.playrole.system.service;

import com.luohh.playrole.annotation.RequestApi;
import com.luohh.playrole.annotation.RequestMethod;
import com.luohh.playrole.annotation.RequestParam;
import com.luohh.playrole.annotation.RequestParamType;
import com.luohh.playrole.annotation.param.RequestNotNull;
import com.luohh.playrole.exception.ApiServiceException;
import com.luohh.playrole.system.entity.SysUser;

/**
 * 用户接口类
 */
@RequestApi(group = "user-service", describe = "用户服务")
public interface IUserService {

    @RequestMethod(describe = "用户登录", checkLogin = false)
    SysUser login(
            @RequestNotNull @RequestParam(name = "phone", describe = "用户手机号") String phone,
            @RequestNotNull @RequestParam(name = "password", describe = "用户密码") String password,
            @RequestParam(name = "loginType", describe = "登录方式", valueDef = "") String loginType) throws ApiServiceException;

    @RequestMethod(describe = "检查是否已经登录", checkLogin = false)
    boolean checkLogin(@RequestNotNull @RequestParam(name = "accessToken", describe = "验证token") String accessToken);

    @RequestMethod(describe = "第三方登录",checkLogin = false)
    SysUser thirdPartLogin(
            @RequestNotNull @RequestParam(name = "openId", describe = "唯一标记") String openId,
            @RequestNotNull @RequestParam(name = "loginType", describe = "登录方式") String loginType,
            @RequestNotNull @RequestParam(name = "nickName", describe = "名称") String nickName,
            @RequestNotNull @RequestParam(name = "gender", describe = "性别") Integer gender,
            @RequestNotNull @RequestParam(name = "avatarUrl", describe = "头像") String avatarUrl
    ) throws ApiServiceException;

}
