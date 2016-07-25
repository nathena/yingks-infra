package com.yingks.infra.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.yingks.infra.utils.LogHelper;

public class PropertiesUtil {

	public static Properties load(String propertiesFile)
	{
		Properties properties = null;
		try
		{
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesFile);
			if( null != in )
			{
				properties = new Properties();   
				properties.load(in);
			}
		}
		catch (IOException e) 
		{   
			e.printStackTrace();
			LogHelper.error(e.getMessage(), e);
		}
		
		return properties;
	}
}
