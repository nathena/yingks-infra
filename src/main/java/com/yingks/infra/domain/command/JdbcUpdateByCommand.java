package com.yingks.infra.domain.command;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.yingks.infra.domain.filter.FilterInterface;
import com.yingks.infra.utils.CollectionUtil;

public class JdbcUpdateByCommand<T> extends JdbcAbstractCommand<T> implements CommandInterface<T> {

	private String where = "";
	private Map<String,Object> paramMap = new HashMap<String, Object>();
	
	public JdbcUpdateByCommand(T entity, FilterInterface<T> filter)
	{
		super(entity);
		this.where = filter.filter();
		
		if(!CollectionUtil.isEmpty(filter.filterParams()))
		{
			paramMap.putAll(filter.filterParams());
		}
	}
	
	@Override
	public void executeCommand() {
		try 
		{
			Map<String,Object> paramMap = new HashMap<String, Object>();
			
			StringBuilder sb = new StringBuilder(" update `");
			sb.append(entityClass.tableName);
			sb.append("` set ");
			String sp="";
			
			String column = null;
			Method method = null;
			Object val = null;
			Set<String> fieldIter = entityClass.idFields;
			for(String fieldName:fieldIter)
			{
				if(entityClass.idFields.contains(fieldName)) {
					continue;
				}
				
				column = entityClass.fieldToColumnMap.get(fieldName);
				method = entityClass.fieldToMethodMap.get(fieldName);
				val = method.invoke(entity);
				
				if(!entityClass.isTransientValue(fieldName,val)) {
					sb.append(sp).append("`").append(column).append("` = :"+fieldName);
					paramMap.put(fieldName, val);
					
					sp=" , ";
				}
			}
			
			sp=" where ";
			sb.append(sp).append(where);
			
			repository.commandUpdate(sb.toString(),paramMap);
		} 
		catch(Exception e) 
		{
			throw new CommandException(CommandExceptionMsg.BASE_JDBC_UPDATE,e);
		}
	}
}
