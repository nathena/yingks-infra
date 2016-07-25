package com.yingks.infra.domain;

public enum GenderType {

	FEMALE("female","女",1),
	MALE("male","男",2);
	
	
	private String value;
	private String name;
	private int index;
	
	public static String getName(int index)
	{
		for(GenderType genderType : GenderType.values() )
		{
			if( genderType.getIndex() == index )
			{
				return genderType.getName();
			}
		}
		return null;
	}
	
	public static String getValue(int index)
	{
		for(GenderType genderType : GenderType.values() )
		{
			if( genderType.getIndex() == index )
			{
				return genderType.getValue();
			}
		}
		return null;
	}
	
	private GenderType(String value,String name,int index)
	{
		this.value = value;
		this.name = name;
		this.index = index;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
