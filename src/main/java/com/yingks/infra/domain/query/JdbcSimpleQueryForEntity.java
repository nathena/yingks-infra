package com.yingks.infra.domain.query;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;

import com.yingks.infra.domain.filter.FilterInterface;
import com.yingks.infra.domain.store.EntitySpecification;
import com.yingks.infra.utils.CollectionUtil;
import com.yingks.infra.utils.StringUtil;


public class JdbcSimpleQueryForEntity<T> extends JdbcAbstractQuery<T> implements QueryForEntityInterface<T>{
	
	private Object key;
	
	public JdbcSimpleQueryForEntity(Class<T> clazz,Object key)
	{
		super(clazz);
		this.key = key;
	}
	
	public JdbcSimpleQueryForEntity(Class<T> clazz,FilterInterface filter )
	{
		super(clazz, filter);
	}
	
	@Override
	public T queryForEntity() 
	{
		try
		{	
			FilterInterface filter = getFilter();
			if( !StringUtil.isEmpty(filter.getDirectFilter()) ) 
			{
				return repository.getEntity(entityClass.clazz, filter.getDirectFilter(), filter.getNamedParams() );
			}
			else
			{
				StringBuilder namedSql = new StringBuilder(" select ");
				String sp=" ";
				if( !CollectionUtil.isEmpty(filter.getFields()) ) 
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
				
				namedSql.append(" from `").append(entityClass.tableName).append("` where  1 ");
				
				sp=" and ";
				
				Map<String, Object> paramMap = new HashMap<>();
				if( null != key ) 
				{
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
				} 
				else if( !StringUtil.isEmpty( filter.getNamedFitler()) )
				{
					namedSql.append(sp).append("(").append(filter.getNamedFitler() ).append(")");
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
				
				namedSql.append(" LIMIT 1");
				
				paramMap.putAll(filter.getNamedParams());
				
				return repository.getEntity(entityClass.clazz, namedSql.toString(), paramMap);
			}
		}
		catch(Exception e) 
		{
			throw new QueryException(QueryExceptionMsg.BASE_JDBC_QUERY_ILLEGAL);
		}
	}
}
