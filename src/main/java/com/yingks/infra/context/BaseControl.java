package com.yingks.infra.context;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yingks.infra.utils.LogHelper;
import com.yingks.infra.utils.NumberUtil;

public abstract class BaseControl {
	
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response)
	{
		return true;
	}

	public boolean afterCompletion(HttpServletRequest request,HttpServletResponse response)
	{
		return true;
	}
	
	public void toResponse(Object content)
	{
		LogHelper.debug("\n返回结果:==" + content.toString() + "==");
		toResponse(content,"text/plain");
	}
	
	public int getRequestPageNo()
	{
		RequestContext contexts = AppsContext.currentRequestContext();
		HttpServletRequest request = contexts.getRequest();
		String page = request.getParameter("page");
		if(page==null)
		{
			return 1;
		}
		else
		{
			return Integer.valueOf(page,10);
		}
	}
	
	public int getRequestPageRows()
	{
		RequestContext contexts = AppsContext.currentRequestContext();
		HttpServletRequest request = contexts.getRequest();
		
		String rows = request.getParameter("rows");
		if(rows==null)
		{
			int _pageRow = NumberUtil.parseInt(AppsContext.getProperty("pageRows"));
			
			return _pageRow>0?_pageRow:10;
		}
		else
		{
			return Integer.valueOf(rows,10);
		}
	}
	
	public boolean isAjaxRequest(HttpServletRequest request)
	{
		//ajax 请求
		if( "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With")) || NumberUtil.parseInt(request.getParameter("inajax")) > 0 )
		{
			return true;
		}
		return false;
	}
	
	protected void toResponse(Object content,String content_type)
	{
		RequestContext contexts = AppsContext.currentRequestContext();
		HttpServletResponse response = contexts.getResponse();
		
		PrintWriter writer = null;
		try
		{
			if(content_type!=null)
			{
				response.setContentType(content_type);
			}
			response.setCharacterEncoding("UTF-8");
			
			writer = response.getWriter();
			writer.print(content);
			writer.flush();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(writer!=null)
			{
				writer.close();
			}
		}
	}
}
