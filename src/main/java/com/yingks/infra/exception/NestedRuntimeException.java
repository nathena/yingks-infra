package com.yingks.infra.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public abstract class NestedRuntimeException extends RuntimeException
{
    /**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;

	private Throwable cause;
    
    private String code;
    private String msg;
    
    public NestedRuntimeException(String code,String msg) 
    {
        super(msg);
        
        this.code = code;
        this.msg = msg;
    }
    
    public NestedRuntimeException(String code ,String msg, Throwable cause) 
    {
        super(msg,cause);
        
        this.code = code;
        this.msg = msg;
        this.cause = cause;
    }
    
    public String getCode() {
		return code;
	}
    
    public String getMsg() {
		return msg;
	}
    
    /**
     * 返回此 throwable 的 cause；或者如果 cause 不存在或未知，则返回 null。
     *
     */
    public Throwable getCause() 
    {
        return (this.cause == this ? null : this.cause);
    }
    
    /**
     *  返回详细消息字符串。
     * 
     */
    public String getMessage() 
    {
        if (getCause() != null) 
        {
            StringBuffer buf = new StringBuffer();
            
            if (super.getMessage() != null) 
            {
                buf.append(super.getMessage()).append("; ");
            }
            
            buf.append(" Nested exception is ").append(getCause());
            return buf.toString();
        }
        else 
        {
            return super.getMessage();
        }
    }
    
    /**
     * 将此 throwable 及其追踪输出到指定的输出流。
     * 
     * @param ps 指定的输出流
     */
    public void printStackTrace(PrintStream ps) 
    {
        if (getCause() == null) 
        {
            super.printStackTrace(ps);
        }
        else 
        {
            ps.println(this);
            ps.print("Caused by: ");
            getCause().printStackTrace(ps);
        }
    }

    /**
     * 将此 throwable 及其追踪输出到指定的 PrintWriter。
     * 
     * @param pw 指定的 PrintWriter
     */
    public void printStackTrace(PrintWriter pw) 
    {
        if (getCause() == null) 
        {
            super.printStackTrace(pw);
        }
        else 
        {
            pw.println(this);
            pw.print("Caused by: ");
            getCause().printStackTrace(pw);
        }
    }
}
