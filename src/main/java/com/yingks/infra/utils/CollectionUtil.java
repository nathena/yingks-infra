package com.yingks.infra.utils;

import java.util.Collection;
import java.util.Map;

/**  
 * @Title: CollectionUtil.java
 * @Package com.jytnn.jx.biz.utils
 * @Description: 集合容器帮助类
 * @author 青梅
 * @date 2014-11-17 下午4:31:10
 * @version V1.0 
 * @UpdateHis:
 *      TODO  
 */
public class CollectionUtil {
	public static boolean isEmpty(Collection<?> col) {
		return (col == null || col.size() == 0);
	}
	
	public static boolean isEmpty(Object[] objs) {
		return (objs == null || objs.length == 0);
	}
	
	public static boolean isEmpty(Map map) {
		return (map == null || CollectionUtil.isEmpty(map.keySet()));
	}
	
	public static boolean isLengthEqual(Collection<?> col1, Collection<?> col2) {
		return (col1 != null && col2 != null && col1.size() == col2.size());
	}
	
	public static boolean isLengthEqual(Object[] objs1, Object[] objs2) {
		return (objs1 != null && objs2 != null && objs1.length == objs2.length);
	}
	
	public static boolean isLengthEqual(Object[] objs1, Collection<?> cols2) {
		return (objs1 != null && cols2 != null && objs1.length == cols2.size());
	}
}
