package com.gyf.shiro.utils;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @author 22413
 * @date 2022/3/15-16:02
 * @Description TODO
 */
public class MD5Encrypt {

    /**
     * 使用MD5对所添加用户的密码进行加密
     *
     * @param password 密码
     * @param account 账号
     * @return
     */
    public static String encryptedPassword(String password,String account){
        //使用MD5+salt+hash 散列    还是调用Md5的构造方法
        Md5Hash md5Hash2=new Md5Hash(password,account,1024);
        return md5Hash2.toString();
    }
}
