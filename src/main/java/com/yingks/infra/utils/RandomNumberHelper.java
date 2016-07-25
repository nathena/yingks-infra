package com.yingks.infra.utils;

import java.util.Random;

public final class RandomNumberHelper {
	
	private static Random random_engine = null;
	private static char[] rand_char = null;
	private static int rand_factor_length = 0;
	private static String rand_factor = "0123456789";
	
	public static String nextString(int length)
	{
		String random = null;
		if( length>1 )
		{
			if( random_engine == null )
			{
				random_engine = new Random();
				rand_char = rand_factor.toCharArray();
				rand_factor_length = rand_char.length;
			}
			
			char[] randBuffer = new char[length];
			for (int i=0; i<length; i++) 
			{
				randBuffer[i] = rand_char[random_engine.nextInt(rand_factor_length)];
			}
			
			random = new String(randBuffer);
		}
		
		return random;
	}
}
