package com.yingks.infra.domain.query;

import com.yingks.infra.domain.data.EntityReflectUtils.ClassSpecification;
import com.yingks.infra.domain.data.AbstractEntityAware;
import com.yingks.infra.domain.data.JdbcGeneralRepository;

public abstract class JdbcAbstractQuery<T> extends AbstractEntityAware<T>{

	protected JdbcGeneralRepository repository;
	protected ClassSpecification<T> entityClass;
	
	public JdbcAbstractQuery(Class<T> clazz) {
		super(clazz);
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
}
