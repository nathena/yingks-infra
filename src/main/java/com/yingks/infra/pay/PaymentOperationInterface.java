package com.yingks.infra.pay;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PaymentOperationInterface {

	public void notify(TradeNotify msg);
	public boolean checkPaymentParams(HttpServletRequest request,HttpServletResponse response);
}
