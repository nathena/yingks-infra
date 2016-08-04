package com.yingks.wx.msg;

public class NamingVerifyFail extends AbstractEventMessage {

	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;

	private String FailTime;      //失败发生时间 (整形)，时间戳
	private String FailReason;    //认证失败的原因 
	
	
	public String getFailTime() {
		return FailTime;
	}
	public void setFailTime(String failTime) {
		FailTime = failTime;
	}
	public String getFailReason() {
		return FailReason;
	}
	public void setFailReason(String failReason) {
		FailReason = failReason;
	}
}
