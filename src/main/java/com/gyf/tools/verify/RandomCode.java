package com.gyf.tools.verify;

import java.util.Random;

/**
 * @author 郭云飞
 * @date 2021/7/30-17:51
 * @Description TODO
 */
public class RandomCode {

    /**
     * 随机生成6位数的验证码
     * @return String code
     */
    public String randomCode(){
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }
}
