package com.yingks.wx.msg;

public interface ProcessMsgHandler {

	//关注/取消关注事件
	public void process(SubscribeMessage msg);
	//扫描带参数二维码事件，注意会发送两个事件：1、SubscribeMessage；2、ScanMessage
	public void process(ScanMessage msg);
	//上报地理位置事件
	public void process(LocationEventMessage msg);
	//点击菜单拉取消息时的事件推送
	public void process(ClickMessage msg);
	//点击菜单跳转链接时的事件推送 
	public void process(ViewMessage msg);
	
	public void process(QualificationVerifySuccess msg);
	public void process(QualificationVerifyFail msg);
	public void process(NamingVerifySuccess msg);
	public void process(NamingVerifyFail msg);
	public void process(AnnualRenew msg);
	public void process(VerifyExpired msg);
	
	public void process(LocationMessage msg);
	public void process(TextMessage msg);
	public void process(ImageMessage msg);
	public void process(VoiceMessage msg);
	public void process(ShortVideoMessage msg);
	public void process(VideoMessage msg);
	public void process(LinkMessage msg);
	
}
