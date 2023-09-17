package com.example.demo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseUtils {
    public static String GetTimeString(){
        // 创建一个 SimpleDateFormat 对象，指定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 获取当前时间
        Date now = new Date();

        // 使用 SimpleDateFormat 格式化日期对象为字符串
        return sdf.format(now);

    }
}
