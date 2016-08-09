package com.yingks.infra.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * 类说明：字符串，消息处理工具类
 * 
 */

public final class StringUtil
{
	final static String emptyValue = "";
	/**
	 * 不允许实例化
	 * 
	 */
	private StringUtil() {
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return 空返回 true,非空返回false
	 */
	public static boolean isEmpty(Object... strs) {
		boolean entry = false;
		for (Object str : strs) {
			entry = true;
			if (str == null) {
				return true;
			} else if (str.toString().trim().length() == 0) {
				return true;
//			} else if (str.toString().trim().equals("null")) {
//				return true;
			}
		}

		if (!entry) {
			return true;
		}
		return false;
	}

	/**
	 * 格式化字符串，数量字段专用，返回##0.0000.
	 * 
	 * @param sQty
	 * @return
	 */
	public static String formatQty(String sQty) {
		if (sQty == null || sQty.equals("")) {
			sQty = "0";
		}
		Double dQty = new Double(sQty);
		DecimalFormat df = new DecimalFormat("##0.000");
		sQty = df.format(dQty);
		return sQty;
	}

	/**
	 * 格式化字符串，数量字段专用，当f=null时默认返回###,##0.0000，并自动去除小数点后的0 其它按格式返回
	 * 
	 * @param sQty
	 * @param f
	 * @return
	 */
	public static String formatQty(String sQty, String f) {
		if (sQty == null || sQty.equals(""))
			sQty = "0";

		Double dQty = new Double(sQty);

		DecimalFormat df = new DecimalFormat("##0.0000");
		if (f != null) {
			df = new DecimalFormat(f);
		}
		sQty = df.format(dQty);
		return sQty;
	}

	/**
	 * 格式化字符串，数量字段专用，返回######.0000，并自动去除小数点后的0
	 * 
	 * @param dQty
	 * @return
	 */
	public static String formatQty(double dQty) {
		DecimalFormat df = new DecimalFormat("##0.0000");

		return df.format(dQty);
	}

	// 两位小数
	public static String formatQty2(double dQty) {
		DecimalFormat df = new DecimalFormat("##0.00");

		return df.format(dQty);
	}

	// 三位小数
	public static String formatQty3(double dQty) {
		DecimalFormat df = new DecimalFormat("##0.000");

		return df.format(dQty);
	}

	/**
	 * 格式化字符串，数量字段专用，当f=null时默认返回###,###.0000，并自动去除小数点后的0 其它按格式返回
	 * 
	 * @param dQty
	 * @param f
	 * @return
	 */
	public static String formatQty(double dQty, String f) {
		DecimalFormat df = new DecimalFormat("##0.0000");
		if (f != null) {
			df = new DecimalFormat(f);
		}
		String sQty = null;
		sQty = df.format(dQty);
		return sQty;
	}

	/**
	 * 格式化字符串，单价字段专用，返回######.000000，并自动去除小数点后的0
	 * 
	 * @param sQty
	 * @return
	 */
	public static String formatPrice(String sPrice) {
		if (sPrice == null || sPrice.equals(""))
			sPrice = "0";

		Double dPrice = new Double(sPrice);
		DecimalFormat df = new DecimalFormat("##0.000");
		return df.format(dPrice);
	}

	/**
	 * 格式化字符串，单价字段专用，当f=null时默认返回###,###.000000，并自动去除小数点后的0 其它按格式返回
	 * 
	 * @param sPrice
	 * @param f
	 * @return
	 */
	public static String formatPrice(String sPrice, String f) {
		if (sPrice == null || sPrice.equals(""))
			sPrice = "0";
		Double dPrice = new Double(sPrice);
		DecimalFormat df = new DecimalFormat("##0.000000");
		if (f != null) {
			df = new DecimalFormat(f);
		}
		sPrice = df.format(dPrice);
		return sPrice;
	}

	/**
	 * 格式化字符串，单价字段专用，返回######.000000，并自动去除小数点后的0
	 * 
	 * @param dPrice
	 * @return
	 */
	public static String formatPrice(double dPrice) {
		DecimalFormat df = new DecimalFormat("##0.000000");
		return df.format(dPrice);
	}

	// 两位小数
	public static String formatPrice2(double dPrice) {
		DecimalFormat df = new DecimalFormat("##0.000");
		return df.format(dPrice);
	}

	/**
	 * 格式化字符串，单价字段专用，当f=null时默认返回###,###.000000，并自动去除小数点后的0 其它按格式返回
	 * 
	 * @param dPrice
	 * @param f
	 * @return
	 */
	public static String formatPrice(double dPrice, String f) {
		DecimalFormat df = new DecimalFormat("##0.000000");
		if (f != null) {
			df = new DecimalFormat(f);
		}
		String sPrice = df.format(dPrice);
		return sPrice;
	}

	/**
	 * 格式化字符串，金额字段专用，返回######.000000，并自动去除小数点后的0
	 * 
	 * @param sQty
	 * @return
	 */
	public static String formatMny(String sMny) {
		if (sMny == null || sMny.equals(""))
			sMny = "0";

		Double dMny = new Double(sMny);
		DecimalFormat df = new DecimalFormat("##0.00");
		return df.format(dMny);
	}

	/**
	 * 格式化字符串，金额字段专用，当f=null时默认返回###,###.000000，并自动去除小数点后的0 其它按格式返回
	 * 
	 * @param sMny
	 * @param f
	 * @return
	 */
	public static String formatMny(String sMny, String f) {
		if (sMny == null || sMny.equals(""))
			sMny = "0";

		Double dMny = new Double(sMny);
		DecimalFormat df = new DecimalFormat("##0.000000");
		if (f != null) {
			df = new DecimalFormat(f);
		}
		sMny = df.format(dMny);
		return sMny;
	}

	public static String formatQty1(double dQty) {
		DecimalFormat df = new DecimalFormat("##0.00");
		return df.format(dQty);
	}

	/**
	 * 摘要：
	 * 
	 * @说明：
	 * @创建：作者:whj 创建时间：2007-12-19
	 * @param sParam
	 * @return
	 * @修改历史： [序号](whj 2007-12-19)<修改说明>
	 */
	public static String formatResult(String sParam) {
		if (sParam == null || sParam.equals("0")) {
			return "";
		} else {
			String sResult = "";
			DecimalFormat df = new DecimalFormat("###,###.00");
			sResult = df.format(Double.valueOf(sParam));
			return sResult;
		}
	}

	// 对象转化成Str
	public static String convertStrDefautNull(Object obj) {
		if (null == obj) {
			return null;
		} else {
			return obj.toString();
		}
	}

	// 对象转化成Str
	public static String convertStr(Object obj) 
	{
		if (null == obj) 
		{
			return "";
		} 
		else 
		{
			return obj.toString();
		}
	}

	// 对象转化成int
	public static Integer convertInt(Object obj) {
		if (null == obj) {
			return null;
		} else {
			if (obj.toString().equals("")) {
				return null;
			}
			if(!Validator.isNumber(obj.toString())){
				return null;
			}
			return Integer.parseInt(obj.toString());
		}
	}
	
	// 对象转化成Long
	public static Long convertLong(Object obj) {
		if (null == obj) {
			return null;
		} else {
			if (obj.toString().equals("")) {
				return null;
			}
			return Long.parseLong(obj.toString());
		}
	}

	// 对象转化成BigDecimal
	public static BigDecimal convertBigDecimal(Object obj) {
		if (null == obj) {
			return null;
		} else {
			if (obj.toString().equals("")) {
				return null;
			}
			return new BigDecimal(obj.toString());
		}
	}

	// 对象转化成Double
	public static Double convertDouble(Object obj) {
		if (null == obj) {
			return null;
		} else {
			if (obj.toString().equals("")) {
				return null;
			}
			return Double.parseDouble(obj.toString());
		}
	}
	// 对象转化成Float
	public static Float convertFloat(Object obj) {
		if (null == obj) {
			return null;
		} else {
			if (obj.toString().equals("")) {
				return null;
			}
			return Float.parseFloat(obj.toString());
		}
	}
	// 对象转化成Date
	public static Date convertDate(Object obj, String formate) {
		if (null == obj) {
			return null;
		} else {
			try {
				return DateTimeUtil.getStrToDate(obj.toString(), formate);
			} catch (Exception e) {
				return null;
			}
		}
	}

	// 对象转化成T
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T convertT(Object obj, Class tp) {
		if (tp == Integer.class) {
			return (T) convertInt(obj);
		} else if (tp == Long.class) {
			return (T) convertLong(obj);
		} else if (tp == Double.class) {
			return (T) convertDouble(obj);
		} else if (tp == String.class) {
			return (T) convertStr(obj);
		} else if (tp == Date.class) {
			return (T) convertDate(obj, "yyyy-MM-dd HH:mm:ss");
		} else if (tp == Short.class) {
			if (null == obj || obj.toString().length() == 0) {
				return null;
			}
			Short s = Short.parseShort(obj.toString());
			return (T) s;
		} else if (tp == Byte.class) {
			if (null == obj || obj.toString().length() == 0) {
				return null;
			}
			Byte b = Byte.parseByte(obj.toString());
			return (T) b;
		} else {
			return null;
		}
	}

	/**
	 * @说明: 根据个数获取序号
	 * @param count
	 *            值 2
	 * @param bits
	 *            位数 5
	 * @return 00002
	 */
	public static String getCdByCount(String finalCd, String count, int bits) {
		StringBuffer suffix = new StringBuffer(finalCd);
		int len = bits - count.length();
		if (len > 0) {
			for (int i = 0; i < len; i++)
				suffix.append("0");
		}
		suffix.append(count);
		return suffix.toString();
	}

	public static boolean isLetter(char ch) {
		return Character.isLowerCase(ch) || Character.isUpperCase(ch);
	}
}
