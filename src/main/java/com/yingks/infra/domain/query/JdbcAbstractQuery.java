package com.yingks.infra.domain.query;

import com.yingks.infra.domain.data.AbstractEntityAware;
import com.yingks.infra.domain.data.JdbcGeneralRepository;
import com.yingks.infra.domain.filter.FilterInterface;
import com.yingks.infra.domain.filter.SimpleFilterImpl;

public abstract class JdbcAbstractQuery<T> extends AbstractEntityAware<T>{

	protected JdbcGeneralRepository repository;
	
	private FilterInterface queryConfig;//查询参数
	
	public JdbcAbstractQuery(Class<T> clazz) {
		super(clazz);
	}
	
	public JdbcAbstractQuery(Class<T> clazz, FilterInterface queryConfig) {
		super(clazz);
		this.queryConfig = queryConfig;
	}

	public JdbcAbstractQuery(Class<T> clazz,JdbcGeneralRepository repository)
	{
		this(clazz);
		this.setRepository(repository);
	}

	public JdbcGeneralRepository getRepository() {
		return repository;
	}

	public void setRepository(JdbcGeneralRepository repository) {
		this.repository = repository;
	}
	
	protected FilterInterface getQueryConfig() {
		return queryConfig == null ? SimpleFilterImpl.DEFAULT_QUERY_CONFIG : queryConfig;
	}
}
