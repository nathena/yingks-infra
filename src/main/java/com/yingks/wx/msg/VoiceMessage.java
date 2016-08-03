package com.yingks.wx.msg;

public class VoiceMessage extends BaseMessage {
	
	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;
	
	private String MediaId;
	private String Format;
	
	public String getMediaId() {
		return this.MediaId;
	}
	public void setMediaId(String mediaId) {
		this.MediaId = mediaId;
	}
	public String getFormat() {
		return this.Format;
	}
	public void setFormat(String format) {
		this.Format = format;
	}
}
