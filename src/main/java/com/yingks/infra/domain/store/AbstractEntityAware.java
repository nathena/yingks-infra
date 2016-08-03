package com.yingks.infra.domain.store;

import com.yingks.infra.domain.store.EntityReflectUtils.ClassSpecification;

public abstract class AbstractEntityAware<T> {

	protected ClassSpecification<T> entityClass;
	
	public AbstractEntityAware(Class<T> clazz)
	{
		this.entityClass = EntityReflectUtils.getEntityClass(clazz);
	}
}
