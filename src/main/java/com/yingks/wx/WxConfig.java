package com.yingks.wx;

public class WxConfig {

	private boolean debug;
	
	private String token;
	
	private String appId;
	private String appSecret;
	
	private String redirect_uri;
	private String access_code_state = WxConfig.class.getName();
	
	private OpenidScopeType scope;
	private InfoLangType lang;
	
	//微信支付相关
	public String mchId;
	public String mchKey;	//商户密钥
	public String notifyUrl;  //支付完成后的回调处理页面,*替换成notify_url.asp所在路径
	public String jsWxPayApiUrl; //jsWxPayApiUrl
	public String appNotifyUrl;  //app支付完成后的回调处理页面,*替换成notify_url.asp所在路径
	public String nativeNotifyUrl;  //native支付完成后的回调处理页面,*替换成notify_url.asp所在路径
	public String success_url;
	public String failed_url;
	
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
	
	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public enum OpenidScopeType
	{
		snsapi_base,
		snsapi_userinfo;
		
		public static OpenidScopeType getByName(String name)
		{
			for(OpenidScopeType type: OpenidScopeType.values())
			{
				if( type.name().equals(name))
				{
					return type;
				}
			}
			return snsapi_userinfo;
		}
	}
	
	public enum InfoLangType
	{
		zh_CN,zh_TW,en;
		
		public static InfoLangType getByName(String name)
		{
			for(InfoLangType type: InfoLangType.values())
			{
				if( type.name().equals(name))
				{
					return type;
				}
			}
			return zh_CN;
		}
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

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getMchKey() {
		return mchKey;
	}

	public void setMchKey(String mchKey) {
		this.mchKey = mchKey;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getJsWxPayApiUrl() {
		return jsWxPayApiUrl;
	}

	public void setJsWxPayApiUrl(String jsWxPayApiUrl) {
		this.jsWxPayApiUrl = jsWxPayApiUrl;
	}

	public String getAppNotifyUrl() {
		return appNotifyUrl;
	}

	public void setAppNotifyUrl(String appNotifyUrl) {
		this.appNotifyUrl = appNotifyUrl;
	}

	public String getNativeNotifyUrl() {
		return nativeNotifyUrl;
	}

	public void setNativeNotifyUrl(String nativeNotifyUrl) {
		this.nativeNotifyUrl = nativeNotifyUrl;
	}

	public String getSuccess_url() {
		return success_url;
	}

	public void setSuccess_url(String success_url) {
		this.success_url = success_url;
	}

	public String getFailed_url() {
		return failed_url;
	}

	public void setFailed_url(String failed_url) {
		this.failed_url = failed_url;
	}

	
}
