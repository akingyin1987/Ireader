package com.akingyin.base.utils;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *   日期类工具
 * @ Description:
 * @ Author king
 * @ Date 2017/2/22 17:19
 *
 * @ Version V1.0
 * 注意：SimpleDateFormat不是线程安全的，线程安全需用{@code ThreadLocal<SimpleDateFormat>}
 */

public class DateUtil {

  private   DateUtil(){
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

  public static final String YYYY_MM_DD="yyyy-MM-dd";

  public static final String HH_MM="HH:mm";

  public static final String HH_MM_SS="HH:mm:ss";

  private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<>();
  private static final ThreadLocal<SimpleDateFormat> SDF_THREAD_LOCAL = new ThreadLocal<>();

  private static final Object object = new Object();

  /******************** 时间相关常量 ********************/
  /**
   * 秒与毫秒的倍数
   */
  public static final int SEC  = 1000;
  /**
   * 分与毫秒的倍数
   */
  public static final int MIN  = 60000;
  /**
   * 时与毫秒的倍数
   */
  public static final int HOUR = 3600000;
  /**
   * 天与毫秒的倍数
   */
  public static final int DAY  = 86400000;

  public enum TimeUnit {
    MSEC,
    SEC,
    MIN,
    HOUR,
    DAY

  }

  /**
   * 获取SimpleDateFormat
   *
   * @param pattern
   *            日期格式
   * @return SimpleDateFormat对象
   * @throws RuntimeException
   *             异常：非法日期格式
   */
  private static SimpleDateFormat getDateFormat(String pattern)
          throws RuntimeException {
    SimpleDateFormat dateFormat = threadLocal.get();
    if (dateFormat == null) {
      synchronized (object) {
        if (dateFormat == null) {
          dateFormat = new SimpleDateFormat(pattern);
          dateFormat.setLenient(false);
          threadLocal.set(dateFormat);
        }
      }
    }
    dateFormat.applyPattern(pattern);
    return dateFormat;
  }


  private static SimpleDateFormat getDefaultFormat() {
    SimpleDateFormat simpleDateFormat = SDF_THREAD_LOCAL.get();
    if (simpleDateFormat == null) {
      simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
      SDF_THREAD_LOCAL.set(simpleDateFormat);
    }
    return simpleDateFormat;
  }

  /**
   * 将时间戳转为时间字符串
   * @param millis
   * @return
   */
  public static String millis2String(Long millis,SimpleDateFormat  simpleDateFormat) {
    if(null == millis){
      return StringUtils.DEFAULT_EMPTY;
    }
    return simpleDateFormat.format(new Date(millis));
  }

  /**
   * 将时间戳转为时间字符串
   * @param millis
   * @return
   */
  public static String millis2String(Long millis) {
    if(null == millis){
      return StringUtils.DEFAULT_EMPTY;
    }
    return getDefaultFormat().format(new Date(millis));
  }

  /**
   * 将时间转为指定格式字符串
   * @param millis
   * @param pattern
   * @return
   */
  public static String millis2String(Long millis, String pattern) {
    if(null == millis){
      return  StringUtils.DEFAULT_EMPTY;
    }
    return getDateFormat(pattern).format(new Date(millis));
  }

  public static long string2Millis(String time) {
    return string2Millis(time, DEFAULT_PATTERN);
  }


  /**
   * 将时间字符串转为时间戳
   * <p>time格式为pattern</p>
   *
   * @param time    时间字符串
   * @param pattern 时间格式
   * @return 毫秒时间戳
   */

  public static long string2Millis(String time, String pattern) {
    try {
      return new SimpleDateFormat(pattern, Locale.getDefault()).parse(time).getTime();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return -1;
  }

  /**
   * 将时间字符串转为Date类型
   * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
   *
   * @param time 时间字符串
   * @return Date类型
   */
   public static Date string2Date(String time) {
    return string2Date(time, DEFAULT_PATTERN);
  }

  /**
   * 将时间字符串转为Date类型
   * <p>time格式为pattern</p>
   *
   * @param time    时间字符串
   * @param pattern 时间格式
   * @return Date类型
   */
  public static Date string2Date(String time, String pattern) {
    return new Date(string2Millis(time, pattern));
  }


  /**
   * 将Date类型转为时间字符串
   * <p>格式为yyyy-MM-dd HH:mm:ss</p>
   *
   * @param date Date类型时间
   * @return 时间字符串
   */
  public static String date2String(Date date) {
    return date2String(date, DEFAULT_PATTERN);
  }

  /**
   * 将Date类型转为时间字符串
   * <p>格式为pattern</p>
   *
   * @param date    Date类型时间
   * @param pattern 时间格式
   * @return 时间字符串
   */
  public static String date2String(Date date, String pattern) {
    return new SimpleDateFormat(pattern, Locale.getDefault()).format(date);
  }

  /**
   * 将Date类型转为时间戳
   *
   * @param date Date类型时间
   * @return 毫秒时间戳
   */
  public static long date2Millis(Date date) {
    return date.getTime();
  }

  /**
   * 将时间戳转为Date类型
   *
   * @param millis 毫秒时间戳
   * @return Date类型时间
   */
  public static Date millis2Date(long millis) {
    return new Date(millis);
  }

  /**
   * 获取两个时间差（单位：unit）
   * <p>time0和time1格式都为yyyy-MM-dd HH:mm:ss</p>
   *
   * @param time0 时间字符串0
   * @param time1 时间字符串1
   * @param unit  单位类型

   * @return unit时间戳
   */
  public static long getTimeSpan(String time0, String time1, TimeUnit unit) {
    return getTimeSpan(time0, time1, unit, DEFAULT_PATTERN);
  }

  /**
   * 获取两个时间差（单位：unit）
   * <p>time0和time1格式都为format</p>
   *
   * @param time0   时间字符串0
   * @param time1   时间字符串1
   * @param unit    单位类型

   * @param pattern 时间格式
   * @return unit时间戳
   */
  public static long getTimeSpan(String time0, String time1, TimeUnit unit, String pattern) {

    return millis2TimeSpan(Math.abs(string2Millis(time0, pattern) - string2Millis(time1, pattern)), unit);
  }

  /**
   * 获取两个时间差（单位：unit）
   *
   * @param date0 Date类型时间0
   * @param date1 Date类型时间1
   * @param unit  单位类型

   * @return unit时间戳
   */
  public static long getTimeSpan(Date date0, Date date1,TimeUnit unit) {
    return millis2TimeSpan(Math.abs(date2Millis(date0) - date2Millis(date1)), unit);
  }


  /**
   * 毫秒时间戳转以unit为单位的时间长度
   *
   * @param millis 毫秒时间戳
   * @param unit   单位类型

   * @return 以unit为单位的时间长度
   */
  public static long millis2TimeSpan(long millis, TimeUnit unit) {
    switch (unit) {
      default:
      case MSEC:
        return millis;
      case SEC:
        return millis / SEC;
      case MIN:
        return millis / MIN;
      case HOUR:
        return millis / HOUR;
      case DAY:
        return millis / DAY;
    }
  }
  /**
   * 获取当前毫秒时间戳
   *
   * @return 毫秒时间戳
   */
  public static long getNowTimeMills() {
    return System.currentTimeMillis();
  }

  /**
   * 获取当前时间字符串
   * <p>格式为yyyy-MM-dd HH:mm:ss</p>
   *
   * @return 时间字符串
   */
  public static String getNowTimeString() {
    return millis2String(System.currentTimeMillis(), DEFAULT_PATTERN);
  }

  /**
   * 获取当前时间字符串
   * <p>格式为pattern</p>
   *
   * @param pattern 时间格式
   * @return 时间字符串
   */
  public static String getNowTimeString(String pattern) {
    return millis2String(System.currentTimeMillis(), pattern);
  }

  /**
   * 获取友好型与当前时间的差
   * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
   *
   * @param time 时间字符串
   * @return 友好型与当前时间的差
   * <ul>
   * <li>如果小于1秒钟内，显示刚刚</li>
   * <li>如果在1分钟内，显示XXX秒前</li>
   * <li>如果在1小时内，显示XXX分钟前</li>
   * <li>如果在1小时外的今天内，显示今天15:32</li>
   * <li>如果是昨天的，显示昨天15:32</li>
   * <li>其余显示，2016-10-15</li>
   * <li>时间不合法的情况全部日期和时间信息，如星期六 十月 27 14:21:20 CST 2007</li>
   * </ul>
   */
  public static String getFriendlyTimeSpanByNow(String time) {
    return getFriendlyTimeSpanByNow(time, DEFAULT_PATTERN);
  }

  public static String getFriendlyTimeSpanByNow(String time, String pattern) {
    return getFriendlyTimeSpanByNow(string2Millis(time, pattern));
  }

  public static String getFriendlyTimeSpanByNow(Date date) {
    return getFriendlyTimeSpanByNow(date.getTime());
  }

  /**
   * 获取友好型与当前时间的差
   *
   * @param millis 毫秒时间戳
   * @return 友好型与当前时间的差
   * <ul>
   * <li>如果小于1秒钟内，显示刚刚</li>
   * <li>如果在1分钟内，显示XXX秒前</li>
   * <li>如果在1小时内，显示XXX分钟前</li>
   * <li>如果在1小时外的今天内，显示今天15:32</li>
   * <li>如果是昨天的，显示昨天15:32</li>
   * <li>其余显示，2016-10-15</li>
   * <li>时间不合法的情况全部日期和时间信息，如星期六 十月 27 14:21:20 CST 2007</li>
   * </ul>
   */
  @SuppressLint("DefaultLocale")
  public static String getFriendlyTimeSpanByNow(long millis) {
    long now = System.currentTimeMillis();
    long span = now - millis;
    if (span < 0) {
      return String.format("%tc",
          millis);// U can read http://www.apihome.cn/api/java/Formatter.html to understand it.
    }
    if (span < 1000) {
      return "刚刚";
    } else if (span < MIN) {
      return String.format("%d秒前", span / SEC);
    } else if (span < HOUR) {
      return String.format("%d分钟前", span / MIN);
    }
    // 获取当天00:00
    long wee = (now / DAY) * DAY - 8 * HOUR;
    if (millis >= wee) {
      return String.format("今天%tR", millis);
    } else if (millis >= wee - DAY) {
      return String.format("昨天%tR", millis);
    } else {
      return String.format("%tF", millis);
    }
  }


  /**
   * 当天的开始时间
   * @return
   */
  public static long startOfTodDay() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    Date date=calendar.getTime();
    return date.getTime();
  }

  /**
   * 当天的结束时间
   * @return
   */
  public static long endOfTodDay() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    calendar.set(Calendar.MILLISECOND, 999);
    Date date=calendar.getTime();
    return date.getTime();
  }


  public static long startOfThisWeek() {// 当周开始时间
    Calendar currentDate = Calendar.getInstance();
    currentDate.setFirstDayOfWeek(Calendar.MONDAY);
    currentDate.set(Calendar.HOUR_OF_DAY, 0);
    currentDate.set(Calendar.MINUTE, 0);
    currentDate.set(Calendar.SECOND, 0);
    currentDate.set(Calendar.MILLISECOND, 0);
    currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    Date date=currentDate.getTime();
    return date.getTime();
  }

  /**
   * 功能：获取本周的结束时间 示例：2013-05-19 23:59:59
   */
  public static long endOfThisWeek() {// 当周结束时间
    Calendar currentDate = Calendar.getInstance();
    currentDate.setFirstDayOfWeek(Calendar.MONDAY);
    currentDate.set(Calendar.HOUR_OF_DAY, 23);
    currentDate.set(Calendar.MINUTE, 59);
    currentDate.set(Calendar.SECOND, 59);
    currentDate.set(Calendar.MILLISECOND, 999);
    currentDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
    Date date=currentDate.getTime();
    return date.getTime();
  }
  /**
   * 功能：获取本月的开始时间
   */
  public static long startOfThisMonth() {// 当周开始时间
    Calendar currentDate = Calendar.getInstance();
    currentDate.set(Calendar.HOUR_OF_DAY, 0);
    currentDate.set(Calendar.MINUTE, 0);
    currentDate.set(Calendar.SECOND, 0);
    currentDate.set(Calendar.MILLISECOND, 0);
    currentDate.set(Calendar.DAY_OF_MONTH, 1);
    Date date=currentDate.getTime();
    return date.getTime();
  }

  /**
   * 本月结束时间
   * @return
   */
  public static long endOfThisMonth() {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.DAY_OF_MONTH, 1);
    cal.set(Calendar.HOUR_OF_DAY, 23);
    cal.set(Calendar.MINUTE, 59);
    cal.set(Calendar.SECOND, 59);
    cal.set(Calendar.MILLISECOND, 999);
    cal.add(Calendar.MONTH, 1);
    cal.add(Calendar.DATE, -1);
    Date date=cal.getTime();
    return date.getTime();
  }
}
