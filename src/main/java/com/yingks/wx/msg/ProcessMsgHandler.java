package com.yingks.wx.msg;

public interface ProcessMsgHandler {

	public void process(SubscribeMessage msg);
	public void process(ScanMessage msg);
	public void process(ClickMessage msg);
	public void process(LinkEventMessage msg);
	public void process(LocationEventMessage msg);
	public void process(ViewMessage msg);
	
	public void process(LocationMessage msg);
	public void process(TextMessage msg);
	public void process(PicMessage msg);
	public void process(VoiceMessage msg);
	public void process(ShortVideoMessage msg);
	public void process(VideoMessage msg);
	public void process(LinkMessage msg);
	
}
