package com.yingks.infra.context;

public class Test extends Test1 {

	public Test()
	{
		System.out.println("11111");
	}
	
	public static void main(String[] arg)
	{
		new Test();
	}
}

class Test1
{
	public Test1()
	{
		System.out.println("2222");
	}
}
