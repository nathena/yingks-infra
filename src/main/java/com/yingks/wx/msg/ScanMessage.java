package com.yingks.wx.msg;

public class ScanMessage extends AbstractEventMessage {

	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;

	//事件KEY值，是一个32位无符号整数，即创建二维码时的二维码scene_id 
	private String EventKey;
	//二维码的ticket，可用来换取二维码图片 
	private String Ticket;
	public String getEventKey() {
		return EventKey;
	}
	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}
	public String getTicket() {
		return Ticket;
	}
	public void setTicket(String ticket) {
		Ticket = ticket;
	}
}
