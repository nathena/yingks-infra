package com.yingks.infra.crypto;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

public final class Base64Coder {

    public Base64Coder() {
    }

    public static byte[] decode(byte[] data) 
    {
    	return Base64.decodeBase64(data);
    }
    
    public static byte[] decode(String data)
    {
    	return Base64.decodeBase64(data.replaceAll("\\s", "+"));
    }

    public static String encode(byte[] data) 
    {
    	return Base64.encodeBase64String(data);
    }
    
    public static String encode(String data) throws UnsupportedEncodingException
    {
    	return Base64.encodeBase64String(data.getBytes("UTF-8"));
    }
    
    //URL安全的编码方式+换成-,/换成_去掉最后的=
    public static String uriSafeEncode(byte[] data)
    {
    	String encodeStr = Base64.encodeBase64String(data);
    	String uriSafeStr = encodeStr.replaceAll("\\+", "-").replaceAll("/", "_").replaceAll("=", "");
    	return uriSafeStr.substring(0, uriSafeStr.length());
    }
    //URL安全的解码方式
    public static byte[] uriSafeDncode(byte[] data)
    {
    	String decodeStr = new String(data);
    	decodeStr = decodeStr.replaceAll("-", "\\+").replaceAll("_", "/");
    	return Base64.decodeBase64(decodeStr.getBytes());
    }
}
