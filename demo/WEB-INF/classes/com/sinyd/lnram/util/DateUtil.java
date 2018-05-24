package com.sinyd.lnram.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd";//
    public static final String DEFAULT_FORMAT_TIMESTAMP = "yyyy-MM-dd HH:mm:ss";//

    /**
     * 字符串转换成日期 如果转换格式为空，则利用默认格式进行转换操作
     * 
     * @param str 字符串
     * @param format 日期格式
     * @return 日期
     * @throws java.text.ParseException
     */
    public static Date strDate(String str, String format) {
        if (null == str || "".equals(str)) {
            return null;
        }
        // 如果没有指定字符串转换的格式，则用默认格式进行转换
        if (null == format || "".equals(format)) {
            format = DEFAULT_FORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符串转换时间戳
     * 
     * @param str
     * @return
     */

    public static Timestamp strTimestamp2(String str) {
        Date date = strDate(str, DEFAULT_FORMAT_TIMESTAMP);
        return new Timestamp(date.getTime());
    }

    /**
     * 字符串转换时间戳
     * 
     * @param str
     * @return
     */

    public static Timestamp strTimestamp(String str) {
        Date date = strDate(str, DEFAULT_FORMAT);
        return new Timestamp(date.getTime());
    }

    /**
     * 时间转换为字符串
     * 
     * @param date 时间
     * @param format 日期格式
     * @return 字符串
     */
    public static String timestampStr(Date date, String format) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String timestampStr(Date date) {
        return timestampStr(date, DEFAULT_FORMAT_TIMESTAMP);
    }

    /**
     * 检验输入是否为正确的日期格式(不含秒的任何情况),严格要求日期正确性,格式:yyyy-MM-dd HH:mm
     * 
     * @param sourceDate
     * @return
     */
    public static boolean checkDate(String sourceDate) {
        if (sourceDate == null) {
            return false;
        }
        try {
            sourceDate = sourceDate.replaceAll("-", "/");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            dateFormat.setLenient(false);
            dateFormat.parse(sourceDate);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 获取字符串型的 某月份/日期
     * 
     * @param start 指定日期或月份
     * @param value 需要增减的月份数或天数
     * @return 字符串
     */
    public static String getVorTime(String start, int value) {
        if (start != null) {
            start = start.replaceAll("-", "/");
        } else {
            return "";
        }
        int mode = 2;
        if (start.length() == 7)
            mode = 1;
        else if (start.length() == 10)
            mode = 2;
        else {
            //我处理不了  你回去吧
            return "";
        }
        SimpleDateFormat simpleDateFormat = null;
        if (mode == 1)
            simpleDateFormat = new SimpleDateFormat("yyyy/MM");
        if (mode == 2)
            simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date date = simpleDateFormat.parse(start);
            Calendar calender = Calendar.getInstance();
            calender.setTime(date);
            if (mode == 1)
                calender.add(Calendar.MONTH, value);
            if (mode == 2)
                calender.add(Calendar.DAY_OF_YEAR, value);
            simpleDateFormat.format(calender.getTime());
            return simpleDateFormat.format(calender.getTime()).toString();
        } catch (Exception e) {
            System.out.print("DateUtil error:getVorTime()" + e.getMessage());
        }
        return start;
    }

    /**
     * 2个字符串类型的月份之间差
     * 
     * @param datestr1
     * @param datestr2
     * @return 数值
     * @throws ParseException
     */
    public static int getMonthNum(String datestr1, String datestr2) throws ParseException {
        datestr1 = datestr1.replaceAll("-", "").replaceAll("/", "").replaceAll("\\.", "");
        datestr2 = datestr2.replaceAll("-", "").replaceAll("/", "").replaceAll("\\.", "");
        //System.out.println (datestr1+","+datestr2);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        Date date1 = format.parse(datestr1);
        Date date2 = format.parse(datestr2);
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return (cal2.get(1) - cal1.get(1)) * 12 + (cal2.get(2) - cal1.get(2));
    }

    /**
     * Description: 参数之前时间
     * 
     * @param specifiedDay 需要格式化的时间
     * @param field Calendar类型
     * @param format DEFAULT_FORMAT DEFAULT_FORMAT_TIMESTAMP
     * @param time 要提前的时间
     * @return Date: 2015年12月17日下午3:09:23
     * @author: sunch
     */
    public static String getBeforeTime(String specifiedDay, int field, String format, int time) {
        Calendar c = Calendar.getInstance();
        Date date;
        try {
            date = new SimpleDateFormat(format).parse(specifiedDay);
            c.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        c.set(field, c.get(field) - time);
        String dayBefore = new SimpleDateFormat(format).format(c.getTime());
        return dayBefore;
    }
}
