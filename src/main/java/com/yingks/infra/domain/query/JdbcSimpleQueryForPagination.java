package com.yingks.infra.domain.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yingks.infra.domain.AggregateConditionInterface;
import com.yingks.infra.domain.Pagination;
import com.yingks.infra.utils.CollectionUtil;

public class JdbcSimpleQueryForPagination<T> extends JdbcAbstractQuery<T> implements QueryForPaginationInterface<T> {

	private List<String> selectedfieldNames;
	private String where = "";
	private Map<String,Object> paramMap = new HashMap<String, Object>();
	
	public JdbcSimpleQueryForPagination(AggregateConditionInterface condition )
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
	public Pagination<T> queryForPagination()
	{
		Pagination<T> pagination = new Pagination<>();
		
		pagination.setTotal(total());
		pagination.setRows(queryForList());
		
		return pagination;
	}

	protected List<T> queryForList() {
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

	protected long total() {
		
		StringBuilder namedSql = new StringBuilder(" select count(1) ");
		namedSql.append(" from `").append(entityClass.tableName).append("` where ").append(where);
		
		return repository.queryForLong(namedSql.toString(), paramMap);
	}
}
