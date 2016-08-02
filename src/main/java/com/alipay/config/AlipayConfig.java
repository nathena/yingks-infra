package com.alipay.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {
	
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "2088811588754524";
	
	// 交易安全检验码，由数字和字母组成的32位字符串
	// 如果签名方式设置为“MD5”时，请设置该参数
	public static String key = "yyvo138kyi332jzoy3igr4tjf4ev67qt";
	
    // 商户的私钥
    // 如果签名方式设置为“0001”时，请设置该参数
	//商户私钥，pkcs8格式
	public static String private_key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALP2rrlStnqnW0rlJeXy2IvJnXV0oJpeXnyeL3DpTtP6MMeJ7vvdM01owQGBXNqhaZsNKN/KkSKzLMrLGnmwyyxT1Hu/VeXb0z5WsfAYyFnj0Mh26H3eBE+P3eOOEGAI3wBA2EgIT8OX0DJ3sBi87P50bqT5rrWdUN4sJ2i5L2g7AgMBAAECgYBfhZZkY9Tb8azxagPdtDLgr9lWGNuOVHzOpKAFzLC8r1Xo8/vX0CeE9Q9NHz8MBRUq6adCciDrTDOASKH66a5NKuTtrYwFfX8mmRBU4b1HgaDGOqtKb1nvi1BKJJZs8zmp5dOlyRqSVqQPcxlqtbotD9jbxaPgXyQcW60dtADuIQJBAOGl+5EoH+nXIW2IesNTkGDQz9zEhDY3rEZ7Rgh7KV42wlj/wq05tw5fM0QJT3PGCeMDG565AQik6cWoopI0CYkCQQDMK5UMPXDcW8hstarz+VVYm70dH+PaQHsnl7WM/zDL5uP22OvSKBzdlaF3JPwSM4TpsBkyOKy+NDVICBpRyyajAkBC8m/03Sa6xi0QxlFF6mEHmGxTX6qqO1JNhv18Pq5DWZPHu/oSUvFQvkYDhY+hFbyRMbbcHJ+F5QfGsQWqwu2hAkEAjFfU74H4Z3Cr15YU+fnE4dDD8RYvsKGsRi3xfQcZir+eqdq4V1B9bw9g2x5QK07AqUYEO9JbY69NhMMVHG5RkwJBAKFO9PGCZYwtsKgkhiPmjnKU3Z3wgo2HJeytSZ8FuzqfdXN6t0ausbqg7PVKjjf/YUGdy6Y9yeY/yeaSsQ4q29E=";
	
    // 支付宝的公钥
    // 如果签名方式设置为“0001”时，请设置该参数
	public static String ali_public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	

	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "";

	// 字符编码格式 目前支持  utf-8
	public static String input_charset = "utf-8";
	
	// 签名方式，选择项：0001(RSA)、MD5
	public static String sign_type = "MD5";
	// 无线的产品中，签名方式为rsa时，sign_type需赋值为0001而不是RSA
}
