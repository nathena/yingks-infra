package com.yingks.infra.pay.wx;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import com.alibaba.fastjson.JSONObject;
import com.yingks.infra.crypto.MD5Coder;
import com.yingks.infra.exception.NestedRuntimeException;
import com.yingks.infra.pay.AbstractPayOperation;
import com.yingks.infra.pay.PayChannelEnum;
import com.yingks.infra.pay.PayStatusEnum;
import com.yingks.infra.pay.PaymentOperationInterface;
import com.yingks.infra.pay.TradeNotify;
import com.yingks.infra.pay.TradeNotify.Type;
import com.yingks.infra.pay.exception.PayException;
import com.yingks.infra.utils.DateTimeUtil;
import com.yingks.infra.utils.HttpUtil;
import com.yingks.infra.utils.StringUtil;

public class WxPayapiOperation extends AbstractPayOperation {

	private static Logger logger  = Logger.getLogger(WxPayapiOperation.class);
	
	private static String wxOrderUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	private static String appId = WxConfig.appId;
	private static String appSecret = WxConfig.appSecret;
	private static String mchId = WxConfig.mchId;
	private static String mchKey = WxConfig.mchKey;
	
	private static String jsWxPayApiUrl = WxConfig.jsWxPayApiUrl;
	private static String notifyUrl = WxConfig.notifyUrl;
	private static String success_url = WxConfig.success_url;
	private static String failed_url = WxConfig.failed_url;
	
	public WxPayapiOperation(PaymentOperationInterface paymentOperation) {
		super(paymentOperation);
	}

	public void toPay(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		logger.debug(" === wxpay api start === ");
		
		if( !paymentOperation.checkPaymentParams(request,response) )
		{
			logger.debug(" === wxpay api === 验证请求参数错误...... ");
			throw new PayException("验证请求参数错误");
		}
		
		
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
		
		String snsapi = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId
						+"&redirect_uri="+URLEncoder.encode(jsWxPayApiUrl, "UTF-8")
						+"&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect";
		
		response.sendRedirect(snsapi);
		
		logger.debug(" === wxpay api end === ");
	}
	
	public void callJsWxPayApiUrl(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException, DocumentException
	{
		logger.debug(" =========  wxpay jsapi start ==========  ");
		//openId
		String wxOpenIdCode = request.getParameter("code");
		if(StringUtil.isEmpty(wxOpenIdCode))
		{
			throw new PayException("用户未授权");
		}
		
		String access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appId+"&secret="+appSecret+"&code="+wxOpenIdCode+"&grant_type=authorization_code";
		String access_token_result = HttpUtil.doGet(access_token_url, null);
		if(StringUtil.isEmpty(access_token_result))
		{
			throw new PayException("获取用户access_token失败！");
		}
		
		JSONObject access_token_json = JSONObject.parseObject(access_token_result);
		String openid = access_token_json.getString("openid");

		if(StringUtil.isEmpty(openid))
		{
			throw new PayException("获取用户openid失败！");
		}
		
		//=================================
		//jsapi接口
		//=================================
		HttpSession session = request.getSession();
		
		String out_trade_no = (String)session.getAttribute("out_trade_no");
		String subject = (String)session.getAttribute("subject");
		String total_fee = (String)session.getAttribute("total_fee");
		
		String timestamp = DateTimeUtil.getTimeStamp()+"";
		String noncestr  = MD5Coder.encode(appId+appSecret).toUpperCase();

		Map<String,String> requestData = new TreeMap<String,String>();
		requestData.put("notify_url", notifyUrl);
		requestData.put("nonce_str", noncestr);
		requestData.put("out_trade_no", out_trade_no);
		requestData.put("spbill_create_ip", request.getRemoteAddr());
		requestData.put("total_fee", total_fee);
		requestData.put("appid", appId);
		if(!StringUtil.isEmpty(subject) && subject.length() > 32) {
			requestData.put("body", subject.substring(0, 29) + "...");
		} else {
			requestData.put("body", subject);
		}
		requestData.put("detail", subject);
		requestData.put("mch_id", mchId);
		requestData.put("openid", openid);
		requestData.put("attach", appId);
		requestData.put("trade_type", "JSAPI");
		//签名
		StringBuilder signData = new StringBuilder();
		String mark = "";
		for(String key : requestData.keySet())
		{
			signData.append(mark).append(key).append("=").append(requestData.get(key));
			mark = "&";
		}
		signData.append(mark).append("key=").append(mchKey);
		logger.info(signData.toString());
		String sign = MD5Coder.encode(signData.toString()).toUpperCase();
		requestData.put("sign", sign);

		//unifiedorder 预支付订单
		StringBuilder requestXmlData = new StringBuilder();
		requestXmlData.append("<xml>");
		for(String key : requestData.keySet())
		{
			requestXmlData.append("<").append(key.toLowerCase()).append("><![CDATA[")
			  .append(requestData.get(key))
			  .append("]]></").append(key.toLowerCase()).append(">");
		}
		requestXmlData.append("</xml>");
		logger.info(" ====== "+requestXmlData.toString());
		byte[] result = HttpUtil.doRestPost(wxOrderUrl, requestXmlData.toString());
		String xmlData = new String(result,"UTF-8");
		logger.info(" ====== "+xmlData);
		
		Document prepayOrderData = DocumentHelper.parseText(xmlData);
		String prepayId = "";
		String returnCode = prepayOrderData.selectSingleNode("//xml/return_code").getText();
		if( !"SUCCESS".equalsIgnoreCase(returnCode) )
		{
			throw new PayException(prepayOrderData.selectSingleNode("//xml/return_msg").getText());
		}
		String resultCode = prepayOrderData.selectSingleNode("//xml/result_code").getText();
		if( !"SUCCESS".equalsIgnoreCase(resultCode) )
		{
			StringBuilder failMsg = new StringBuilder();
			failMsg.append(prepayOrderData.selectSingleNode("//xml/err_code").getText()).append(":");
			failMsg.append(prepayOrderData.selectSingleNode("//xml/err_code_des").getText());
			throw new PayException(failMsg.toString());
		}
		prepayId =  prepayOrderData.selectSingleNode("//xml/prepay_id").getText();
		if( StringUtil.isEmpty(prepayId) )
		{
			throw new PayException("获取预支付订单失败！");
		}
		
		Map<String,String> getBrandWCPayRequestSignData = new TreeMap<String,String>();
		getBrandWCPayRequestSignData.put("appId", appId);
		getBrandWCPayRequestSignData.put("timeStamp", timestamp);
		getBrandWCPayRequestSignData.put("nonceStr", noncestr);
		getBrandWCPayRequestSignData.put("package", "prepay_id="+prepayId);
		getBrandWCPayRequestSignData.put("signType","MD5");//md5

		StringBuilder getBrandWCPayRequestSignDataString = new StringBuilder();
		mark = "";
		for(String key : getBrandWCPayRequestSignData.keySet())
		{
			getBrandWCPayRequestSignDataString.append(mark).append(key).append("=").append(getBrandWCPayRequestSignData.get(key));
			mark = "&";
		}
		getBrandWCPayRequestSignDataString.append(mark).append("key=").append(mchKey);
		logger.info(" ===== getBrandWCPayRequestSignDataString : "+getBrandWCPayRequestSignDataString.toString());
		sign = MD5Coder.encode(getBrandWCPayRequestSignDataString.toString()).toUpperCase();

		StringBuilder jsapi_context = new StringBuilder();
		
		jsapi_context.append("<script type=\"text/javascript\">");
		jsapi_context.append("function onBridgeReady(){");
		jsapi_context.append("WeixinJSBridge.invoke('getBrandWCPayRequest',");
		jsapi_context.append("{");
		jsapi_context.append("\"appId\"    : \""+appId+"\",");
		jsapi_context.append("\"timeStamp\": \""+timestamp+"\",");
		jsapi_context.append("\"nonceStr\" : \""+noncestr+"\",");
		jsapi_context.append("\"package\"  : \"prepay_id="+prepayId+"\",");
		jsapi_context.append("\"signType\" : \"MD5\","); 
		jsapi_context.append("\"paySign\"  : \""+sign+"\""); 
		jsapi_context.append("},");
		jsapi_context.append("function(res)");
		jsapi_context.append("{");
		jsapi_context.append("if(res.err_msg == \"get_brand_wcpay_request:ok\" )"); 
		jsapi_context.append("{");
		jsapi_context.append("location.replace(\""+success_url+"\");"); 
		jsapi_context.append("}"); 
		jsapi_context.append("else if(res.err_msg == \"get_brand_wcpay_request:cancel\" )"); 
		jsapi_context.append("{");
		jsapi_context.append("location.replace(\""+failed_url+"\");");
		jsapi_context.append("}");
		jsapi_context.append("else if(res.err_msg == \"get_brand_wcpay_request:fail\" )"); 
		jsapi_context.append("{");
		jsapi_context.append("location.replace(\""+failed_url+"\");");
		jsapi_context.append("} else {");
		jsapi_context.append("location.replace(\""+failed_url+"\");");
		jsapi_context.append("}");
		jsapi_context.append("}");
		jsapi_context.append(")");
		jsapi_context.append("}");

		jsapi_context.append("if (typeof WeixinJSBridge == \"undefined\")");
		jsapi_context.append("{");
		jsapi_context.append("if( document.addEventListener )");
		jsapi_context.append("{");
		jsapi_context.append("document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);");
		jsapi_context.append("}");
		jsapi_context.append("else if (document.attachEvent)");
		jsapi_context.append("{");
		jsapi_context.append("document.attachEvent('WeixinJSBridgeReady', onBridgeReady);"); 
		jsapi_context.append("document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);");
		jsapi_context.append("}");
		jsapi_context.append("}");
		jsapi_context.append("else");
		jsapi_context.append("{");
		jsapi_context.append("onBridgeReady();");
		jsapi_context.append("}");
		jsapi_context.append("</script>");
		
		toResponse(jsapi_context,"text/html;charset=UTF-8");
		
		logger.debug(" =========  wxpay jsapi end ==========  ");
	}
	
	public void willNotify(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		logger.debug(" === wxpay api notify start === ");
		
		try
		{
			//notifyData
			InputStream inStream = request.getInputStream();
			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			outSteam.close();
			inStream.close();
			String result = new String(outSteam.toByteArray(), "utf-8");
			
			logger.info(" ==== wxpay notify result : "+result);
			
			Document notifyDataDoc = DocumentHelper.parseText(result);
			
			String return_code = notifyDataDoc.selectSingleNode("//xml/return_code").getText();
			if( !"SUCCESS".equalsIgnoreCase(return_code) )
			{
				toResponse("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[微信支付通知return_code fail]]></return_msg></xml>"); 
				logger.info(" === wxpay notify result > return_msg : "+notifyDataDoc.selectSingleNode("//xml/return_msg").getText());
				return;
			}
			
			String nonce_str = notifyDataDoc.selectSingleNode("//xml/nonce_str").getText();
			if( !MD5Coder.encode(appId+appSecret).toUpperCase().equals(nonce_str) )
			{
				toResponse("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[微信支付通知签名失败]]></return_msg></xml>");
				logger.info(" === wxpay notify result > nonce_str : "+nonce_str);
				return;
			}
			
			String transaction_id = notifyDataDoc.selectSingleNode("//xml/transaction_id").getText();
			String out_trade_no = notifyDataDoc.selectSingleNode("//xml/out_trade_no").getText();
			
			//orderquery
			Map<String,String> orderqueryData = new TreeMap<String,String>();
			orderqueryData.put("appid", appId);
			orderqueryData.put("mch_id", mchId);
			orderqueryData.put("transaction_id", transaction_id);
			orderqueryData.put("out_trade_no", out_trade_no);
			orderqueryData.put("nonce_str", nonce_str);
			
			StringBuilder signData = new StringBuilder();
			String mark = "";
			for(String key : orderqueryData.keySet())
			{
				signData.append(mark).append(key).append("=").append(orderqueryData.get(key));
				mark = "&";
			}
			signData.append(mark).append("key=").append(mchKey);
			logger.info(signData.toString());
			String sign = MD5Coder.encode(signData.toString()).toUpperCase();
			
			orderqueryData.put("sign", sign);
			
			StringBuilder requestXmlData = new StringBuilder();
			requestXmlData.append("<xml>");
			for(String key : orderqueryData.keySet())
			{
				requestXmlData.append("<").append(key.toLowerCase()).append("><![CDATA[")
				  .append(orderqueryData.get(key))
				  .append("]]></").append(key.toLowerCase()).append(">");
			}
			requestXmlData.append("</xml>");
			logger.info(" ====== requestXmlData : "+requestXmlData.toString());
			
			String orderqueryUrl = "https://api.mch.weixin.qq.com/pay/orderquery";
			byte[] orderqueryResult = HttpUtil.doRestPost(orderqueryUrl, requestXmlData.toString());
			String orderqueryXmlData = new String(orderqueryResult,"UTF-8");
			logger.info(" ====== orderqueryXmlData : "+orderqueryXmlData);
			
			Document orderqueryDataDoc = DocumentHelper.parseText(orderqueryXmlData);
			
			String returnCode = orderqueryDataDoc.selectSingleNode("//xml/return_code").getText();
			if( !"SUCCESS".equalsIgnoreCase(returnCode) )
			{
				toResponse("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[微信支付通知微信订单不对]]></return_msg></xml>"); 
				logger.info(" === wxpay notify orderquery returnCode > return_msg : "+orderqueryDataDoc.selectSingleNode("//xml/return_msg").getText());
				return;
			}
			String resultCode = orderqueryDataDoc.selectSingleNode("//xml/result_code").getText();
			if( !"SUCCESS".equalsIgnoreCase(resultCode) )
			{
				toResponse("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[微信支付通知微信订单不对]]></return_msg></xml>");
				logger.info(" === wxpay notify orderquery resultCode > result_msg : "+orderqueryDataDoc.selectSingleNode("//xml/err_code").getText()
						+" "+orderqueryDataDoc.selectSingleNode("//xml/err_code_des").getText());
				return;
			}
			
			TradeNotify msg = new TradeNotify();
			msg.setChannel(PayChannelEnum.weixin);
			msg.setStatus(PayStatusEnum.SUCCEES);
			msg.setOutTradeNo(out_trade_no);
			msg.setTradeNo("wxpay"+transaction_id);
			msg.setTradeStatus(PayStatusEnum.SUCCEES.name());
			msg.setNotifyMoney("");
			msg.setTradeType(Type.web);
			
			paymentOperation.notify(msg);
			
			toResponse("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
		}
		catch(NestedRuntimeException e) {
			//因为service需要抛出异常才能回滚 这里接收service的业务异常返回成功,记录失败原因
			logger.error("alipay回调出现业务异常:" + e.getMsg(), e);
			
			toResponse("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
		}
		catch(Exception e) {
			toResponse("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[微信通知请求失败]]></return_msg></xml>"); 
			logger.error(e);
		}
		
		logger.debug(" === wxpay api notify end === ");
	}
}
