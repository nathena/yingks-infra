package com.yingks.infra.crypto;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;

public class PBECoder {

	private static final String ALGORITHM = "PBEWITHMD5andDES";  
    //迭代次数  
	private static final int ITERATION_COUNT = 100;
	
	private static final String password = PBECoder.class.getName();
	
	private PBECoder()
	{
		
	}
	
	/** 
     * 盐初始化<br> 
     * 盐长度必须为8字节 
     * @return byte[] 盐 
     */  
    public static byte[] initSalt(){  
        //实例化安全随机数  
        SecureRandom random = new SecureRandom();  
        //生产盐  
        return random.generateSeed(8);  
    }  
    /** 
     * 转换密钥 
     * @param password 密码 
     * @return key 密钥 
     * @throws Exception 
     */  
    private static Key generateKey(String password)throws Exception{  
        //密钥材料转换  
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());  
        //实例化  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);  
        SecretKey secretKey = keyFactory.generateSecret(keySpec);  
        return secretKey;  
    }  
    /** 
     * 加密 
     * @param data 数据 
     * @param password 密码 
     * @param salt 盐 
     * @return byte[] 加密数据 
     * @throws Exception 
     */  
    private static byte[] encrypt(byte[] data,String password,byte[] salt) throws Exception{  
        Key key = generateKey(password);  
        //实例化PBE参数材料  
        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, ITERATION_COUNT);  
        Cipher cipher = Cipher.getInstance(ALGORITHM);  
        cipher.init(Cipher.ENCRYPT_MODE, key,parameterSpec);  
        return cipher.doFinal(data);  
    }  
    private static byte[] decrypt(byte[] data, String password,byte[] salt) throws Exception{  
        Key key = generateKey(password);  
        //实例化PBE参数材料  
        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, ITERATION_COUNT);  
        Cipher cipher = Cipher.getInstance(ALGORITHM);  
        cipher.init(Cipher.DECRYPT_MODE, key,parameterSpec);  
        return cipher.doFinal(data);  
    }
    
    public static String encode(String data,byte[] salt) 
	{
		try
		{
			byte[] b = data.getBytes("UTF-8"); 
			b = encrypt(b,password,salt);
			
			data = Base64.encodeBase64String(b);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
		return data;
	}
	
	public static String decode(String data,byte[] salt)
	{
		try
		{
			byte[] b = Base64.decodeBase64(data.replaceAll("\\s", "+"));
			b = decrypt(b,password,salt);
			
			data = new String(b,"UTF-8");
		}
		catch(Exception e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
		return data;
	}
	
	public static void main(String[] arg)
	{
		byte[] b = initSalt();
		
		String a = PBECoder.encode("1234FADSFA",b);
		
		System.out.println(a.length()+" "+a);
		
		String d = PBECoder.decode(a, b);
		
		System.out.println(d.length()+" "+d);
		
	}
}
