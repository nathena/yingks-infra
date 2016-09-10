package com.yingks.wx.pay;

import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.yingks.infra.crypto.MD5Coder;
import com.yingks.infra.crypto.SHA1Coder;
import com.yingks.infra.exception.NestedRuntimeException;
import com.yingks.infra.utils.CollectionUtil;
import com.yingks.infra.utils.DateTimeUtil;
import com.yingks.infra.utils.HttpUtil;
import com.yingks.infra.utils.LogHelper;
import com.yingks.infra.utils.RandomHelper;
import com.yingks.infra.utils.StringUtil;
import com.yingks.pay.AbstractPayOperation;
import com.yingks.pay.PayChannelEnum;
import com.yingks.pay.PayNotifyAbleInterface;
import com.yingks.pay.PayStatusEnum;
import com.yingks.pay.TradeNotify;
import com.yingks.pay.TradeNotify.Type;
import com.yingks.wx.WxConfig;

public class WxPayappOperation extends AbstractPayOperation {

	private static Logger logger  = Logger.getLogger(WxPayappOperation.class);
	
	private WxConfig wxConfig;
	
	public WxPayappOperation(WxConfig wxConfig, PayNotifyAbleInterface paymentOperation) {
		super(paymentOperation);
		
		this.wxConfig = wxConfig;
	}


	public void toPay(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		logger.debug(" === wxpay app start === ");
		
		JSONObject retJson = new JSONObject();
		
		//商户订单号
		String out_trade_no = new String(request.getParameter("WIDout_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//订单名称
		String subject = new String(request.getParameter("WIDsubject"));
		//付款金额
		String total_fee = new String(request.getParameter("WIDtotal_fee").getBytes("ISO-8859-1"),"UTF-8");
		
		HttpSession session = request.getSession();
		
		session.setAttribute("subject", subject);
		session.setAttribute("total_fee", total_fee);
		session.setAttribute("out_trade_no", out_trade_no);
		
		if(StringUtil.isEmpty(subject) || StringUtil.isEmpty(out_trade_no) || StringUtil.isEmpty(total_fee)) 
		{
			logger.error("========== 必填项为空=====================");
			logger.error("========== subject:" + subject + "=====================");
			logger.error("========== out_trade_no:" + out_trade_no + "=====================");
			logger.error("========== total_fee:" + total_fee + "=====================");
			
			retJson.put("retcode", "1");
			retJson.put("retmsg", "请求参数为空");
			toResponse(retJson.toJSONString(),"application/json");
			return;
		}
		
		String wxAccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + wxConfig.getAppId() + "&secret=" + wxConfig.getAppSecret();
		String wxPreOrderIdUrl = "https://api.weixin.qq.com/pay/genprepay?access_token=";
		byte[] result = HttpUtil.doRestPost(wxAccessTokenUrl, "");
		JSONObject token = JSONObject.parseObject(new String(result,"UTF-8"));
		if(token == null || StringUtil.isEmpty(token.getString("access_token"))) {
			logger.error(" ============== 获取access_token出错 ================ ");
			
			retJson.put("retcode", "1");
			retJson.put("retmsg", "获取access_token出错");
			toResponse(retJson.toJSONString(),"application/json");
			
			return;
		}
		
		String accessToken = token.getString("access_token");
		
		// ======== package参数 ================
		SortedMap<String, String> sortedMap = new TreeMap<String,String>();
		sortedMap.put("bank_type", "WX");
		sortedMap.put("body", subject);
		sortedMap.put("fee_type", "1");
		sortedMap.put("input_charset", "UTF-8");
		sortedMap.put("notify_url", wxConfig.getNotifyUrl());
		sortedMap.put("out_trade_no", out_trade_no);
		sortedMap.put("partner", wxConfig.getMchId());
		sortedMap.put("spbill_create_ip", request.getRemoteAddr());
		sortedMap.put("total_fee", total_fee);

		StringBuilder dataBuilder = new StringBuilder();
		String mark = "";
		for(String key : sortedMap.keySet()) {
			dataBuilder.append(mark).append(key).append("=").append(sortedMap.get(key));
			mark = "&";
		}
		
		if(dataBuilder == null || dataBuilder.length() <= 0) {
			logger.error(" ============== 构造package数据出错 ================ ");
			retJson.put("retcode", "1");
			retJson.put("retmsg", "构造package数据出错");
			toResponse(retJson.toJSONString(),"application/json");
			return;
		}
		String sign = MD5Coder.encode(dataBuilder.toString() + "&key=" + wxConfig.getMchKey()).toUpperCase(Locale.CHINA);
		String packageData =  dataBuilder.append("&sign=").append(sign).toString();

		// ===============                  =================
		String noncestr = RandomHelper.nextString(31);
		String timeStamp = DateTimeUtil.getTimeStamp() + "";
		// =============== app_signature参数 =================
		sortedMap = new TreeMap<String, String>();
		sortedMap.put("appid", wxConfig.getAppId());
		sortedMap.put("appkey", wxConfig.getAppSecret());
		sortedMap.put("noncestr", noncestr);
		sortedMap.put("package", packageData);
		sortedMap.put("timestamp", timeStamp);
		sortedMap.put("traceid", wxConfig.getAppId());

		dataBuilder = new StringBuilder();
		JSONObject postData = new JSONObject();
		mark = "";
		for(String key : sortedMap.keySet()) {
			dataBuilder.append(mark).append(key).append("=").append(sortedMap.get(key));
			if(!"appKey".equals(key)) {
				postData.put(key, sortedMap.get(key));
			}
			mark = "&";
		}

		postData.put("sign_method", "sha1");

		if(dataBuilder == null || dataBuilder.length() <= 0) {
			logger.error(" ============== 构造app_signature数据出错 ================ ");
			
			return;
		}
		String appSignature = SHA1Coder.encode(dataBuilder.toString());
		postData.put("app_signature", appSignature);

		result = HttpUtil.doRestPost(wxPreOrderIdUrl + accessToken, postData.toJSONString());
		JSONObject preOrder = JSONObject.parseObject(new String(result,"UTF-8"));

		if(preOrder != null && !StringUtil.isEmpty(preOrder.getString("prepayid"))) {
			retJson.put("retcode", preOrder.getString("errcode"));
			retJson.put("timestamp", timeStamp);
			retJson.put("prepayid", preOrder.getString("prepayid"));
			retJson.put("noncestr", noncestr);
			retJson.put("app_signature", appSignature);
		} else {
			LogHelper.error(" ============== " + preOrder.getString("errmsg") + " ================ ");
			
			retJson.put("retcode", preOrder.getString("errcode"));
			retJson.put("retmsg", preOrder.getString("errmsg"));
		}
		toResponse(retJson.toJSONString(),"application/json");
		
		logger.debug(" === wxpay app  end === ");
	}
	
	
	public void willNotify(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		logger.debug(" === wxpay app notify start === ");
		
		try
		{
			//============== 获取url回调参数 ==============
			String postSign = request.getParameter("sign");
			String inputCharset = request.getParameter("input_charset");
			SortedMap<String, String> sortedMap = new TreeMap<String, String>();
			if(!CollectionUtil.isEmpty(request.getParameterMap())) {
				for(String key : request.getParameterMap().keySet()) {
					if(!key.equals("sign")) {
						if("GBK".equals(inputCharset.toUpperCase(Locale.CHINA))) {
							sortedMap.put(key, new String(request.getParameter(key).getBytes("GBK"), "UTF-8"));
						} else {
							sortedMap.put(key, request.getParameter(key));
						}
					}
				}
			}

			StringBuilder dataBuilder = new StringBuilder();
			String mark = "";
			for(String key : sortedMap.keySet()) {
				dataBuilder.append(mark).append(key).append("=").append(sortedMap.get(key));
				mark = "&";
			}
			if(dataBuilder == null || dataBuilder.length() <= 0) {
				logger.error(" ============== 构造sign数据出错 ================ ");
				toResponse("fail");
				return;
			}
			String sign = MD5Coder.encode(dataBuilder.toString() + "&key=" + wxConfig.getMchKey()).toUpperCase(Locale.CHINA);
			if(!sign.equals(postSign)) {
				logger.error(" ============== 签名验证不正确  ================ ");
				toResponse("fail");
				return;
			}

			String out_trade_no = sortedMap.get("out_trade_no");
			String transaction_id = sortedMap.get("transaction_id");
			String total_fee = sortedMap.get("total_fee");
			
			TradeNotify msg = new TradeNotify();
			msg.setChannel(PayChannelEnum.weixin);
			msg.setStatus(PayStatusEnum.SUCCEES);
			msg.setPaymentNo(out_trade_no);
			msg.setTradeNo("wxpay"+transaction_id);
			msg.setTradeStatus(PayStatusEnum.SUCCEES.name());
			msg.setNotifyMoney(total_fee);
			msg.setTradeType(Type.web);
			msg.setMsg(JSONObject.toJSONString(sortedMap));
			
			paymentOperation.notify(msg);
			
			toResponse("success");
		}
		catch(NestedRuntimeException e) 
		{
			//因为service需要抛出异常才能回滚 这里接收service的业务异常返回成功,记录失败原因
			logger.error("alipay回调出现业务异常:" + e.getMessage(), e);
			
			toResponse("success");
		} 
		catch(Exception e) 
		{
			logger.error(" ============== msg:" + e.getMessage() + "  ================ ", e);
			toResponse("fail");
		}
		
		logger.debug(" === wxpay app notify end === ");
	}
}
