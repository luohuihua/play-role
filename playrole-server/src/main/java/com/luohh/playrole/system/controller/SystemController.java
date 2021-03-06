package com.luohh.playrole.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.luohh.playrole.system.mapper.SysUserMapper;
import com.luohh.playrole.system.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/sys")
public class SystemController {
    @Autowired
    private SysUserMapper sysUserMapper;

    @ResponseBody
    @RequestMapping("/test")
    public Object test() {
//        DBContextHolder.slave();
//        List<SysUser> unimallUsers = sysUserMapper.selectList(null);
//        unimallUsers=unimallUserMapper.queryUnimallUser(new UnimallUser());
        QueryWrapper<SysUser> sysUserQueryWrapper=new QueryWrapper<>();
        sysUserQueryWrapper.eq("phone","13537927121");
        SysUser sysUser =sysUserMapper.selectOne(sysUserQueryWrapper);
        return sysUser;
    }
}
