package com.yingks.infra.crypto;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AESCoder 
{

	private final static String KEY_ALGORITHM = "AES";
	private final static String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	
	//AES 1234567891234567 16
	private static final Key key = new SecretKeySpec("1234567891234567".getBytes(), KEY_ALGORITHM); //18个字符
	
	private AESCoder()
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
			throw new RuntimeException("AESCode 加码异常 ", e);
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
			throw new RuntimeException("AESCode 解码异常", e);
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
		String e = AESCoder.encode("在做加密解密过程中，报错了： javax.crypto.BadPaddingException: Given final block not properly padded怎么回事，都是按照你的写的，怎么会不对呢？仔细分析一下，不难发现，该异常是在解密的时候抛出的，加密的方法没有问题");
		System.out.println(e.length());
		
		String d = AESCoder.decode("Fh8Ey07WQ LIwDQ4c6zdXQ==");
		System.out.println(d);
		
		System.out.println(AESCoder.encode("1234FADSFA").length());
	}
}
