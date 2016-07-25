package com.yingks.infra.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;

public class GZipUtil {

	public static byte[] decode(byte[] data)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		GZIPInputStream gzip = null;
		ByteArrayInputStream bais = null;
		try
		{
			bais = new ByteArrayInputStream(data);
			gzip = new GZIPInputStream(bais);
			byte[] buf = new byte[1024*1024];
			int num = -1;
			while ((num = gzip.read(buf, 0, buf.length)) != -1) 
			{
			    baos.write(buf, 0, num);
			}
		}
		catch(Exception e)
		{
			LogHelper.info(" Not in GZIP format ==== "+new String(data));
			try
			{
				baos.write(data);
			}
			catch(Exception ex)
			{
				
			}
		}
		finally
		{
			try
			{
				if( null != gzip )
				{
					gzip.close();
				}
			}
			catch(Exception e)
			{
				
			}
			
			try
			{
				baos.close();
			}
			catch(Exception e)
			{
				
			}
			
			try
			{
				bais.close();
			}
			catch(Exception e)
			{
				
			}
		}
		
		return baos.toByteArray();
	}
}
