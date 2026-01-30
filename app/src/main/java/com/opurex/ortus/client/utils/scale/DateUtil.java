package com.opurex.ortus.client.utils.scale;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	/**
	 * 日期转化为字符串
	 * 
	 * @param date
	 *            具体的日期
	 * @param parser
	 *            解析器格式
	 * @return String 格式化后的日期字符串
	 */
	public static String formatDate(Date date, String parser) {
		DateFormat simpleDateFormat = new SimpleDateFormat(parser);
		String dateString = "" ;
		try {
			dateString = simpleDateFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateString;
	}

	/**
	 * 字符串转化为日期格式
	 * 
	 * @param dateStr
	 *            日期字符串
	 * @param formatStr
	 *            转化器格式
	 * @return Date
	 * 
	 *         格式化后的日期
	 */
	public static Date StringToDate(String dateStr, String formatStr) {
		DateFormat dd = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = dd.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

}
