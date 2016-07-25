package com.yingks.infra.domain.query;

import com.yingks.infra.exception.NestedRuntimeException;

public class QueryException extends NestedRuntimeException
{
	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;

	public QueryException(QueryExceptionMsg code) {
		super(code.getCode(),code.getMsg());
	}
	
	public QueryException(QueryExceptionMsg code,Throwable cause) {
		super(code.getCode(),code.getMsg(),cause);
	}
}
  
