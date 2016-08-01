package com.yingks.infra.domain.query;

import com.yingks.infra.domain.filter.AbstractFilter;
import com.yingks.infra.domain.filter.AbstractDirectSQLQuery;
import com.yingks.infra.domain.filter.FilterInterface;
import com.yingks.infra.utils.StringUtil;

public class JdbcSimpleQueryForLong<T> extends JdbcAbstractQuery<T> implements QueryForLongInterface<T> {
	
	public JdbcSimpleQueryForLong(Class<T> clazz,FilterInterface condition )
	{
		super(clazz, condition);
	}
	
	@Override
	public long queryForLong() 
	{
		FilterInterface queryConfig = getQueryConfig();
		if(queryConfig instanceof AbstractDirectSQLQuery) {
			AbstractDirectSQLQuery query = (AbstractDirectSQLQuery)queryConfig;
			return repository.queryForLong(query.sql(), query.filterParams());
		} else if(queryConfig instanceof AbstractFilter) {
			AbstractFilter query = (AbstractFilter)queryConfig;
			
			StringBuilder namedSql = new StringBuilder(" select count(1) ");
			namedSql.append(" from `").append(entityClass.tableName).append("` where 1 ");
			
			if(!StringUtil.isEmpty(query.filter()))
				namedSql.append("AND (").append(query.filter()).append(")");
			
			return repository.queryForLong(namedSql.toString(), query.filterParams());
		} else {
			throw new QueryException(QueryExceptionMsg.BASE_JDBC_QUERY_ILLEGAL);
		}
	}
}
