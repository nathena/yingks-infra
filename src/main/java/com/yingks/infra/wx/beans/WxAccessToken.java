package com.yingks.infra.wx.beans;

public class WxAccessToken {

	private String accessToken;
	private String appId;
	private String appSercet;
	private int expiresIn;
	private int createIn;
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
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
