package com.yingks.infra.pay;

import java.io.Serializable;

public class TradeNotify implements Serializable {

	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;

	private String paymentNo;
	private String tradeNo;
	private String tradeStatus;
	private String notifyMoney;
	private String msg;
	
	private Type tradeType;
	
	private PayStatusEnum status;
	private PayChannelEnum channel;
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getPaymentNo() {
		return paymentNo;
	}
	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public PayStatusEnum getStatus() {
		return status;
	}
	public void setStatus(PayStatusEnum status) {
		this.status = status;
	}
	public PayChannelEnum getChannel() {
		return channel;
	}
	public void setChannel(PayChannelEnum channel) {
		this.channel = channel;
	}
	public String getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public String getNotifyMoney() {
		return notifyMoney;
	}
	public void setNotifyMoney(String notifyMoney) {
		this.notifyMoney = notifyMoney;
	}
	
	public Type getTradeType() {
		return tradeType;
	}
	public void setTradeType(Type tradeType) {
		this.tradeType = tradeType;
	}

	public enum Type
	{
		app(1,"app"),web(2,"web");
		
		private int code;
		private String name;
		
		private Type(int code,String name)
		{
			this.code = code;
			this.name = name;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		public static Type getByCode(int code)
		{
			for(Type type : Type.values())
			{
				if( type.code == code )
				{
					return type;
				}
			}
			return web;
		}
		
		public static Type getByName(String name)
		{
			for(Type type : Type.values())
			{
				if( type.name.equals(name) )
				{
					return type;
				}
			}
			return web;
		}
	}
}
