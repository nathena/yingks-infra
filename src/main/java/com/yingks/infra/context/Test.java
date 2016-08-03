package com.yingks.infra.context;

public class Test extends Test4 {

	public Test()
	{
	}
	
	public Test(int a)
	{
		
	}
	
	public static void main(String[] arg)
	{
		Test test = new Test(1);
	}
}

class Test4
{
	public Test4()
	{
		this.aaa();
	}
	
	public Test4(int a)
	{
		System.out.println("121212");
	}
	
	public void aaa()
	{
		System.out.println(this.getClass());
	}
}
