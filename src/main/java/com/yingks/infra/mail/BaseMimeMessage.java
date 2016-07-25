package com.yingks.infra.mail;

import java.io.InputStream;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeMessage;

public class BaseMimeMessage extends MimeMessage {

	public BaseMimeMessage(Session session) 
	{
		super(session);
	}
	
	public BaseMimeMessage(Folder arg0, InputStream arg1, int arg2)	throws MessagingException 
	{
		super(arg0, arg1, arg2);
	}

	public BaseMimeMessage(Folder arg0, int arg1) 
	{
		super(arg0, arg1);
	}

	public BaseMimeMessage(Folder arg0, InternetHeaders arg1, byte[] arg2, int arg3) throws MessagingException 
	{
		super(arg0, arg1, arg2, arg3);
	}

	public BaseMimeMessage(MimeMessage arg0) throws MessagingException 
	{
		super(arg0);
	}

	public BaseMimeMessage(Session arg0, InputStream arg1)	throws MessagingException 
	{
		super(arg0, arg1);
	}

	@Override
	protected void updateMessageID() throws MessagingException {
		
		setHeader("Message-ID", "1234567890");
	}

}
