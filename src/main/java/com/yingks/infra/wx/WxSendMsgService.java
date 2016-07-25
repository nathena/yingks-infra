package com.yingks.infra.wx;

import com.alibaba.fastjson.JSONObject;
import com.yingks.infra.utils.HttpUtil;
import com.yingks.infra.utils.LogHelper;
import com.yingks.infra.utils.StringUtil;
import com.yingks.infra.wx.beans.WxSendMsgResult;
import com.yingks.infra.wx.enums.WxMsgType;
import com.yingks.infra.wx.exception.WxException;
import com.yingks.infra.wx.exception.WxExceptionMsg;

public class WxSendMsgService {

	private static String send_api = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=%s";
	
	private WxAccessTokenService wxAccessTokenService;
	
	public WxSendMsgService(String appid,String appSercet)
	{
		wxAccessTokenService = new WxAccessTokenService(appid,appSercet);
	}
	
	public WxSendMsgResult sendText(String[] openids,String content)
	{
		if( null == openids || openids.length == 0 )
		{
			throw new WxException(WxExceptionMsg.wx_openids_not_null);
		}
		
		JSONObject json = new JSONObject();
		json.put("touser", openids);
		json.put("msgtype", WxMsgType.text.name());
		
		JSONObject con = new JSONObject();
		con.put("content", content);
		
		json.put("text", con);
		
		String api = String.format(send_api, wxAccessTokenService.getToken().getAccessToken());
		
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
