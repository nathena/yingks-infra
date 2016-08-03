package com.yingks.infra.context;

public class Test {

	private static String wxuserList_api = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=%s&next_openid=%s";
	
	public static void main(String[] arg)
	{
		System.out.println(String.format(wxuserList_api, "111",""));
	}
}

