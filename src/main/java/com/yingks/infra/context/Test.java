package com.yingks.infra.context;

import java.util.ArrayList;
import java.util.List;

import com.yingks.infra.domain.data.EntityReflectUtils;

public class Test{

	public Test()
	{
	}
	
	public <T> void aa(List<T> clazz)
	{
		System.out.println( clazz.getClass() );
	}
	
	public <T> void aaa(T clazz)
	{
		System.out.println( clazz.getClass() );
		System.out.println( EntityReflectUtils.getEntityClass(clazz).clazz );
	}
	
	public static void main(String[] arg)
	{
		Test test = new Test();
		test.aa(new ArrayList<Test4>());
		test.aaa(new Test4());
	}
}

class Test4
{
	
}
