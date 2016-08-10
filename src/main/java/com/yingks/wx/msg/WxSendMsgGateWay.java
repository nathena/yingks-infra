package com.yingks.wx.msg;

import com.alibaba.fastjson.JSONObject;
import com.yingks.infra.utils.HttpUtil;
import com.yingks.infra.utils.LogHelper;
import com.yingks.infra.utils.StringUtil;
import com.yingks.wx.WxAccessTokenGateWay;
import com.yingks.wx.WxConfig;
import com.yingks.wx.exception.WxException;
import com.yingks.wx.exception.WxExceptionMsg;

public class WxSendMsgGateWay
{
	private static String custom_send_api = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s";
	private static String send_api = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=%s";
	private static String send_template_api = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";
	
	private WxAccessTokenGateWay wxAccessTokenService;
	
	public WxSendMsgGateWay(WxConfig config)
	{
		this.wxAccessTokenService = new WxAccessTokenGateWay(config);
	}
	
	public void send(WxTemplate template)
	{
		String api = String.format(send_template_api, wxAccessTokenService.geWxAccessToken().getToken());
		String data = JSONObject.toJSONString(template);
		byte[] result = HttpUtil.doRestPost(api, data);
		
		String res = new String(result);
		
		LogHelper.debug("发送微信文本消息完成 => "+res+" = "+data);
	}
	
	public WxSendMsgResult sendText(String openid,String content)
	{
		JSONObject json = new JSONObject();
		json.put("touser", openid);
		json.put("msgtype", WxSendMsgType.text.name());
		
		JSONObject con = new JSONObject();
		con.put("content", content);
		
		json.put("text", con);
		
		String api = String.format(custom_send_api, wxAccessTokenService.geWxAccessToken().getToken());
		
		byte[] result = HttpUtil.doRestPost(api, json.toJSONString());
		String res = new String(result);
		LogHelper.debug("发送微信文本消息完成 => "+res);
		
		return parseResponse(res);
	}
	
	public WxSendMsgResult groupSendText(String[] openids,String content)
	{
		if( null == openids || openids.length == 0 )
		{
			throw new WxException(WxExceptionMsg.wx_openids_not_null);
		}
		
		JSONObject json = new JSONObject();
		json.put("touser", openids);
		json.put("msgtype", WxSendMsgType.text.name());
		
		JSONObject con = new JSONObject();
		con.put("content", content);
		
		json.put("text", con);
		
		String api = String.format(send_api, wxAccessTokenService.geWxAccessToken().getToken());
		
		byte[] result = HttpUtil.doRestPost(api, json.toJSONString());
		String res = new String(result);
		LogHelper.debug("发送微信文本消息完成 => "+res);
		
		return parseResponse(res);
	}
	
	private WxSendMsgResult parseResponse(String res)
	{
		if( StringUtil.isEmpty(res) )
			return null;
		
		JSONObject json = JSONObject.parseObject(res);
		
		WxSendMsgResult result = new WxSendMsgResult();
		result.setErrcode(json.getString("errcode"));
		result.setErrmsg(json.getString("errmsg"));
		result.setMsg_data_id(json.getString("msg_data_id"));
		result.setMsg_id(json.getString("msg_id"));
		result.setType(json.getString("type"));
		
		return result;
	}
}
