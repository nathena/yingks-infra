package com.yingks.infra.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SpringHandlerInterceptorAdapter extends HandlerInterceptorAdapter
{

	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, 
			Object handler) throws Exception {
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		if( handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod)handler;
			Object bean = handlerMethod.getBean();

			RequestValidate validates = handlerMethod.getMethodAnnotation(RequestValidate.class);
			if(validates != null) {
				String errMsg = RequestValidator.getErrorMsg(request, validates);
				if(!RequestValidator.isPass(errMsg)) {
					if(bean instanceof RequestValidateResponse) {
						((RequestValidateResponse)bean).validateResponse(request, response, 
								validates.failedView(), errMsg);
					} else {
						RequestValidator.defaultResponse(request, response, validates.failedView(), errMsg);
					}
					return false;
				}
			}
			
			if(bean instanceof BaseControl) {
				BaseControl obj = (BaseControl)bean;
				
				if(!obj.preHandle(request, response)) {
					return false;
				}
			}
			
			if(bean instanceof MustLoginedInterface) {
				MustLoginedInterface obj = (MustLoginedInterface)bean;
				if(!obj.isLogined(request,response)) {
					return false;
				}
			}
		}
		
		return super.preHandle(request, response, handler);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request,HttpServletResponse response, 
			Object handler, Exception ex) throws Exception {
		
		if( handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod)handler;
			Object bean = handlerMethod.getBean();
			
			if(bean instanceof BaseControl) {
				BaseControl obj = (BaseControl)bean;
				if(!obj.afterCompletion(request, response)) {
					return;
				}
			}
		}
		
		super.afterCompletion(request, response, handler, ex);
	}
}
