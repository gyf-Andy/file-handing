package com.gyf.controller;

import com.gyf.entity.UserInfo;
import com.gyf.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 郭云飞
 * @since 2021-12-21
 */
@Api(tags = "认证授权",value = "用户认证授权接口")
@RestController
@RequestMapping("/api/userInfo")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 用户登陆系统
     * @param userAccount 用户的邮箱账号
     * @param password 用户的密码
     * @return
     */
    @ApiOperation("用户登陆")
    @RequestMapping(value = "login",method= RequestMethod.POST)
    public UsernamePasswordToken login( String userAccount,  String password){
       return userInfoService.login(userAccount, password);
    }

    /**
     * 用户登出系统
     */
    @ApiOperation("用户登出")
    @RequestMapping(value = "logout",method= RequestMethod.GET)
    public void logout(){
        Subject subject= SecurityUtils.getSubject();
        subject.logout();
    }

    /**
     *发送验证码
     * @param email 邮箱账号
     * @param request session
     * @return
     */
    @ApiOperation("发送验证码")
    @RequestMapping(value = "sendEmail",method= RequestMethod.POST)
    public String sendEmail(String email,HttpServletRequest request){
        return userInfoService.sendEmail(email,request);
    }

    /**
     * 用户注册
     * @param userAccount 用户邮箱账号
     * @param password 用户密码
     * @param verifyCode 验证码
     * @param request 用来获取session中的验证码
     * @return 注册提示
     */
    @ApiOperation("用户注册")
    @RequestMapping(value = "register",method= RequestMethod.POST)
    public String register(String userAccount,String password,String verifyCode,HttpServletRequest request){
        return userInfoService.register(userAccount, password, verifyCode, request);
    }

    /**
     * 获取登陆账号的信息
     * @param userAccount 用户邮箱账号
     * @return
     */
    @ApiOperation("获取一个用户的信息")
    @RequestMapping(value = "getOneUserInfo",method= RequestMethod.POST)
    public UserInfo getOneUserInfo(String userAccount){
        return userInfoService.login(userAccount);
    }

    /**
     * 更新账号信息
     * @param userAccount 用户账号
     * @param password 用户原密码
     * @param newPassword 用户新密码
     * @param nikeName 账号昵称
     * @return 1 更新成功 0 更新失败
     */
    @ApiOperation("更新用户的信息")
    @RequestMapping(value = "updateUserInfo",method= RequestMethod.POST)
    public int updateUserInfo(String userAccount,String password,String newPassword,String nikeName){
        return userInfoService.updateUserInfo(userAccount, password, newPassword, nikeName);
    }

    /**
     * 验证用户身份
     * @return
     */
    @ApiOperation("忘记密码，验证用户身份")
    @RequestMapping(value = "authenticate",method= RequestMethod.POST)
    public String authenticate(String userAccount,String verifyCode,HttpServletRequest request){
        return userInfoService.authenticate(userAccount, verifyCode, request);
    }

    /**
     * 根据账号更改密码
     * @param userAccount 账号
     * @param newPassword 新密码
     */
    @ApiOperation("根据账号更改密码")
    @RequestMapping(value = "updatePasswordByUserAccount",method= RequestMethod.POST)
    public void updatePasswordByUserAccount(String userAccount,String newPassword){
        userInfoService.updatePasswordByUserAccount(userAccount, newPassword);
    }
}

