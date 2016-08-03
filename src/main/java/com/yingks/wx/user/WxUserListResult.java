package com.yingks.wx.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WxUserListResult implements Serializable {

	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;

	private String total;
	private String count;
	private Data   data;
	private String next_openid;
	
	
	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public String getNext_openid() {
		return next_openid;
	}

	public void setNext_openid(String next_openid) {
		this.next_openid = next_openid;
	}

	public class Data
	{
		private List<String> openid = new ArrayList<>();

		public List<String> getOpenid() {
			return openid;
		}

		public void setOpenid(List<String> openid) {
			this.openid = openid;
		}
	}
}
