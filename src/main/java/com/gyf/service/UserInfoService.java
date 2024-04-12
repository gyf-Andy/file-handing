package com.gyf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gyf.entity.UserInfo;
import org.apache.shiro.authc.UsernamePasswordToken;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 郭云飞
 * @since 2021-12-21
 */
public interface UserInfoService extends IService<UserInfo> {

    UsernamePasswordToken login(String userAccount, String password);

    /**
     * 发送验证码邮件
     * @param email 邮箱
     * @param request 请求
     * @return
     */
    String sendEmail(String email,HttpServletRequest request);

    /**
     * 获取单条账号的信息
     * @param userAccount
     * @return
     */
    UserInfo login(String userAccount);

    /**
     * 注册账号
     * @param userAccount 用户账号
     * @param password 用户密码
     * @param verifyCode 验证码
     * @param request  用来获取session中的验证码
     * @return
     */
    String register(String userAccount, String password, String verifyCode, HttpServletRequest request);

    /**
     * 更新账号信息
     * @param userAccount 账号
     * @param password 原密码
     * @param newPassword 新密码
     * @param nikeName 昵称
     * @return 是否跟新成功 1成功 0失败
     */
    int updateUserInfo(String userAccount,String password,String newPassword,String nikeName);

    /**
     * 验证用户身份
     * @param userAccount 邮箱账号
     * @param verifyCode 验证码
     * @param request
     * @return
     */
    String authenticate(String userAccount,String verifyCode,HttpServletRequest request);

    /**
     * 根据账号修改密码
     * @param userAccount 用户账号
     * @param newPassword 新密码
     */
    void updatePasswordByUserAccount(String userAccount,String newPassword);
}
