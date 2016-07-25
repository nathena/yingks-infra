package com.yingks.infra.crypto;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;



public final class RSACoder {

	/**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";
    
    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;
    
	/**
	* 生成密钥对
	* @return KeyPair
	* @throws RuntimeException
	*/
	protected static KeyPair generateKeyPair() throws RuntimeException 
	{
		try 
		{
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
			final int KEY_SIZE = 1024;//这个值关系到块加密的大小。不要太大，否则效率会低
			keyPairGen.initialize(KEY_SIZE, new SecureRandom());
			KeyPair keyPair = keyPairGen.genKeyPair();
			return keyPair;
		} 
		catch (Exception e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
     * <p>
     * 用私钥对信息生成数字签名
     * </p>
     * 
     * @param data 已加密数据
     * @param privateKey 私钥
     * 
     * @return
     * @throws Exception
     */
    private static byte[] sign(byte[] data, byte[] privateKey) throws Exception 
    {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return signature.sign();
    }

    /**
     * <p>
     * 校验数字签名
     * </p>
     * 
     * @param data 已加密数据
     * @param publicKey 公钥
     * @param sign 数字签名
     * 
     * @return
     * @throws Exception
     * 
     */
    private static boolean verify(byte[] data, byte[] publicKey, byte[] sign)  throws Exception 
    {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        
        return signature.verify(sign);
    }

    /**
     * <P>
     * 私钥解密
     * </p>
     * 
     * @param encryptedData 已加密数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    private static byte[] decryptByPrivateKey(byte[] encryptedData, byte[] privateKey) throws Exception 
    {
    	
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) 
        {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) 
            {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            }
            else 
            {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        
        return decryptedData;
    }

    /**
     * <p>
     * 公钥解密
     * </p>
     * 
     * @param encryptedData 已加密数据
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    private static byte[] decryptByPublicKey(byte[] encryptedData, byte[] publicKey) throws Exception 
    {
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) 
        {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) 
            {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } 
            else 
            {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * <p>
     * 公钥加密
     * </p>
     * 
     * @param data 源数据
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    private static byte[] encryptByPublicKey(byte[] data, byte[] publicKey)  throws Exception 
    {
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) 
        {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) 
            {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } 
            else 
            {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        
        return encryptedData;
    }

    /**
     * <p>
     * 私钥加密
     * </p>
     * 
     * @param data 源数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    private static byte[] encryptByPrivateKey(byte[] data, byte[] privateKey) throws Exception 
    {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) 
        {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } 
            else 
            {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }
	
	//利用公钥加密
	public static String encode(String orgData)
	{
		try
		{
			return new String(Base64Coder.encode(encryptByPublicKey(orgData.getBytes(),getRSAPublicKeyFromDerFile())));
		}
		catch(Exception e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	//利用私钥解密
	public static String decode(String base64Source)
	{
		try
		{
			return new String(decryptByPrivateKey( Base64Coder.decode(base64Source.replaceAll("\\s", "+")),getRSAPrivateKeyFromDerFile() ));
		}
		catch(Exception e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	//从文件中读取RSA私钥
	private static byte[] getRSAPrivateKeyFromDerFile() throws Exception
	{
		InputStream in = RSACoder.class.getResourceAsStream("private_key.der");
   		int bytesRead=0;
    	ByteArrayOutputStream baos=new ByteArrayOutputStream();
    	byte[] buff=new byte[1024];
    	while (-1 != (bytesRead = in.read(buff, 0, buff.length)))
    	{
      		baos.write(buff,0,bytesRead);                               
    	}    
    	in.close();
    	in=null;
    	buff=null;
    	
    	return baos.toByteArray();
	}
	
	//从文件中读取RSA公钥
	private static byte[] getRSAPublicKeyFromDerFile() throws Exception
	{
		InputStream in = RSACoder.class.getResourceAsStream("public_key.der");
		int bytesRead=0;
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		byte[] buff=new byte[1024];
		while (-1 != (bytesRead = in.read(buff, 0, buff.length)))
		{
			baos.write(buff,0,bytesRead);                               
		}    
		in.close();
		in=null;
		buff=null;
		
		return baos.toByteArray();
	}
	
	/**
	*
	* @param args
	* @throws Exception
	*/
	public static void main(String[] args) throws Exception 
	{		
//		//生成密钥对
//		
//		KeyPair keyPair =  generateKeyPair();
//		RSAPublicKey pubKey = (RSAPublicKey) keyPair.getPublic();
//		RSAPrivateKey priKey = (RSAPrivateKey) keyPair.getPrivate();		
//		
//		//保存公钥成DER格式
//		byte[] pubKeyDerBytes = pubKey.getEncoded();
//		File file1=new File("/public_key.der");
//		OutputStream out1 = new FileOutputStream(file1);
//		out1.write(pubKeyDerBytes);
//		out1.close();
//		
//		//保存私钥成DER格式
//		byte[] priKeyDerBytes = priKey.getEncoded();//Java能识别的DER格式
//		File file2=new File("/private_key.der");
//		OutputStream out2 = new FileOutputStream(file2);
//		out2.write(priKeyDerBytes);
//		out2.close();
		
		String _encode = encode("叶吓云、杨先亭");
    	System.out.println("私钥加密："+_encode);
    	
    	String _decode = decode(_encode);
    	System.out.println("公钥解密："+_decode);
	}
}

