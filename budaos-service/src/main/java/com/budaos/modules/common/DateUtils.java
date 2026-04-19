package com.budaos.modules.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {

	/**
	 * 时间格式(yyyy-MM-dd HH:mm:ss)
	 */
	public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 将yyyy-MM-dd HH:mm:ss格式的字符转Date
	 * @param str
	 * @return
	 */
	public static Date convertStringToDate(String str) {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try { 
			date = sdf.parse(str);
		} catch (ParseException e) { 
			e.printStackTrace(); 
		} 
		return date;
	}
	
	/**
	 * 日期转星期
	 * @param datetime yyyy-MM-dd
	 * @return
	 */
    public static String getDayOfWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 指示一个星期中的某天。
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; 
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
 
    /**
	 * <p>获取两日期间所有的日期</p>  
	 * @author huj
	 * @data 2019年11月30日	
	 * @param startTime yyyy-MM-dd
	 * @param endTime yyyy-MM-dd
	 * @return
	 * @apiNote
	 * 假设传入2021-11-08, 2021-11-14
	 * 则返回[2021-12-29, 2021-12-30, 2021-12-31, 2022-01-01, 2022-01-02, 2022-01-03, 2022-01-04, 2022-01-05, 2022-01-06, 2022-01-07]
	 */
	public static List<String> getDays(String startTime, String endTime) {
		// 返回的日期集合
        List<String> days = new ArrayList<String>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
			Date start = dateFormat.parse(startTime);
			Date end = dateFormat.parse(endTime);
			Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);
            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
            	days.add(dateFormat.format(tempStart.getTime()));
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return days;
	}
	
	/**
	 * <p>获取当前日期N天后的日期</p>  
	 * @author huj
	 * @data 2019年11月29日	
	 * @param number 天数，正整数/负整数
	 * @return 传入数字几就返回几天后的日期,格式为yyyy-MM-dd
	 * @apiNote 示例：假设现在时间是2021-12-18，如果传入正数7，那么返回结果为2021-12-25，如果传了负数7，那么返回结果为：2021-12-11
	 */
	public static String getAfterDate(Integer number) {
		Calendar c = Calendar.getInstance();
        Date today = new Date();
		c.add(Calendar.DATE, number);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        c.setTime(today);
        return date;
	}
	
	 
   /** 
    * 获取未来 第 past 天的日期 
    * <p>获取当前日期N天后的日期</p>  
    * @param past 
    * @return 
    */  
	public static String getFetureDate(int past) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
		Date today = calendar.getTime();  
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String result = format.format(today);
		return result;
   } 
	
	/** 
    * 获取过去任意天内的日期数组
    * @param intervals     intervals天内 
    * @return              日期数组 
    */  
   public static List<String> getBeforeDays(Integer intervals) {  
       List<String> pastDaysList = new ArrayList<>();  
       List<String> fetureDaysList = new ArrayList<>();  
       //for (int i = 0; i < intervals; i++) {
       for (int i = intervals; i >= 0; i--) {
           pastDaysList.add(getPastDate(i));
           fetureDaysList.add(getFetureDate(i));
       }  
       return pastDaysList;  
   }
   
	
   /** 
    * 获取过去第几天的日期 
    * 
    * @param past 
    * @return 
    */  
   public static String getPastDate(int past) {
       Calendar calendar = Calendar.getInstance();  
       calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);  
       Date today = calendar.getTime();  
       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
       String result = format.format(today);  
       return result;  
   }
   

	/**
	 * 计算今天属于周几
	 * @return
	 */
	public static String getWeekName() {
		Date today = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		int weekDay = c.get(Calendar.DAY_OF_WEEK);
		return getWeekNameByKey(weekDay);
  	}

	/**
	 * 计算并返回指定日期属于周几
	 * @param day 格式要求为yyyy-MM-dd
	 * @return
	 */
	public static String getWeekName(String day) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = dateFormat.parse(day);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int weekDay = c.get(Calendar.DAY_OF_WEEK);
			return getWeekNameByKey(weekDay);
		} catch (Exception e) {
			return "";
		}
	}

  	private static String getWeekNameByKey(int key) {
		if (key < 0 || key > 7) {
			return "";
		}
		Map<Object, Object> map = new HashMap<>();
		map.put(1, "周日");
		map.put(2, "周一");
		map.put(3, "周二");
		map.put(4, "周三");
		map.put(5, "周四");
		map.put(6, "周五");
		map.put(7, "周六");
		return map.get(key).toString();
	}
  	

	/**
	 * 将传入时间与当前时间进行对比，是否今天\昨天\前天\同一年
	 * @param date
	 * @return
	 */
	public static String getTime(Date date) {
		boolean sameYear = false;
		String todySDF = "HH:mm";
		String yesterDaySDF = "昨天";
		String beforYesterDaySDF = "前天";
		String otherSDF = "MM-dd HH:mm";
		String otherYearSDF = "yyyy-MM-dd HH:mm";
		SimpleDateFormat sfd = null;
		String time = "";
		Calendar dateCalendar = Calendar.getInstance();
		dateCalendar.setTime(date);
		Date now = new Date();
		Calendar todayCalendar = Calendar.getInstance();
		todayCalendar.setTime(now);
		todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
		todayCalendar.set(Calendar.MINUTE, 0);

		if (dateCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR)) {
			sameYear = true;
		} else {
			sameYear = false;
		}

		if (dateCalendar.after(todayCalendar)) {// 判断是不是今天
			sfd = new SimpleDateFormat(todySDF);
			time = sfd.format(date);
			return time;
		} else {
			todayCalendar.add(Calendar.DATE, -1);
			if (dateCalendar.after(todayCalendar)) {// 判断是不是昨天
				sfd = new SimpleDateFormat(todySDF);
				time = yesterDaySDF + " " + sfd.format(date);

				//time = yesterDaySDF;
				return time;
			}
			todayCalendar.add(Calendar.DATE, -1);
			if (dateCalendar.after(todayCalendar)) {// 判断是不是前天

				sfd = new SimpleDateFormat(todySDF);
				time = beforYesterDaySDF + " " + sfd.format(date);
				//time = beforYesterDaySDF;
				return time;
			}
		}

		if (sameYear) {
			sfd = new SimpleDateFormat(otherSDF);
			time = sfd.format(date);
		} else {
			sfd = new SimpleDateFormat(otherYearSDF);
			time = sfd.format(date);
		}

		return time;
	}

	/**
	 * 将时间戳改为yyyy-MM-dd HH:mm:ss
	 * @param timestamp
	 * @return
	 */
	public static String converTimestampToStr(Long timestamp) {
		timestamp = timestamp * 1000;
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date(timestamp));
	}
	
	/**
	 * 将时间戳改为yyyy-MM-dd HH:mm:ss
	 * @param timestampStr
	 * @return
	 */
	public static String converTimestampToStr(String timestampStr) {
		Long timestamp = Long.parseLong(timestampStr) * 1000;
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date(timestamp));
	}
	
}
