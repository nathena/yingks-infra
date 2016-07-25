package com.yingks.infra.crypto;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class DESCoder 
{

	private final static String KEY_ALGORITHM = "DES";
	private final static String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";
	//DES 12345678 8
	
	//private final static String KEY_ALGORITHM = "DESede";
	//private final static String CIPHER_ALGORITHM = "DESede/ECB/PKCS5Padding";
	
	//DESede 112
	private static final Key key = new SecretKeySpec("12345678".getBytes(), KEY_ALGORITHM); //18个字符
	
	private DESCoder()
	{
		 
	}
	 
	public static String encode(String data) 
	{
		try
		{
			byte[] b = data.getBytes("UTF-8"); 
			b = desEncode(b);
			
			data = Base64.encodeBase64String(b);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
		return data;
	}
	
	public static String decode(String data)
	{
		try
		{
			byte[] b = Base64.decodeBase64(data.replaceAll("\\s", "+"));
			b = desDecode(b);
			
			data = new String(b,"UTF-8");
		}
		catch(Exception e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
		return data;
	}
	
	private static byte[] desEncode(byte[] data) throws Exception
	{
		 Cipher cipher =  Cipher.getInstance(CIPHER_ALGORITHM);
		 cipher.init(Cipher.ENCRYPT_MODE, key);
		 
		 return cipher.doFinal(data);
	}
	
	private static byte[] desDecode(byte[] data) throws Exception
	{
		Cipher cipher =  Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);
		 
		return cipher.doFinal(data);
	}
	
	public static void main(String[] arg)
	{
		System.out.println(DESCoder.encode("1234FADSFA").length());
	}
}
