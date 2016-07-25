package com.yingks.infra.mail;


/**
 * 
 * @Description: mail 安全连接 类型 
 * @author nathena 
 * @date 2013-4-25 下午2:38:15 
 *
 */
public enum MailEncryptionTypes {
	
	Default("",1), TLS("TLS",2), SSL("SSL",3);
	
	private String name;  
    private int index;  
    
    private MailEncryptionTypes(String name, int index) {  
        this.name = name;  
        this.index = index;  
    }  

    public static String getName(int index) {  
        for (MailEncryptionTypes c : MailEncryptionTypes.values()) {  
            if (c.getIndex() == index) {  
                return c.name;  
            }  
        }  
        return null;  
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
