package com.yingks.infra.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class BaseJavaMailAuthenticator extends Authenticator {
	String userName=null;  
    String password=null;  
       
    public BaseJavaMailAuthenticator()
    {
    	
    }
    
    public BaseJavaMailAuthenticator(String username, String password) 
    {   
        this.userName = username;   
        this.password = password;   
    }
    
    protected PasswordAuthentication getPasswordAuthentication()
    {  
        return new PasswordAuthentication(userName, password);  
    }  
}
