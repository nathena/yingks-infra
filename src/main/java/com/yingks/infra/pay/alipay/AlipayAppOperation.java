package com.yingks.infra.pay.alipay;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alipay.util.AlipayNotify;
import com.pay.alipay.sign.RSA;
import com.yingks.infra.pay.AbstractPayOperation;
import com.yingks.infra.pay.PayChannelEnum;
import com.yingks.infra.pay.PayStatusEnum;
import com.yingks.infra.pay.PaymentOperationInterface;
import com.yingks.infra.pay.TradeNotify;
import com.yingks.infra.pay.TradeNotify.Type;
import com.yingks.infra.pay.exception.PayException;
import com.yingks.infra.utils.NumberUtil;
import com.yingks.infra.utils.StringUtil;

public class AlipayappOperation extends AbstractPayOperation  {

	private static Logger logger  = Logger.getLogger(AlipayappOperation.class);
	
	//privateKey
	private static String privateKey = AlipayConfig.private_key;
	//服务器异步通知页面路径
	private static String notify_url = AlipayConfig.app_notify_url;
	
	public AlipayappOperation(PaymentOperationInterface paymentOperation) {
		super(paymentOperation);
	}

	public void toPay(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		logger.debug(" === alipay app === 正在进入支付宝...... ");
		
		if( !paymentOperation.checkPaymentParams(request,response) )
		{
			logger.debug(" === alipay === 验证请求参数错误...... ");
			throw new PayException("验证请求参数错误");
		}
		
		String subject = new String(request.getParameter("WIDsubject").getBytes("ISO-8859-1"),"UTF-8");
		String out_trade_no = new String(request.getParameter("WIDout_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		String body = new String(request.getParameter("body").getBytes("ISO-8859-1"),"UTF-8");;
		String total_fee = new String(request.getParameter("WIDtotal_fee").getBytes("ISO-8859-1"),"UTF-8");
		total_fee = NumberUtil.parseDouble(NumberUtil.parseInt(total_fee)/100)+"";
		
		if(StringUtil.isEmpty(body)) {
			body = subject;
		}
		
		//签约合作者身份ID
		String orderInfo = "partner=" + "\"2088811588754524\"";
		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"open@jytnn.com\"";
		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + out_trade_no + "\"";
		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";
		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";
		// 商品金额
		orderInfo += "&total_fee=" + "\"" + total_fee + "\"";
		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + notify_url+ "\"";
		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";
		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";
		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";
		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";
		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";
		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";
		//签名
		String sign = URLEncoder.encode(RSA.sign(orderInfo, privateKey), "utf-8");
		orderInfo = orderInfo + "&sign=\"" + sign + "\"&sign_type=\"RSA\"";
		
		toResponse( orderInfo.trim() );
		
		logger.debug(" === alipay app === 请求完毕...... ");
	}
	
	public void willNotify(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		logger.debug(" ===== alipay app notify_url  ====== ");
		
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
		//商户订单号
		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//支付宝交易号
		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//交易状态
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
		//交易金额
		String notifyMoney = new String(request.getParameter("total_fee").getBytes("ISO-8859-1"),"UTF-8");

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
					msg.setTradeType(Type.app);
					
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
		
		logger.debug(" ==== alipay app notified ====== out_trade_no :"+out_trade_no+",trade_no : "+trade_no+",trade_status:"+trade_status);
	}
}
