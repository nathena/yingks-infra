package com.yingks.wx.msg;

//自定义菜单事件
public class ClickMessage extends AbstractEventMessage {

	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;

	//事件KEY值，与自定义菜单接口中KEY值对应 
	private String EventKey;

	public String getEventKey() {
		return EventKey;
	}

	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}
	
	
}
