package com.yingks.wx.msg;

public class NamingVerifySuccess extends AbstractEventMessage {

	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;

	private String ExpiredTime;//有效期 (整形)，指的是时间戳，将于该时间戳认证过期

	public String getExpiredTime() {
		return ExpiredTime;
	}

	public void setExpiredTime(String expiredTime) {
		ExpiredTime = expiredTime;
	}
	
	
}
