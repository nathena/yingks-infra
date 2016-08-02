package com.yingks.infra.pay.alipay;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import com.alipay.util.AlipayNotify;
import com.alipay.util.AlipaySubmit;
import com.alipay.util.UtilDate;
import com.yingks.infra.pay.AbstractPayOperation;
import com.yingks.infra.pay.PayChannelEnum;
import com.yingks.infra.pay.PayStatusEnum;
import com.yingks.infra.pay.PaymentOperationInterface;
import com.yingks.infra.pay.TradeNotify;
import com.yingks.infra.pay.TradeNotify.Type;
import com.yingks.infra.pay.exception.PayException;
import com.yingks.infra.utils.NumberUtil;

public class AlipayapiOperation extends AbstractPayOperation {

	private static Logger logger  = Logger.getLogger(AlipayapiOperation.class);
	
	//支付宝网关地址
	private static String ALIPAY_GATEWAY_NEW = "http://wappaygw.alipay.com/service/rest.htm?";
	////////////////////////////////////调用授权接口alipay.wap.trade.create.direct获取授权码token//////////////////////////////////////
	//返回格式
	private static String format = "xml";
	//必填，不需要修改
	//返回格式
	private static String v = "2.0";
	//必填，不需要修改
	//请求号
	private static String req_id = UtilDate.getOrderNum();
	//必填，须保证每次请求都是唯一
	//req_data详细信息
	//服务器异步通知页面路径
	private static String notify_url = AlipayConfig.notify_url;
	//页面跳转同步通知页面路径
	private static String return_url = AlipayConfig.return_url;
	//操作中断返回地址
	private static String merchant_url = AlipayConfig.merchant_url;
	//用户付款中途退出返回商户的地址。需http://格式的完整路径，不允许加?id=123这类自定义参数
	//卖家支付宝帐户
	private static String seller_email = AlipayConfig.seller_email;
	//必填
	
	public AlipayapiOperation(PaymentOperationInterface paymentOperation) {
		super(paymentOperation);
	}
	
	public void toPay(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		logger.debug(" === alipay === 正在进入支付宝...... ");
		
		if( !paymentOperation.checkPaymentParams(request,response) )
		{
			logger.debug(" === alipay === 验证请求参数错误...... ");
			throw new PayException("验证请求参数错误");
		}
		
		//商户订单号
		String out_trade_no = new String(request.getParameter("WIDout_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//订单名称
		String subject = new String(request.getParameter("WIDsubject").getBytes("ISO-8859-1"),"UTF-8");
		//付款金额
		String total_fee = new String(request.getParameter("WIDtotal_fee").getBytes("ISO-8859-1"),"UTF-8");
		total_fee = NumberUtil.parseDouble(NumberUtil.parseInt(total_fee)/100)+"";
		//必填
		
		HttpSession session = request.getSession();
		
		session.setAttribute("out_trade_no", out_trade_no);
		session.setAttribute("subject", subject);
		session.setAttribute("total_fee", total_fee);
		
		//请求业务参数详细
		String req_dataToken = "<direct_trade_create_req><notify_url>" + notify_url + "</notify_url><call_back_url>" + return_url + "</call_back_url><seller_account_name>" + seller_email + "</seller_account_name><out_trade_no>" + out_trade_no + "</out_trade_no><subject>" + subject + "</subject><total_fee>" + total_fee + "</total_fee><merchant_url>" + merchant_url + "</merchant_url></direct_trade_create_req>";
		
		//把请求参数打包成数组
		Map<String, String> sParaTempToken = new HashMap<String, String>();
		sParaTempToken.put("service", "alipay.wap.trade.create.direct");
		sParaTempToken.put("partner", AlipayConfig.partner);
		sParaTempToken.put("_input_charset", AlipayConfig.input_charset);
		sParaTempToken.put("sec_id", AlipayConfig.sign_type);
		sParaTempToken.put("format", format);
		sParaTempToken.put("v", v);
		sParaTempToken.put("req_id", req_id);
		sParaTempToken.put("req_data", req_dataToken);
		
		//建立请求
		String sHtmlTextToken = AlipaySubmit.buildRequest(ALIPAY_GATEWAY_NEW,"", "",sParaTempToken);
		
		//URLDECODE返回的信息
		sHtmlTextToken = URLDecoder.decode(sHtmlTextToken,AlipayConfig.input_charset);
		
		//获取token
		String request_token = AlipaySubmit.getRequestToken(sHtmlTextToken);
		
		//业务详细
		String req_data = "<auth_and_execute_req><request_token>" + request_token + "</request_token></auth_and_execute_req>";
		//必填
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "alipay.wap.auth.authAndExecute");
		sParaTemp.put("partner", AlipayConfig.partner);
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("sec_id", AlipayConfig.sign_type);
		sParaTemp.put("format", format);
		sParaTemp.put("v", v);
		sParaTemp.put("req_data", req_data);
		
		//建立请求
		String sHtmlText = AlipaySubmit.buildRequest(ALIPAY_GATEWAY_NEW, sParaTemp, "get", "确认");
		
		toResponse(sHtmlText,"text/html;charset=UTF-8");
		
		logger.debug(" === alipay === 请求完毕...... ");
	}
	
	public void willNotify(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		logger.debug(" ===== alipay notify_url  ====== ");
		
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//RSA签名解密
	   	if(AlipayConfig.sign_type.equals("0001")) {
	   		params = AlipayNotify.decrypt(params);
	   	}
		
		//XML解析notify_data数据
		Document doc_notify_data = DocumentHelper.parseText(params.get("notify_data"));
		//商户订单号
		String out_trade_no = doc_notify_data.selectSingleNode( "//notify/out_trade_no" ).getText();
		//支付宝交易号
		String trade_no = doc_notify_data.selectSingleNode( "//notify/trade_no" ).getText();
		//交易状态
		String trade_status = doc_notify_data.selectSingleNode( "//notify/trade_status" ).getText();
		//交易金额
		String notifyMoney = doc_notify_data.selectSingleNode( "//notify/total_fee" ).getText();

		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

		if(AlipayNotify.verifyNotify(params)){//验证成功
			//////////////////////////////////////////////////////////////////////////////////////////
			//请在这里加上商户的业务逻辑程序代码

			//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			
			if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS") ){
				//判断该笔订单是否在商户网站中已经做过处理
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//如果有做过处理，不执行商户的业务程序
					
				//注意：
				//该种交易状态只在两种情况下出现
				//1、开通了普通即时到账，买家付款成功后。
				//2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。
				try
				{
					TradeNotify msg = new TradeNotify();
					msg.setChannel(PayChannelEnum.alipay);
					msg.setStatus(PayStatusEnum.SUCCEES);
					msg.setOutTradeNo(out_trade_no);
					msg.setTradeNo(trade_no);
					msg.setTradeStatus(trade_status);
					msg.setNotifyMoney(notifyMoney);
					msg.setTradeType(Type.web);
					
					paymentOperation.notify(msg);
				}
				catch(Exception e)
				{
					logger.error(e);
				}
				toResponse("success");	//请不要修改或删除
			} 
			//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
			//////////////////////////////////////////////////////////////////////////////////////////
		}
		else
		{//验证失败
			toResponse("fail");
			
			logger.error(" ===== alipay notify_url fail ====== out_trade_no :"+out_trade_no+",trade_no : "+trade_no+",trade_status:"+trade_status);
		}
		
		logger.debug(" ==== alipay notified ====== out_trade_no :"+out_trade_no+",trade_no : "+trade_no+",trade_status:"+trade_status);
	}
	
	public void callBack(HttpServletRequest request,HttpServletResponse response) throws DocumentException, ServletException, IOException
	{
		logger.debug(" ===== alipay voucher ====== ");
		
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		
		//XML解析notify_data数据
		Document doc_notify_data = DocumentHelper.parseText(params.get("notify_data"));
		//商户订单号
		String out_trade_no = doc_notify_data.selectSingleNode( "//notify/out_trade_no" ).getText();
		//支付宝交易号
		String trade_no = doc_notify_data.selectSingleNode( "//notify/trade_no" ).getText();
		//交易状态
		String trade_status = doc_notify_data.selectSingleNode( "//notify/trade_status" ).getText();
		//交易金额
		String notifyMoney = doc_notify_data.selectSingleNode( "//notify/total_fee" ).getText();
		
		TradeNotify msg = new TradeNotify();
		msg.setChannel(PayChannelEnum.alipay);
		msg.setOutTradeNo(out_trade_no);
		msg.setStatus(PayStatusEnum.SUCCEES);
		msg.setTradeNo(trade_no);
		msg.setTradeStatus(trade_status);
		msg.setNotifyMoney(notifyMoney);
		msg.setTradeType(Type.web);
		
		request.setAttribute("tradeNotify", msg);
		
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		//计算得出通知验证结果
		boolean verify_result = AlipayNotify.verifyReturn(params);
		
		if(verify_result){//验证成功
			request.getRequestDispatcher(AlipayConfig.pay_success_url).forward(request, response);
		}else{
			request.getRequestDispatcher(AlipayConfig.pay_failed_url).forward(request, response);
		}
		
		logger.debug(" ===== alipay voucher end ====== ");
	}
}
