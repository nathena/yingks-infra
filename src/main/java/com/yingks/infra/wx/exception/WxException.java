package com.yingks.infra.wx.exception;

import com.yingks.infra.exception.NestedRuntimeException;

public class WxException extends NestedRuntimeException {

	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;

	public WxException(WxExceptionMsg msg, Throwable cause) {
		super(msg.getCode(), msg.getMsg(),cause);
	}

	public WxException(WxExceptionMsg msg) {
		super(msg.getCode(), msg.getMsg());
	}

	public WxException(WxExceptionMsg msg, Throwable cause,Object... values) {
		super(msg.getCode(), msg.getMsg(values),cause);
	}

	public WxException(WxExceptionMsg msg,Object... values) {
		super(msg.getCode(), msg.getMsg(values));
	}

}
