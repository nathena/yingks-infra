package com.yingks.infra.domain.query;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;

import com.yingks.infra.domain.data.EntitySpecification;
import com.yingks.infra.domain.filter.FilterInterface;
import com.yingks.infra.utils.CollectionUtil;


public class JdbcSimpleQueryForEntity<T> extends JdbcAbstractQuery<T> implements QueryForEntityInterface<T>{
	
	private Object key;
	
	private List<String> selectedfieldNames;
	private String where = "";
	private Map<String,Object> paramMap = new HashMap<String, Object>();
	
	public JdbcSimpleQueryForEntity()
	{
		
	}
	
	public JdbcSimpleQueryForEntity(Object key)
	{
		this();
		this.key = key;
	}
	
	public JdbcSimpleQueryForEntity(FilterInterface condition )
	{
		this();
		this.selectedfieldNames = condition.filterFields();
		this.where = condition.filter();
		if(!CollectionUtil.isEmpty(condition.filterParams()))
		{
			paramMap.putAll(condition.filterParams());
		}
	}
	
	@Override
	public T queryForEntity() 
	{
		try
		{
			StringBuilder namedSql = new StringBuilder(" select ");
			String sp=" ";
			if( !CollectionUtil.isEmpty(selectedfieldNames) )
			{
				for(String fieldName:selectedfieldNames)
				{
					namedSql.append(sp).append("`").append(fieldName).append("`");
					sp = ", ";
				}
			}
			else
			{
				namedSql.append(" * ");
			}
			
			namedSql.append(" from `").append(entityClass.tableName).append("` where  1 ");
			
			sp=" and ";
			
			if( null != key )
			{
				paramMap = new HashMap<>();
				if( entityClass.idFields.size()>1 && EntitySpecification.isEmbeddableAccessor(key)) {
					Map<Class<?>,Set<Field>> accessor = EntitySpecification.getAllAccessor(key.getClass());
					Set<Field> keyField = accessor.get(Column.class);
					
					String name = null;
					Method method = null;
					Object val = null;
					
					for(Field field : keyField)
					{
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
			else
			{
				namedSql.append(sp).append(where);
			}
			
			return repository.getEntity(entityClass.clazz, namedSql.toString(), paramMap);
		}
		catch(Exception e) 
		{
			throw new QueryException(QueryExceptionMsg.BASE_JDBC_QUERY);
		}
	}
}
