package com.yingks.wx.msg;

public class PicMessage extends BaseMessage{
	
	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;
	
	private String PicUrl;
	private String MediaId;
	
	
	public String getPicUrl() {
		return this.PicUrl;
	}
	public void setPicUrl(String picUrl) {
		this.PicUrl = picUrl;
	}
	public String getMediaId() {
		return this.MediaId;
	}
	public void setMediaId(String mediaId) {
		this.MediaId = mediaId;
	}
}
