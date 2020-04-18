package com.wangh7.wht.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {

    public long getTimeLong() {
        Date date = new Date();
        return date.getTime();
    }

    public String getTimeString() {
        Date date =new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

}
