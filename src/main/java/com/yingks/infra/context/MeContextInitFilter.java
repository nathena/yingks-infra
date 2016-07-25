/**
 * 
 */
package com.yingks.infra.context;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yingks.infra.utils.LogHelper;

/**
 * @author GaoWx
 *
 */
public class MeContextInitFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		LogHelper.info(" ======= context start ====== ");
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		try
		{
			XssHttpWrapper xssHttpWrapper = new XssHttpWrapper(httpRequest);
			AppsContext.initRequestContext(xssHttpWrapper, (HttpServletResponse)response);
			chain.doFilter(xssHttpWrapper, response);
		}
		finally
		{
			LogHelper.info(" ======= context finally ====== ");
			AppsContext.destoryRequestContext();
		}
	}

	@Override
	public void destroy() {
		
	}

}
