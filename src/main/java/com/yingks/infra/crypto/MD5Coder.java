package com.yingks.infra.crypto;


public final class MD5Coder {
	
	public static String encode(byte[] src)
	{
		StringBuilder sb=new StringBuilder();
		try 
		{
			java.security.MessageDigest md=java.security.MessageDigest.getInstance("MD5");
			md.update(src);
			for(byte b:md.digest())
			{
				sb.append(Integer.toString(b>>>4&0xF,16)).append(Integer.toString(b&0xF,16));
			}
		} 
		catch (Exception e) 
		{
			throw new RuntimeException(e.getMessage(), e);
		}
		return sb.toString();
	} 
	
	public static String encode(byte[] src,byte[] key)
	{
		StringBuilder sb=new StringBuilder();
		try 
		{
			java.security.MessageDigest md=java.security.MessageDigest.getInstance("MD5");
			md.update(src);
			for(byte b:md.digest(key))
			{
				sb.append(Integer.toString(b>>>4&0xF,16)).append(Integer.toString(b&0xF,16));
			}
		} 
		catch (Exception e) 
		{
			throw new RuntimeException(e.getMessage(), e);
		}
		return sb.toString();
	} 
	
	public static String encode(String src)
	{
		try
		{
			if(src==null)
			{
				return "";
			}
			return encode(src.getBytes("UTF-8"));
		}
		catch(Exception e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public static String encode(String src,String key)
	{
		try
		{
			if(src==null)
			{
				return "";
			}
			return encode(src.getBytes("UTF-8"),key.getBytes("UTF-8"));
		}
		catch(Exception e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public static void main(String[] arg)
	{
		System.out.println(encode("12345678","19pay.com.cn"));
	}
}
