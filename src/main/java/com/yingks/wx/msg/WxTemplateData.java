package com.yingks.wx.msg;

import java.io.Serializable;

public class WxTemplateData implements Serializable 
{

	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;
	
	private String value;  
    private String color; 
    
    public WxTemplateData()
    {
    	
    }
    
    public WxTemplateData(String value,String color)
    {
    	this.value = value;
    	this.color = color;
    }
    
    public String getValue() {  
        return value;  
    }  
    public void setValue(String value) {  
        this.value = value;  
    }  
    public String getColor() {  
        return color;  
    }  
    public void setColor(String color) {  
        this.color = color;  
    } 
}
