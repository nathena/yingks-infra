package com.yingks.wx.msg;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSONObject;
import com.yingks.infra.crypto.SHA1Coder;
import com.yingks.wx.WxConfig;

/**
 * 目前仅支持明文处理
 * @Title: WxReceiveMsgGateWay.java
 * @Package com.yingks.wx.msg
 * @author nathena  
 * @date 2016年8月4日 下午3:04:47
 * @version V1.0 
 * @UpdateHis:
 *      TODO
 */
public class WxReceiveMsgGateWay 
{
	private static Logger logger = Logger.getLogger(WxReceiveMsgGateWay.class);
	
	private WxConfig config;
	private ProcessMsgHandler msgHandler;
	
	public WxReceiveMsgGateWay(WxConfig config, ProcessMsgHandler msgHandler)
	{
		this.config = config;
		this.msgHandler = msgHandler;
	}
	
	public void valid(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce     = request.getParameter("nonce");
		String echostr   = request.getParameter("echostr");
		
		String token     = config.getToken();
		
		List<String> data = new ArrayList<>();
		data.add(token);
		data.add(timestamp);
		data.add(nonce);
		
		Collections.sort(data);
		
		StringBuffer tmpStr = new StringBuffer();
		for(String val : data)
		{
			tmpStr.append(val);
		}
		
		String sha1TmpStr = SHA1Coder.encode(tmpStr.toString());
		if( signature.equals(sha1TmpStr) )
		{
			response.getWriter().write(echostr);
		}
		else if( config.isDebug() )
		{
			response.getWriter().write(echostr);//也正常
		}
	}
	
	public void parserData(InputStream in) throws DocumentException, IOException
	{
		Map<String, String> msgData = new HashMap<>();
		
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
	
	protected void processData(Map<String, String> msgData)
	{
		logger.debug(" =====   WxReceiveMsgGateWay processData  ====== "+JSONObject.toJSONString(msgData) );
		
		if("event".equals(msgData.get("MsgType")) )
		{
			//事件消息
			//关注
			if( WxEventMsgType.subscribe.name().equals(msgData.get("Event")) )
			{
				SubscribeMessage msg = new SubscribeMessage();
				msg.setToUserName(msgData.get("ToUserName"));
				msg.setFromUserName(msgData.get("FromUserName"));
				msg.setCreateTime(msgData.get("CreateTime"));
				msg.setMsgType(msgData.get("MsgType"));
				msg.setEvent(msgData.get("Event"));
				
				msg.setEventKey(msgData.get("EventKey"));
				msg.setTicket(msgData.get("Ticket"));
				
				
				msgHandler.process(msg);
			}
			else if( WxEventMsgType.SCAN.name().equals(msgData.get("Event")) )
			{
				ScanMessage msg = new ScanMessage();
				
				msg.setToUserName(msgData.get("ToUserName"));
				msg.setFromUserName(msgData.get("FromUserName"));
				msg.setCreateTime(msgData.get("CreateTime"));
				msg.setMsgType(msgData.get("MsgType"));
				msg.setEvent(msgData.get("Event"));
				msg.setEventKey(msgData.get("EventKey"));
				msg.setTicket(msgData.get("Ticket"));
				
				msgHandler.process(msg);
			}
			else if( WxEventMsgType.LOCATION.name().equals(msgData.get("Event")) )
			{
				LocationEventMessage msg = new LocationEventMessage();
				msg.setToUserName(msgData.get("ToUserName"));
				msg.setFromUserName(msgData.get("FromUserName"));
				msg.setCreateTime(msgData.get("CreateTime"));
				msg.setMsgType(msgData.get("MsgType"));
				msg.setEvent(msgData.get("Event"));
				msg.setLatitude(msgData.get("Latitude"));
				msg.setLongitude(msgData.get("Longitude"));
				msg.setPrecision(msgData.get("Precision"));
				
				msgHandler.process(msg);
			}
			else if( WxEventMsgType.CLICK.name().equals(msgData.get("Event")) )
			{
				ClickMessage msg = new ClickMessage();
				msg.setToUserName(msgData.get("ToUserName"));
				msg.setFromUserName(msgData.get("FromUserName"));
				msg.setCreateTime(msgData.get("CreateTime"));
				msg.setMsgType(msgData.get("MsgType"));
				msg.setEvent(msgData.get("Event"));
				msg.setEventKey(msgData.get("EventKey"));
				
				msgHandler.process(msg);
			}
			else if( WxEventMsgType.VIEW.name().equals(msgData.get("Event")) )
			{
				ViewMessage msg = new ViewMessage();
				msg.setToUserName(msgData.get("ToUserName"));
				msg.setFromUserName(msgData.get("FromUserName"));
				msg.setCreateTime(msgData.get("CreateTime"));
				msg.setMsgType(msgData.get("MsgType"));
				msg.setEvent(msgData.get("Event"));
				msg.setEventKey(msgData.get("EventKey"));
				
				msgHandler.process(msg);
			}
			else if( WxEventMsgType.qualification_verify_success.name().equals(msgData.get("Event")) )
			{
				QualificationVerifySuccess msg = new QualificationVerifySuccess();
				
				msg.setToUserName(msgData.get("ToUserName"));
				msg.setFromUserName(msgData.get("FromUserName"));
				msg.setCreateTime(msgData.get("CreateTime"));
				msg.setMsgType(msgData.get("MsgType"));
				msg.setEvent(msgData.get("Event"));
				
				msg.setExpiredTime(msgData.get("ExpiredTime"));
				
				msgHandler.process(msg);
			}
			else if( WxEventMsgType.qualification_verify_fail.name().equals(msgData.get("Event")) )
			{
				QualificationVerifyFail msg = new QualificationVerifyFail();
				
				msg.setToUserName(msgData.get("ToUserName"));
				msg.setFromUserName(msgData.get("FromUserName"));
				msg.setCreateTime(msgData.get("CreateTime"));
				msg.setMsgType(msgData.get("MsgType"));
				msg.setEvent(msgData.get("Event"));
				
				msg.setFailTime(msgData.get("FailTime"));
				msg.setFailReason(msgData.get("FailReason"));
				
				msgHandler.process(msg);
			}
			else if( WxEventMsgType.naming_verify_success.name().equals(msgData.get("Event")) )
			{
				NamingVerifySuccess msg = new NamingVerifySuccess();
				
				msg.setToUserName(msgData.get("ToUserName"));
				msg.setFromUserName(msgData.get("FromUserName"));
				msg.setCreateTime(msgData.get("CreateTime"));
				msg.setMsgType(msgData.get("MsgType"));
				msg.setEvent(msgData.get("Event"));
				
				msg.setExpiredTime(msgData.get("ExpiredTime"));
				
				msgHandler.process(msg);
			}
			else if( WxEventMsgType.naming_verify_fail.name().equals(msgData.get("Event")) )
			{
				NamingVerifyFail msg = new NamingVerifyFail();
				
				msg.setToUserName(msgData.get("ToUserName"));
				msg.setFromUserName(msgData.get("FromUserName"));
				msg.setCreateTime(msgData.get("CreateTime"));
				msg.setMsgType(msgData.get("MsgType"));
				msg.setEvent(msgData.get("Event"));
				
				msg.setFailTime(msgData.get("FailTime"));
				msg.setFailReason(msgData.get("FailReason"));
				
				msgHandler.process(msg);
			}
			else if( WxEventMsgType.annual_renew.name().equals(msgData.get("Event")) )
			{
				AnnualRenew msg = new AnnualRenew();
				
				msg.setToUserName(msgData.get("ToUserName"));
				msg.setFromUserName(msgData.get("FromUserName"));
				msg.setCreateTime(msgData.get("CreateTime"));
				msg.setMsgType(msgData.get("MsgType"));
				msg.setEvent(msgData.get("Event"));
				
				msg.setExpiredTime(msgData.get("ExpiredTime"));
				
				msgHandler.process(msg);
			}
			else if( WxEventMsgType.verify_expired.name().equals(msgData.get("Event")) )
			{
				VerifyExpired msg = new VerifyExpired();
				
				msg.setToUserName(msgData.get("ToUserName"));
				msg.setFromUserName(msgData.get("FromUserName"));
				msg.setCreateTime(msgData.get("CreateTime"));
				msg.setMsgType(msgData.get("MsgType"));
				msg.setEvent(msgData.get("Event"));
				
				msg.setExpiredTime(msgData.get("ExpiredTime"));
				
				msgHandler.process(msg);
			}
			else
			{
				logger.debug(" =====   WxReceiveMsgGateWay processData not event hanlder to process ====== "+JSONObject.toJSONString(msgData) );
			}
		}
		else if( WxGeneralMsgType.text.name().equals(msgData.get("MsgType")) )
		{
			TextMessage msg = new TextMessage();
			msg.setToUserName(msgData.get("ToUserName"));
			msg.setFromUserName(msgData.get("FromUserName"));
			msg.setCreateTime(msgData.get("CreateTime"));
			msg.setMsgType(msgData.get("MsgType"));
			msg.setMsgId(msgData.get("MsgId"));
			
			msg.setContent(msgData.get("Content"));
			
			msgHandler.process(msg);
		}
		else if( WxGeneralMsgType.image.name().equals(msgData.get("MsgType")) )
		{
			ImageMessage msg = new ImageMessage();
			
			msg.setToUserName(msgData.get("ToUserName"));
			msg.setFromUserName(msgData.get("FromUserName"));
			msg.setCreateTime(msgData.get("CreateTime"));
			msg.setMsgType(msgData.get("MsgType"));
			msg.setMsgId(msgData.get("MsgId"));
			
			msg.setMediaId(msgData.get("MediaId"));
			msg.setPicUrl(msgData.get("PicUrl"));
			
			msgHandler.process(msg);
		}
		else if( WxGeneralMsgType.voice.name().equals(msgData.get("MsgType")) )
		{
			VoiceMessage msg = new VoiceMessage();
			
			msg.setToUserName(msgData.get("ToUserName"));
			msg.setFromUserName(msgData.get("FromUserName"));
			msg.setCreateTime(msgData.get("CreateTime"));
			msg.setMsgType(msgData.get("MsgType"));
			msg.setMsgId(msgData.get("MsgId"));
			
			msg.setMediaId(msgData.get("MediaId"));
			msg.setRecognition(msgData.get("Recognition"));
			msg.setFormat(msgData.get("Format"));
			
			msgHandler.process(msg);
		}
		else if( WxGeneralMsgType.video.name().equals(msgData.get("MsgType")) )
		{
			VideoMessage msg = new VideoMessage();
			
			msg.setToUserName(msgData.get("ToUserName"));
			msg.setFromUserName(msgData.get("FromUserName"));
			msg.setCreateTime(msgData.get("CreateTime"));
			msg.setMsgType(msgData.get("MsgType"));
			msg.setMsgId(msgData.get("MsgId"));
			
			msg.setMediaId(msgData.get("MediaId"));
			msg.setThumbMediaId(msgData.get("ThumbMediaId"));
			
			msgHandler.process(msg);
		}
		else if( WxGeneralMsgType.shortvideo.name().equals(msgData.get("MsgType")) )
		{
			ShortVideoMessage msg = new ShortVideoMessage();
			
			msg.setToUserName(msgData.get("ToUserName"));
			msg.setFromUserName(msgData.get("FromUserName"));
			msg.setCreateTime(msgData.get("CreateTime"));
			msg.setMsgType(msgData.get("MsgType"));
			msg.setMsgId(msgData.get("MsgId"));
			
			msg.setMediaId(msgData.get("MediaId"));
			msg.setThumbMediaId(msgData.get("ThumbMediaId"));
			
			msgHandler.process(msg);
		}
		else if( WxGeneralMsgType.location.name().equals(msgData.get("MsgType")) )
		{
			LocationMessage msg = new LocationMessage();
			
			msg.setToUserName(msgData.get("ToUserName"));
			msg.setFromUserName(msgData.get("FromUserName"));
			msg.setCreateTime(msgData.get("CreateTime"));
			msg.setMsgType(msgData.get("MsgType"));
			msg.setMsgId(msgData.get("MsgId"));
			
			msg.setLocation_X(msgData.get("Location_X"));
			msg.setLocation_Y(msgData.get("Location_Y"));
			msg.setScale(msgData.get("Scale"));
			msg.setLabel(msgData.get("Label"));
			
			msgHandler.process(msg);
		}
		else if( WxGeneralMsgType.link.name().equals(msgData.get("MsgType")) )
		{
			LinkMessage msg = new LinkMessage();
			
			msgHandler.process(msg);
		}
		else
		{
			logger.debug(" =====   WxReceiveMsgGateWay processData not hanlder to process ====== "+JSONObject.toJSONString(msgData) );
		}
	}
}
