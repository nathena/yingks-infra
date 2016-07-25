package com.yingks.infra.domain.query;

import java.lang.reflect.ParameterizedType;

import com.yingks.infra.domain.data.EntityClass;
import com.yingks.infra.domain.data.EntityClass.ClassSpecification;
import com.yingks.infra.domain.data.JdbcGeneralRepository;

public abstract class JdbcAbstractQuery<T> {

	protected JdbcGeneralRepository repository;
	protected ClassSpecification<T> entityClass;
	
	public JdbcAbstractQuery()
	{
		@SuppressWarnings("unchecked")
		Class<T> claszz = (Class<T>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		this.entityClass = EntityClass.getEntityClass(claszz);
	}
	
	public JdbcAbstractQuery(JdbcGeneralRepository repository)
	{
		this();
		this.setRepository(repository);
	}

	public JdbcGeneralRepository getRepository() {
		return repository;
	}

	public void setRepository(JdbcGeneralRepository repository) {
		this.repository = repository;
	}
}
