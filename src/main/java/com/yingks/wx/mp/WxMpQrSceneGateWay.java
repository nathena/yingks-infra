package com.yingks.wx.mp;

import java.nio.charset.Charset;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.yingks.infra.utils.HttpUtil;
import com.yingks.infra.utils.StringUtil;
import com.yingks.wx.WxAccessTokenGateWay;
import com.yingks.wx.WxConfig;
import com.yingks.wx.exception.WxException;
import com.yingks.wx.exception.WxExceptionMsg;

public class WxMpQrSceneGateWay {

	private static Logger logger = Logger.getLogger(WxMpQrSceneGateWay.class);
	
	private static String qr_scene = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s";
	private static String qr_ticket = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=%s";
	
	private WxAccessTokenGateWay wxAccessTokenService;
	
	public WxMpQrSceneGateWay(WxConfig config)
	{
		wxAccessTokenService = new WxAccessTokenGateWay(config);
	}
	
	public WxMpQrResult create(WxMpQr qr)
	{
		String api = String.format(qr_scene, wxAccessTokenService.geWxAccessToken().getToken());
		String data = qr.toString();
		
		logger.debug(" ============= WxMpQr : "+data);
		
		byte[] result = HttpUtil.doRestPost(api, data);
		
		String resultData = new String(result,Charset.forName("UTF-8"));
		
		JSONObject wxuser_json = JSONObject.parseObject(resultData);
		if( !StringUtil.isEmpty(wxuser_json.getString("errcode")) )
		{
			throw new WxException(WxExceptionMsg.wx_error_40013);
		}
		
		return JSONObject.parseObject(resultData, WxMpQrResult.class);
	}
	
	public WxMpQrResult createTemporary(WxMpQr qr)
	{
		String api = String.format(qr_scene, wxAccessTokenService.geWxAccessToken().getToken());
		String data = qr.toTemporaryString();
		
		logger.debug(" ============= WxMpQr : "+data);
		
		byte[] result = HttpUtil.doRestPost(api, data);
		
		String resultData = new String(result,Charset.forName("UTF-8"));
		
		JSONObject wxuser_json = JSONObject.parseObject(resultData);
		if( !StringUtil.isEmpty(wxuser_json.getString("errcode")) )
		{
			throw new WxException(WxExceptionMsg.wx_error_40013);
		}
		
		return JSONObject.parseObject(resultData, WxMpQrResult.class);
	}
	
	public String getScence(String ticketId)
	{
		return String.format(qr_ticket, ticketId);
	}
}
