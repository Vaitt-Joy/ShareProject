package com.vz.share.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {

    public static final String yyyy_MM_dd = "yyyy-MM-dd";

    public static final String yyyy_MM_dd_HH_mm_ss = yyyy_MM_dd + " HH:mm:ss";

    public static Date parse(String dateStr) {
        return parse(dateStr, yyyy_MM_dd, null);
    }

    public static Date parse(String dateStr, String pattern) {
        return parse(dateStr, pattern, null);
    }

    public static Date parse(String dateStr, String pattern, Date defaultValue) {
        if (dateStr == null || dateStr.equals("")) return defaultValue;

        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            LoggerManager.error(e);
            return defaultValue;
        }
    }

    public static String format(Date date) {
        return format(date, yyyy_MM_dd);
    }

    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.format(date);
        } else {
            return null;
        }
    }

    public static String getCurrentDateAndTimeString() {
        Date date = new Date();
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date addDay(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, day);

        return c.getTime();
    }

    public static Date addMonth(Date date, int month) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, month);

        return c.getTime();
    }

    /**
     * 获取昨天的日期
     *
     * @return Date 返回类型
     * @throws
     * @Title getYesterday
     */
    public static Date getYesterday() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        return date;
    }

    /**
     * 获取昨天的日期
     *
     * @return Date 返回类型
     * @throws
     * @Title getYesterday
     */
    public static Date getSevenDaysAgo() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        date = calendar.getTime();
        return date;
    }

    /**
     * 获取n天以前的日期
     *
     * @return
     */
    public static Date getDaysAgo(int day) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1 * day);
        date = calendar.getTime();
        return date;
    }

    public static void main(String argg[]) {
//		Date date = new Date();
//		Date newDate = DateUtil.addDay(date, -3);

        Date newDate = getYesterday();

        System.out.println(DateUtil.format(newDate));
    }

    /**
     * 将时间字符串转换为时间对象
     *
     * @param dateTimeStr 格式：yyyyMMddHHmmss
     * @return Date
     */
    public static Date stringToDateTimeNoSplit(String dateTimeStr) {
        if (!isDateTimeNoSplit(dateTimeStr)) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = null;
        try {
            date = df.parse(dateTimeStr.trim());
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date;
    }

    /**
     * 判断是否是日期时间
     *
     * @param strDate "yyyyMMddHHmmss"
     * @return false不是，true为是
     */
    public static boolean isDateTimeNoSplit(String strDate) {
        if (strDate == null) {
            return false;
        }
        String date = "^(((((1[6-9]|[2-9]\\d)\\d{2})(0?[13578]|1[02])(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})(0?[13456789]|1[012])(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})0?2(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))0?229))((([0-1]{1})([0-9]{1}))|(2?[0-3]))([0-5][0-9])([0-5][0-9]))";
        Pattern p1 = Pattern.compile(date);
        Matcher m1 = p1.matcher(strDate);
        if (m1.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 返回格式化的时间字符串，格式：yyyy-MM-dd HH:mm
     *
     * @return
     */
    public static String fotmatDateNoSecond(Date myDate) {
        if (myDate == null)
            myDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String strDate = formatter.format(myDate);
        return strDate;
    }

    /**
     * 返回格式化的时间字符串，格式：yyyy-MM-dd HH:mm:ss
     *
     * @param myDate
     * @return
     */
    public static String fotmatDateWithSecond(Date myDate) {
        if (myDate == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = formatter.format(myDate);
        return strDate;
    }

    /**
     * @param strDate 日期字符串
     * @param fal     　日期格式　如yyyy-MM-dd hh:mm:ss
     * @return Date类型如为null转换失败
     */
    public static Date getDate(String strDate, String fal) {
        try {
            SimpleDateFormat dateFarmat;
            dateFarmat = new SimpleDateFormat(fal);
            Date utilDate = dateFarmat.parse(strDate);
            return utilDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回格式化的时间字符串，格式：yyyyMMddHHmmss
     *
     * @param myDate
     * @return
     */
    public static String fotmatDateTimeNoSplit(Date myDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String strDate = formatter.format(myDate);
        return strDate;
    }

    /**
     * 将时间字符串转换为时间对象
     *
     * @param aValue 格式：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date stringToUtilDateWithTime(String aValue) {
        /*
		 * if (!isDateTime(aValue)) { return null; }
		 */
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = df.parse(aValue.trim());
        } catch (ParseException ex) {
            df = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
            try {
                date = df.parse(aValue.trim());
            } catch (ParseException e) {
                df = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date = df.parse(aValue.trim());
                } catch (ParseException e1) {
                    df = new SimpleDateFormat("yyyy-M-dd");
                    try {
                        date = df.parse(aValue.trim());
                    } catch (ParseException e2) {
                        df = new SimpleDateFormat("yyyyMMdd");
                        try {
                            date = df.parse(aValue.trim());
                        } catch (ParseException e3) {
                            e3.printStackTrace();
                        }
                    }
                }
            }
        }
        return date;
    }
}
