package com.yingks.infra.domain.query;

public enum QueryExceptionMsg {

	COMMAND_ARG_MUST_ENTITY("infra.ex.jdbc.entity","执行domain命令，参数错误。必须包含一个实体"),
	
	BASE_JDBC_CREATE("infra.ex.jdbc.create","JDBC创建对象时执行sql异常"),
	BASE_JDBC_UPDATE("infra.ex.jdbc.update","JDBC更新对象时执行sql异常"),
	BASE_JDBC_DELETE("infra.ex.jdbc.delete","JDBC删除对象时执行sql异常"),
	BASE_JDBC_QUERY("infra.ex.jdbc.query","JDBC查询对象时执行sql异常"),
	BASE_JDBC_QUERY_ILLEGAL("infra.ex.jdbc.query.illegal","无法执行的查询请求");
	
	private String code;
	private String msg;
	
	private QueryExceptionMsg(String code,String msg)
	{
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	
}
