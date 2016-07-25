package com.yingks.infra.domain.command;

import com.yingks.infra.exception.NestedRuntimeException;

public class CommandException extends NestedRuntimeException
{
	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;

	public CommandException(CommandExceptionMsg code) {
		super(code.getCode(),code.getMsg());
	}
	
	public CommandException(CommandExceptionMsg code,Throwable cause) {
		super(code.getCode(),code.getMsg(),cause);
	}
}
  
