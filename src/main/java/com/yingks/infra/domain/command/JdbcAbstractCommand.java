package com.yingks.infra.domain.command;

import java.lang.reflect.ParameterizedType;

import com.yingks.infra.domain.data.EntityClass;
import com.yingks.infra.domain.data.EntityClass.ClassSpecification;
import com.yingks.infra.domain.data.JdbcGeneralRepository;

public abstract class JdbcAbstractCommand<T> implements CommandInterface<T> {

	protected JdbcGeneralRepository repository;
	protected T entity;
	
	protected ClassSpecification<T> entityClass;
	
	public JdbcAbstractCommand()
	{
		@SuppressWarnings("unchecked")
		Class<T> claszz = (Class<T>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		this.entityClass = EntityClass.getEntityClass(claszz);
	}
	
	public JdbcAbstractCommand(T entity)
	{
		this();
		this.setEntity(entity);
	}
	
	public JdbcAbstractCommand(JdbcGeneralRepository repository, T entity)
	{
		this();
		this.setRepository(repository);
		this.setEntity(entity);
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
