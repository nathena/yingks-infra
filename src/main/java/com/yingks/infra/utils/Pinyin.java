package com.yingks.infra.utils;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.lang3.StringUtils;

public final class Pinyin {

	/**
	 * 
	 * <p>Title: getPinyin</p> 
	 * <p>Description: 获取所有的拼音，包括多音字</p> 
	 * @author nathena 
	 * @date 2013-4-26 下午6:21:52 
	 * @param src
	 * @return String
	 */
	public static String getPinyin(String src)
	{
		return makeStringByStringSet(getPinyinSet(src));
	}
	
	/**
	 * 
	 * <p>Title: getFristPinyin</p> 
	 * <p>Description: 获取拼音，多音字取第一个音量</p> 
	 * @author nathena 
	 * @date 2013-4-26 下午6:22:22 
	 * @param src
	 * @return String
	 */
	public static String getFristPinyin(String src)
	{
		String py = makeStringByStringSet(getPinyinSet(src));
		String[] _py = StringUtils.split(py,",");
		
		return _py[0];
	}
	
	/**
	 * 
	 * <p>Title: getFristPinyin1</p> 
	 * <p>Description: 获取拼音，多音字取第一个音量 的 第一个字母</p> 
	 * @author nathena 
	 * @date 2013-4-26 下午6:22:50 
	 * @param src
	 * @return String
	 */
	public static String getFristPinyin1(String src)
	{
		String py = makeStringByStringSet(getPinyinSet(src));
		String[] _py = StringUtils.split(py,",");
		
		return _py[0].substring(0,1);
	}
	
	/**
	 * 
	 * <p>
	 * Title: makeStringByStringSet
	 * </p>
	 * <p>
	 * Description: 字符串集合转换字符串(逗号分隔)
	 * </p>
	 * 
	 * @author nathena
	 * @date 2013-4-26 下午6:05:24
	 * @param stringSet
	 * @return String
	 */
	private static String makeStringByStringSet(Set<String> stringSet) {
		StringBuilder str = new StringBuilder();
		int i = 0;
		for (String s : stringSet) {
			if (i == stringSet.size() - 1) {
				str.append(s);
			} else {
				str.append(s + ",");
			}
			i++;
		}
		return str.toString().toLowerCase();
	}

	/**
	 * 
	 * <p>
	 * Title: getPinyin
	 * </p>
	 * <p>
	 * Description: 获取拼音集合
	 * </p>
	 * 
	 * @author nathena
	 * @date 2013-4-26 下午6:08:56
	 * @param src
	 * @return Set<String>
	 */
	private static Set<String> getPinyinSet(String src) {
		if (src != null && !src.trim().equalsIgnoreCase("")) {
			char[] srcChar = src.toCharArray();
			// 汉语拼音格式输出类
			HanyuPinyinOutputFormat hanYuPinOutputFormat = new HanyuPinyinOutputFormat();

			// 输出设置，大小写，音标方式等
			hanYuPinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
			hanYuPinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			//hanYuPinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

			String[][] temp = new String[src.length()][];
			char c;
			for (int i = 0; i < srcChar.length; i++) {
				c = srcChar[i];
				// 是中文或者a-z或者A-Z转换拼音(我的需求，是保留中文或者a-z或者A-Z)
				if (String.valueOf(c).matches("[\\u4E00-\\u9FA5]+")) {
					try {
						temp[i] = PinyinHelper.toHanyuPinyinStringArray(srcChar[i], hanYuPinOutputFormat);
					} catch (BadHanyuPinyinOutputFormatCombination e) {
						e.printStackTrace();
					}
				} else if (((int) c >= 65 && (int) c <= 90)|| ((int) c >= 97 && (int) c <= 122)) {
					temp[i] = new String[] { String.valueOf(srcChar[i]) };
				} else {
					temp[i] = new String[] { "" };
				}
			}
			String[] pingyinArray = Exchange(temp);
			Set<String> pinyinSet = new HashSet<String>();
			for (int i = 0; i < pingyinArray.length; i++) {
				pinyinSet.add(pingyinArray[i]);
			}
			return pinyinSet;
		}
		return null;
	}

	/**
	 * 
	 * <p>Title: Exchange</p> 
	 * <p>Description: </p> 
	 * @author nathena 
	 * @date 2013-4-26 下午6:09:21 
	 * @param strJaggedArray
	 * @return String[]
	 */
	private static String[] Exchange(String[][] strJaggedArray) {
		String[][] temp = DoExchange(strJaggedArray);
		return temp[0];
	}

	/**
	 * 
	 * <p>
	 * Title: DoExchange
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @author nathena
	 * @date 2013-4-26 下午6:09:33
	 * @param strJaggedArray
	 * @return String[][]
	 */
	private static String[][] DoExchange(String[][] strJaggedArray) {
		int len = strJaggedArray.length;
		if (len >= 2) {
			int len1 = strJaggedArray[0].length;
			int len2 = strJaggedArray[1].length;
			int newlen = len1 * len2;
			String[] temp = new String[newlen];
			int Index = 0;
			for (int i = 0; i < len1; i++) {
				for (int j = 0; j < len2; j++) {
					temp[Index] = strJaggedArray[0][i] + strJaggedArray[1][j];
					Index++;
				}
			}
			String[][] newArray = new String[len - 1][];
			for (int i = 2; i < len; i++) {
				newArray[i - 1] = strJaggedArray[i];
			}
			newArray[0] = temp;
			return DoExchange(newArray);
		} else {
			return strJaggedArray;
		}
	}
	
	public static void main(String[] arg)
	{
		System.out.println(getFristPinyin1("泉州"));
	}
}
