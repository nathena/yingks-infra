package com.yingks.infra.pay;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.yingks.infra.context.AppsContext;
import com.yingks.infra.context.RequestContext;

public abstract class AbstractPayOperation {

	private static Logger logger  = Logger.getLogger(AbstractPayOperation.class);
	
	protected PayNotifyAbleInterface paymentOperation;
	
	public AbstractPayOperation(PayNotifyAbleInterface paymentOperation)
	{
		this.paymentOperation = paymentOperation;
	}
	
	protected void toResponse(Object content)
	{
		logger.debug("\n返回结果:==" + content.toString() + "==");
		toResponse(content,"text/plain");
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
