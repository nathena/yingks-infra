package com.yingks.infra.domain.command;

import com.yingks.infra.domain.filter.FilterInterface;
import com.yingks.infra.domain.filter.SimpleFilterImpl;
import com.yingks.infra.domain.store.AbstractEntityAware;
import com.yingks.infra.domain.store.JdbcGeneralRepository;

public abstract class JdbcAbstractCommand<T> extends AbstractEntityAware<T> implements CommandInterface<T> {

	protected JdbcGeneralRepository repository;
	protected T entity;
	private FilterInterface filter;//查询参数
	
	public JdbcAbstractCommand(Class<T> clazz) {
		super(clazz);
	}
	
	public JdbcAbstractCommand(Class<T> clazz, FilterInterface filter) {
		super(clazz);
		this.filter = filter;
	}

	@SuppressWarnings("unchecked")
	public JdbcAbstractCommand(T entity)
	{
		this((Class<T>)entity.getClass());
		this.setEntity(entity);
	}
	
	@SuppressWarnings("unchecked")
	public JdbcAbstractCommand(T entity, FilterInterface filter)
	{
		this((Class<T>)entity.getClass());
		
		this.setEntity(entity);
		this.filter = filter;
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
	
	protected FilterInterface getFilter() {
		return filter == null ? SimpleFilterImpl.DEFAULT_FILTER : filter;
	}
}
