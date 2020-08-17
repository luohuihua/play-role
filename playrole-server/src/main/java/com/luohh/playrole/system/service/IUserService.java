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

    @RequestMethod(describe = "用户登录")
    public SysUser login(
            @RequestNotNull @RequestParam(name = "phone", type = RequestParamType.COMMON, describe = "用户手机号") String phone,
            @RequestNotNull @RequestParam(name = "password", type = RequestParamType.COMMON, describe = "用户密码") String password,
            @RequestParam(name = "loginType", type = RequestParamType.COMMON, describe = "登录方式") Integer loginType,
            @RequestParam(name = "raw", type = RequestParamType.COMMON, describe = "第三方平台返回的数据") String raw,
            @RequestNotNull @RequestParam(name = "ip", type = RequestParamType.IP, describe = "登录IP") String ip) throws ApiServiceException;

    @RequestMethod(describe = "第三方登录")
    public SysUser thirdPartLogin(
            @RequestNotNull @RequestParam(name = "loginType", type = RequestParamType.COMMON, describe = "第三方代号") Integer loginType,
            @RequestNotNull @RequestParam(name = "ip", type = RequestParamType.IP, describe = "用户Ip") String ip,
            @RequestNotNull @RequestParam(name = "raw", type = RequestParamType.COMMON, describe = "第三方平台返回的数据") String raw) throws ApiServiceException;

}
