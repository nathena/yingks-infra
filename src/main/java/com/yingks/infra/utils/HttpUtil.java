package com.yingks.infra.utils;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
/**
 * 
 * @author nathena
 * 
 */
public final class HttpUtil 
{

	public static String doGet( String url,Map<String, String> headers )
	{
		String reponseBody = "";
		
		HttpClient httpclient = new HttpClient();
		GetMethod get = new GetMethod(url);
		get.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8");
		
		if( null!=headers && headers.size()>0 )
		{
			for(String key : headers.keySet())
			{
				get.addRequestHeader(key, headers.get(key));
			}
		}
		try
		{
			httpclient.executeMethod(get);
			reponseBody = new String(get.getResponseBody(),"UTF-8");
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			get.releaseConnection();
		}
		 
		return reponseBody;
	}
	
	public static String doPost(String url,Map<String, String> params,Map<String, String> headers)
	{
		String reponseBody = "";
		
		HttpClient httpclient = new HttpClient();
		PostMethod method = new PostMethod(url);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8");
		
		if( null!=headers && headers.size()>0 )
		{
			for(String key : headers.keySet())
			{
				method.addRequestHeader(key, headers.get(key));
			}
		}
		try
		{
			if(params != null)
			{
				for (Entry<String, String> e : params.entrySet()) 
				{
					method.addParameter(e.getKey(), e.getValue());
				}
			}
			
			httpclient.executeMethod(method);
			reponseBody = new String(method.getResponseBody(),"UTF-8");
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			method.releaseConnection();
		}
		 
		return reponseBody;
	}
	
	public static byte[] doGetBytes( String url ,Map<String, String> headers )
	{
		byte[] reponseBody = null;
		
		HttpClient httpclient = new HttpClient();
		GetMethod method = new GetMethod(url);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8");
		
		if( null!=headers && headers.size()>0 )
		{
			for(String key : headers.keySet())
			{
				method.addRequestHeader(key, headers.get(key));
			}
		}
		try
		{
			httpclient.executeMethod(method);
			reponseBody = method.getResponseBody();
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			method.releaseConnection();
		}
		 
		return reponseBody;
	}
	
	public static byte[] doPostBytes(String url,Map<String, String> params,Map<String, String> headers)
	{
		byte[] reponseBody = null;
		
		HttpClient httpclient = new HttpClient();
		PostMethod method = new PostMethod(url);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8");
		
		if( null!=headers && headers.size()>0 )
		{
			for(String key : headers.keySet())
			{
				method.addRequestHeader(key, headers.get(key));
			}
		}
		try
		{
			if(params != null)
			{
				for (Entry<String, String> e : params.entrySet()) 
				{
					method.addParameter(e.getKey(), e.getValue());
				}
			}
			
			httpclient.executeMethod(method);
			reponseBody = method.getResponseBody();
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			method.releaseConnection();
		}
		 
		return reponseBody;
	}
	
	public static byte[] doRestPost(String url,String params,Map<String, String> headers)
	{
		byte[] reponseBody = null;
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		post.setHeader(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8");


		if( null!=headers && headers.size()>0 )
		{
			for(String key : headers.keySet())
			{
				post.addHeader(key, headers.get(key));
			}
		}
		try
		{
			if(params != null)
			{
				post.setEntity(new StringEntity(params, Charset.forName("UTF-8")));
			}

			CloseableHttpResponse response = httpclient.execute(post);
			reponseBody = EntityUtils.toByteArray(response.getEntity());
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			post.releaseConnection();
		}
		 
		return reponseBody;
	}
	
	public static byte[] doRestPost(String url,String params)
	{
		byte[] reponseBody = null;
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		post.setHeader(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8");

		try
		{
			if(params != null)
			{
				post.setEntity(new StringEntity(params, Charset.forName("UTF-8")));
			}

			CloseableHttpResponse response = httpclient.execute(post);
			reponseBody = EntityUtils.toByteArray(response.getEntity());
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			post.releaseConnection();
		}
		 
		return reponseBody;
	}
	
	public static void main(String[] arg) throws Exception
	{
//		HttpClient httpclient = new HttpClient();
//		PostMethod post = new PostMethod("https://tgw.baofoo.com/wapmobile");
//		post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8");
//		try {
//		    httpclient.executeMethod(post);
//		    System.out.println(post.getResponseBodyAsString());
//		  } finally {
//			  post.releaseConnection();
//		  }
		HttpUtil.doRestPost("http://www.baidu.com", "在遥");
	}
}
