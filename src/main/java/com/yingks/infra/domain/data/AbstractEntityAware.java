package com.yingks.infra.domain.data;

import com.yingks.infra.domain.data.EntityReflectUtils.ClassSpecification;

public abstract class AbstractEntityAware<T> {

	protected ClassSpecification<T> entityClass;
	
	public AbstractEntityAware(Class<T> clazz)
	{
		this.entityClass = EntityReflectUtils.getEntityClass(clazz);
	}
}
