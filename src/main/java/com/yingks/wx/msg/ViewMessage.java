package com.yingks.wx.msg;

//点击菜单跳转链接时的事件推送 
public class ViewMessage extends AbstractEventMessage {

	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;

	//事件KEY值，设置的跳转URL 
	private String EventKey;

	public String getEventKey() {
		return EventKey;
	}

	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}
	
	
}
