package com.yingks.infra.domain.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yingks.infra.domain.filter.AbstractFilter;
import com.yingks.infra.domain.filter.AbstractDirectSQLQuery;
import com.yingks.infra.domain.filter.FilterInterface;
import com.yingks.infra.utils.CollectionUtil;
import com.yingks.infra.utils.StringUtil;

public class JdbcSimpleQueryForList<T> extends JdbcAbstractQuery<T> implements QueryForListInterface<T> {
	private Integer offset;
	private Integer rowSize;
	public JdbcSimpleQueryForList(Class<T> clazz,FilterInterface condition )
	{
		super(clazz, condition);
	}
	
	public JdbcSimpleQueryForList(Class<T> clazz,FilterInterface condition, int offset, int rowSize)
	{
		super(clazz, condition);
		this.offset = offset;
		this.rowSize = rowSize;
	}
	
	public JdbcSimpleQueryForList(Class<T> clazz,FilterInterface condition, int limit)
	{
		super(clazz, condition);
		this.offset = limit;
	}
	
	@Override
	public List<T> queryForList() 
	{
		FilterInterface queryConfig = getQueryConfig();
		if(queryConfig instanceof AbstractDirectSQLQuery) {
			AbstractDirectSQLQuery query = (AbstractDirectSQLQuery)queryConfig;
			return repository.getList(entityClass.clazz, query.sql(), query.filterParams());
		} else if(queryConfig instanceof AbstractFilter) {
			AbstractFilter query = (AbstractFilter)queryConfig;
			
			Map<String, Object> namedParams = query.filterParams();
			StringBuilder namedSql = new StringBuilder(" select ");
			String sp=" ";
			if(!CollectionUtil.isEmpty(query.filterFields()))
				for(String fieldName: query.filterFields()) {
					String columnName = entityClass.fieldToColumnMap.get(fieldName);
					columnName = StringUtil.isEmpty(columnName) ? fieldName : columnName;
					namedSql.append(sp).append("`").append(columnName).append("`");
					sp = ", ";
				}
			else
				namedSql.append(" * ");
			
			namedSql.append(" from `").append(entityClass.tableName).append("` where 1 ");
			
			if(!StringUtil.isEmpty(query.filter()))
				namedSql.append("AND (").append(query.filter()).append(")");
			
			if(!StringUtil.isEmpty(query.order()))
				namedSql.append(" ORDER BY ").append(query.order());
			
			if(offset != null && rowSize != null) {
				namedSql.append("LIMIT :__offset, :__rowSize");
				if(namedParams == null) namedParams = new HashMap<>();
				namedParams.put("__offset", offset);
				namedParams.put("__rowSize", rowSize);
			} else if(offset != null) {
				namedSql.append("LIMIT :__offset");
				if(namedParams == null) namedParams = new HashMap<>();
				namedParams.put("__offset", offset);
			}
			
			return repository.getList(entityClass.clazz, namedSql.toString(), namedParams);
		} else {
			throw new QueryException(QueryExceptionMsg.BASE_JDBC_QUERY_ILLEGAL);
		}
	}
}
