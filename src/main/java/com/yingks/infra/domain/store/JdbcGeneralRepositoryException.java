package com.yingks.infra.domain.store;

import org.springframework.dao.DataAccessException;

public class JdbcGeneralRepositoryException extends DataAccessException {

	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 * @param cause
	 */
	public JdbcGeneralRepositoryException(String msg, Throwable cause)
	{
		super(msg,cause);
	}
	
	/**
     * 构造一个带指定详细消息的
     * 
     * @param msg 详细消息
     */
	public JdbcGeneralRepositoryException(String msg)
	{
		super(msg);
	}
}
