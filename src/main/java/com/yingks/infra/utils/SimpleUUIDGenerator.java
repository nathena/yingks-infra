package com.yingks.infra.utils;

public class SimpleUUIDGenerator {

	public static String generateUUID()
	{
		StringBuilder uuid = new StringBuilder();
		
		uuid.append( DateTimeUtil.getCurrentClock() );
		uuid.append( RandomNumberHelper.nextString(2) );
		
		return uuid.toString();
	}
	
	
	public static void main(String[] arg)
	{
		String a = generateUUID(),b = generateUUID();
		System.out.println( a +"\n"+b+"\n"+a.length() );
	}
}
