package com.markerhub.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.markerhub.common.dto.LoginDto;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.User;
import com.markerhub.mapper.UserMapper;
import com.markerhub.service.UserService;
import com.markerhub.util.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestControllerAdvice
public class LoginController {

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;

    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {

        User user = userService.getOne(new QueryWrapper<User>().eq("username", loginDto.getUsername()));
        List<User> users = userMapper.selectList(new QueryWrapper<User>().select().eq("username", loginDto.getUsername()));
        for(User user1 : users){
            user1.getPassword();
        }
        Assert.notNull(user, "用户不存在");

        if(!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))){
            return Result.fail("密码不正确");
        }

        String jwt = jwtUtil.generateToken(user.getId());

        response.setHeader("Authorization", jwt);
        response.setHeader("Access-control-Expose-Headers", "Authorization");

        return Result.success(MapUtil.builder()
                .put("id", user.getId())
                .put("username", user.getUsername())
                .put("avatar", user.getAvatar())
                .put("email", user.getEmail())
                .map()
        );
    }
    /*
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response){

        User user = userService.getOne(new QueryWrapper<User>().eq("username",loginDto.getUsername()));
        Assert.notNull(user,"用户不存在"); //为空则提示 用户不存在

        if(!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))){
            return Result.fail("密码不正确");
        }

        String jwt = jwtUtil.generateToken(user.getId());
        response.setHeader("Authorization",jwt);
        response.setHeader("Access-control-Expose-Headers","Authorization");

        return Result.success(MapUtil.builder()
                .put("id", user.getId())
                .put("username", user.getUsername())
                .put("avatar",user.getAvatar())
                .put("email",user.getEmail())
                .map()
                //返回用户信息
        );
    }*/

    //退出接口
    @RequiresAuthentication
    @GetMapping("/logout")
    public Result logout(){
        SecurityUtils.getSubject().logout();

        return Result.success(null);
    }
}
