package com.yingks.infra.domain.store;


public class PrimitiveTypeChecked {
	
	public static boolean checkNumberType(Class<?> requiredType)
	{
		String requiredName = requiredType.getName();
		String[] _keys = new String[]{"int","byte","double","short","float","long"};
		for(String _key : _keys)
		{
			if( _key.equalsIgnoreCase(requiredName))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean checkFullObjectType(Class<?> requiredType)
	{
		String requiredName = requiredType.getName();
		String[] _keys = new String[]{"java.lang.Integer","java.lang.Byte","java.lang.Double","java.lang.Short","java.lang.Float","java.lang.Long"};
		for(String _key : _keys)
		{
			if( _key.equalsIgnoreCase(requiredName))
			{
				return true;
			}
		}
		return false;
	}
}
