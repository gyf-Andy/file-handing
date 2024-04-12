package com.gyf.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author 郭云飞
 * @date 2022/3/2-16:49
 * @Description TODO
 */
public class GetDateString {
    /**
     * 获取当前日期
     * @return
     */
    public static String getDate(){
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * 生成不重复的uuid
     * @return
     */
    public static String getUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
