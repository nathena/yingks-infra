package com.yingks.infra.domain.query;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;

import com.yingks.infra.domain.data.EntitySpecification;
import com.yingks.infra.domain.data.JdbcGeneralRepositoryException;
import com.yingks.infra.domain.filter.AbstractFilter;
import com.yingks.infra.domain.filter.AbstractDirectSQLQuery;
import com.yingks.infra.domain.filter.FilterInterface;
import com.yingks.infra.utils.CollectionUtil;
import com.yingks.infra.utils.StringUtil;


public class JdbcSimpleQueryForEntity<T> extends JdbcAbstractQuery<T> implements QueryForEntityInterface<T>{
	
	private Object key;
	
	public JdbcSimpleQueryForEntity(Class<T> clazz,Object key)
	{
		super(clazz);
		this.key = key;
	}
	
	public JdbcSimpleQueryForEntity(Class<T> clazz,FilterInterface condition )
	{
		super(clazz, condition);
	}
	
	@Override
	public T queryForEntity() 
	{
		try
		{	
			FilterInterface queryConfig = getQueryConfig();
			if(queryConfig instanceof AbstractDirectSQLQuery) {
				AbstractDirectSQLQuery query = (AbstractDirectSQLQuery)queryConfig;
				return repository.getEntity(entityClass.clazz, query.sql(), query.filterParams());
			} else if(queryConfig instanceof AbstractFilter) {
				AbstractFilter query = (AbstractFilter)queryConfig;
				
				StringBuilder namedSql = new StringBuilder(" select ");
				String sp=" ";
				if( !CollectionUtil.isEmpty(query.filterFields()) ) {
					for(String fieldName: query.filterFields()) {
						String columnName = entityClass.fieldToColumnMap.get(fieldName);
						columnName = StringUtil.isEmpty(columnName) ? fieldName : columnName;
						namedSql.append(sp).append("`").append(columnName).append("`");
						sp = ", ";
					}
				} else {
					namedSql.append(" * ");
				}
				
				namedSql.append(" from `").append(entityClass.tableName).append("` where  1 ");
				
				sp=" and ";
				
				Map<String, Object> paramMap = query.filterParams();
				if( null != key ) {
					paramMap = paramMap == null ? new HashMap<>() : paramMap;
					if( entityClass.idFields.size()>1 && EntitySpecification.isEmbeddableAccessor(key)) {
						Map<Class<?>,Set<Field>> accessor = EntitySpecification.getAllAccessor(key.getClass());
						Set<Field> keyField = accessor.get(Column.class);
						
						String name = null;
						Method method = null;
						Object val = null;
						
						for(Field field : keyField) {
							name = EntitySpecification.getName(field);
							method = EntitySpecification.getReadMethod(field);
							val = method.invoke(key);
							
							namedSql.append(sp).append("`").append(name).append("` = :"+name);
							sp=" and ";
							
							paramMap.put(name, val);
						}
						
					} else {
						String fieldName = entityClass.idFields.iterator().next();
						String column = entityClass.fieldToColumnMap.get(fieldName);
						
						namedSql.append(sp).append("`").append(column).append("` = :"+fieldName);
						paramMap.put(fieldName, key);
					}
				} else {
					namedSql.append(sp).append("(").append(query.filter()).append(")");
				}
				
				if(!StringUtil.isEmpty(query.order()))
					namedSql.append(" ORDER BY ").append(query.order());
				
				namedSql.append(" LIMIT 1");
				
				return repository.getEntity(entityClass.clazz, namedSql.toString(), paramMap);
			} else {
				throw new JdbcGeneralRepositoryException("无法执行该查询请检查代码");
			}
		}
		catch(Exception e) 
		{
			throw new QueryException(QueryExceptionMsg.BASE_JDBC_QUERY_ILLEGAL);
		}
	}
}
