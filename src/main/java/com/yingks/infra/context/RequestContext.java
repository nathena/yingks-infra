package com.yingks.infra.context;

import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.yingks.infra.crypto.RSACoder;
import com.yingks.infra.utils.IpUtil;
import com.yingks.infra.utils.StringUtil;
import com.yingks.infra.utils.Validator;


public class RequestContext 
{
	private Map<String,Cookie> _cookies = new ConcurrentHashMap<String, Cookie>();
	
	private Map<String, Object> _cookieBeans = new ConcurrentHashMap<String, Object>();
	
	private Map<String, Object> _sessionBeans = new ConcurrentHashMap<String, Object>();
	
	private HttpSession httpSession;
	
	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private String ip;
	
	private String domain;
	
	private String severName;
	
	private String contextPath;
	
	private int port;
	
	private String port2 = "";
	
	public String getPort2() {
		return port2;
	}

	public int getPort() {
		return port;
	}
	
	public String getContextPath() {
		return contextPath;
	}

	public RequestContext(HttpServletRequest request, HttpServletResponse response)
	{
		this.request = request;
		this.response = response;

		String severName = request.getServerName();
		String _server = "";
		if( !StringUtil.isEmpty(severName) && !Validator.isIpv4(severName) && !"localhost".equalsIgnoreCase(severName))
		{
			String[] hNames = severName.split("\\.");
			if( null != hNames && hNames.length>2 )
			{
				_server   = severName.substring(0, severName.indexOf("."));
				severName = "."+severName.substring(severName.indexOf(".")+1);
			}
		}
		this.domain = severName;
		this.severName = _server;
		this.contextPath = request.getContextPath();
		this.port = request.getServerPort();
		if( 80 != this.port )
		{
			this.port2 = ":"+this.port;
		}
		
		this.ip = IpUtil.getRemoteAddr(request);
		this.httpSession = request.getSession();
		
		this.request.setAttribute("ctx", this.contextPath);
		
		initCookies();
	}
	
	public void destroy()
	{
		_sessionBeans.clear();
		_cookieBeans.clear();
		_cookies.clear();
	}
	
	public void destroySession()
	{
		clearCookie();
		clearSession();
		
		destroy();
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getBeanFormSession(Class<T> clazz)
	{
		String key = clazz.getName();
		if( !_sessionBeans.containsKey(key))
		{
			String value = (String)getSession(key);
			if( !StringUtil.isEmpty(value))
			{
				_sessionBeans.put(key, JSON.parseObject(value, clazz));
			}
		}
		
		return (T)_sessionBeans.get(key);
	}
	
	public <T> void addBeanToSession(T object)
	{
		String data = JSON.toJSONString(object);
		String key = object.getClass().getName();
		
		setSession(key, data);
		_sessionBeans.put(key, object);
	}
	
	public <T> void removeBeanFormSession(Class<T> clazz)
	{
		String key = clazz.getName();
		
		_sessionBeans.remove(key);
		removeSession(key);
	}
	
	//-- begin cookie cache

	@SuppressWarnings("unchecked")
	public <T> T getBeanFormCookie(Class<T> clazz)
	{
		String key = clazz.getName();
		if( !_cookieBeans.containsKey(key))
		{
			String value = getCookieValue(key);
			if( !StringUtil.isEmpty(value))
			{
				_cookieBeans.put(key, JSON.parseObject(value, clazz));
			}
		}
		
		return (T)_cookieBeans.get(key);
	}
	
	public <T> void addBeanToCookie(T object)
	{
		String data = JSON.toJSONString(object);
		String key = object.getClass().getName();
		
		setCookie(key, data);
		_cookieBeans.put(key, object);
	}
	
	public <T> void addBeanToCookie(T object,int cookie_expire)
	{
		String data = JSON.toJSONString(object);
		String key = object.getClass().getName();
		
		setCookie(key, data, cookie_expire);
		_cookieBeans.put(key, object);
	}
	
	public <T> void addBeanToCookie(T object,int cookie_expire,String path)
	{
		String data = JSON.toJSONString(object);
		String key = object.getClass().getName();
		
		setCookie(key, data, cookie_expire,path);
		_cookieBeans.put(key, object);
	}
	
	public <T> void removeBeanFormCookie(Class<T> clazz)
	{
		String key = clazz.getName();
		
		_cookieBeans.remove(key);
		removeCookie(key);
	}
	
	//-- end bean cache
	
	public Object getSession(String session_name)
	{
		return httpSession.getAttribute(session_name);
	}
	
	public void setSession(String session_name, Object mixed)
	{
		httpSession.setAttribute(session_name, mixed);
	}
	
	public void removeSession(String session_name)
	{
		httpSession.removeAttribute(session_name);
	}
	
	public void clearSession()
	{
		Enumeration<String> listSess =  httpSession.getAttributeNames();
		while(listSess.hasMoreElements())
		{
			removeSession(listSess.nextElement());
		}
	}
	
	public Cookie getCookie(String cookie_name)
	{
		return _cookies.get(cookie_name);
	}
	
	public String getCookieValue(String cookie_name)
	{
		String value = "";
		Cookie cookie = getCookie(cookie_name);
		
		if(cookie !=null)
		{
			value = cookie.getValue();
			if(!StringUtil.isEmpty(value))
			{
				value = RSACoder.decode(cookie.getValue());
			}
		}
		
		return value;
	}
	
	public void setCookie(String cookie_name,String cookie_value,int cookie_expire,String cookie_path)
	{
		setCookie(cookie_name,cookie_value,cookie_expire,cookie_path,this.domain);
	}
	
	public void setCookie(String cookie_name,String cookie_value,int cookie_expire)
	{
		setCookie(cookie_name,cookie_value,cookie_expire,"/",this.domain);
	}
	
	public void setCookie(String cookie_name,String cookie_value)
	{
		setCookie(cookie_name,cookie_value,-1,"/",this.domain);
	}
	
	public void removeCookie(String cookie_name)
	{
		_cookies.remove(cookie_name);
		setCookie(cookie_name,null);
	}
	
	public void clearCookie()
	{
		for(String name : _cookies.keySet())
		{
			removeCookie(name);
		}
	}
	
	public String getIp() {
		return ip;
	}
	
	public String getDomain() {
		return domain;
	}
	
	public String getServerName()
	{
		return severName;
	}

	private void initCookies()
	{
		Cookie[] cookies = request.getCookies();
		if(null==cookies||0==cookies.length){
			return;
		}
		
		for(Cookie cookie : cookies)
		{
			_cookies.put(cookie.getName(), cookie);
		}
	}
	
	private void setCookie(String cookie_name,String cookie_value,int cookie_expire,String cookie_path,String cookie_domain)
	{
		if( !StringUtil.isEmpty(cookie_value))
		{
			cookie_value = RSACoder.encode(cookie_value);
		}
		
		Cookie cookie = new Cookie(cookie_name, cookie_value);
		cookie.setMaxAge(cookie_expire);
		
		if( !StringUtil.isEmpty(cookie_path))
		{
			cookie.setPath(cookie_path);
		}
		
		if( !StringUtil.isEmpty(cookie_domain) )
		{
			cookie.setDomain(cookie_domain);
		}
		
		cookie.setHttpOnly(true);
		
	    response.addCookie(cookie);
	}
}
