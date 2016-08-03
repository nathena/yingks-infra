package com.yingks.wx;

import java.io.Serializable;

public class WxAccessToken implements Serializable 
{

	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String token;
	private String appId;
	private String appSercet;
	private int expiresIn;
	private int createIn;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public int getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
	public int getCreateIn() {
		return createIn;
	}
	public void setCreateIn(int createIn) {
		this.createIn = createIn;
	}
	public String getAppSercet() {
		return appSercet;
	}
	public void setAppSercet(String appSercet) {
		this.appSercet = appSercet;
	}

}
