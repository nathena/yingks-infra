package com.yingks.infra.domain;

import com.yingks.infra.domain.store.JdbcGeneralRepository;

public final class DbKit extends BaseJdbcAggregateImpl {

	private JdbcGeneralRepository repository;
	
	public DbKit(JdbcGeneralRepository repository)
	{
		this.repository = repository;
	}
	

	@Override
	public JdbcGeneralRepository getRepository() {
		return this.repository;
	}
	
}
