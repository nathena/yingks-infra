package com.yingks.wx.mp;

import java.io.Serializable;

public class WxMpQrResult implements Serializable {

	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;

	private String ticket;
	private String expire_seconds;
	private String url;
	private String qrCodeUrl;
	
	public void setQrCodeUrl(String qrCodeUrl) {
		this.qrCodeUrl = qrCodeUrl;
	}

	public String getQrCodeUrl()
	{
		return this.qrCodeUrl;
	}
	
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
		this.qrCodeUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket;
	}
	public String getExpire_seconds() {
		return expire_seconds;
	}
	public void setExpire_seconds(String expire_seconds) {
		this.expire_seconds = expire_seconds;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
