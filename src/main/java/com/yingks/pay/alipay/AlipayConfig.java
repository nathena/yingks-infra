package com.yingks.pay.alipay;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AlipayConfig extends com.alipay.config.AlipayConfig {

	public static String seller_email = null;
	
	public static String notify_url = null;
	public static String app_notify_url = null;
	
	public static String return_url = null;
	public static String merchant_url = null;
	
	public static String pay_success_url = null;
	public static String pay_failed_url = null;
	
	
	static {
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("alipay.properties");
		if( null != in ) {
			Properties properties = new Properties();   
			try 
			{   
				properties.load(in);
				
				partner = properties.getProperty("partner");
				key = properties.getProperty("key");
				
				private_key = properties.getProperty("private_key");
				ali_public_key = properties.getProperty("ali_public_key");
				input_charset = properties.getProperty("input_charset","utf-8");
				
				log_path   = properties.getProperty("log_path",".\\");
				sign_type = properties.getProperty("sign_type", "RSA");
				
				seller_email = properties.getProperty("seller_email",partner);
				notify_url = properties.getProperty("notify_url");
				app_notify_url = properties.getProperty("app_notify_url");
				return_url      = properties.getProperty("return_url");
				merchant_url   = properties.getProperty("merchant_url");
				
				pay_success_url = properties.getProperty("pay_success_url");
				pay_failed_url = properties.getProperty("pay_failed_url");
			} 
			catch(IOException e) 
			{
				e.printStackTrace();   
			}
		}
	}
}
