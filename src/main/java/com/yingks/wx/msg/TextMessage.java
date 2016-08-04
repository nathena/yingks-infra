package com.yingks.wx.msg;

public class TextMessage extends AbstractGeneralMessage{
	
	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;
	
	private String Content;

	public String getContent() {
		return this.Content;
	}
	public void setContent(String content) {
		this.Content = content;
	}
	
}
