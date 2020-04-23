package com.dictation.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName TimeUtil
 * @Description
 * @Author zlc
 * @Date 2020-04-23 19:20
 */
public class TimeUtil {

    public static long getSecondsToNextMonday4pm(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.MILLISECOND,0);
        Long timeOut = (calendar.getTimeInMillis()-System.currentTimeMillis()) / 1000;
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
        if(dayOfWeek != 1){
            dayOfWeek -= 1;
        }else{
            dayOfWeek = 7;
        }
        timeOut += (7-dayOfWeek)*24*60*60 + 4*60*60; //到下周一的凌晨4点消失
        return timeOut;
    }
}
