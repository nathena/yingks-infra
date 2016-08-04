package com.yingks.infra.domain.query;

import com.yingks.infra.domain.Pagination;
import com.yingks.infra.domain.filter.FilterInterface;

public class JdbcSimpleQueryForPagination<T> extends JdbcAbstractQuery<T> implements QueryForPaginationInterface<T> {

	private JdbcSimpleQueryForList<T> jdbcSimpleQueryForList;
	private JdbcSimpleQueryForLong<T> jdbcSimpleQueryForLong;
	
	public JdbcSimpleQueryForPagination(Class<T> clazz,FilterInterface filter )
	{
		super(clazz, filter);
		
		jdbcSimpleQueryForList = new JdbcSimpleQueryForList<>(clazz, filter);
		jdbcSimpleQueryForLong = new JdbcSimpleQueryForLong<>(clazz, filter);
	}
	
	
	@Override
	public Pagination<T> queryForPagination()
	{
		jdbcSimpleQueryForList.setRepository(getRepository());
		jdbcSimpleQueryForLong.setRepository(getRepository());
		
		Pagination<T> pagination = new Pagination<>();
		
		pagination.setTotal(jdbcSimpleQueryForLong.queryForLong());
		pagination.setRows(jdbcSimpleQueryForList.queryForList());
		pagination.setPage((int) getFilter().getPage());
		pagination.setPageSize((int) getFilter().getOffset());
		return pagination;
	}
}
