package com.yingks.infra.pay.wx;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class WxConfig {

	public static String mchId		= null;//商户号
	public static String mchKey  	= null;	//商户密钥
	public static String appId		= null;	//appid
	public static String appSecret	= null;	//appsecret
	public static String appKey		= null;	//paysignkey 128位字符串(非appkey)
	//public static String signType    = "MD5";
	 
	public static String notifyUrl	= null;  //支付完成后的回调处理页面,*替换成notify_url.asp所在路径
	public static String jsWxPayApiUrl = null; //jsWxPayApiUrl
	
	public static String appNotifyUrl	= null;  //app支付完成后的回调处理页面,*替换成notify_url.asp所在路径
	
	public static String nativeNotifyUrl = null;  //native支付完成后的回调处理页面,*替换成notify_url.asp所在路径
	
	public static String success_url = null;
	public static String failed_url = null;
	
	static {
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("alipay.properties");
		if( null != in ) {
			Properties properties = new Properties();   
			try 
			{   
				properties.load(in);
				
				mchId = properties.getProperty("mchId");
				mchKey = properties.getProperty("mchKey");
				
				appId = properties.getProperty("appId");
				appSecret = properties.getProperty("appSecret");
				appKey = properties.getProperty("appKey");
				
				//signType = properties.getProperty("signType","sha1");
				
				notifyUrl   = properties.getProperty("notifyUrl");
				jsWxPayApiUrl = properties.getProperty("jsWxPayApiUrl");
				
				appNotifyUrl = properties.getProperty("appNotifyUrl");
				nativeNotifyUrl = properties.getProperty("nativeNotifyUrl");
				
				success_url = properties.getProperty("success_url");
				failed_url = properties.getProperty("failed_url");
			} 
			catch(IOException e) 
			{
				e.printStackTrace();   
			}
		}
	}
}
