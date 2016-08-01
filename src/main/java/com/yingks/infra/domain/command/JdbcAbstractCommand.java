package com.yingks.infra.domain.command;

import com.yingks.infra.domain.data.AbstractEntityAware;
import com.yingks.infra.domain.data.JdbcGeneralRepository;

public abstract class JdbcAbstractCommand<T> extends AbstractEntityAware<T> implements CommandInterface<T> {

	protected JdbcGeneralRepository repository;
	protected T entity;
	
	public JdbcAbstractCommand(Class<T> clazz) {
		super(clazz);
	}

	@SuppressWarnings("unchecked")
	public JdbcAbstractCommand(T entity)
	{
		this((Class<T>)entity.getClass());
		this.setEntity(entity);
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
}
