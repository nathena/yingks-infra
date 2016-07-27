package com.yingks.infra.domain.query;

import java.util.HashMap;
import java.util.Map;

import com.yingks.infra.domain.filter.FilterInterface;
import com.yingks.infra.utils.CollectionUtil;

public class JdbcSimpleQueryForLong<T> extends JdbcAbstractQuery<T> implements QueryForLongInterface<T> {

	private String where = "";
	private Map<String,Object> paramMap = new HashMap<String, Object>();
	
	public JdbcSimpleQueryForLong(FilterInterface condition )
	{
		super();
		
		this.where = condition.filter();
		if(!CollectionUtil.isEmpty(condition.filterParams()))
		{
			paramMap.putAll(condition.filterParams());
		}
	}
	
	@Override
	public long queryForLong() 
	{
		StringBuilder namedSql = new StringBuilder(" select count(1) ");
		namedSql.append(" from `").append(entityClass.tableName).append("` where ").append(where);
		
		return repository.queryForLong(namedSql.toString(), paramMap);
	}
}
