package com.hds.xquark.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {

  public static Date getYesterdayStart(Date today) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(today);
    calendar.add(Calendar.DATE, -1);
    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    return calendar.getTime();
  }

  public static Date getYesterdayEnd(Date today) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(today);
    calendar.add(Calendar.DATE, -1);
    calendar.set(Calendar.HOUR, 23);
    calendar.set(Calendar.MINUTE, 59);
    return calendar.getTime();
  }

  public static Date getNextDayStart(Date today) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(today);
    calendar.add(Calendar.DATE, 1);
    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.MINUTE, 0);
    return calendar.getTime();
  }

  /**
   * string类型的日期加一天返回
   */
  public static String addOne(String date) {
    String nextDate = "";
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");/*** 加一天*/
    try {
      Date dd = df.parse(date);
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(dd);
      calendar.add(Calendar.DAY_OF_MONTH, 1);//加一天
      nextDate = df.format(calendar.getTime());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return nextDate;
  }

  /**
   * 给日期加秒数
   * @param date 日期
   * @param seconds 秒数时间
   * @return 增加后日期
   */
  public static Date addSeconds(Date date, int seconds) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.SECOND, seconds);
    return cal.getTime();
  }

  /**
   * 判断两个date是否为同一天
   *
   * @param date1 第一个date
   * @param date2 第二个date
   * @return true or false
   */
  public static boolean isSameDay(Date date1, Date date2) {
    if (date1 == null || date2 == null) {
      throw new IllegalArgumentException("The dates must not be null");
    }
    Calendar cal1 = Calendar.getInstance();
    cal1.setTime(date1);
    Calendar cal2 = Calendar.getInstance();
    cal2.setTime(date2);
    return isSameDay(cal1, cal2);
  }

  /**
   * 判断两个Calendar对象是否为同一天
   *
   * @param cal1 第一个Calendar
   * @param cal2 第一个Calendar
   * @return true or false
   */
  public static boolean isSameDay(Calendar cal1, Calendar cal2) {
    if (cal1 == null || cal2 == null) {
      throw new IllegalArgumentException("The dates must not be null");
    }
    return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
        cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
        cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
  }

  /**
   * 判断时期是否为今天
   *
   * @param date Date对象
   * @return true or false
   */
  public static boolean isToday(Date date) {
    return isSameDay(date, Calendar.getInstance().getTime());
  }

  /**
   * 判断Calendar时期是否为今天
   *
   * @param cal Calendar对象
   * @return true or false
   */
  public static boolean isToday(Calendar cal) {
    return isSameDay(cal, Calendar.getInstance());
  }

  /**
   * 判断前一个日期是否在后一个日期之前
   *
   * @param cal1 Calender1
   * @param cal2 Calender2
   * @return true or false
   */
  public static boolean isBeforeDay(Calendar cal1, Calendar cal2) {
    if (cal1 == null || cal2 == null) {
      throw new IllegalArgumentException("The dates must not be null");
    }
    if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) {
      return true;
    }
    if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) {
      return false;
    }
    if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) {
      return true;
    }
    if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) {
      return false;
    }
    return cal1.get(Calendar.DAY_OF_YEAR) < cal2.get(Calendar.DAY_OF_YEAR);
  }

  /**
   * 判断前一个日期是否在后一个日期之前
   *
   * @param date1 date1
   * @param date2 date2
   * @return true or false
   */
  public static boolean isBeforeDay(Date date1, Date date2) {
    if (date1 == null || date2 == null) {
      throw new IllegalArgumentException("The dates must not be null");
    }
    Calendar cal1 = Calendar.getInstance();
    cal1.setTime(date1);
    Calendar cal2 = Calendar.getInstance();
    cal2.setTime(date2);
    return isBeforeDay(cal1, cal2);
  }

  /**
   * 判断前一个日期是否在后一个日期之后
   *
   * @param date1 data1
   * @param date2 date2
   * @return true or false
   */
  public static boolean isAfterDay(Date date1, Date date2) {
    if (date1 == null || date2 == null) {
      throw new IllegalArgumentException("The dates must not be null");
    }
    Calendar cal1 = Calendar.getInstance();
    cal1.setTime(date1);
    Calendar cal2 = Calendar.getInstance();
    cal2.setTime(date2);
    return isAfterDay(cal1, cal2);
  }

  /**
   * 判断前一个日期是否在后一个日期之后
   *
   * @param cal1 calendar1
   * @param cal2 calendar2
   * @return true or false
   */
  public static boolean isAfterDay(Calendar cal1, Calendar cal2) {
    if (cal1 == null || cal2 == null) {
      throw new IllegalArgumentException("The dates must not be null");
    }
    if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) {
      return false;
    }
    if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) {
      return true;
    }
    if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) {
      return false;
    }
    if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) {
      return true;
    }
    return cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR);
  }

  /**
   * 将某个日期转变为天数的结束
   *
   * @param date 日期
   * @return 一天的结束日期
   */
  public static Date getEndOfDay(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    calendar.set(Calendar.MILLISECOND, 999);
    return calendar.getTime();
  }

  /**
   * 将某个日期转变为天数的开始
   *
   * @param date 日期
   * @return 一天的开始日期
   */
  public static Date getStartOfDay(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }

  /**
   * 返回两个日期相隔几天
   *
   * @param d1 日期1
   * @param d2 日期2
   * @return 相隔天数
   */
  public static long getDifferenceDays(Date d1, Date d2) {
    long diff = Math.abs(d2.getTime() - d1.getTime());
    return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
  }

  /**
   * 增加日期
   * @param from 原日期
   * @param year 年
   * @param month 月
   * @param day 日
   * @return 新日期
   */
  public static Date addDate(Date from, Integer year, Integer month, Integer day) {
    Calendar c = Calendar.getInstance();
    c.setTime(from);
    if (year != null) {
      c.add(Calendar.YEAR, year);
    }
    if (month != null) {
      c.add(Calendar.MONTH, month);
    }
    if (day != null) {
      c.add(Calendar.DATE, day);
    }
    return c.getTime();
  }

  /**
   * 日期增加一年
   *
   * @param from 原日期
   * @param year 年数
   * @return 新日期
   */
  public static Date addYear(Date from, Integer year) {
    Calendar c = Calendar.getInstance();
    c.setTime(from);
    c.add(Calendar.YEAR, year);
    return c.getTime();
  }

  /**
   * 日期增加一月
   *
   * @param from 原日期
   * @param month 年数
   * @return 新日期
   */
  public static Date addMonth(Date from, Integer month) {
    Calendar c = Calendar.getInstance();
    c.setTime(from);
    c.add(Calendar.MONTH, month);
    return c.getTime();
  }

  /**
   * 日期增加一天
   *
   * @param from 原日期
   * @param day 天数
   * @return 新日期
   */
  public static Date addDay(Date from, Integer day) {
    Calendar c = Calendar.getInstance();
    c.setTime(from);
    c.add(Calendar.DATE, day);
    return c.getTime();
  }

  public static void main(String[] args) {
    Date now = new Date();
    System.out.println(getDifferenceDays(now, getYesterdayStart(now)));
  }
}
