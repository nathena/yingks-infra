package com.yingks.infra.domain.query;

import com.yingks.infra.domain.filter.FilterInterface;
import com.yingks.infra.utils.StringUtil;

public class JdbcSimpleQueryForLong<T> extends JdbcAbstractQuery<T> implements QueryForLongInterface<T> {
	
	public JdbcSimpleQueryForLong(Class<T> clazz,FilterInterface filter )
	{
		super(clazz, filter);
	}
	
	@Override
	public long queryForLong() 
	{
		try
		{
			FilterInterface filter = getFilter();
			if(!StringUtil.isEmpty(filter.getDirectFilter()) )
			{
				return repository.queryForLong(filter.getDirectFilter(), filter.getNamedParams());
			}
			else
			{
				StringBuilder namedSql = new StringBuilder(" select count(1) ");
				namedSql.append(" from `").append(entityClass.tableName).append("` where 1 ");
				
				if(!StringUtil.isEmpty( filter.getNamedFitler() ))
				{
					namedSql.append("AND (").append(filter.getNamedFitler()).append(")");
				}
				
				return repository.queryForLong(namedSql.toString(), filter.getNamedParams() );
			}
		}
		catch(Exception e) 
		{
			throw new QueryException(QueryExceptionMsg.BASE_JDBC_QUERY_ILLEGAL);
		}
	}
}
