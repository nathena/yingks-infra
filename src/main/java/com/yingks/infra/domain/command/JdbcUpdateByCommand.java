package com.yingks.infra.domain.command;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.yingks.infra.domain.filter.FilterInterface;
import com.yingks.infra.utils.StringUtil;

public class JdbcUpdateByCommand<T> extends JdbcAbstractCommand<T> implements CommandInterface<T> {
	
	public JdbcUpdateByCommand(T entity, FilterInterface filter)
	{
		super(entity, filter);
	}
	
	@Override
	public void executeCommand() {
		try 
		{
			FilterInterface filter = getFilter();
			if( !StringUtil.isEmpty(filter.getDirectFilter(this)) ) 
			{
				repository.commandUpdate(filter.getDirectFilter(this), filter.getNamedParams() );
			}
			else
			{
				StringBuilder namedSql = new StringBuilder("UPDATE `").append(entityClass.tableName).append("`");
				namedSql.append(" SET ");
				
				String sp="";
				String column = null; Method method = null; Object val = null;
				Map<String, Object> paramMap = new HashMap<>();
				Set<String> fieldIter = entityClass.fieldToColumnMap.keySet();
				for(String fieldName : fieldIter)
				{
					if(entityClass.idFields.contains(fieldName))
						continue;
					
					column = entityClass.fieldToColumnMap.get(fieldName);
					method = entityClass.fieldToMethodMap.get(fieldName);
					val = method.invoke(entity);
					
					if(!entityClass.isTransientValue(fieldName,val)) {
						namedSql.append(sp).append("`").append(column).append("` = :"+fieldName);
						paramMap.put(fieldName, val);
						
						sp=" , ";
					}
				}
				
				namedSql.append(" WHERE 1 ");
				if(!StringUtil.isEmpty(filter.getNamedFitler()))
				{
					namedSql.append("AND (").append(filter.getNamedFitler()).append(")");
				}
				
				paramMap.putAll(filter.getNamedParams());
				
				repository.commandUpdate(namedSql.toString(), paramMap);
			}
		} 
		catch(Exception e) 
		{
			throw new CommandException(CommandExceptionMsg.BASE_JDBC_UPDATE,e);
		}
	}
}
