/*
 * Copyright (C) 2015-2018 S.Violet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Project GitHub: https://github.com/shepherdviolet/thistle
 * Email: shepherdviolet@163.com
 */

package sviolet.thistle.util.conversion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具
 * 
 * @author S.Violet ()
 *
 */

public class DateTimeUtils {

    /**************************************************
     * 获得当前日期相关
     */

    private static ThreadLocal<SimpleDateFormat> defaultDateFormat = new ThreadLocal<>();
	private static ThreadLocal<SimpleDateFormat> defaultTimeFormat = new ThreadLocal<>();
	private static ThreadLocal<SimpleDateFormat> defaultDateTimeFormat = new ThreadLocal<>();

	private static SimpleDateFormat getDefaultDateFormat(){
		SimpleDateFormat format = defaultDateFormat.get();
		if (format == null) {
			format = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
			defaultDateFormat.set(format);
		}
		return format;
	}

	private static SimpleDateFormat getDefaultTimeFormat(){
		SimpleDateFormat format = defaultTimeFormat.get();
		if (format == null) {
			format = new SimpleDateFormat("HH:mm:ss.SSS", Locale.SIMPLIFIED_CHINESE);
			defaultTimeFormat.set(format);
		}
		return format;
	}

    private static SimpleDateFormat getDefaultDateTimeFormat(){
		SimpleDateFormat format = defaultDateTimeFormat.get();
		if (format == null) {
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.SIMPLIFIED_CHINESE);
			defaultDateTimeFormat.set(format);
		}
		return format;
	}

	/**
	 * 获得当前日期
	 */
	public static String getDate(){
		return getDefaultDateFormat().format(new Date());
	}
	
	/**
	 * 获得当前时间
	 */
	public static String getTime(){
		return getDefaultTimeFormat().format(new Date());
	}
	
	/**
	 * 获得当前日期和时间
	 */
	public static String getDateTime(){
		return getDefaultDateTimeFormat().format(new Date());
	}
	
	/**
	 * 获得指定格式的时间
	 * @param template 格式
	 */
	public static String getDateTime(String template){
		SimpleDateFormat format = new SimpleDateFormat(template, Locale.SIMPLIFIED_CHINESE);
		return format.format(new Date());
	}
	
	/**
	 * 得到当前毫秒值
	 */
	public static long getCurrentTimeMillis(){
		return System.currentTimeMillis();
	}

	/**
	 * 获得当前纳秒时间, 该时间仅用于计算程序经过时间, 不保证精确
     */
	public static long getNanoTime(){
		return System.nanoTime();
	}

    /*************************************************
     * 指定时间相关
     */
	
	/**
	 * 根据timeMillis获得日期
	 */
	public static String getDate(long timeMillis){
		return SimpleDateFormat.getDateInstance().format(new Date(timeMillis));
	}
	
	/**
	 * 根据timeMillis获得时间
	 */
	public static String getTime(long timeMillis){
		return SimpleDateFormat.getTimeInstance().format(new Date(timeMillis));
	}
	
	/**
	 * 根据timeMillis获得日期和时间
	 */
	public static String getDateTime(long timeMillis){
		return SimpleDateFormat.getDateTimeInstance().format(new Date(timeMillis));
	}
	
	/**
	 * 根据timeMillis获得指定格式的时间
	 * @param template 格式
	 */
	public static String getDateTime(String template, long timeMillis){
		SimpleDateFormat format = new SimpleDateFormat(template, Locale.SIMPLIFIED_CHINESE);
		return format.format(new Date(timeMillis));
	}

    /**
     * 根据Date获得日期
     */
    public static String getDate(Date date){
        return SimpleDateFormat.getDateInstance().format(date);
    }

    /**
     * 根据Date获得时间
     */
    public static String getTime(Date date){
        return SimpleDateFormat.getTimeInstance().format(date);
    }

    /**
     * 根据Date获得日期和时间
     */
    public static String getDateTime(Date date){
        return SimpleDateFormat.getDateTimeInstance().format(date);
    }

    /**
     * 根据Date获得指定格式的时间
     * @param template 格式
     */
    public static String getDateTime(String template, Date date){
        SimpleDateFormat format = new SimpleDateFormat(template, Locale.SIMPLIFIED_CHINESE);
        return format.format(date);
    }

	/*******************************************
	 *  String转换为日期
	 */

	/**
	 * 将字符串按格式转换为Date
	 * @param dateStr 日期字符串
	 * @param pattern 格式, 例如yyyy-MM-dd HH-mm-ss
	 * @throws ParseException
	 */
	public static Date parseDate(String dateStr, String pattern) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.SIMPLIFIED_CHINESE);
		return format.parse(dateStr);
	}

	/**
	 * 将字符串按格式转换为Date, 若转换异常则返回fallback值
	 * @param dateStr 日期字符串
	 * @param pattern 格式, 例如yyyy-MM-dd HH-mm-ss
	 * @param fallback 若转换异常则返回该值
	 */
	public static Date parseDate(String dateStr, String pattern, Date fallback) {
		SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.SIMPLIFIED_CHINESE);
		try {
			return format.parse(dateStr);
		} catch (ParseException e) {
			return fallback;
		}
	}

    /********************************************
     * 日期计算
     */


}
