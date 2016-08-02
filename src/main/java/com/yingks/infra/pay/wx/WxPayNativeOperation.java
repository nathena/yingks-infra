package com.yingks.infra.pay.wx;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.yingks.infra.crypto.MD5Coder;
import com.yingks.infra.pay.AbstractPayOperation;
import com.yingks.infra.pay.PaymentOperationInterface;
import com.yingks.infra.pay.exception.PayException;
import com.yingks.infra.utils.HttpUtil;
import com.yingks.infra.utils.MatrixToImageWriter;
import com.yingks.infra.utils.StringUtil;

public class WxPayNativeOperation extends AbstractPayOperation {

	private static Logger logger  = Logger.getLogger(WxPayNativeOperation.class);
	
	private static String wxOrderUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	private static String appId = WxConfig.appId;
	private static String appSecret = WxConfig.appSecret;
	private static String mchId = WxConfig.mchId;
	private static String mchKey = WxConfig.mchKey;
	
	private static String notifyUrl = WxConfig.nativeNotifyUrl;
	
	public WxPayNativeOperation(PaymentOperationInterface paymentOperation) {
		super(paymentOperation);
	}

	public void toPay_pattern_one(HttpServletRequest request,HttpServletResponse response) throws WriterException, IOException
	{
		logger.debug(" === wxpay native pattern one start === ");
		
		String out_trade_no = request.getParameter("out_trade_no");
		
		if( StringUtil.isEmpty(out_trade_no) )
		{
			throw new PayException(" out_trade_no 为空");
		}

		//签名
		//String noncestr = MD5Coder.encode(appId+appSecret).toUpperCase(Locale.CHINA);

		SortedMap<String, Object> sortedMap = new TreeMap<String, Object>();
		sortedMap.put("appid", appId);
		sortedMap.put("mch_id", mchId);
		sortedMap.put("product_id",out_trade_no);
		sortedMap.put("time_stamp",System.currentTimeMillis());

		StringBuilder signData = new StringBuilder();
		String mark = "";
		for(String key : sortedMap.keySet())
		{
			signData.append(mark).append(key).append("=").append(sortedMap.get(key));
			mark = "&";
		}
		signData.append(mark).append("key=").append(mchKey);
		String sign = MD5Coder.encode(signData.toString()).toUpperCase(Locale.CHINA);
		sortedMap.put("sign", sign);

		StringBuilder payUrl = new StringBuilder("weixin://wxpay/bizpayurl?");
		mark = "";
		for(String key : sortedMap.keySet())
		{
			payUrl.append(mark).append(key).append("=").append(sortedMap.get(key));
			mark = "&";
		}

		//TODO根据payUrl生成二维码
		if(!StringUtil.isEmpty(payUrl.toString())) 
		{
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>(); 
			hints.put(EncodeHintType.CHARACTER_SET, "GBK"); 
			
			BitMatrix matrix = new MultiFormatWriter().encode(payUrl.toString(), BarcodeFormat.QR_CODE, 300, 300, hints); 

			response.setContentType("image/jpeg");
		    MatrixToImageWriter.writeToStream(matrix, "png", response.getOutputStream()); 

		}
		
		logger.debug(" === wxpay native pattern one end === ");
	}
	
	public String toPay_pattern_two(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException, DocumentException
	{
		logger.debug(" === wxpay native pattern two start === ");
		
		String trade_type = "NATIVE";
		String attach  = appId;
		//商户订单号
		String out_trade_no = new String(request.getParameter("WIDout_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//订单名称
		String subject = new String(request.getParameter("WIDsubject"));
		//付款金额
		String total_fee = new String(request.getParameter("WIDtotal_fee").getBytes("ISO-8859-1"),"UTF-8");
		
		//签名
		String noncestr = MD5Coder.encode(appId+appSecret).toUpperCase(Locale.CHINA);

		SortedMap<String, String> sortedMap = new TreeMap<String, String>();
		sortedMap.put("notify_url", notifyUrl);
		sortedMap.put("nonce_str", noncestr);
		sortedMap.put("out_trade_no", out_trade_no);
		sortedMap.put("spbill_create_ip", request.getRemoteAddr());
		sortedMap.put("total_fee", total_fee);
		sortedMap.put("appid", appId);
		sortedMap.put("body", subject);
		sortedMap.put("mch_id", mchId);
		sortedMap.put("attach", attach);
		sortedMap.put("product_id",out_trade_no);
		sortedMap.put("trade_type", trade_type);

		StringBuilder signData = new StringBuilder();
		String mark = "";
		for(String key : sortedMap.keySet())
		{
			signData.append(mark).append(key).append("=").append(sortedMap.get(key));
			mark = "&";
		}
		signData.append(mark).append("key=").append(mchKey);
		String sign = MD5Coder.encode(signData.toString()).toUpperCase(Locale.CHINA);
		sortedMap.put("sign", sign);

		//getPrepayId
		String prepayId = "";
		String code_url = "";
		StringBuilder requestXmlData = new StringBuilder();
		requestXmlData.append("<xml>");
		for(String key : sortedMap.keySet())
		{
			requestXmlData.append("<").append(key.toLowerCase(Locale.CHINA)).append("><![CDATA[")
				  .append(sortedMap.get(key))
				  .append("]]></").append(key.toLowerCase(Locale.CHINA)).append(">");
		}
		requestXmlData.append("</xml>");
		byte[] result = HttpUtil.doRestPost(wxOrderUrl, requestXmlData.toString());
		String xmlData = new String(result,"UTF-8");

		Document prepayOrderData = DocumentHelper.parseText(xmlData);

		String returnCode = prepayOrderData.selectSingleNode("//xml/return_code").getText();
		if( !"SUCCESS".equalsIgnoreCase(returnCode) )
		{
			return ("app支付获取预支付订单出错"+prepayOrderData.selectSingleNode("//xml/return_msg").getText());
		}
		String resultCode = prepayOrderData.selectSingleNode("//xml/result_code").getText();
		if( !"SUCCESS".equalsIgnoreCase(resultCode) )
		{
			StringBuilder failMsg = new StringBuilder();
			failMsg.append(prepayOrderData.selectSingleNode("//xml/err_code").getText()).append(":");
			failMsg.append(prepayOrderData.selectSingleNode("//xml/err_code_des").getText());
			return ("app支付获取预支付订单出错"+failMsg.toString());
		}
		prepayId =  prepayOrderData.selectSingleNode("//xml/prepay_id").getText();
		code_url =  prepayOrderData.selectSingleNode("//xml/code_url").getText();

		return (" prepayId => "+prepayId+" ;code_url => "+code_url);
	}
	
	public void willNotify(HttpServletRequest request,HttpServletResponse response) throws IOException, DocumentException
	{
		logger.info(" ==== native getPrepayId notify start == ");
		
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
		String nativeNotifyData = new String(outSteam.toByteArray(), "utf-8");

		logger.info(" ==== wxpay native notify result : "+nativeNotifyData);

		Document notifyDataDoc = DocumentHelper.parseText(nativeNotifyData);

		String _appid = notifyDataDoc.selectSingleNode("//xml/appid").getText();
		String _openid = notifyDataDoc.selectSingleNode("//xml/openid").getText();
		String _mch_id = notifyDataDoc.selectSingleNode("//xml/mch_id").getText();
		String _nonce_str = notifyDataDoc.selectSingleNode("//xml/nonce_str").getText();
		String _product_id = notifyDataDoc.selectSingleNode("//xml/product_id").getText();
		String _sign = notifyDataDoc.selectSingleNode("//xml/sign").getText();

		//调用
		String trade_type = "NATIVE";
		String attach  = appId;
		//商户订单号
		String out_trade_no = new String(request.getParameter("WIDout_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//订单名称
		String subject = new String(request.getParameter("WIDsubject"));
		//付款金额
		String total_fee = new String(request.getParameter("WIDtotal_fee").getBytes("ISO-8859-1"),"UTF-8");

		SortedMap<String, String> sortedMap = new TreeMap<String, String>();
		sortedMap.put("notify_url", notifyUrl);
		sortedMap.put("nonce_str", _nonce_str);
		sortedMap.put("out_trade_no", _product_id);
		sortedMap.put("spbill_create_ip", request.getRemoteAddr());
		sortedMap.put("total_fee", total_fee);
		sortedMap.put("appid", appId);
		sortedMap.put("body", subject);
		sortedMap.put("mch_id", mchId);
		sortedMap.put("attach", attach);
		sortedMap.put("product_id",_product_id);
		sortedMap.put("trade_type", trade_type);

		StringBuilder signData = new StringBuilder();
		String mark = "";
		for(String key : sortedMap.keySet())
		{
			signData.append(mark).append(key).append("=").append(sortedMap.get(key));
			mark = "&";
		}
		signData.append(mark).append("key=").append(mchKey);
		String sign = MD5Coder.encode(signData.toString()).toUpperCase(Locale.CHINA);
		sortedMap.put("sign", sign);

		//getPrepayId
		String prepayId = "";
		String code_url = "";
		StringBuilder requestXmlData = new StringBuilder();
		requestXmlData.append("<xml>");
		for(String key : sortedMap.keySet())
		{
			requestXmlData.append("<").append(key.toLowerCase(Locale.CHINA)).append("><![CDATA[")
				  .append(sortedMap.get(key))
				  .append("]]></").append(key.toLowerCase(Locale.CHINA)).append(">");
		}
		requestXmlData.append("</xml>");
		byte[] result = HttpUtil.doRestPost(wxOrderUrl, requestXmlData.toString());
		String xmlData = new String(result,"UTF-8");

		Document prepayOrderData = DocumentHelper.parseText(xmlData);

		SortedMap<String, String> responseData = new TreeMap<String, String>();

		responseData.put("appid", appId);
		responseData.put("mch_id", mchId);
		responseData.put("nonce_str", _nonce_str);

		StringBuilder responseDataSign = new StringBuilder();

		String returnCode = prepayOrderData.selectSingleNode("//xml/return_code").getText();

		if( "SUCCESS".equalsIgnoreCase(returnCode) )
		{
			responseData.put("return_code", "SUCCESS");
			responseData.put("return_msg", "SUCCESS");
			
			String resultCode = prepayOrderData.selectSingleNode("//xml/result_code").getText();
			if( "SUCCESS".equalsIgnoreCase(resultCode) )
			{
				prepayId =  prepayOrderData.selectSingleNode("//xml/prepay_id").getText();
				
				responseData.put("result_code", "SUCCESS");
				responseData.put("err_code_des", "");
			}
			else
			{
				responseData.put("result_code", "FAIL");
				responseData.put("err_code_des", "获取结果失败");
			}
		}
		else
		{
			responseData.put("return_code", "FAIL");
			responseData.put("return_msg", "获取结果失败");
			responseData.put("result_code", "FAIL");
			responseData.put("err_code_des", "获取结果失败");
		}

		responseData.put("prepay_id", prepayId);

		mark = "";
		for(String key : responseData.keySet())
		{
			responseDataSign.append(mark).append(key).append("=").append(responseData.get(key));
			mark = "&";
		}
		responseDataSign.append(mark).append("key=").append(mchKey);

		responseData.put("sign", MD5Coder.encode(signData.toString()).toUpperCase(Locale.CHINA));

		StringBuilder responseXml = new StringBuilder();
		responseXml.append("<xml>");
		for(String key : responseData.keySet())
		{
			responseXml.append("<").append(key.toLowerCase(Locale.CHINA)).append("><![CDATA[")
				  .append(responseData.get(key))
				  .append("]]></").append(key.toLowerCase(Locale.CHINA)).append(">");
		}
		responseXml.append("</xml>");
		toResponse(responseXml.toString());
		
		logger.info(" ==== native getPrepayId notify end == ");
	}
}
