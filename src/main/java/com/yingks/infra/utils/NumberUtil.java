package com.yingks.infra.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public final class NumberUtil 
{
	private final static String[] chineseNumber = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
	private final static String[] chineseIntBit = { "元", "拾", "佰", "仟" };
	private final static String[] chineseLongBit = { "万", "亿" };
	private final static String[] chineseDecBit = { "角", "分" };
	private final static String chineseZero = "整";
	
	public static boolean isBoolean(Byte val)
	{
		if( null == val || val == 0 )
			return false;
		
		return true;
	}
	
	public static boolean isBoolean(Integer val)
	{
		if( null == val || val == 0 )
			return false;
		
		return true;
	}
	
	public static boolean isBoolean(Long val)
	{
		if( null == val || val == 0 )
			return false;
		
		return true;
	}
	
	public static boolean isBoolean(Float val)
	{
		if( null == val || val == 0 )
			return false;
		
		return true;
	}
	
	public static boolean isBoolean(Short val)
	{
		if( null == val || val == 0 )
			return false;
		
		return true;
	}
	
	public static boolean isBoolean(Double val)
	{
		if( null == val || val == 0 )
			return false;
		
		return true;
	}
	
	public static float formatFloat(Object val)
	{
		try
		{
			DecimalFormat df = new DecimalFormat("0.0");
			return Float.parseFloat(df.format( parseDouble(val) ));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			//LogHelper.error(e.getMessage(), e);
		}
		
		return 0.00f;
		
	}
	
	public static double formatDouble(Object val)
	{
		try
		{
			DecimalFormat df = new DecimalFormat("0.0");
			return Double.parseDouble(df.format( parseDouble(val) ));
		}
		catch(Exception e)
		{
			LogHelper.error(e.getMessage(), e);
		}
		
		return 0.00;
	}
	
	public static int parseInt(Object str)
	{
		try
		{
			if( !StringUtil.isEmpty(str) )
			{
				return Integer.parseInt(format(str,"#"));
			}
			return 0;
		}
		catch(Exception e)
		{
			LogHelper.error(e.getMessage(), e);
		}
		
		return 0;
	}
	
	public static float parseFloat(Object str)
	{
		try
		{
			if( !StringUtil.isEmpty(str) )
			{
				return Float.parseFloat(str.toString());
			}
			return 0;
		}
		catch(Exception e)
		{
			LogHelper.error(e.getMessage(), e);
		}
		
		return 0;
	}
	
	public static double parseDouble(Object str)
	{
		try
		{
			if( !StringUtil.isEmpty(str) )
			{
				return Double.parseDouble(str.toString());
			}
			return 0;
		}
		catch(Exception e)
		{
			LogHelper.error(e.getMessage(), e);
		}
		
		return 0;
	}
	
	public static long parseLong(Object str)
	{
		try
		{
			if( !StringUtil.isEmpty(str) )
			{
				return Long.parseLong(format(str,"#"));
			}
			return 0;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			//LogHelper.error(e.getMessage(), e);
		}
		
		return 0;
	}
	
	public static byte parseByte(Object str)
	{
		try
		{
			if( !StringUtil.isEmpty(str) )
			{
				if( "true".equalsIgnoreCase(str.toString()))
				{
					str = "1";
				}
				else if( "false".equalsIgnoreCase(str.toString()) )
				{
					str = "0";
				}
				
				return Byte.parseByte(format(str,"#"));
			}
			return 0;
		}
		catch(Exception e)
		{
			LogHelper.error(e.getMessage(), e);
		}
		
		return 0;
	}
	
	public static short parseShort(Object str)
	{
		try
		{
			if( !StringUtil.isEmpty(str) )
				return Short.parseShort(str.toString());
			
			return 0;
		}
		catch(Exception e)
		{
			LogHelper.error(e.getMessage(), e);
		}
		
		return 0;
	}
	
	public static BigDecimal parseBigDecimal(Object obj) 
	{
		
		try
		{
			if( !StringUtil.isEmpty(obj) )
				return new BigDecimal(obj.toString());
			
			return null;
		}
		catch(Exception e)
		{
			LogHelper.error(e.getMessage(), e);
		}
		
		return null;
	}
	
	public static int parseInt(Object[] strArr,int index)
	{
		try
		{
			if( null == strArr || strArr.length == 0 || strArr.length <= index)
				return 0;
			
			return parseInt(strArr[index]);
		}
		catch(Exception e)
		{
			LogHelper.error(e.getMessage(), e);
		}
		
		return 0;
	}
	
	public static float parseFloat(Object[] strArr,int index)
	{
		try
		{
			if( null == strArr || strArr.length == 0 || strArr.length <= index)
				return 0;
			
			return parseFloat(strArr[index]);
		}
		catch(Exception e)
		{
			LogHelper.error(e.getMessage(), e);
		}
		
		return 0;
	}
	
	public static short parseShort(Object[] strArr,int index)
	{
		try
		{
			if( null == strArr || strArr.length == 0 || strArr.length <= index)
				return 0;
			
			return parseShort(strArr[index]);
		}
		catch(Exception e)
		{
			LogHelper.error(e.getMessage(), e);
		}
		
		return 0;
	}
	
	public static double parseDouble(Object[] strArr,int index)
	{
		try
		{
			if( null == strArr || strArr.length == 0 || strArr.length <= index)
				return 0;
			
			return parseDouble(strArr[index]);
		}
		catch(Exception e)
		{
			LogHelper.error(e.getMessage(), e);
		}
		
		return 0;
	}
	
	public static long parseLong(Object[] strArr,int index)
	{
		try
		{
			if( null == strArr || strArr.length == 0 || strArr.length <= index)
				return 0;
			
			return parseLong(strArr[index]);
		}
		catch(Exception e)
		{
			LogHelper.error(e.getMessage(), e);
		}
		
		return 0;
	}
	
	public static byte parseByte(Object[] strArr,int index)
	{
		try
		{
			if( null == strArr || strArr.length == 0 || strArr.length <= index)
				return 0;
			
			return parseByte(strArr[index]);
		}
		catch(Exception e)
		{
			LogHelper.error(e.getMessage(), e);
		}
		
		return 0;
	}
	
	public static BigDecimal parseBigDecimal(Object[] arrays,int index) 
	{
		try
		{
			if( null == arrays || arrays.length == 0 || arrays.length <= index)
				return null;
			
			return parseBigDecimal(arrays[index]);
		}
		catch(Exception e)
		{
			LogHelper.error(e.getMessage(), e);
		}
		
		return null;
	}
	
	/**
	 * 格式化字符串，金额字段专用，当f=null时默认返回###,###.000000，并自动去除小数点后的0 其它按格式返回
	 * 
	 * @param dMny
	 * @param f
	 * @return
	 */
	public static String format(Object val, String f) 
	{
		DecimalFormat df = null;
		if (f != null) {
			df = new DecimalFormat(f);
		}
		else
		{
			df = new DecimalFormat("###,###0.000000");
		}
		
		return df.format( parseDouble(val) );
	}
	
	/**
	 * 阿拉伯数字转换为中文大写数字
	 * 
	 * @param sNum
	 * @return
	 */
	public static StringBuffer convertNumberToChinese(String sNum) {
		String sNumber = format(sNum, "#####0.00");
		// 查询小数点位置
		int nDecPos = 0;
		for (int i = 0; i < sNumber.length(); i++) {
			if (sNumber.charAt(i) == '.') {
				nDecPos = i;
				break;
			}
		}
		// 整数位
		StringBuffer sbNumber = new StringBuffer();
		// 小数位
		StringBuffer sbDecimal = new StringBuffer();
		// 最终转换结果
		StringBuffer sbChinese = new StringBuffer();
		if (nDecPos > 0) {
			sbNumber.append(sNumber.substring(0, nDecPos)).reverse();
			sbDecimal.append(sNumber.substring(nDecPos + 1));
		} else {
			sbNumber.append(sNumber).reverse();
		}
		int nLen = sbNumber.length();
		for (int i = 0; i < nLen; i++) {
			String strNum = String.valueOf(sbNumber.charAt(i));
			int nNum = Integer.parseInt(strNum);
			int nPos = i % 4;
			int nZero = i / 4;
			if (nZero > 0) {
				if (nPos > 0) {
					if (nNum > 0)
						sbChinese.append(chineseIntBit[nPos]);

					sbChinese.append(chineseNumber[nNum]);
				} else {
					if (nNum > 0)
						sbChinese.append(chineseLongBit[nZero - 1]);
					sbChinese.append(chineseNumber[nNum]);
				}
			} else {
				if (nNum > 0)
					sbChinese.append(chineseIntBit[nPos]);

				if (i <= (nLen - 1) && nNum != 0)
					sbChinese.append(chineseNumber[nNum]);
			}
		}
		sbChinese.reverse();
		if (nDecPos > 0) {
			String decimals = sbDecimal.toString();
			if (decimals.equals("")) {
				sbChinese.append("整");
			} else {
				Double tempdb = Double.parseDouble(decimals);
				if (tempdb == 0) {
					sbChinese.append("整");
				} else {
					for (int i = 0; i < sbDecimal.length(); i++) {
						String strNum = String.valueOf(sbDecimal.charAt(i));
						int nNum = Integer.parseInt(strNum);
						int nPos = i % 4;
						sbChinese.append(chineseNumber[nNum]);
						sbChinese.append(chineseDecBit[nPos]);
					}
				}
			}
		} else {
			sbChinese.append(chineseZero);
		}
		return sbChinese;
	}

	/**
	 * 阿拉伯数字转换为中文大写数字
	 * 
	 * @param dNum
	 * @return
	 */
	public static StringBuffer convertNumberToChinese(double dNum) {
		String sNum = String.valueOf(dNum);

		return convertNumberToChinese(sNum);
	}

	/**
	 * 阿拉伯数字转换为中文大写数字
	 * 
	 * @param nNum
	 * @return
	 */
	public static StringBuffer convertNumberToChinese(long nNum) {
		String sNum = String.valueOf(nNum);

		return convertNumberToChinese(sNum);
	}

	/**
	 * 阿拉伯数字转换为中文大写数字
	 * 
	 * @param nNum
	 * @return
	 */
	public static StringBuffer convertNumberToChinese(int nNum) {
		String sNum = String.valueOf(nNum);

		return convertNumberToChinese(sNum);
	}

	/**
	 * 阿拉伯数字转换为英文字母
	 * 
	 * @param nNum
	 * @return
	 */
	public static String convertNumberToLetter(Long num, boolean isUp) {
		String numStr = num + "";
		StringBuffer returnStr = new StringBuffer();
        for (byte b : numStr.getBytes()) {
        	returnStr.append((char) (isUp ? b + 16 : b + 48)); 
        }  
        
        return returnStr.toString();
	}
	
	public static void main(String[] arg)
	{
		System.out.println(NumberUtil.parseByte("12.00") );
	}
}
