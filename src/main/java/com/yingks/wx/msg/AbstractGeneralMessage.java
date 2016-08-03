package com.yingks.wx.msg;

public class AbstractGeneralMessage extends BaseMessage {

	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;

	
	private String MsgId;


	public String getMsgId() {
		return MsgId;
	}

	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
	
}
