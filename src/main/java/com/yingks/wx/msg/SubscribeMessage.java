package com.yingks.wx.msg;

/**
 * 关注/取消关注事件
 * @Title: SubscribeMessage.java
 * @Package com.yingks.wx.msg
 * @author nathena  
 * @date 2016年8月4日 下午1:45:43
 * @version V1.0 
 * @UpdateHis:
 *      TODO
 */
public class SubscribeMessage extends AbstractEventMessage {

	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;

	//事件KEY值，qrscene_为前缀，后面为二维码的参数值
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
