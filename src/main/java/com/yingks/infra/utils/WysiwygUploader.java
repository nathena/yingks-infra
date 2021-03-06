package com.yingks.infra.utils;

import java.io.ByteArrayInputStream;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.yingks.infra.crypto.Base64Coder;

public class WysiwygUploader {

	private static Logger logger = Logger.getLogger(WysiwygUploader.class);
	
	/**
	 * 
	 * @Title: weaveData
	 * @author nathena
	 * @param @param data
	 * @param @param storePath 图片存取路径
	 * @param @param remoteHost 图片访问路径
	 * @param @return
	 * @return String 
	 * @throws
	 */
	public static String weaveData(String data,String storePath,String remoteHost)
	{
		Document doc = Jsoup.parse(data);
		
		Elements imgs = doc.getElementsByTag("img");
		for(Element img : imgs)
		{
			processImageElement(img,storePath,remoteHost);
		}
		return doc.body().html();
	}
	
	private static void processImageElement(Element image,String storePath,String remoteHost)
	{
		try
		{
			if( null == image || StringUtil.isEmpty(storePath) || StringUtil.isEmpty(remoteHost))
			{
				return;
			}
			
			String src = image.attr("src");
			
			if( StringUtil.isEmpty(src) )
			{
				return;
			}
			
			int index = src.indexOf(";base64,");
			if( index<0 )
			{
				index = src.indexOf(";BASE64,");
			}
			if( index <0 )
			{
				return;
			}
			
			String data = src.substring(index+8, src.length());
			String mimeVal = src.substring(5,index);//data:
				   mimeVal = mimeVal.split("\\/")[1]; //imaga/png=>png
				   
			String path = "/"+SimpleUUIDGenerator.generateUUID()+"."+mimeVal;
			File imageFile = new File(storePath+path);
			if (!imageFile.exists()) 
			{
				if( !imageFile.mkdirs() )
				{
					logger.debug("上传文件时，创建文件夹"+imageFile.getAbsolutePath()+"失败");
					throw new RuntimeException("上传文件时，创建文件夹失败");
				}
			}
			
			ImageIO.write(ImageIO.read(new ByteArrayInputStream(Base64Coder.decode(data))), mimeVal, imageFile);
			
			image.attr("src", remoteHost+path);
		}
		catch(Exception e)
		{
			logger.error(e);
		}
	}
}
