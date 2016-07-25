package com.yingks.infra.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

public final class ResourceLoader 
{
	private static ClassLoader getLoader() throws IllegalAccessException,InvocationTargetException
	{
		// Are we running on a JDK 1.2 or later system?
		Method method = null;
		try 
		{
			method = Thread.class.getMethod("getContextClassLoader", null);
		} 
		catch (NoSuchMethodException e) 
		{
			// We are running on JDK 1.1
			return null;
		}
		return (ClassLoader) method.invoke(Thread.currentThread(), null);
	}
	
	public static Class<?> loadClass(String className )
	{
		ClassLoader classLoader;
		Class<?> clazz;
		
		try 
		{
			classLoader = getLoader();
			if (classLoader != null) 
			{
				clazz = classLoader.loadClass(className);
				if (clazz != null)
					return clazz;
			}
			// We could not find resource. Ler us now try with the
			// classloader that loaded this class.
			classLoader = ResourceLoader.class.getClassLoader();
			if (classLoader != null) 
			{
				clazz = classLoader.loadClass(className);
				if (clazz != null)
					return clazz;
			}
			clazz = Class.forName(className);
		} 
		catch (Throwable t) 
		{
			return null;
		}
		return clazz;
	}
	
	public static URL getResourece(String resource)
	{
		ClassLoader classLoader = null;
		URL url = null;

		try 
		{
			classLoader = getLoader();
			if (classLoader != null) 
			{
				url = classLoader.getResource(resource);
				if (url != null)
					return url;
			}
			// We could not find resource. Ler us now try with the
			// classloader that loaded this class.
			classLoader = ResourceLoader.class.getClassLoader();
			if (classLoader != null) 
			{
				url = classLoader.getResource(resource);
				if (url != null)
					return url;
			}
		} 
		catch (Throwable t) 
		{
			return null;
		}
		return ClassLoader.getSystemResource(resource);
	}
	
}
