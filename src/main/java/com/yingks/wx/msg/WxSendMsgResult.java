package com.yingks.wx.msg;

import java.io.Serializable;

public class WxSendMsgResult implements Serializable 
{

	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;

	private String type; 
	private String errcode;
	private String errmsg;
	private String msg_id;
	private String msg_data_id;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getErrcode() {
		return errcode;
	}
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public String getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}
	public String getMsg_data_id() {
		return msg_data_id;
	}
	public void setMsg_data_id(String msg_data_id) {
		this.msg_data_id = msg_data_id;
	}
	
	
}
