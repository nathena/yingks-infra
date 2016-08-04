package com.yingks.wx;

public class WxConfig {

	private String token;
	
	private String appId;
	private String appSecret;
	
	private String redirect_uri;
	private String access_code_state = WxConfig.class.getName();
	
	private OpenidScopeType scope;
	private InfoLangType lang;
	
	public WxConfig(String token, String appId, String appSecret, String redirect_uri, String access_code_state, OpenidScopeType scope, InfoLangType lang)
	{
		this.setToken(token);
		this.setAppId(appId);
		this.setAppSecret(appSecret);
		this.setRedirect_uri(redirect_uri);
		this.setAccess_code_state(access_code_state);
		this.setScope(scope);
		this.setLang(lang);
	}
	
	public enum OpenidScopeType
	{
		snsapi_base,
		snsapi_userinfo
	}
	
	public enum InfoLangType
	{
		zh_CN,zh_TW,en
	}

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

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getRedirect_uri() {
		return redirect_uri;
	}

	public void setRedirect_uri(String redirect_uri) {
		this.redirect_uri = redirect_uri;
	}

	public String getAccess_code_state() {
		return access_code_state;
	}

	public void setAccess_code_state(String access_code_state) {
		this.access_code_state = access_code_state;
	}

	public OpenidScopeType getScope() {
		return scope;
	}

	public void setScope(OpenidScopeType scope) {
		this.scope = scope;
	}

	public InfoLangType getLang() {
		return lang;
	}

	public void setLang(InfoLangType lang) {
		this.lang = lang;
	}

	
}
