package com.yingks.infra.domain.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yingks.infra.domain.filter.FilterInterface;
import com.yingks.infra.utils.CollectionUtil;

public class JdbcSimpleQueryForList<T> extends JdbcAbstractQuery<T> implements QueryForListInterface<T> {

	private List<String> selectedfieldNames;
	private String where = "";
	private Map<String,Object> paramMap = new HashMap<String, Object>();
	
	public JdbcSimpleQueryForList(FilterInterface condition )
	{
		super();
		
		this.selectedfieldNames = condition.fetchFieldNames();
		this.where = condition.filterCondition();
		if(!CollectionUtil.isEmpty(condition.filterConditionNamedParams()))
		{
			paramMap.putAll(condition.filterConditionNamedParams());
		}
	}
	
	@Override
	public List<T> queryForList() 
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
		
		namedSql.append(" from `").append(entityClass.tableName).append("` where ").append(where);
		
		return repository.getList(entityClass.clazz, namedSql.toString(),paramMap);
	}
}
