package com.gmy.blog.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具
 * 
 * @author GMY
 *
 */
public class DateUtil {
	/**
	 * 判断当前时间与传入时间差 显示不同
	 * 
	 * @param date
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String paselDate(String date) {
		Date current = null, old = null;
		if (date != null && !date.equals("")) {
			date = date.substring(0, 18);
			SimpleDateFormat d_full = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat d_hours = new SimpleDateFormat("HH:mm");
			SimpleDateFormat d_month = new SimpleDateFormat("MM-dd");
			SimpleDateFormat d_year = new SimpleDateFormat("yyyy-MM");
			current = new Date(System.currentTimeMillis());// 获取当前时间
			try {
				old = d_full.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			String str_hours = d_hours.format(old);
			String str_month = d_month.format(old);
			String str_year = d_year.format(old);

			long diff = current.getTime() - old.getTime();// 这样得到的差值是微秒级别
			long days = diff / (1000 * 60 * 60 * 24);
			long hours = (diff - days * (1000 * 60 * 60 * 24))
					/ (1000 * 60 * 60);
			long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
					* (1000 * 60 * 60))
					/ (1000 * 60);
			if (days == 0) {
				if (hours == 0) {
					if (minutes <= 2) {
						return "刚刚";
					} else {
						return minutes + "分钟前";
					}
				} else {
					return hours + "小时前";
				}
			} else if (days == 1) {
				return "昨天" + str_hours;
			} else if (days < 365) {
				return str_month;
			} else {
				return str_year;
			}
		} else {
			return "";
		}

	}
}
