package com.yingks.wx.user;

import com.alibaba.fastjson.JSONObject;
import com.yingks.infra.utils.HttpUtil;
import com.yingks.infra.utils.StringUtil;
import com.yingks.wx.WxAccessTokenGateWay;
import com.yingks.wx.WxConfig;
import com.yingks.wx.exception.WxException;
import com.yingks.wx.exception.WxExceptionMsg;

public class WxUserGateWay {

	private static String wxuser_api = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=%s";
	private static String wxuserList_api = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=%s&next_openid=%s";
	
	private WxConfig config;
	private WxAccessTokenGateWay wxAccessTokenService;
	
	public WxUserGateWay(WxConfig config)
	{
		this.config = config;
		this.wxAccessTokenService = new WxAccessTokenGateWay(config);
	}
	
	public WxUser getWxUser(String openId)
	{
		String api = String.format(wxuser_api, wxAccessTokenService.geWxAccessToken().getToken(),openId, config.getLang().name() );
		String result = HttpUtil.doGet(api, null);
		
		if( StringUtil.isEmpty(result) )
		{
			throw new WxException(WxExceptionMsg.wx_resp_nothing);
		}
		
		JSONObject wxuser_json = JSONObject.parseObject(result);
		if( !StringUtil.isEmpty(wxuser_json.getString("errcode")) )
		{
			throw new WxException(WxExceptionMsg.wx_error_40013);
		}
		
		return JSONObject.parseObject(result, WxUser.class);
	}
	
	public WxUserListResult getWxUsers(String next_openid)
	{
		String api = String.format(wxuserList_api, wxAccessTokenService.geWxAccessToken().getToken(),next_openid);
		
		String result = HttpUtil.doGet(api, null);
		
		if( StringUtil.isEmpty(result) )
		{
			throw new WxException(WxExceptionMsg.wx_resp_nothing);
		}
		
		JSONObject wxuser_json = JSONObject.parseObject(result);
		if( !StringUtil.isEmpty(wxuser_json.getString("errcode")) )
		{
			throw new WxException(WxExceptionMsg.wx_error_40013);
		}
		
		return JSONObject.parseObject(result, WxUserListResult.class);
	}
}
