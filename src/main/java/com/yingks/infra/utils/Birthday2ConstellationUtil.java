package com.yingks.infra.utils;

import java.util.Date;

import com.yingks.infra.utils.DateTimeUtil;
import com.yingks.infra.utils.StringUtil;

/**  
 * @Title: UserBirthdayUtil.java
 * @Package com.jytnn.yf.domain.utils
 * @Description: 用户生日相关的工具类,根据生日计算年龄和星座
 * @author 青梅
 * @date 2015-3-18 下午1:51:05
 * @version V1.0 
 * @UpdateHis:
 *      TODO  
 */
public class Birthday2ConstellationUtil {
	
    private final static int[] dayArr = new int[] { 20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22 };  
    private final static String[] constellationArr = new String[] {"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座" };  

	public static int getAgeByBirthday(String birthday) {
		if(!StringUtil.isEmpty(birthday)) {
			int a = DateTimeUtil.getYear(DateTimeUtil.getStrToDate(birthday, "yyyy-MM-dd"));
			int b = DateTimeUtil.getYear();
			if(b >= a) {
				return b - a;
			}
		}
		
		return 0;
	}
	
	public static int getAgeByBirthday(Date birthday) {
		if(birthday != null) {
			int a = DateTimeUtil.getYear(birthday);
			int b = DateTimeUtil.getYear();
			if(b >= a) {
				return b - a;
			}
		}
		
		return 0;
	}
	
	public static String getConstellationByBirthday(String birthday) {
		if(StringUtil.isEmpty(birthday)) {
			return null;
		}
		
		int month = DateTimeUtil.getMonth(DateTimeUtil.getStrToDate(birthday, "yyyy-MM-dd"));
		int day = DateTimeUtil.getDay(DateTimeUtil.getStrToDate(birthday, "yyyy-MM-dd"));
		
		if(0 < month && 13 > month && 0 < day && 32 > day) {
			return day < dayArr[month - 1] ? constellationArr[month - 1] : constellationArr[month];  
		} else {
			return "摩羯座";//默认摩羯座
		}
	}
	
	public static String getConstellationByBirthday(Date birthday) {
		if(birthday == null) {
			return null;
		}
		
		int month = DateTimeUtil.getMonth(birthday);
		int day = DateTimeUtil.getDay(birthday);
		
		if(0 < month && 13 > month && 0 < day && 32 > day) {
			return day < dayArr[month - 1] ? constellationArr[month - 1] : constellationArr[month];  
		} else {
			return "摩羯座";//默认摩羯座
		}
	}
	
	public static void main(String[] args) {
		System.out.print(getConstellationByBirthday("1991-2-11"));
	}
}
