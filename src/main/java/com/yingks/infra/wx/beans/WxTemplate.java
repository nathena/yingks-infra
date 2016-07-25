package com.yingks.infra.wx.beans;

import java.util.HashMap;
import java.util.Map;

public class WxTemplate 
{
	private String template_id;  
    private String touser;  
    private String url;  
    private String topcolor;  
    private Map<String,WxTemplateData> data = new HashMap<String, WxTemplateData>();  
      
    public String getTemplate_id() {  
        return template_id;  
    }  
    public void setTemplate_id(String template_id) {  
        this.template_id = template_id;  
    }  
    public String getTouser() {  
        return touser;  
    }  
    public void setTouser(String touser) {  
        this.touser = touser;  
    }  
    public String getUrl() {  
        return url;  
    }  
    public void setUrl(String url) {  
        this.url = url;  
    }  
    public String getTopcolor() {  
        return topcolor;  
    }  
    public void setTopcolor(String topcolor) {  
        this.topcolor = topcolor;  
    }  
    public Map<String,WxTemplateData> getData() {  
        return data;  
    }  
    public void addData(String key, WxTemplateData data) {  
        this.data.put(key, data);  
    }
}
