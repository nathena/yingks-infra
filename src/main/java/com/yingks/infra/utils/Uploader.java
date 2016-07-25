package com.yingks.infra.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yingks.infra.context.AppsContext;

public class Uploader {

	/**
	 * 文件头部信息，十六进制信息，取前4位 
	 * JPEG (jpg)，文件头：FFD8FFe1
	 * PNG (png)，文件头：89504E47 
	 * GIF (gif)，文件头：47494638 
	 * TIFF (tif)，文件头：49492A00 
	 * Windows Bitmap (bmp)，文件头：424D
	 * CAD (dwg)，文件头：41433130 
	 * Adobe Photoshop (psd)，文件头：38425053 
	 * Rich Text Format (rtf)，文件头：7B5C727466 
	 * XML (xml)，文件头：3C3F786D6C HTML
	 * (html)，文件头：68746D6C3E 
	 * Email [thorough only]  (eml)，文件头：44656C69766572792D646174653A 
	 * Outlook Express (dbx)，文件头：CFAD12FEC5FD746F 
	 * Outlook (pst)，文件头：2142444E 
	 * MS Word/Excel (xls.or.doc)，文件头：D0CF11E0 
	 * MS Access (mdb)，文件头：5374616E64617264204A
	 * WordPerfect (wpd)，文件头：FF575043 
	 * Postscript (eps.or.ps)，文件头：252150532D41646F6265 
	 * Adobe Acrobat (pdf)，文件头：255044462D312E 
	 * Quicken (qdf)，文件头：AC9EBD8F
	 * Windows Password (pwl)，文件头：E3828596 
	 * ZIP Archive (zip)，文件头：504B0304 
	 * RAR Archive (rar)，文件头：52617221 
	 * Wave (wav)，文件头：57415645 
	 * AVI (avi)，文件头：41564920 
	 * Real Audio (ram)，文件头：2E7261FD 
	 * Real Media (rm)，文件头：2E524D46 
	 * MPEG (mpg)，文件头：000001BA 
	 * MPEG (mpg)，文件头：000001B3 
	 * Quicktime (mov)，文件头：6D6F6F76
	 * Windows Media (asf)，文件头：3026B2758E66CF11 
	 * MIDI (mid)，文件头：4D546864
	 * MP4 (mp4)，文件头：文件头：00000020667479706d70
	 */
	private static final String allowUploadFileType = "ffd8ffdb 89504e47 504e470d ffd8ffe1 40496e69 ffd8ffe0 47494638 d0cf11e0 49545346 25504446 504B0304 52617221 00000020667479706d70 0000001c";
	
	private static final String[] allowUploadFileSuffix = new String[]{"gif","jpg","jpeg","png","bmp","mp4","zip","rar"};
	
	private static boolean useDefault = false;
	
	private static Set<String> allowUploadExts = new HashSet<>();
	private static Map<String, String> extToTypeBytes = new HashMap<>();
	static {
		try {
			loadMimeTypes();
			initSetting();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void initSetting() {
		if(useDefault) {
			allowUploadExts.add("gif");
			allowUploadExts.add("jpg");
			allowUploadExts.add("jpeg");
			allowUploadExts.add("png");
			allowUploadExts.add("bmp");
			allowUploadExts.add("mp4");
			allowUploadExts.add("zip");
			allowUploadExts.add("rar");
		}
		
		extToTypeBytes.put("jpeg", "FFD8FFE1");extToTypeBytes.put("jpg", "FFD8FFe1");
		extToTypeBytes.put("png", "89504E47");extToTypeBytes.put("gif", "47494638");
		extToTypeBytes.put("tif", "49492A00");extToTypeBytes.put("bmp", "424D");
		extToTypeBytes.put("dwg", "41433130");extToTypeBytes.put("psd", "38425053");
		extToTypeBytes.put("rtf", "7B5C727466");extToTypeBytes.put("xml", "3C3F786D6C");
		extToTypeBytes.put("html", "68746D6C3E");extToTypeBytes.put("eml", "44656C69766572792D646174653A");
		extToTypeBytes.put("dbx", "CFAD12FEC5FD746F");extToTypeBytes.put("pst", "2142444E");
		extToTypeBytes.put("xls", "D0CF11E0");extToTypeBytes.put("doc", "D0CF11E0");
		extToTypeBytes.put("mdb", "5374616E64617264204A");extToTypeBytes.put("wpd", "FF575043");
		extToTypeBytes.put("eps", "252150532D41646F6265");extToTypeBytes.put("ps", "252150532D41646F6265");
		extToTypeBytes.put("pdf", "AC9EBD8F");extToTypeBytes.put("qdf", "AC9EBD8F");
		extToTypeBytes.put("pwl", "E3828596");extToTypeBytes.put("zip", "504B0304");
		extToTypeBytes.put("rar", "52617221");extToTypeBytes.put("wav", "57415645");
		extToTypeBytes.put("avi", "41564920");extToTypeBytes.put("ram", "2E524D46");
		extToTypeBytes.put("rm", "2E524D46");extToTypeBytes.put("mpg", "000001BA");//TODO MPEG (mpg)，文件头：000001B3 
		extToTypeBytes.put("mov", "6D6F6F76");extToTypeBytes.put("asf", "3026B2758E66CF11");
		extToTypeBytes.put("mid", "4D546864");extToTypeBytes.put("mp4", "00000020667479706d70");
	}
	
	private static void loadMimeTypes() throws IOException {
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("mime-types.properties");
		if( null != in ) {
			Properties properties = new Properties();   
			properties.load(in);
			String allowUpload = properties.getProperty("upload_allow");
			
			if(StringUtil.isEmpty(allowUpload)) {
				useDefault = true;
				LogHelper.warn("==== 没有自定义上传类型限制,将允许默认上传类型");
			} else {
				allowUploadExts.addAll(Arrays.asList(allowUpload.split(",")));
			}
		} else {
			useDefault = true;
		}
	}
	
	private static void validate(byte[] b, String fileExt) {
		if(!allowUploadExts.contains(fileExt)) {
			LogHelper.error("allow etxs:" + StringUtils.join(allowUploadExts, ",") + ",really etx:" + fileExt);
			
			throw new RuntimeException("不被允许扩展名");
		}
		
		if (b != null) 
		{
			int size = b.length;
			String hex = null;
			StringBuilder contentType = new StringBuilder();
			for (int i = 0; i < size; i++) 
			{
				hex = Integer.toHexString(b[i] & 0xFF);
				if (hex.length() == 1) 
				{
					hex = "0" + hex;
				}
				contentType.append(hex);
				if (i > 2) 
				{
					break;
				}
			}
			
			String byteStr = extToTypeBytes.get(fileExt);
			if(byteStr != null && contentType.toString().startsWith(byteStr)) {
				LogHelper.error("expect byte data:" + byteStr.toLowerCase() + ",really byte data:" + contentType);
				throw new RuntimeException("不支持的上传类型");
			}
		}
	}
	
	/**
	 * 不指定文件参数名的上传--单文件
	 * @param request
	 * @param path
	 * @param name
	 * @return
	 * @throws IOException
	 * @throws RuntimeException
	 */
	public static String saveAsFile(HttpServletRequest request, String path, Object name) throws IOException,RuntimeException 
	{
		if( null == request || StringUtil.isEmpty(path) || StringUtil.isEmpty(name) )
		{
			throw new RuntimeException("上传文件参数有错");
		}
		
		String savedName = "";
		try
		{
			MultipartHttpServletRequest picRequest = (MultipartHttpServletRequest) request;
			Map<String, MultipartFile> tmpfiles = picRequest.getFileMap();
	
			if (!CollectionUtil.isEmpty(tmpfiles)) 
			{
				MultipartFile file = tmpfiles.get(0);
				byte[] data = file.getBytes();
				
				//检查扩展名
				String tmpfileName = file.getOriginalFilename();
				String fileExt = tmpfileName.substring(tmpfileName.lastIndexOf(".") + 1).toLowerCase();
				validate(data, fileExt);
				
				savedName = saveFile(data, path, name + "." + fileExt);
			}
			else
			{
				LogHelper.info(" ==== Uploader saveAsFile 上传文件为空");
			}
		}
		catch(ClassCastException e)
		{
			LogHelper.error(e.getMessage(), e);
		}

		return savedName;
	}
	
	/**
	 * 不指定文件参数名的上传--多文件
	 * @param request
	 * @param path
	 * @param name
	 * @return
	 * @throws IOException
	 * @throws RuntimeException
	 */
	public static List<String> saveAsFiles(HttpServletRequest request, String path, Object name) throws IOException,RuntimeException 
	{
		if( null == request || StringUtil.isEmpty(path) || StringUtil.isEmpty(name) )
		{
			throw new RuntimeException("上传文件参数有错");
		}
		
		List<String> fileNames = new ArrayList<>();
		try
		{
			MultipartHttpServletRequest picRequest = (MultipartHttpServletRequest) request;
			Map<String, MultipartFile> tmpfiles = picRequest.getFileMap();
	
			if (!CollectionUtil.isEmpty(tmpfiles)) 
			{
				for(MultipartFile file: tmpfiles.values()) {
					if (null != file && 0<file.getSize() ) 
					{
						byte[] data = file.getBytes();
						
						//检查扩展名
						String tmpfileName = file.getOriginalFilename();
						String fileExt = tmpfileName.substring(tmpfileName.lastIndexOf(".") + 1).toLowerCase();
						validate(data, fileExt);
						
						String savedName = saveFile(data, path, name + "." + fileExt);
						fileNames.add(savedName);
					}
				}
			}
			else
			{
				LogHelper.info(" ==== Uploader saveAsFile 上传文件为空");
			}
		}
		catch(ClassCastException e)
		{
			LogHelper.error(e.getMessage(), e);
		}

		return fileNames;
	}
	
	public static String saveAsFile(HttpServletRequest request,String uploadName, String path, Object name) throws IOException,RuntimeException 
	{
		if( null == request || StringUtil.isEmpty(uploadName) || StringUtil.isEmpty(path) || StringUtil.isEmpty(name) )
		{
			throw new RuntimeException("上传文件参数有错");
		}
		
		String savedName = "";
		try
		{
			MultipartHttpServletRequest picRequest = (MultipartHttpServletRequest) request;
			MultipartFile tmpfile = picRequest.getFile(uploadName);
	
			if (null != tmpfile && 0<tmpfile.getSize() ) 
			{
				byte[] data = tmpfile.getBytes();
				
				//数据检验
				String tmpfileName = tmpfile.getOriginalFilename();
				String fileExt = tmpfileName.substring(tmpfileName.lastIndexOf(".") + 1).toLowerCase();
				validate(data, fileExt);
				
				savedName = saveFile(data, path, name + "." + fileExt);
			}
			else
			{
				LogHelper.info(" ==== Uploader saveAsFile 上传文件 "+uploadName+" 为空");
			}
		}
		catch(ClassCastException e)
		{
			LogHelper.error(e.getMessage(), e);
		}

		return savedName;
	}

	public static List<String> saveAsFiles(HttpServletRequest request,String uploadName, String path, Object name) throws IOException,RuntimeException
	{
		if( null == request || StringUtil.isEmpty(uploadName) || StringUtil.isEmpty(path) || StringUtil.isEmpty(name) )
		{
			throw new RuntimeException("上传文件参数有错");
		}

		String savedName = "";
		List<String> fileNames = new ArrayList<>();
		try
		{
			MultipartHttpServletRequest picRequest = (MultipartHttpServletRequest) request;
			List<MultipartFile> tmpfiles = picRequest.getFiles(uploadName);

			if(CollectionUtil.isEmpty(tmpfiles)) {
				return fileNames;
			}

			int index = 0;
			for(MultipartFile tmpfile : tmpfiles) {
				String suffix = index ++ + "";
				if (null != tmpfile && 0<tmpfile.getSize() ) {
					byte[] data = tmpfile.getBytes();
					
					//数据检验
					String tmpfileName = tmpfile.getOriginalFilename();
					String fileExt = tmpfileName.substring(tmpfileName.lastIndexOf(".") + 1).toLowerCase();
					validate(data, fileExt);
					
					savedName = saveFile(data, path, name + suffix + "." + fileExt);
					fileNames.add(savedName);
				}
				else {
					LogHelper.info(" ==== Uploader saveAsFile 上传文件 "+uploadName+" 为空");
				}
			}

		}
		catch(ClassCastException e)
		{
			LogHelper.error(e.getMessage(), e);
		}

		return fileNames;
	}
	
	public static String saveAsFile(HttpServletRequest request,String uploadName, String path, Object name,long maxSize) throws IOException,RuntimeException 
	{
		if( null == request || StringUtil.isEmpty(uploadName) || StringUtil.isEmpty(path) || StringUtil.isEmpty(name) )
		{
			throw new RuntimeException("上传文件参数有错");
		}
		
		String savedName = "";
		try
		{
			MultipartHttpServletRequest picRequest = (MultipartHttpServletRequest) request;
			MultipartFile tmpfile = picRequest.getFile(uploadName);

			if (null != tmpfile && 0<tmpfile.getSize() ) 
			{
				byte[] data = tmpfile.getBytes();
				
				//检查扩展名
				String tmpfileName = tmpfile.getOriginalFilename();
				String fileExt = tmpfileName.substring(tmpfileName.lastIndexOf(".") + 1).toLowerCase();
				validate(data, fileExt);
				
				savedName = saveFile(data, path, name + "." + fileExt);
			}
			else
			{
				LogHelper.info(" ==== Uploader saveAsFile 上传文件 "+uploadName+" 为空");
			}
		}
		catch(ClassCastException e)
		{
			LogHelper.error(e.getMessage(), e);
		}

		return savedName;
	}
	
	public static String saveAsFile(MultipartFile file, String path, Object name,long maxSize) throws IOException,RuntimeException 
	{
		if( null == file || StringUtil.isEmpty(path) || StringUtil.isEmpty(name) )
		{
			throw new RuntimeException("上传文件参数有错");
		}
		
		String savedName = "";
		try
		{
			if (null != file && 0<file.getSize() ) 
			{
				byte[] data = file.getBytes();
				
				//检查扩展名
				String tmpfileName = file.getOriginalFilename();
				String fileExt = tmpfileName.substring(tmpfileName.lastIndexOf(".") + 1).toLowerCase();
				validate(data, fileExt);
				
				savedName = saveFile(data, path, name + "." + fileExt);
			}
			else
			{
				LogHelper.info(" ==== Uploader saveAsFile 上传文件 "+file.getOriginalFilename()+" 为空");
			}
		}
		catch(ClassCastException e)
		{
			LogHelper.error(e.getMessage(), e);
		}

		return savedName;
	}
	
	public static String saveAsFile(FileItem item,String path, Object name,long maxSize) throws IOException,RuntimeException 
	{
		String savedName = "";
		
		if (null != item && !item.isFormField() && 0 < item.getSize()) 
		{
			byte[] data = item.get();
			
			//检查扩展名
			String fileName = item.getName();
			String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
			validate(data, fileExt);
			
			savedName = saveFile(data, path, name + "." + fileExt);
		}

		return savedName;
	}

	private static boolean validateType(byte[] b) 
	{
		if (b != null) 
		{
			int size = b.length;
			String hex = null;
			StringBuilder contentType = new StringBuilder();
			for (int i = 0; i < size; i++) 
			{
				hex = Integer.toHexString(b[i] & 0xFF);
				if (hex.length() == 1) 
				{
					hex = "0" + hex;
				}
				contentType.append(hex);
				if (i > 2) 
				{
					break;
				}
			}
			
			if (allowUploadFileType.toLowerCase().indexOf(contentType.toString()) > -1) 
			{
				return true;
			}
		}
		return false;
	}
	
	private static String saveFile(byte[] data,String path,String fileName) throws IOException
	{
		String savePath = AppsContext.uploadDir().concat("/").concat(path);
		File foder = new File(savePath);
		if (!foder.exists()) 
		{
			if( !foder.mkdirs() )
			{
				LogHelper.error("上传文件时，创建文件夹"+foder.getAbsolutePath()+"失败");
				throw new RuntimeException("上传文件时，创建文件夹失败");
			}
		}

		File file = new File(savePath,fileName);
		
		FileCopyUtils.copy(data, file);

		return path + "/" + fileName;
	}
	
	private static String byteToHexString(byte[] b)
	{
		StringBuilder contentType = new StringBuilder();
		if (b != null) 
		{
			int size = b.length;
			String hex = null;
			
			for (int i = 0; i < size; i++) 
			{
				hex = Integer.toHexString(b[i] & 0xFF);
				if (hex.length() == 1) 
				{
					hex = "0" + hex;
				}
				contentType.append(hex);
				if (i > 2) 
				{
					break;
				}
			}
		}
		return contentType.toString();
	}
	
	private static byte[] getFileBytes(String path) 
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		FileChannel fc = null;
		FileInputStream is = null;
		try
		{
			is = new FileInputStream(path);
			fc = is.getChannel();
			
			ByteBuffer bb = ByteBuffer.allocate(1024);
			
			while(fc.read(bb)>0)
			{
				baos.write(bb.array());
				bb.clear();
			}
			bb.clear();
		}
		catch(Exception e)
		{
			try {
				is.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			try {
				fc.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
		
		return baos.toByteArray();
	}
	
	public static void main(String[] arg)
	{
		byte[] b = getFileBytes("/Users/nathena/Downloads/jytnn/images/banner33.png");
		
		System.out.println(byteToHexString(b));
	}
}
