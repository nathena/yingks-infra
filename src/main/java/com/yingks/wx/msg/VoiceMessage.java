package com.yingks.wx.msg;

public class VoiceMessage extends AbstractGeneralMessage {
	
	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;
	
	private String MediaId;//语音消息媒体id，可以调用多媒体文件下载接口拉取数据。
	private String Format;//语音格式，如amr，speex等
	private String Recognition;
	
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
	public String getRecognition() {
		return Recognition;
	}
	public void setRecognition(String recognition) {
		Recognition = recognition;
	}
	
}
