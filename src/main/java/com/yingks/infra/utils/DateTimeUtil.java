package com.yingks.infra.utils;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 说明：时间工具类
 */
public final class DateTimeUtil 
{
	public static final String ymd = "yyyy-MM-dd";
	public static final String ymdhm = "yyyy-MM-dd HH:mm";
	public static final String ymdhms = "yyyy-MM-dd HH:mm:ss";

	public static final long ONE_MINUTE = 60000L;
    public static final long ONE_HOUR = 3600000L;
    public static final long ONE_DAY = 86400000L;
    public static final long ONE_WEEK = 604800000L;
  
    public static final String ONE_SECOND_AGO = "秒前";
    public static final String ONE_MINUTE_AGO = "分钟前";
    public static final String ONE_HOUR_AGO = "小时前";
    public static final String ONE_DAY_AGO = "天前";
    public static final String ONE_MONTH_AGO = "月前";
    public static final String ONE_YEAR_AGO = "年前";
	
	private static TimeZone timeZone  = TimeZone.getTimeZone("Asia/Shanghai");
	private static Map<Integer, String> weekDays = new HashMap<Integer, String>();
	private static Map<Integer, String> weekDaysEn = new HashMap<Integer, String>();
	
    public static final int ADD_UNIT_DAY = 5;
    public static final int ADD_UNIT_MONTH = 2;
    public static final int ADD_UNIT_YEAR = 1;
    
	static{
		
		weekDays.put(1, "周日");
		weekDays.put(2, "周一");
		weekDays.put(3, "周二");
		weekDays.put(4, "周三");
		weekDays.put(5, "周四");
		weekDays.put(6, "周五");
		weekDays.put(7, "周六");
		
		weekDaysEn.put(1, "周日");
		weekDaysEn.put(2, "周一");
		weekDaysEn.put(3, "周二");
		weekDaysEn.put(4, "周三");
		weekDaysEn.put(5, "周四");
		weekDaysEn.put(6, "周五");
		weekDaysEn.put(7, "周六");
	}
	
	private static SimpleDateFormat getSimpleDateFormat(String format)
	{
		SimpleDateFormat sf = new SimpleDateFormat(format);
		sf.setTimeZone(timeZone);
		
		return sf;
	}
	
	private static Calendar getCalendar()
	{
		return GregorianCalendar.getInstance(timeZone);
	}
	
	/**
	 * @说明 得到格式为 format 的日期Str
	 * @param date 日期
	 * @param format 转化格式
	 * @return String 
	 */
	public static String getDateToStr(Date date, String format) {
		if(date == null) {
			return "";
		}
		if (format == null) {
			format = ymdhm;
		}
		Calendar c = getCalendar();
		c.setTimeInMillis(date.getTime());

		return DateFormatUtils.format(c, format, timeZone, Locale.US);
	}

	public static String getDateToStr(String dateStr,String format1,String format2)
	{
		String strDate = "";
		try
		{
			SimpleDateFormat simpleDateFormat1 = getSimpleDateFormat(format1);
			Date date = simpleDateFormat1.parse(dateStr);
			
			SimpleDateFormat simpleDateFormat2 = getSimpleDateFormat(format2);
			
			strDate = simpleDateFormat2.format(date);
		}
		catch(Exception ex)
		{
		}
		return strDate;
	}
	/**
	 * @说明 得到格式为 format 的日期
	 * @param str
	 * @param format 转化格式
	 * @return Date
	 */
	public static Date getStrToDate(String str,String format)
	{
		Date date = null;
		
		try
		{
			if( null != str)
			{
				SimpleDateFormat simpleDateFormat = getSimpleDateFormat(format);
				
				date = simpleDateFormat.parse(str);
			}
		}
		catch(Exception pe)
		{
			//LogHelper.error(pe.getMessage(),pe);
		}
		return date;
		
	}

	/**
	 * 2015-08-08 TO date start time
	 * @param time
	 * @return
	 */
	public static Date getDayStart(String time) {
		long timestamp = stringToLong(time);
		return getDayStartFromMills(timestamp);
	}

	public static Date getDayEnd(String time) {
		long timestamp = stringToLong(time);
		return getDayEndFromMills(timestamp);
	}

	public static long stringToLong(String time) {
		return stringToLong(time, null);

	}


	public static long stringToLong(String time, String[] format) {
		if (format == null) {
			format = new String[]{
					"yyyy-MM-dd HH:mm:ss"
					, "yyyy-MM-dd HH:mm"
					, "yyyy-MM-dd HH"
					, "yyyy-MM-dd"
					, "yyyyMMdd HH:mm:ss"
					, "yyyyMMdd HH:mm"
					, "yyyyMMdd HH"
					, "yyyyMMdd"
					, "yyyyMM"
					, "yyyy/MM/dd"
					, "yyyy/MM"
					, "yyyy.MM.dd HH:mm:ss"
					, "yyyy.MM.dd HH:mm"
					, "yyyy.MM.dd HH"
					, "yyyy.MM.dd"};
		}
		Date date = null;
		try {
			date = DateUtils.parseDate(time, format);
			return date.getTime();
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Date getTimeStampToDate(int timestamp)
	{
		Calendar calendar = getCalendar();
		calendar.setTimeInMillis((long)timestamp*1000);
		return calendar.getTime();
	}
	public static Date getTimeStampToDate(String timestamp)
	{
		try
		{
			Long l = NumberUtil.parseLong(timestamp);
			Calendar calendar = getCalendar();
			calendar.setTimeInMillis(l);
			return calendar.getTime();
		}
		catch(Exception e)
		{
			return null;
		}
	}
	public static String getTimeStampToString(int timestamp,String format)
	{
		return getDateToStr(getTimeStampToDate(timestamp), format);
	}
	
	/**
	 * @说明 获取系统时间年份
	 * @return int
	 */
	public static int getYear() 
	{
		Calendar calendar = getCalendar();
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * @说明 根据指定的时间返回年份
	 * @param date
	 * @return int
	 */
	public static int getYear(Date date) 
	{
		Calendar calendar = getCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * @说明 返回系统时间月份
	 * 2015-8-31 Gaowx 系统时间比实际月数少一个月
	 * @return int
	 */
	public static int getMonth() 
	{
		Calendar calendar = getCalendar();
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * @说明 根据指定的时间返回月份
	 * @return int
	 */
	public static int getMonth(Date date) 
	{
		Calendar calendar = getCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH);
	}

	/**
	 * @说明 返回当前系统时间的日期(天)
	 * @return int
	 */
	public static int getDay() 
	{
		Calendar calendar = getCalendar();
		return calendar.get(Calendar.DATE);
	}
	
	public static int getHour()
	{
		Calendar calendar = getCalendar();
		return calendar.get(Calendar.HOUR);
	}
	
	public static int getMinute()
	{
		Calendar calendar = getCalendar();
		return calendar.get(Calendar.MINUTE);
	}
	
	/**
	 * 
	 * @Title: getSecond
	 * @author 凤梨/nathena
	 * @Description: 获取当前系统秒
	 * @param @return    设定文件
	 * @return int    返回类型
	 * @throws
	 */
	public static int getSecond()
	{
		Calendar calendar = getCalendar();
		return calendar.get(Calendar.SECOND);
	}
	
	/**
	 * 
	 * @Title: getCurrentClock
	 * @author 凤梨/nathena
	 * @param @return    设定文件
	 * @return Map<Intget,Intger>    返回类型 yyyyMMddmm
	 * @throws
	 */
	public static String getCurrentClock()
	{
		Calendar calendar = getCalendar();
		
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR);
		int second = calendar.get(Calendar.SECOND);
		int minute = calendar.get(Calendar.MINUTE);
		
		StringBuilder clock = new StringBuilder();
		clock.append(year).append(month).append(day);
		clock.append( hour*3600 + second*60 + minute+ 10000);
		
		return clock.toString();
	}

	/**
	 * @说明 根据指定的时间返回日期(天)
	 * @return
	 */
	public static int getDay(Date date) 
	{
		Calendar calendar = getCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.DATE);
	}
	/**
	 * @说明：获取月份有多少天
	 * @创建：作者:yxy	创建时间：2011-5-14
	 * @return
	 */
	public static int getDays(int year,int month)
	{
		Calendar calendar = getCalendar();
		calendar.set(Calendar.YEAR,year); 
		calendar.set(Calendar.MONTH, (month-1));//Java月份才0开始算 
		return calendar.getActualMaximum(Calendar.DATE);
	}
	/**
	 * @说明：获取月份有多少天
	 * @创建：作者:yxy	创建时间：2011-5-14
	 * @return
	 */
	public static int getDays(String tgMonth)
	{
		Calendar calendar = getCalendar();
		
		int year=Integer.valueOf(tgMonth.substring(0, 4));
		int month=Integer.valueOf(tgMonth.substring(5,7));
		calendar.set(Calendar.YEAR,year); 
		calendar.set(Calendar.MONTH, (month-1));//Java月份才0开始算 
		return calendar.getActualMaximum(Calendar.DATE);
	}
	/**
	 * @说明 返回自定义时间
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static Date getDateTime(int year, int month, int day) 
	{
		Calendar calendar = getCalendar();
		
		calendar.set(year, month, day);
		return calendar.getTime();
	}
	
	/**
	 * 返回自定义时间
	 *
	 * @author nathena 
	 * @date 2013-7-31 下午1:26:53 
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param min
	 * @param sec
	 * @return Date
	 */
	public static Date getDateTime(int year, int month, int day,int hour,int min,int sec) 
	{
		Calendar calendar = getCalendar();
		
		calendar.set(year, month, day,hour,min,sec);
		return calendar.getTime();
	}
	
	/**
	 * @说明 日期加减
	 * @param type 5--天  2--月 1--年
	 * @param month
	 * @param day
	 * @return
	 */
	public static Date dateTimeAdd(Date date,int type,int amount) 
	{
		Calendar calendar = getCalendar();
		
		calendar.setTime(date);
		calendar.add(type, amount);
		
		return calendar.getTime();
	}
	/**
	 * @说明 日期加减
	 * @param type 5--天  2--月 1--年
	 * @param month
	 * @param day
	 * @return
	 */
	public static Date dateTimeAdd(String str,int type,int amount,String format)
	{
		try 
		{
			SimpleDateFormat simpleDateFormat = getSimpleDateFormat(format);
			Calendar calendar = getCalendar();
			
			calendar.setTime(simpleDateFormat.parse(str));
			calendar.add(type, amount);
			return calendar.getTime();
		} 
		catch (ParseException e) 
		{
			return null;
		}
	}
	
	/**
	 * @说明 日期加减
	 * @param type 5--天  2--月 1--年
	 * @param amount
	 * @param day
	 * @return
	 */
	public static String dateTimeAddToStr(String str,int type,int amount,String format)
	{
		try 
		{
			SimpleDateFormat simpleDateFormat = getSimpleDateFormat(format);
			Calendar calendar = getCalendar();
			
			calendar.setTime(simpleDateFormat.parse(str));
			calendar.add(type, amount);
			return simpleDateFormat.format(calendar.getTime());
		} 
		catch (ParseException e) 
		{
			return "";
		}
	}
	
	/**
	 * @说明 日期加减
	 * @param type 5--天  2--月 1--年
	 * @param amount
	 * @param day
	 * @return
	 */
	public static int dateTimeAddTimeLine(String str,int type,int amount,String format)
	{
		try 
		{
			SimpleDateFormat simpleDateFormat = getSimpleDateFormat(format);
			Calendar calendar = getCalendar();
			calendar.setTime(simpleDateFormat.parse(str));
			calendar.add(type, amount);
			return (int)Math.ceil(calendar.getTime().getTime()/1000);
		} 
		catch (ParseException e) 
		{
			return 0;
		}
	}
	/**
	 * 
	 *摘要：
	 *@说明：根据日期获取当前时间是周几
	 *@创建：作者:yxy 	创建时间：2011-8-18
	 *@param dte
	 *@return 
	 *@修改历史：
	 *		[序号](yxy	2011-8-18)<修改说明>
	 */
	public static Integer getWeekByDate(Date dte)
	{
		Calendar calendar = getCalendar();
		calendar.setTime(dte);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	public static long getMonthStartTime() {
		Calendar calendar = getCalendar();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime().getTime();
	}

	public static long getWeekStartTime() {
		Calendar calendar = getCalendar();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime().getTime();
	}

	/**
	 * 
	 *摘要：
	 *@说明：根据日期获取当前时间是周几
	 *@创建：作者:yxy 	创建时间：2011-8-18
	 *@param str
	 *@param format
	 *@return
	 *@修改历史：
	 *		[序号](yxy	2011-8-18)<修改说明>
	 */
	public static Integer getWeekByStr(String str,String format)
	{
		try 
		{
			SimpleDateFormat simpleDateFormat = getSimpleDateFormat(format);
			Calendar calendar = getCalendar();
			calendar.setTime(simpleDateFormat.parse(str));
			return calendar.get(Calendar.DAY_OF_WEEK);
		} 
		catch (ParseException e) 
		{
			return null;
		}
	}
	
	public static String getWeekDay(String str,String format,String lang)
	{
		int day = getWeekByStr(str,format);
		return "en_us".equalsIgnoreCase(lang)?weekDaysEn.get(day):weekDays.get(day);
	}
	
	public static String getWeekDay(int dateline,String lang)
	{
		int day = getWeekByDate(getTimeStampToDate(dateline));
		return "en_us".equalsIgnoreCase(lang)?weekDaysEn.get(day):weekDays.get(day);
	}
	/**
	 * 
	 *摘要：
	 *@说明：根据两个日期字符串获取之间相差多少天
	 *@创建：作者:yxy 	创建时间：2011-8-18
	 *@param str
	 *@param format
	 *@return
	 *@修改历史：
	 *		[序号](yxy	2011-8-18)<修改说明>
	 */
	public static Integer getDaysDiff(String str1,String str2,String format)
	{
		SimpleDateFormat simpleDateFormat = getSimpleDateFormat(format);
		try {
			long d1 = simpleDateFormat.parse(str1).getTime();
			long d2 = simpleDateFormat.parse(str2).getTime();
			long t = (d2-d1)/1000;
			Long l = t/(3600*24);
			return l.intValue();
		} catch (ParseException e) {
			return -1;
		}
	}
	
	/**
	 * 
	 *摘要：
	 *@说明：根据两个日期获取之间相差多少天
	 *@创建：作者:gaowx 	创建时间：2015-11-18
	 */
	public static Integer getDaysDiff(Date date1, Date date2) {
		long d1 = date1.getTime();
		long d2 = date2.getTime();
		long t = (d2-d1)/1000;
		Long l = t/(3600*24);
		return l.intValue();
	}
	
	/**
	 * 获取时间錯到秒
	 * @return
	 */
	public static int getTimeStamp()
	{
		Calendar calendar = getCalendar();
		return (int)Math.floor(calendar.getTimeInMillis()/1000);
	}
	
	/**
	 * 获取系统当前秒值
	 *
	 * @author nathena 
	 * @date 2013-5-30 上午10:36:33 
	 * @return Timestamp
	 */
	public static Timestamp currentTimeStamp()
	{
		Calendar calendar = getCalendar();
		return new Timestamp(calendar.getTime().getTime());
	}
	
	public static Date getDate()
	{
		Calendar calendar = getCalendar();
		return calendar.getTime();
	}
	
	/**
	 * 获取当前日期，并按格式化输出
	 *
	 * @author nathena 
	 * @date 2013-7-23 下午2:27:15 
	 * @param format
	 * @return String
	 */
	public static String getCurrentDateString(String format)
	{
		Calendar calendar = getCalendar();
		return getDateToStr(calendar.getTime(), format);
	}
	
	public static String format(Date date) 
	{  
		if( null == date )
			return "";
		
        long delta = getDate().getTime() - date.getTime();  
        if (delta < 1L * ONE_MINUTE) {  
            long seconds = toSeconds(delta);  
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;  
        }  
        if (delta < 45L * ONE_MINUTE) {  
            long minutes = toMinutes(delta);  
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;  
        }  
        if (delta < 24L * ONE_HOUR) {  
            long hours = toHours(delta);  
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;  
        }  
        if (delta < 48L * ONE_HOUR) {  
            return "昨天";  
        }  
        if (delta < 30L * ONE_DAY) {  
            long days = toDays(delta);  
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;  
        }  
        if (delta < 12L * 4L * ONE_WEEK) {  
            long months = toMonths(delta);  
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;  
        } else {  
            long years = toYears(delta);  
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;  
        }  
    }  
  
    private static long toSeconds(long date) {  
        return date / 1000L;  
    }  
  
    private static long toMinutes(long date) {  
        return toSeconds(date) / 60L;  
    }  
  
    private static long toHours(long date) {  
        return toMinutes(date) / 60L;  
    }  
  
    private static long toDays(long date) {  
        return toHours(date) / 24L;  
    }  
  
    private static long toMonths(long date) {  
        return toDays(date) / 30L;  
    }  
  
    private static long toYears(long date) {  
        return toMonths(date) / 365L;  
    }


	public static Date getMonthEndFromMills(long time) {
		Calendar calendar = getCalendar();
		calendar.setTimeInMillis(time);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}

	public static Date getMonthStartFromMills(long time) {
		Calendar calendar = getCalendar();
		calendar.setTimeInMillis(time);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date MonthEnd() {
		Calendar calendar = getCalendar();
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return new Date(calendar.getTimeInMillis());
	}

	public static Date nextMonth(int n) {
		Calendar calendar = getCalendar();
		calendar.add(Calendar.MONTH, n);
		return new Date(calendar.getTimeInMillis());
	}

	/**
	 * 获取月份开始时间
	 * @param next 月份偏移，0表示当月，1表示下个月
	 * @return
	 */
	public static Date getMonthStart(int next) {
		return getMonthStartFromMills(nextMonth(next).getTime());
	}

	/**
	 * 获取月份结束时间
	 * @param next 月份偏移，0表示当月，1表示下个月
	 * @return
	 */
	public static Date getMonthEnd(int next) {
		return getMonthEndFromMills(nextMonth(next).getTime());
	}

	public static Date getTodayStart(){
		return getDayStartFromMills(System.currentTimeMillis());
	}

	public static Date getTodayEnd(){
		Calendar calendar = getCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 59);
		return new Date(calendar.getTimeInMillis());
	}

	public static Date getDayStartFromMills(long mills){
		Calendar calendar = getCalendar();
		calendar.setTimeInMillis(mills);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new Date(calendar.getTimeInMillis());
	}

	public static Date getDayEndFromMills(long mills){
		Calendar calendar = getCalendar();
		calendar.setTimeInMillis(mills);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return new Date(calendar.getTimeInMillis());
	}

	public static Date getNextday(long mills){
		Calendar calendar = getCalendar();
		calendar.setTimeInMillis(mills);
		calendar.add(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new Date(calendar.getTimeInMillis());
	}

	public static Date getDaysLater(long mills, int day){
		Calendar calendar = getCalendar();
		calendar.setTimeInMillis(mills);
		calendar.add(Calendar.DATE, day);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return new Date(calendar.getTimeInMillis());
	}

	public static Date getDaysLater(int day){
		Calendar calendar = getCalendar();
		calendar.add(Calendar.DATE, day);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return new Date(calendar.getTimeInMillis());
	}

	public static String getNextDayString(Date time, String format){
		if(time == null)
			return "";
		Date date = getNextday(time.getTime());
		return getDateToStr(date, format);
	}

	public static Date getYesterdayBegin(){
		Calendar calendar = getCalendar();
		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new Date(calendar.getTimeInMillis());
	}

	public static Date getYesterdayEnd(){
		Calendar calendar = getCalendar();
		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 59);
		return new Date(calendar.getTimeInMillis());
	}

	public static String int2Time(int time) {
		int hour = time/100;
		int minute = time%100;
		String hourStr = hour > 10 ? String.valueOf(hour) : "0" + hour;
		String minuteStr = minute > 10 ? String.valueOf(minute) : "0" + minute;
		return hourStr + ":" + minuteStr;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(getWeekStartTime())));
		System.out.println(getMonthStartTime());
	}
}
