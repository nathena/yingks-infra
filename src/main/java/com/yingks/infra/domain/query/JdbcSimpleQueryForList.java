package com.yingks.infra.domain.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yingks.infra.domain.filter.FilterInterface;
import com.yingks.infra.utils.CollectionUtil;
import com.yingks.infra.utils.StringUtil;

public class JdbcSimpleQueryForList<T> extends JdbcAbstractQuery<T> implements QueryForListInterface<T> {
	
	public JdbcSimpleQueryForList(Class<T> clazz,FilterInterface filter )
	{
		super(clazz, filter);
	}
	
	@Override
	public List<T> queryForList() 
	{
		try
		{
			FilterInterface filter = getFilter();
			if(!StringUtil.isEmpty(filter.getDirectFilter()) )
			{
				return repository.getList(entityClass.clazz, filter.getDirectFilter(), filter.getNamedParams());
			}
			else
			{
				Map<String, Object> namedParams = new HashMap<>();
				StringBuilder namedSql = new StringBuilder(" select ");
				String sp=" ";
				if(!CollectionUtil.isEmpty(filter.getFields()))
				{
					for(String fieldName: filter.getFields()) 
					{
						String columnName = entityClass.fieldToColumnMap.get(fieldName);
						columnName = StringUtil.isEmpty(columnName) ? fieldName : columnName;
						namedSql.append(sp).append("`").append(columnName).append("`");
						sp = ", ";
					}
				}
				else
				{
					namedSql.append(" * ");
				}
				
				namedSql.append(" from `").append(entityClass.tableName).append("` where 1 ");
				
				if(!StringUtil.isEmpty(filter.getNamedFitler()))
				{
					namedSql.append("AND (").append(filter.getNamedFitler()).append(")");
				}
				
				if(!StringUtil.isEmpty(filter.getGroup()))
				{
					namedSql.append(" GROUP BY ").append(filter.getGroup());
				}
				
				if(!StringUtil.isEmpty(filter.getHaving()))
				{
					namedSql.append(" HAVING ").append(filter.getHaving());
				}
				
				if(!StringUtil.isEmpty(filter.getOrder()))
				{
					namedSql.append(" ORDER BY ").append(filter.getOrder());
				}
				
				if( filter.getLimit()>0 && filter.getOffset()>0 )
				{
					namedSql.append("LIMIT :__offset, :__rowSize");
					
					namedParams.put("__offset", filter.getLimit());
					namedParams.put("__rowSize", filter.getOffset() );
				}
				else if( filter.getLimit()>0 )
				{
					namedSql.append("LIMIT :__offset");
					namedParams.put("__offset", filter.getLimit());
				}
				
				namedParams.putAll(filter.getNamedParams());
				
				return repository.getList(entityClass.clazz, namedSql.toString(), namedParams);
			}
		}
		catch(Exception e) 
		{
			throw new QueryException(QueryExceptionMsg.BASE_JDBC_QUERY_ILLEGAL);
		}
	}
}
