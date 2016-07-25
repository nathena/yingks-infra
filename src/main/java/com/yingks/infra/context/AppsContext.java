package com.yingks.infra.context;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.yingks.infra.utils.StringUtil;

/**
 * 
 * @author nathena 
 * @date 2013-4-24 下午6:47:41 
 *
 */
public class AppsContext {
	
	public final String VERSION = "1.0";
	
	private static ThreadLocal<RequestContext> requestContext = new ThreadLocal<RequestContext>();
	private static String upload_tmp_path;
	private static String static_uri = "";
	private static String webRoot;
	private static String classPath;
	private static String web_inf;
	//手动为服务器分配的mac id 做分布式时可以作为单台设备的标示,生成某些唯一标示数据时有用 Gaowx 2015-7-14
	private static String server_mac_id;
	private static String image_uri = "";
	private static String video_uri = "";
	//是否调试模式
	private static String debug = "";
	private static ApplicationContext springContext;
	private static Properties properties;
	
	static
	{
		init();
	}
	
	private static void init() throws RuntimeException
	{
		try
		{
			classPath = AppsContext.class.getResource("/").toURI().getPath();
			File file = new File(classPath);
			
			web_inf = file.getParentFile().toURI().getPath();
			webRoot = file.getParentFile().getParentFile().toURI().getPath();
			
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("context.properties");
			if( null != in )
			{
				properties = new Properties();   
				try 
				{   
					properties.load(in);
					
					upload_tmp_path = properties.getProperty("upload_tmp_path");
					static_uri      = properties.getProperty("static_uri");
					//
					server_mac_id   = properties.getProperty("server_mac_id");
					image_uri      	= properties.getProperty("img_uri");
					video_uri   	= properties.getProperty("video_uri");
					debug 			= properties.getProperty("debug");
				}
				catch (IOException e) 
				{   
					e.printStackTrace();   
				}  
			}
			
			if( null == upload_tmp_path)
			{
				upload_tmp_path = webRoot+"upload"+File.separator;
			}
			
			file = null;
			
			springContext = ContextLoader.getCurrentWebApplicationContext();
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
		
    	if( null == springContext )
    	{
			springContext = new ClassPathXmlApplicationContext(new String[]{"classpath*:applicationContext*.xml"});
    	}
	}
	
	public static RequestContext initRequestContext(HttpServletRequest req, HttpServletResponse res)
	{
		RequestContext context = requestContext.get();
		if( null == context )
		{
			context = new RequestContext(req,res);
			requestContext.set(context);
		}
		return context;
	}
	
	public static void destoryRequestContext()
	{
		RequestContext requestContexts = requestContext.get();
		if( null != requestContexts )
		{
			requestContexts.destroy();
		}
		requestContext.remove();
	}
	
	public static RequestContext currentRequestContext() throws RuntimeException
	{
		RequestContext context = requestContext.get();
		if( null == context)
		{
			throw new RuntimeException("UNHOWN request context");
		}
		
		return context;
	}
	
	public static String getClassPath()
	{
		return classPath;
	}
	
	public static String getWebInfPath()
	{
		return web_inf;
	}
	
	public static String webRoot()
	{
		return webRoot;
	}
	
	public static String uploadDir()
	{
		return upload_tmp_path;
	}
	
	public static String staticURI()
	{
		return static_uri;
	}
	public static String serverMacId() {
		return server_mac_id;
	}
	public static String imageUri() {
		return image_uri;
	}
	public static String videoUri() {
		return video_uri;
	}
	public static boolean isDebug() {
		return !StringUtil.isEmpty(debug) && "true".equals(debug);
	}
	public static String getProperty(String key)
	{
		if( null != properties )
		{
			return properties.getProperty(key,"");
		}
		return "";
	}
	
	public static <T> T getBean(Class<T> requiredType)
	{
		return springContext.getBean(requiredType);
	}
	
	public static <T> T getBean(Class<T> requiredType,String name)
	{
		return springContext.getBean(name, requiredType);
	}
	
	public static void pulishEvent(ApplicationEvent... events)
	{
		for(ApplicationEvent event : events)
		{
			springContext.publishEvent(event);
		}
	}
	
	private AppsContext(){}
	
	public static void main(String[] arg)
	{
		System.out.println("upload_tmp_path : "+upload_tmp_path); 
	}
}
