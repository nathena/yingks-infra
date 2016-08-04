package com.yingks.infra.exception;

public class NestedRuntimeException extends RuntimeException
{
    /**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;

    private String code;
    
    public NestedRuntimeException()
    {
    	super();
    }
    
    public NestedRuntimeException(String msg)
    {
    	super(msg);
    }
    
    public NestedRuntimeException(String code,String msg) 
    {
        super(msg);
        this.code = code;
    }
    
    public NestedRuntimeException(String msg,Throwable cause)
    {
    	 super(msg, cause);
    }
    
    public NestedRuntimeException(String code ,String msg, Throwable cause) 
    {
        super(msg,cause);
        this.code = code;
    }
    
    public String getCode() {
		return code;
	}
}
