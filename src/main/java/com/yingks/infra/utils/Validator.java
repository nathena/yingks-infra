package com.yingks.infra.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Validator {
	
	//ipv4
	public static final String IPV4 = "^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$";
	//E-mail地址模式
	public static final String EMAIL = "^\\w+([-_\\.]\\w+)*@\\w+([-_\\.]\\w+)*\\.\\w+([-_\\.]\\w+)*$";
	//Url 模式
	public static final String URL = "^http:\\/\\/[A-Za-z0-9]+\\.?[A-Za-z0-9]+[\\/=\\?%\\-&_~`@[\\]\':+!]*([^<>\"\"])*$";
	//Number 模式
	public static final String NUMBER = "^\\d+$";
	//FLOAT模式,上面的只能匹配正整型
	public static final String FLOAT = "^\\d+(\\.\\d+)?$";
	//中文模式
	public static final String CHINESE = "[\\u0391-\\uFFE5]+";
	//一般非法字符 模式
	public static final String UNSAFE = "[~!@#\\$%\\^&\\*\\+\\(\\)\\[\\]\\{\\}<>\\?\\\\/\'\"]+";
	//除了@外的特殊字符 模式
	public static final String TEXTUNSAFE = "[~!#\\$%\\^&\\*\\+\\(\\)\\[\\]\\{\\}<>\\?\\\\/\'\"]+";
	//特殊情况必须过滤字符 模式
	public static final String SPECIALTYUNSAFE = "[\\s<>\\\\/\'\"]+";
	//phone
	public static final String phone = "^(\\d|-){0,30}$";
	//Mobile
//    private static final String mobile = "^((\\d{2,3})|(\\d{3}\\-))?1(3|5|8)\\d{9}$";
//    private static final String mobile = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
	private static final String mobile = "^1(3|5|8|4|7)\\d{9}$";//只验证前两位
	//Email域模式
	public static final String DOMAINEND = "^\\w+([-_\\.]\\w+)*\\.\\w+([-_\\.]\\w+)*$";
	
	
	/**
     * 验证手机
     *
     * @param String $mobile
     * @return boolean
     */
    public static boolean isMobile( String str) throws RuntimeException
    {
    	if(isNull(str))
		{
			return false;
		}
    	Pattern p = Pattern.compile(mobile);		
		Matcher m = p.matcher(str);
		
		return m.find();
    }
	
	public static boolean isDomainEnd(String str) throws RuntimeException
	{
		if(isNull(str))
		{
			return false;
		}
		Pattern p = Pattern.compile(DOMAINEND);		
		Matcher m = p.matcher(str);
		return m.find();
	}
	
	//长度
	public static int LenB(String str) throws RuntimeException
	{
		if(isNull(str))
			return 0;
		return str.length();
	}
	
	//验证EMAIL
	public static boolean isEmail(String str) throws RuntimeException
	{
		if(isNull(str))
			return false;
		Pattern p = Pattern.compile(EMAIL);		
		Matcher m = p.matcher(str);
		
		return m.find();
	}
	
	public static boolean isUrl(String str) throws RuntimeException
	{
		if(isNull(str))
			return false;
		Pattern p = Pattern.compile(URL);		
		Matcher m = p.matcher(str);
		
		return m.find();
	}
	
	
	public static boolean isNumber(String str) throws RuntimeException
	{
		if(isNull(str))
			return false;
		Pattern p = Pattern.compile(NUMBER);		
		Matcher m = p.matcher(str);
		
		return m.find();
	}
	
	public static boolean isChinese(String str) throws RuntimeException
	{
		if(isNull(str))
			return false;
		Pattern p = Pattern.compile(CHINESE);		
		Matcher m = p.matcher(str);
		
		return m.find();
	}
	
	public static boolean isUnsafe(String str) throws RuntimeException
	{
		if(isNull(str))
			return false;
		Pattern p = Pattern.compile(UNSAFE);		
		Matcher m = p.matcher(str);
		
		return m.find();
	}
	
	public static boolean isTextUnsafe(String str) throws RuntimeException
	{
		if(isNull(str))
			return false;
		Pattern p = Pattern.compile(TEXTUNSAFE);		
		Matcher m = p.matcher(str);
		return m.find();
	}
	
	public static boolean isSpecialtyUnsafe(String str) throws RuntimeException
	{
		if(isNull(str))
			return false;
		Pattern p = Pattern.compile(SPECIALTYUNSAFE);		
		Matcher m = p.matcher(str);
		return m.find();
	}
	
	public static boolean isPhone(String str) throws RuntimeException
	{
		if(isNull(str))
			return true;
		Pattern p = Pattern.compile(phone);		
		Matcher m = p.matcher(str);
		return m.find();
	}
	
	public static boolean isNull(String str)
	{
		if(str == null || str.trim().length() == 0 || "null".equalsIgnoreCase(str.trim()))
			return true;
		return false;
	}
	
	public static boolean isFloat(String str) throws RuntimeException
	{
		if(StringUtil.isEmpty(str))
			return false;
		Pattern p = Pattern.compile(FLOAT);		
		Matcher m = p.matcher(str);
		
		return m.find();
	}
	
	public static boolean isIpv4(String str)
	{
		if(StringUtil.isEmpty(str))
			return false;
		
		Pattern p = Pattern.compile(IPV4);		
		Matcher m = p.matcher(str);
		
		if( m.find() )
		{
			String[] seq = str.split("\\.");
			for(String s :seq )
			{
				if( NumberUtil.parseInt(s) > 255 )
				{
					return false;
				}
			}
			return true;
		}
		
		return false;
	}
	
	public static void main(String[] arg)
	{
		String test = "196.2.22.255";
		System.out.println(isIpv4(test));
		
		System.out.println(isMobile("18058768077"));
	}	
}
