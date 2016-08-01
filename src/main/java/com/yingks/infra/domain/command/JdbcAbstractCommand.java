package com.yingks.infra.domain.command;

import com.yingks.infra.domain.data.AbstractEntityAware;
import com.yingks.infra.domain.data.JdbcGeneralRepository;
import com.yingks.infra.domain.filter.FilterInterface;
import com.yingks.infra.domain.filter.SimpleFilterImpl;

public abstract class JdbcAbstractCommand<T> extends AbstractEntityAware<T> implements CommandInterface<T> {

	protected JdbcGeneralRepository repository;
	protected T entity;
	private FilterInterface queryConfig;//查询参数
	
	public JdbcAbstractCommand(Class<T> clazz) {
		super(clazz);
	}
	
	public JdbcAbstractCommand(Class<T> clazz, FilterInterface queryConfig) {
		super(clazz);
		this.queryConfig = queryConfig;
	}

	@SuppressWarnings("unchecked")
	public JdbcAbstractCommand(T entity)
	{
		this((Class<T>)entity.getClass());
		this.setEntity(entity);
	}
	
	@SuppressWarnings("unchecked")
	public JdbcAbstractCommand(T entity, FilterInterface queryConfig)
	{
		this((Class<T>)entity.getClass());
		this.setEntity(entity);
		this.queryConfig = queryConfig;
	}
	
	public JdbcAbstractCommand(JdbcGeneralRepository repository, T entity)
	{
		this(entity);
		this.setRepository(repository);
	}
	
	public JdbcGeneralRepository getRepository() {
		return repository;
	}

	public void setRepository(JdbcGeneralRepository repository) {
		this.repository = repository;
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}
	
	protected FilterInterface getQueryConfig() {
		return queryConfig == null ? SimpleFilterImpl.DEFAULT_QUERY_CONFIG : queryConfig;
	}
}
