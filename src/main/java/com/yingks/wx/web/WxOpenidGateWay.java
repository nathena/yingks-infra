package com.yingks.wx.web;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import com.alibaba.fastjson.JSONObject;
import com.yingks.infra.utils.DateTimeUtil;
import com.yingks.infra.utils.HttpUtil;
import com.yingks.infra.utils.LogHelper;
import com.yingks.infra.utils.NumberUtil;
import com.yingks.infra.utils.StringUtil;
import com.yingks.wx.WxConfig;
import com.yingks.wx.WxConfig.InfoLangType;
import com.yingks.wx.exception.WxException;
import com.yingks.wx.exception.WxExceptionMsg;
import com.yingks.wx.user.WxUserLbs;

/**
 * 网页授权
 * @Title: WxWebOpenidService.java
 * @Package com.yingks.infra.wx
 * @author nathena  
 * @version V1.0 
 * @UpdateHis:
 *      TODO
 */
public class WxOpenidGateWay {

	private static final String site_access_code_api = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect";
	private static final String openid_access_token_api = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
	
	private static final String refresh_token_api = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s";
	private static final String userinfo_api = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=%s";
	private static final String check_openid = "https://api.weixin.qq.com/sns/auth?access_token=%s&openid=%s";
	
	private String appid;
	private String appSercet;
	
	private WxConfig config;
	
	public WxOpenidGateWay(WxConfig config)
	{
		this.appid = config.getAppId();
		this.appSercet = config.getAppSecret();
		
		this.config = config;
	}
	
	public void reqOpenidCode(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		String url = URLEncoder.encode( config.getRedirect_uri(), "UTF-8");
		String api = String.format(site_access_code_api, appid,url, config.getScope().name() , config.getAccess_code_state());
		
		response.sendRedirect(api);
	}
	
	public WxOpenid getOpenidByCode(String code)
	{
		String api = String.format(openid_access_token_api,appid,appSercet,code);
		String result = HttpUtil.doGet(api, null);
		if( StringUtil.isEmpty(result) )
		{
			throw new WxException(WxExceptionMsg.wx_openid_code_empty,code);
		}
		JSONObject data = JSONObject.parseObject(result);
		if( !StringUtil.isEmpty(data.getString("errcode")) )
		{
			throw new WxException(WxExceptionMsg.wx_openid_error,code,result);
		}
		
		WxOpenid wxOpenid = new WxOpenid();
		
		wxOpenid.setAppId(appid);
		wxOpenid.setAppSercet(appSercet);
		wxOpenid.setAccessToken(data.getString("access_token"));
		wxOpenid.setCreateIn(DateTimeUtil.getTimeStamp());
		wxOpenid.setExpiresIn(NumberUtil.parseInt(data.getString("expires_in")));
		wxOpenid.setScope(data.getString("scope"));
		wxOpenid.setUnionid(data.getString("unionid"));
		wxOpenid.setRefreshToken(data.getString("refresh_token"));
		wxOpenid.setOpenid(data.getString("openid"));
		
		userInfo(wxOpenid,config.getLang());
		
		return wxOpenid;
	}
	
	public void userInfo(WxOpenid wxOpenid, InfoLangType lang)
	{
		String api = String.format(userinfo_api, wxOpenid.getAccessToken(),wxOpenid.getOpenid(),lang.name());
		
		String result = HttpUtil.doGet(api, null);
		if( StringUtil.isEmpty(result) )
		{
			throw new WxException(WxExceptionMsg.wx_userinfo_error,api);
		}
		JSONObject data = JSONObject.parseObject(result);
		if( !StringUtil.isEmpty(data.getString("errcode")) )
		{
			throw new WxException(WxExceptionMsg.wx_userinfo_error,api+"=>"+result);
		}
		
		wxOpenid.setLang(lang.name());
		wxOpenid.setCity(data.getString("city"));
		wxOpenid.setProvince(data.getString("province"));
		wxOpenid.setCountry(data.getString("country"));
		wxOpenid.setHeadimgurl(data.getString("headimgurl"));
		wxOpenid.setNickname(data.getString("nickname"));
		if( "2".equals(StringUtils.trim(data.getString("sex"))))
		{
			wxOpenid.setSex("female");
		}
		else
		{
			wxOpenid.setSex("male");
		}
		wxOpenid.setPrivilege(data.getString("privilege"));
		wxOpenid.setUnionid(data.getString("unionid"));
	}
	
	public void refresh(WxOpenid wxOpenid)
	{
		String api = String.format(refresh_token_api, wxOpenid.getAppId(),wxOpenid.getRefreshToken());
		String result = HttpUtil.doGet(api, null);
		if( StringUtil.isEmpty(result) )
		{
			throw new WxException(WxExceptionMsg.wx_openid_refresh,api);
		}
		JSONObject data = JSONObject.parseObject(result);
		if( !StringUtil.isEmpty(data.getString("errcode")) )
		{
			throw new WxException(WxExceptionMsg.wx_openid_refresh,api+"=>"+result);
		}
		
		wxOpenid.setAccessToken(data.getString("access_token"));
		wxOpenid.setCreateIn(DateTimeUtil.getTimeStamp());
		wxOpenid.setExpiresIn(NumberUtil.parseInt(data.getString("expires_in")));
		wxOpenid.setScope(data.getString("scope"));
		wxOpenid.setRefreshToken(data.getString("refresh_token"));
		wxOpenid.setOpenid(data.getString("openid"));
	}
	
	public boolean checkUserInfo(String accessToken,String openid)
	{
		String api = String.format(check_openid, accessToken,openid);
		
		String result = HttpUtil.doGet(api, null);
		if( StringUtil.isEmpty(result) )
		{
			throw new WxException(WxExceptionMsg.wx_check_user_error,api);
		}
		JSONObject data = JSONObject.parseObject(result);
		if( !StringUtil.isEmpty(data.getString("errcode")) )
		{
			throw new WxException(WxExceptionMsg.wx_check_user_error,api+"=>"+result);
		}
		
		if( "0".equals(data.getString("errcode"))) 
		{
			return true;
		}
		return false;
	}
	
	public WxUserLbs receiveWxLbs(String recevieData)
	{
		WxUserLbs lbs = new WxUserLbs();
		try
		{
			Document doc = DocumentHelper.parseText(recevieData);
			
			String ToUserName = doc.selectSingleNode("//xml/ToUserName").getText();
			String FromUserName = doc.selectSingleNode("//xml/FromUserName").getText();
			String CreateTime = doc.selectSingleNode("//xml/CreateTime").getText();
			String MsgType = doc.selectSingleNode("//xml/MsgType").getText();
			String Event = doc.selectSingleNode("//xml/Event").getText();
			String Latitude = doc.selectSingleNode("//xml/Latitude").getText();
			String Longitude = doc.selectSingleNode("//xml/Longitude").getText();
			String Precision = doc.selectSingleNode("//xml/Precision").getText();
			
			lbs.setCreateTime(CreateTime);
			lbs.setEvent(Event);
			lbs.setFromUserName(FromUserName);
			lbs.setLatitude(Latitude);
			lbs.setLongitude(Longitude);
			lbs.setMsgType(MsgType);
			lbs.setPrecision(Precision);
			lbs.setToUserName(ToUserName);
		}
		catch(Exception e)
		{
			LogHelper.error(e.getMessage(), e);
		}
		
		return lbs;
	}

}
