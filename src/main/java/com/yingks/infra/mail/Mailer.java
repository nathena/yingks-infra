package com.yingks.infra.mail;

import java.io.File;
import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.StringUtils;

import com.sun.mail.util.MailSSLSocketFactory;
import com.yingks.infra.utils.DateTimeUtil;
import com.yingks.infra.utils.LogHelper;

public class Mailer {

	private final static String smtp_transprot = "smtp";
	
	private String host;
	private int port =  25;
	private String mailEncryptionType = MailEncryptionTypes.Default.getName();
	private boolean auth = false;
	private String account;
	private String password;
	private String from;
	private String fromName = "" ;
	
	private String contentType = "text/html; charset=utf-8";
	
	public Mailer()
	{
	}
	
	public Mailer(String host, int port,String account,String password)
	{
		this.host = host;
		this.port = port;
		this.account = account;
		this.password = password;
	}
	
	public Mailer(String host, int port,String account,String password,boolean auth)
	{
		this.host = host;
		this.port = port;
		this.account = account;
		this.password = password;
		this.auth = auth;
	}
	
	private MimeBodyPart createAttachment(String fileName) throws Exception 
	{  
        MimeBodyPart attachmentPart = new MimeBodyPart();  
        FileDataSource fds = new FileDataSource(fileName);  
        attachmentPart.setDataHandler(new DataHandler(fds));  
        attachmentPart.setFileName(fds.getName());  
        return attachmentPart;  
    }
	
	private MimeBodyPart createContent(String contentType,String body, String fileName) throws Exception 
	{  
        // 用于保存最终正文部分  
        MimeBodyPart contentBody = new MimeBodyPart();  
        // 用于组合文本和图片，"related"型的MimeMultipart对象  
        MimeMultipart contentMulti = new MimeMultipart("related");  
 
        // 正文的文本部分  
        MimeBodyPart textBody = new MimeBodyPart();  
        textBody.setContent(body, contentType);  
        contentMulti.addBodyPart(textBody);  
 
        if(!StringUtils.isEmpty(fileName) && new File(fileName).exists())
        {
        	// 正文的图片部分  
            MimeBodyPart jpgBody = new MimeBodyPart();  
            FileDataSource fds = new FileDataSource(fileName);
            jpgBody.setDataHandler(new DataHandler(fds));  
            jpgBody.setContentID("logo_jpg");  
            contentMulti.addBodyPart(jpgBody); 
        }
 
        // 将上面"related"型的 MimeMultipart 对象作为邮件的正文  
        contentBody.setContent(contentMulti);
        
        return contentBody;  
    }
	
	public void sendMail(String toAddress,String title,String body,String relatedFileName,String[] attachments)
	{
		Session session = null;
		Transport transport = null;
		try
		{
			session = getSession();
			transport = session.getTransport(smtp_transprot);
			transport.connect();
			
			Message message = new BaseMimeMessage(session);
			if(!StringUtils.isEmpty(from))
			{
				if(!StringUtils.isEmpty(fromName))
				{
					message.setFrom(new InternetAddress(from,fromName));
				}
				else
				{
					message.setFrom(new InternetAddress(from));
				}
			}
			
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress.replace(";", ",")));
			message.setSubject(title);
			message.setSentDate(DateTimeUtil.getDate());
			
			MimeMultipart allPart = new MimeMultipart("mixed");
			
			MimeBodyPart content = createContent(this.contentType,body, relatedFileName);
			allPart.addBodyPart(content);
			
			if(null!=attachments)
			{
				for(String attachment : attachments)
				{
					allPart.addBodyPart(createAttachment(attachment));
				}
			}
			message.setContent(allPart);
			message.saveChanges();
			
			transport.sendMessage(message, message.getAllRecipients());
			
			LogHelper.debug(" === "+toAddress+" == 发送成功 ");
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			if(transport!=null)
			{
				try
				{
					transport.close();
					transport = null;
				}
				catch(Exception e)
				{
					
				}
			}
		}
	}
	
	public boolean isConnected()
	{
		Session session = null;
		Transport transport = null;
		try
		{
			session = getSession();
			transport = session.getTransport(smtp_transprot);
			transport.connect();
			
			return transport.isConnected();
		}
		catch(Exception e)
		{
			LogHelper.error(e.getMessage(), e);
			return false;
		}
		finally
		{
			if(transport!=null)
			{
				try
				{
					transport.close();
					transport = null;
				}
				catch(Exception e)
				{
					
				}
			}
		}
	}
	
	public String getHost() {
		return host;
	}


	public void setHost(String host) {
		this.host = host;
	}


	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}


	public String getMailEncryptionType() {
		return mailEncryptionType;
	}


	public void setMailEncryptionType(String mailEncryptionType) {
		this.mailEncryptionType = mailEncryptionType;
	}


	public boolean getAuth() {
		return auth;
	}


	public void setAuth(boolean auth) {
		this.auth = auth;
	}


	public String getAccount() {
		return account;
	}


	public void setAccount(String account) {
		this.account = account;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getFrom() {
		return from;
	}


	public void setFrom(String from) {
		this.from = from;
	}


	public String getFromName() {
		return fromName;
	}


	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	private Session getSession() throws GeneralSecurityException
	{
		Properties pro = new Properties();
		pro.put("mail.smtp.host", this.host);
		pro.put("mail.smtp.port", this.port);
		pro.put("mail.smtp.debug", true);
		pro.put("mail.smtp.auth", String.valueOf(this.auth));
		
		if( MailEncryptionTypes.TLS.getName().equalsIgnoreCase(this.mailEncryptionType))
		{
			pro.put("mail.smtp.starttls.enable", "true");
		}
		else if( MailEncryptionTypes.SSL.getName().equalsIgnoreCase(this.mailEncryptionType))
		{
			pro.put("mail.smtp.ssl.enable", "true");
			//pro.put("mail.smtp.starttls.enable", "true");
			pro.put("mail.smtp.socketFactory.port", this.port);
			pro.put("mail.smtp.socketFactory.fallback", true);
//			pro.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			pro.put("mail.smtp.socketFactory.class", new MailSSLSocketFactory());
		}
		
		Session session = null;
		if( MailEncryptionTypes.TLS.getName().equalsIgnoreCase(this.mailEncryptionType))
		{
			session = Session.getInstance(pro);
		}
		else if( MailEncryptionTypes.SSL.getName().equalsIgnoreCase(this.mailEncryptionType))
		{
			session = Session.getInstance(pro, new BaseJavaMailAuthenticator(this.account,this.password));
		}
		else if( this.auth )
		{
			session = Session.getDefaultInstance(pro, new BaseJavaMailAuthenticator(this.account,this.password));
		}
		else
		{
			session = Session.getDefaultInstance(pro);
		}
		
		return session;
	}
	
	public static void main(String[] arg)
	{
		System.out.println("1111");
	}
}
