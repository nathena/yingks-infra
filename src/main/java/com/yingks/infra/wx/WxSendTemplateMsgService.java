package com.yingks.infra.wx;

import com.alibaba.fastjson.JSONObject;
import com.yingks.infra.utils.HttpUtil;
import com.yingks.infra.utils.LogHelper;
import com.yingks.infra.wx.beans.WxTemplate;

public class WxSendTemplateMsgService {

	private static String send_api = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";
	
	private WxAccessTokenService wxAccessTokenService;
	
	public WxSendTemplateMsgService(String appid,String appSercet)
	{
		wxAccessTokenService = new WxAccessTokenService(appid,appSercet);
	}
	
	public void send(WxTemplate template)
	{
		String api = String.format(send_api, wxAccessTokenService.getToken().getAccessToken());
		String data = JSONObject.toJSONString(template);
		byte[] result = HttpUtil.doRestPost(api, data);
		
		String res = new String(result);
		
		LogHelper.debug("发送微信文本消息完成 => "+res+" = "+data);
	}
}
