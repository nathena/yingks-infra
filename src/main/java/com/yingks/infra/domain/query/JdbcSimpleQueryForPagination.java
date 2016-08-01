package com.yingks.infra.domain.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yingks.infra.domain.Pagination;
import com.yingks.infra.domain.filter.FilterInterface;
import com.yingks.infra.utils.CollectionUtil;
import com.yingks.infra.utils.StringUtil;

public class JdbcSimpleQueryForPagination<T> extends JdbcAbstractQuery<T> implements QueryForPaginationInterface<T> {

	private List<String> selectedfieldNames;
	private String where = "";
	private Map<String,Object> paramMap = new HashMap<String, Object>();
	
	public JdbcSimpleQueryForPagination(Class<T> clazz,FilterInterface<T> condition )
	{
		super(clazz);
		
		this.selectedfieldNames = condition.filterFields();
		this.where = condition.filter();
		if(!CollectionUtil.isEmpty(condition.filterParams()))
		{
			paramMap.putAll(condition.filterParams());
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
		
		namedSql.append(" from `").append(entityClass.tableName).append("` where 1 ");
		
		if(!StringUtil.isEmpty(where))
			namedSql.append("AND (").append(where).append(")");
		
		return repository.getList(entityClass.clazz, namedSql.toString(),paramMap);
	}

	protected long total() {
		
		StringBuilder namedSql = new StringBuilder(" select count(1) ");
		namedSql.append(" from `").append(entityClass.tableName).append("` where 1 ");
		
		if(!StringUtil.isEmpty(where))
			namedSql.append("AND (").append(where).append(")");
		
		return repository.queryForLong(namedSql.toString(), paramMap);
	}
}
