package com.gyf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyf.entity.UserInfo;
import com.gyf.mapper.UserInfoMapper;
import com.gyf.service.UserInfoService;
import com.gyf.shiro.utils.MD5Encrypt;
import com.gyf.tools.verify.SendEmail;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 郭云飞
 * @since 2021-12-21
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public UsernamePasswordToken login(String userAccount, String password) {
        //获取当前用户
        Subject subject= SecurityUtils.getSubject();
        subject.logout();//刷新登录信息
        //封装用户的登陆数据
        UsernamePasswordToken token=new UsernamePasswordToken(userAccount,password);
        String result="";
        try{
            subject.login(token); //登陆
            subject.getSession().setTimeout(10 * 60 *1000); //设置登陆过期时间
            return token;
        }catch (UnknownAccountException e){
            throw new UnknownAccountException("账号不存在");
        }catch (IncorrectCredentialsException e){
            throw new IncorrectCredentialsException("密码错误");
        }catch (AuthenticationException e){
            throw new AuthenticationException("账号不存在");
        }
    }

    @Override
    public String sendEmail(String email, HttpServletRequest request) {
        HttpSession httpSession=request.getSession();
        SendEmail sendEmail=new SendEmail();
        Boolean isSend=sendEmail.sendMimeMail(email,httpSession);
        if (isSend==true){
            return "发送成功！";
        }else {
            return "发送失败！";
        }
    }

    @Override
    public UserInfo login(String userAccount) {
        UserInfo userInfo=userInfoMapper.selectById(userAccount);
        return userInfo;
    }

    @Override
    public String register(String userAccount, String password, String verifyCode, HttpServletRequest request) {
        //获取系统里的验证码
        HttpSession session=request.getSession();
        String sessionVerify= (String) session.getAttribute(userAccount);

        //判断账号是否存在
        UserInfo userInfoIsExist = userInfoMapper.selectById(userAccount);
        String result="";
        if (userInfoIsExist!=null){
            result="账号已存在";
        }else {
            if (verifyCode.equals(sessionVerify)){
                UserInfo userInfo=new UserInfo();
                //对密码进行加密
                String MD5Password= MD5Encrypt.encryptedPassword(password,userAccount);
                userInfo.setUserAccount(userAccount).setUserPassword(MD5Password).setRegisteredDate(LocalDate.now());
                userInfoMapper.insert(userInfo);
                result="注册成功";
            }else {
                result="验证码错误";
            }
        }
        return result;
    }

    @Override
    public int updateUserInfo(String userAccount, String password, String newPassword, String nikeName) {
        UserInfo userInfo = userInfoMapper.selectById(userAccount);
        //加密旧密码
        String MD5OldPassword=MD5Encrypt.encryptedPassword(password,userAccount);
        if (userInfo.getUserPassword().equals(MD5OldPassword)){
            //加密旧密码
            String MD5NewPassword=MD5Encrypt.encryptedPassword(newPassword,userAccount);
            UserInfo newUserInfo=new UserInfo();
            newUserInfo.setUserPassword(MD5NewPassword).setUserNikename(nikeName).setUserAccount(userAccount);
            int i = userInfoMapper.updateById(newUserInfo);
            return i;
        }else {
            return 0;
        }
    }

    @Override
    public String authenticate(String userAccount, String verifyCode,HttpServletRequest request) {
        //获取系统里的验证码
        HttpSession session=request.getSession();
        String sessionVerify= (String) session.getAttribute(userAccount);

        //判断账号是否存在
        UserInfo userInfoIsExist = userInfoMapper.selectById(userAccount);
        String result="";
        if (userInfoIsExist!=null){
            if (verifyCode.equals(sessionVerify)){
                result="验证通过";
            }else {
                result="验证码错误";
            }
        }else {
            result="账号不存在";
        }
        return result;
    }

    @Override
    public void updatePasswordByUserAccount(String userAccount, String newPassword) {
        //新密码加密
        String MD5NewPassword=MD5Encrypt.encryptedPassword(newPassword,userAccount);
        UserInfo userInfo=new UserInfo();
        userInfo.setUserAccount(userAccount).setUserPassword(MD5NewPassword);
        userInfoMapper.updateById(userInfo);
    }
}
