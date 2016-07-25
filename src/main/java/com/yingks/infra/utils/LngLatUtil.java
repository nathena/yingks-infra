package com.yingks.infra.utils;

public final class LngLatUtil {

	private static double KM_EARTH_RADIUS = 6378.137;//地球半径（单位km）
	
	/**
	 * 获取经纬度两点距离
	 *
	 * @author nathena 
	 * @date 2013-5-22 下午1:30:22 
	 * @param lngFrom 经度
	 * @param latFrom 纬度
	 * @param lngTo   经度
	 * @param latTo   纬度
	 * @return double 距离（KM）
	 */
	public static double getKmDistance(double lngFrom,double latFrom,double lngTo,double latTo)
	{
		if( 0 == lngFrom || 0 == latFrom || lngTo == 0 || 0 == latTo )
		{
			return 0;
		}
		
		lngFrom = lngFrom*Math.PI/180.0;
		latFrom = latFrom*Math.PI/180.0;
		lngTo   = lngTo*Math.PI/180.0;
		latTo  =  latTo*Math.PI/180.0;
		
		double a = latFrom - latTo;
		double b = lngFrom - lngTo;
		
		double s = 2* Math.asin( Math.sqrt(Math.pow(Math.sin(a/2), 2)) + Math.cos(latFrom)*Math.cos(latTo)*Math.pow(Math.sin(b), 2) ) * KM_EARTH_RADIUS;
		
		s = Math.round(s*10000)/10000;
		
		return s;
	}
}
