package com.orange.blog.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by orange on 16/2/24.
 */
public class TimeUtils {
    public static String formatTime(long time){
        SimpleDateFormat simpleDateFormat;
        Date date=new Date(time);
        if (time/1000<=60*60){//总时间在60分钟内时候 1h=60*60*1000ms;
            simpleDateFormat=new SimpleDateFormat("mm:ss");
        }else //时间大于一小时
        {
            simpleDateFormat=new SimpleDateFormat("hh:mm");
        }
        return simpleDateFormat.format(date);
    }

}
