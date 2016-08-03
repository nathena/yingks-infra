package com.yingks.wx.exception;

public enum WxExceptionMsg 
{
	wx_resp_nothing("10000000","微信放回空"),
	wx_check_user_error("10000005","验证微信用户详细信息异常。msg:%s"),
	wx_userinfo_error("10000004","获取微信用户详细信息异常。msg:%s"),
	wx_openid_refresh("10000003","刷新微信用户openid异常，msg:%s"),
	wx_openid_error("10000002","获取微信用户openid异常，code:%s,msg:%s"),
	wx_openid_code_empty("10000001","获取微信用户openid异常，code:%s"),
	wx_access_token_response_nothing("10000000","获取微信access_token返回值为空，请检查网络"),
	wx_openids_not_null("10000006","微信openid不能为空"),
	wx_error_40013("40013","invalid appid"),
	not_null("1","不允许为空");
	
	
	private String code;
	private String msg;
	
	private WxExceptionMsg(String code,String msg)
	{
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}
	
	public String getMsg(Object[] values) {
		return String.format(msg, values);
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
