package com.yingks.wx.mp;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

public class WxMpQr implements Serializable {

	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;

	private String expire_seconds = "604800";
	
	private String scene_id;
	private String scene_str;
	
	protected enum Action
	{
		QR_SCENE,QR_LIMIT_SCENE
	}
	
	public String getExpire_seconds() {
		return expire_seconds;
	}

	public void setExpire_seconds(String expire_seconds) {
		this.expire_seconds = expire_seconds;
	}

	public String getScene_id() {
		return scene_id;
	}

	public void setScene_id(String scene_id) {
		this.scene_id = scene_id;
	}

	public String getScene_str() {
		return scene_str;
	}

	public void setScene_str(String scene_str) {
		this.scene_str = scene_str;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String toString()
	{
		JSONObject json = new JSONObject();
		json.put("action_name", Action.QR_LIMIT_SCENE.name());
		
		JSONObject scene = new JSONObject();
		scene.put("scene_str", getScene_str());
		scene.put("scene_id", getScene_id());
		
		JSONObject data = new JSONObject();
		data.put("scene", scene);
		
		json.put("action_info", data);
		
		return json.toJSONString();
	}
	
	public String toTemporaryString()
	{
		JSONObject json = new JSONObject();
		json.put("action_name", Action.QR_SCENE.name());
		json.put("expire_seconds", getExpire_seconds());
		
		JSONObject scene = new JSONObject();
		scene.put("scene_str", getScene_str());
		scene.put("scene_id", getScene_id());
		
		JSONObject data = new JSONObject();
		data.put("scene", scene);
		
		json.put("action_info", data);
		
		return json.toJSONString();
	}
}
