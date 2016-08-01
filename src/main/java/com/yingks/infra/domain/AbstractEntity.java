package com.yingks.infra.domain;

import com.yingks.infra.domain.data.EntityClass;
import com.yingks.infra.domain.data.EntityClass.ClassSpecification;

public abstract class AbstractEntity<T> {

	protected ClassSpecification<T> entityClass;
	
	public AbstractEntity(Class<T> clazz)
	{
		this.entityClass = EntityClass.getEntityClass(clazz);
	}
}
