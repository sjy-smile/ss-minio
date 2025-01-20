package com.ss.minio.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期格式化
 */
public class DateUtils {

    public final static int ONE_DAY_SECONDS = 86400;

	private final static String dateFormat = "yyyy.MM.dd";

    public final static String shortFormat = "yyyyMMdd";
    public final static String longFormat = "yyyyMMddHHmmss";
    public final static String milliFormat = "yyyyMMddHHmmssSSS";
    public final static String webFormat = "yyyy/MM/dd";
    public final static String timeFormat = "HH:mm:ss";
    public final static String monthFormat = "yyyy-MM";
    public final static String chineseDtFormat = "yyyy年MM月dd日";
    public final static String newFormat = "yyyy-MM-dd HH:mm:ss";
    public final static String dayFormat = "yyyy-MM-dd";
    public final static String noSecondFormat = "yyyy-MM-dd HH:mm";
    public final static int ONE_DAY_MILL_SECONDS = 86400000;

    private DateUtils() {
    }

    /**
     * 得到时间戳格式化后的时间
     * @param time
     * @return
     */
    public static String getDateFormatStr(Long time) {
        if (null == time){
            return null;
        }
        Date date = new Date(time);
        SimpleDateFormat simple = new SimpleDateFormat(dateFormat);
        try {
            simple.setLenient(false);
            return simple.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param start 较小的时间
     * @param end   较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int getDaysBetween(Date start, Date end) {
        SimpleDateFormat sdf = new SimpleDateFormat(newFormat);
        try {
            start = sdf.parse(sdf.format(start));
            end = sdf.parse(sdf.format(end));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        long time1 = cal.getTimeInMillis();
        cal.setTime(end);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }


    /**
     * 只根据天数算时间差
     *
     * @param start
     * @param end
     * @return
     */
    public static int getDayDifference(Date start, Date end) {
        SimpleDateFormat sdf = new SimpleDateFormat(shortFormat);
        try {
            start = sdf.parse(sdf.format(start));
            end = sdf.parse(sdf.format(end));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        long time1 = cal.getTimeInMillis();
        cal.setTime(end);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 将"yyyy-MM-dd HH:mm:ss"格式的字符串转化成日期格式
     *
     * @param sd
     * @return
     */
    public static Date formatString2Date(String sd) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sdf.parse(sd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将"yyyy/MM/dd"格式的字符串转换成日期格式
     *
     * @param sd
     * @return
     */
    public static Date webFormat2Date(String sd) {
        return convert2Date(sd, webFormat);
    }

    /**
     * 将"yyyyMMddHHmmss"格式的字符串转换成日期格式
     *
     * @param sd
     * @return
     */
    public static Date longFormat2Date(String sd) {
        return convert2Date(sd, longFormat);
    }

    /**
     * 将"yyyy-MM-dd"格式的字符串转换成日期格式
     *
     * @param sd
     * @return
     */
    public static Date dayFormat2Date(String sd) {
        return convert2Date(sd, dayFormat);
    }

    /**
     * 两个日期相减得到秒数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int subtraction(Date date1, Date date2) {
        long l = (date2.getTime() - date1.getTime()) / 1000;
        return Integer.valueOf(String.valueOf(l));
    }

    /**
     * 时间加多少秒
     * @param date1
     * @param secs
     * @return
     */

    public static Date addSeconds(Date date1, long secs) {
        return new Date(date1.getTime() + (secs * 1000));
    }

    /**
     * 获取指定格式的日期字符串
     *
     * @param date
     * @param dateFormat
     * @return
     */
    public static String getDateString(Date date, DateFormat dateFormat) {
        if (date == null || dateFormat == null) {
            return "";
        }
        return dateFormat.format(date);
    }


    /**
     * 获取“HH:mm:ss”格式的日期字符串
     *
     * @param date
     * @return
     */
    public final static String getDateHMSString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(timeFormat);
        return getDateString(date, dateFormat);
    }

    /**
     * 获取“yyyyMMddHHmmss”格式的日期字符串
     *
     * @param date
     * @return
     */
    public final static String getLongDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(longFormat);
        return getDateString(date, dateFormat);
    }

    /**
     * 获取“yyyy-MM-dd”格式的日期字符串
     *
     * @param date
     * @return
     */
    public final static String getDayDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(dayFormat);
        return getDateString(date, dateFormat);
    }

    /**
     * 获取“yyyyMMdd”格式的日期字符串
     *
     * @param date
     * @return
     */
    public final static String getShortDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(shortFormat);
        return getDateString(date, dateFormat);
    }

    /**
     * 获取“yyyyMMddHHmmssSSS”格式的日期字符串
     *
     * @param date
     * @return
     */
    public final static String getMilliDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(milliFormat);
        return getDateString(date, dateFormat);
    }

    /**
     * 获取“yyyy-MM-dd HH:mm:ss”格式的日期字符串
     *
     * @param date
     * @return
     */
    public final static String getNewFormat(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(newFormat);
        return getDateString(date, dateFormat);
    }

    /**
     * 获取“yyyy-MM-dd HH:mm”格式的日期字符串
     *
     * @param date
     * @return
     */
    public final static String getNoSecondFormat(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(noSecondFormat);
        return getDateString(date, dateFormat);
    }

    /**
     * 获取“yyyy/MM/dd”格式的日期字符串
     *
     * @param date
     * @return
     */
    public final static String getWebDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(webFormat);
        return getDateString(date, dateFormat);
    }

    /**
     * 获取“yyyy-MM”格式的日期字符串
     *
     * @param date
     * @return
     */
    public final static String getMonthDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(monthFormat);
        return getDateString(date, dateFormat);
    }

    /**
     * 获取“yyyy-MM”格式的日期字符串
     *
     * @param year
     * @param month
     * @return
     */
    public final static String getMonthDateString(int year, int month) {
        Date date = webFormat2Date(year + "/" + month + "/01");
        DateFormat dateFormat = new SimpleDateFormat(monthFormat);
        return getDateString(date, dateFormat);
    }

    /**
     * 获取日期中当前月份的天数
     *
     * @param date
     * @return
     */
    public static int getDaysOfDate(Date date) {
        return getDaysOfMonth(getYear(date), getMonth(date));
    }

    /**
     * 获取日期中当前季度的天数
     *
     * @param date
     * @return
     */
    public static int getDaysOfSeason(Date date) {
        Date startSeason = getCurrentQuarterStartTime(date);
        Date endSeason = getLastDayOfSeason(date);
        return getDaysBetween(startSeason, endSeason);
    }

    /**
     * 当前季度的开始时间，即2012-01-1 00:00:00
     *
     * @return
     */
    public static Date getCurrentQuarterStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 6);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * 获取日期的年
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取日期的月
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取日期的日
     *
     * @return
     */
    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
	
	/**
	 * 获取当天0点0分0秒时间戳
	 *
	 * @return 时间戳
	 */
	public static long getZeroTimestampStart() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

	/**
	 * 获取当天23点59分59秒
	 *
	 * @return 返回时间戳
	 */
	public static long getZeroTimestampDayEnd() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return  cal.getTimeInMillis();
	}


    /**
     * 获取指定月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
        gregorianCalendar.add(Calendar.MONTH, 1);
        gregorianCalendar.add(Calendar.DAY_OF_MONTH, -1);
        return gregorianCalendar.getTime();
    }

    /**
     * 获取指定月的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
        return gregorianCalendar.getTime();
    }
	
	    /**
     * 获取指定月的第一天 0点0分0秒
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonthZeroStart(Date date) {
        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
        gregorianCalendar.set(Calendar.HOUR_OF_DAY, 0);
        gregorianCalendar.set(Calendar.MINUTE, 0);
        gregorianCalendar.set(Calendar.SECOND, 0);
        gregorianCalendar.set(Calendar.MILLISECOND, 0);
        return gregorianCalendar.getTime();
    }

    /**
     * 获取指定月的最后一天 23点59分59秒
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfMonthZeroEnd(Date date) {
        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
        gregorianCalendar.add(Calendar.MONTH, 1);
        gregorianCalendar.add(Calendar.DAY_OF_MONTH, -1);
        gregorianCalendar.set(Calendar.HOUR_OF_DAY, 23);
        gregorianCalendar.set(Calendar.MINUTE, 59);
        gregorianCalendar.set(Calendar.SECOND, 59);
        gregorianCalendar.set(Calendar.MILLISECOND, 999);
        return gregorianCalendar.getTime();
    }

    /**
     * 获取指定时间0点0分0秒时间戳
     *
     * @return 时间戳
     */
    public static Date getZeroTimestampStart(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取指定时间23点59分59秒
     *
     * @return 返回时间戳
     */
    public static Date getZeroTimestampDayEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
	
    /**
     * 获取当前季度的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfSeason(Date date) {
        Calendar gregorianCalendar = Calendar.getInstance();
        gregorianCalendar.setTime(date);
        int currentMonth = gregorianCalendar.get(Calendar.MONTH) + 1;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                gregorianCalendar.set(Calendar.MONTH, 2);
                gregorianCalendar.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                gregorianCalendar.set(Calendar.MONTH, 5);
                gregorianCalendar.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                gregorianCalendar.set(Calendar.MONTH, 8);
                gregorianCalendar.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                gregorianCalendar.set(Calendar.MONTH, 11);
                gregorianCalendar.set(Calendar.DATE, 31);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gregorianCalendar.getTime();
    }

    /**
     * 获取下个月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayNextMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 2);
        calendar.set(Calendar.DATE, 0);
        return calendar.getTime();
    }

    /**
     * 增加月
     *
     * @param date
     * @param amount
     * @return
     */
    public static Date addMonths(Date date, int amount) {
        return add(date, 2, amount);
    }

    /**
     * 减一天
     *
     * @param date
     * @return
     */
    public static Date subtractOneDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
        return c.getTime();
    }

    /**
     * 加一天
     *
     * @param date
     * @return
     */
    public static Date addOneDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
        return c.getTime();
    }

    /**
     * 获取当前季度的最后一个月（自然季）
     *
     * @param date
     * @return
     */
    public static int getQuarterLastMonth(Date date) {
        // 获取当前时间的月份
        int month = getMonth(date);
        // quarter 是第几季度，qLastMonth 是这个季度的最后一个月是几月
        int quarter = (int) Math.floor(month / 3) + 1;
        return quarter * 3;
    }

    /**
     * 增加指定的时间
     *
     * @param date
     * @param calendarField Calendar.MINUTE  类型
     * @param amount
     * @return
     */
    private static Date add(Date date, int calendarField, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }


    /**
     * 增加分钟
     *
     * @param date
     * @param amount
     * @return
     */
    public static Date addMinute(Date date, int amount) {
        return add(date, Calendar.MINUTE, amount);
    }

    /**
     * 把日期字符串格式化成日期类型
     *
     * @param dateStr
     * @param format
     * @return
     */
    private static Date convert2Date(String dateStr, String format) {
        SimpleDateFormat simple = new SimpleDateFormat(format);
        try {
            simple.setLenient(false);
            return simple.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取月份的天数
     *
     * @param year
     * @param month
     * @return
     */
    private static int getDaysOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 时分秒归零
     *
     * @param d
     * @return
     */
    public static Date buildZero(Date d) {
        String temp = getWebDateString(d);
        return webFormat2Date(temp);
    }

    /**
     * 时分最大化
     *
     * @param d
     * @return
     */
    public static Date buildLast(Date d) {
        String year = String.valueOf(getYear(d));
        String month, day;
        if (getMonth(d) < 10) month = "0" + getMonth(d);
        else month = String.valueOf(getMonth(d));
        if (getDay(d) < 10) day = "0" + getDay(d);
        else day = String.valueOf(getDay(d));
        String temp = year + month + day + "235959";
        return longFormat2Date(temp);
    }

    /**
     * 获取两个日期之间的月份数量
     *
     * @param start
     * @param end
     * @return
     */
    public static int getMonthsBetween(Date start, Date end) {
        int startYear = getYear(start);
        int startMonth = getMonth(start);
        int startDay = getDay(start);
        int endYear = getYear(end);
        int endMonth = getMonth(end);
        int endDay = getDay(end);
        int monthsBetweenYear = (endYear - startYear) * 12;
        int monthsBetweenMonth = endMonth - startMonth;
        int result = monthsBetweenYear + monthsBetweenMonth;
        if (endDay > startDay) result++;
        return result;
    }

    /**
     * 毫秒转化
     */
    public static String formatTime(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;
        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;
        String strDay = day < 10 ? "0" + day : "" + day; //天
        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;

        String time = "0分钟";

        if (strDay.equals("00")) {
            if (strDay.equals("00") && strHour.equals("00") && strMinute.equals("00")) {
                time = "0分钟 ";
            } else if (strDay.equals("00") && strHour.equals("00")) {
                time = strMinute + "分钟 ";
            } else {
                time = strHour + "小时" + strMinute + "分钟 ";
            }
        } else {
            time = strDay + "天" + strHour + "小时" + strMinute + "分钟 ";
        }

        return time;
    }

    /**
     * 获取短工单编号
     *
     * @return
     */
    public static String getCode(int n) {
        String string = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ";//保存数字0-9 和 大小写字母
        char[] ch = new char[n]; //声明一个字符数组对象ch 保存 验证码
        for (int i = 0; i < n; i++) {
            Random random = new Random();//创建一个新的随机数生成器
            int index = random.nextInt(string.length());//返回[0,string.length)范围的int值    作用：保存下标
            ch[i] = string.charAt(index);//charAt() : 返回指定索引处的 char 值   ==》保存到字符数组对象ch里面
        }
        //将char数组类型转换为String类型保存到result
        //String result = new String(ch);//方法一：直接使用构造方法      String(char[] value) ：分配一个新的 String，使其表示字符数组参数中当前包含的字符序列。
        String result = String.valueOf(ch);//方法二： String方法   valueOf(char c) ：返回 char 参数的字符串表示形式。
        String yearLast = new SimpleDateFormat("yy", Locale.CHINESE).format(Calendar.getInstance().getTime());
        return yearLast + "_" + result;
    }

    /**
     * 将分钟数转为小时,天
     *
     * @return
     */
    public static String dataConversion(int min) {
        String DateTimes = "";
        long days = min / (60 * 24);
        long hours = (min % (60 * 24)) / (60);
        long minutes = (min % (60));
        if (days > 0) {
            DateTimes = days + "天" + hours + "小时" + minutes + "分钟";
        } else if (hours > 0) {
            DateTimes = hours + "小时" + minutes + "分钟";
        } else if (minutes > 0) {
            DateTimes = minutes + "分钟";
        } else {
            DateTimes = 0 + "分钟";
        }
        return DateTimes;
    }


    /**
     * 格式化日期
     *
     * @param date    日期
     * @param pattern 格式
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        if (date == null)
            return null;
        DateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }


    /**
     * 获取今天是星期几
     *
     * @param dt
     * @return
     */
    public static Integer getWeek(Date dt) {
        Integer[] weekDays = {7, 1, 2, 3, 4, 5, 6};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }


    /**
     * 判断日期是否是本周
     *
     * @param date
     * @return
     */
    public static boolean isThisWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.setTime(date);
        int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        if (paramWeek == currentWeek) {
            return true;
        }
        return false;
    }


    /**
     * 得到系统时间到第二天凌晨的时间差
     * 返回秒数
     *
     * @return
     */
    public static long getSecondDayDifference() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

}
