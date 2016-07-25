package com.yingks.infra.context;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import com.yingks.infra.utils.LogHelper;
import com.yingks.infra.utils.StringUtil;
import com.yingks.infra.utils.Validator;

public class RequestValidator {
	public static final List<String> RULES = Arrays.asList(
			new String[]{"REQUIRED", "MOBILE", "INTEGER", "DOUBLE", "EQUAL"});
	public static final List<String> SECOND_RULES = Arrays.asList(
			new String[]{"MAX", "MIN", "PARAM", "CONSTANT"});
	
	private static final int INDEX_PARAM = 0, INDEX_RULE = 1, INDEX_VALUE = 2;
	private static final int INDEX_FIRST_RULE = 0, INDEX_SECOND_RULE = 1, INDEX_SECOND_REQUIRED = 2;
	
	private RequestValidator(){};
	//判断是否通过
	public static boolean isPass(String errMsg) {
		return StringUtil.isEmpty(errMsg);
	}
	//参数验证失败时返回的默认实现,controller可实现RequestValidateResponse接口自定义返回
	//默认采用rest风格以http状态码表征请求错误
	public static final void defaultResponse(HttpServletRequest request, HttpServletResponse response, 
			String failedTargetView, String msg) {
		PrintWriter writer = null;
		try {
			if(StringUtil.isEmpty(failedTargetView)) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
				writer = response.getWriter();
				writer.write(msg);
				writer.flush();
			} else {
				request.setAttribute("errMsg", msg);
				request.getRequestDispatcher(failedTargetView).forward(request, response);
			}			
		} catch (ServletException | IOException e) {
			LogHelper.error("跳转时错误", e);
		} finally {
			if(writer != null) {
				writer.close();
			}
		}
	}
	//获取返回的错误描述
	public static final String getErrorMsg(HttpServletRequest request, RequestValidate validates) {
		if(validates != null && validates.fileds() != null) {
			for(String filed : validates.fileds()) {
				String[] filedParts = filed.split(";");
				if(filedParts.length < 2) {
					continue;
				}
				String filedName = filedParts[INDEX_PARAM];
				String rules = filedParts[INDEX_RULE];
				String msg = filedParts.length == 3 ? filedParts[INDEX_VALUE] : "参数错误";
				
				String parameterValue = request.getParameter(filedName);

				for(String rule : rules.split(",")) {
					if(!validate(rule, parameterValue, request)) {
						LogHelper.info("\n 参数" + filedName + ",要求:" + rule + ",实际:" + parameterValue);
						return msg;
					}
				}
			}
		}
		
		return null;
	}
	//单条规则验证
	private static boolean validate(String rule, String value, HttpServletRequest request) {
		if(rule == null) {
			LogHelper.warn("\n 参数验证规则书写有误 rule :" + rule);
			return true;
		}

		String[] rules = rule.split(":");
		rules[INDEX_FIRST_RULE] = rules[INDEX_FIRST_RULE].toUpperCase();
		
		if(!RULES.contains(rules[INDEX_FIRST_RULE])) {
			LogHelper.warn("\n 参数验证规则书写有误 rule :" + rule);
			return true;
		}
		
		switch(rules[INDEX_FIRST_RULE]) {
		case "REQUIRED":
			return requiredRule(rules, value);
		case "MOBILE":
			return mobileRule(value);
		case "INTEGER":
			return integerRule(rules, value);
		case "DOUBLE":
			return floatRule(rules, value);
		case "EQUAL":
			if(rules.length != 3) {
				LogHelper.warn("\n 参数验证规则书写有误 rule :" + rule);
				return true;
			}
			String compareValue = "PARAM".equals(rules[INDEX_SECOND_RULE]) ? rules[INDEX_SECOND_REQUIRED]
					: request.getParameter(rules[INDEX_SECOND_REQUIRED]);
			return equalRule(rules, value, compareValue);
		default:
			LogHelper.warn("\n 参数验证规则书写有误 rule :" + rule);
			return true;
		}
	}
	
	private static boolean requiredRule(String[] rules, String value) {
		if(StringUtil.isEmpty(value)) {
			return false;
		}
		
		if(rules.length == 3) {
			rules[INDEX_SECOND_RULE] = rules[INDEX_SECOND_RULE].toUpperCase();
			
			switch(rules[INDEX_SECOND_RULE]) {
			case "MAX":
				return  value.length() <= Integer.valueOf(rules[INDEX_SECOND_REQUIRED]);
			case "MIN":
				return  value.length() >= Integer.valueOf(rules[INDEX_SECOND_REQUIRED]);
			default:
				LogHelper.warn("\n 参数验证规则书写有误 INDEX_SECOND_RULE :" + rules[INDEX_SECOND_RULE]);
				return true;
			}
		}
		
		return !StringUtil.isEmpty(value);
	}
	
	private static boolean mobileRule(String value) {
		return StringUtil.isEmpty(value) ? true : Validator.isMobile(value);
	}
	
	private static boolean integerRule(String[] rules, String value) {
		if(StringUtil.isEmpty(value)) {
			return true;
		}

		if(!Validator.isNumber(value)) {
			return false;
		}
		
		if(rules.length == 3) {
			rules[INDEX_SECOND_RULE] = rules[INDEX_SECOND_RULE].toUpperCase();
			
			switch(rules[INDEX_SECOND_RULE]) {
			case "MAX":
				return  Integer.valueOf(value) <= Integer.valueOf(rules[INDEX_SECOND_REQUIRED]);
			case "MIN":
				return  Integer.valueOf(value) >= Integer.valueOf(rules[INDEX_SECOND_REQUIRED]);
			default:
				LogHelper.warn("\n 参数验证规则书写有误 INDEX_SECOND_RULE :" + rules[INDEX_SECOND_RULE]);
				return true;
			}
		}
		
		return true;
	}
	
	private static boolean floatRule(String[] rules, String value) {
		if(StringUtil.isEmpty(value)) {
			return true;
		}

		if(!Validator.isFloat(value)) {
			return false;
		}
		
		if(rules.length == 3) {
			rules[INDEX_SECOND_RULE] = rules[INDEX_SECOND_RULE].toUpperCase();
			
			switch(rules[INDEX_SECOND_RULE].toUpperCase()) {
			case "MAX":
				return  Double.valueOf(value) <= Double.valueOf(rules[INDEX_SECOND_REQUIRED]);
			case "MIN":
				return  Double.valueOf(value) >= Double.valueOf(rules[INDEX_SECOND_REQUIRED]);
			default:
				LogHelper.warn("\n 参数验证规则书写有误 INDEX_SECOND_RULE :" + rules[INDEX_SECOND_RULE]);
				return true;
			}
		}
		
		return true;
	}
	
	private static boolean equalRule(String[] rules, String value, String compareValue) {
		if(compareValue == null) {
			return value == null;
		}
		
		return compareValue.equals(value);
	}
}
