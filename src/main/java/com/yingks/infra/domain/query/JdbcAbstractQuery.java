package com.yingks.infra.domain.query;

import com.yingks.infra.domain.data.AbstractEntityAware;
import com.yingks.infra.domain.data.JdbcGeneralRepository;
import com.yingks.infra.domain.filter.FilterInterface;
import com.yingks.infra.domain.filter.SimpleFilterImpl;

public abstract class JdbcAbstractQuery<T> extends AbstractEntityAware<T>{

	protected JdbcGeneralRepository repository;
	
	private FilterInterface filter;//查询参数
	
	public JdbcAbstractQuery(Class<T> clazz) {
		super(clazz);
	}
	
	public JdbcAbstractQuery(Class<T> clazz, FilterInterface filter) {
		super(clazz);
		this.filter = filter;
	}

	public JdbcGeneralRepository getRepository() {
		return repository;
	}

	public void setRepository(JdbcGeneralRepository repository) {
		this.repository = repository;
	}
	
	protected FilterInterface getFilter() {
		return filter == null ? SimpleFilterImpl.DEFAULT_FILTER : filter;
	}
}
