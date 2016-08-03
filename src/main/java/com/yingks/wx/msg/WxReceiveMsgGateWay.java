package com.yingks.wx.msg;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class WxReceiveMsgGateWay 
{
	private Map<String, String> msgData = new HashMap<>();
	
	private ProcessMsgHandler msgHandler;
	
	public WxReceiveMsgGateWay(ProcessMsgHandler msgHandler)
	{
		this.msgHandler = msgHandler;
	}
	
	public void parserData(InputStream in) throws DocumentException, IOException
	{
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(in);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		@SuppressWarnings("unchecked")
		List<Element> elementList = root.elements();
		// 遍历所有子节点
		for (Element e : elementList)
		{
			msgData.put(e.getName(), e.getText());
		}

		// 释放资源
		in.close();
	}
	
	protected void processData()
	{
		if("event".equals(msgData.get("MsgType")) )
		{
			//事件消息
		}
		else
		{
			//普通消息
		}
	}
}
