package com.yingks.infra.domain.command;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.yingks.infra.utils.StringUtil;

public class JdbcUpdateCommand<T> extends JdbcAbstractCommand<T> implements CommandInterface<T> {

	public JdbcUpdateCommand(T entity)
	{
		super(entity);
	}
	
	@Override
	public void executeCommand() {
		try {
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
				
				if(!StringUtil.isEmpty(column) && !entityClass.isTransientValue(fieldName,val)) {
					sb.append(sp).append("`").append(column).append("` = :"+fieldName);
					paramMap.put(fieldName, val);
					
					sp=" , ";
				}
			}
			
			sp=" where ";
			
			fieldIter = entityClass.idFields;
			for(String idField:fieldIter)
			{
				column = entityClass.fieldToColumnMap.get(idField);
				method = entityClass.fieldToMethodMap.get(idField);
				val = method.invoke(entity);
				
				sb.append(sp).append("`").append(column).append("` = :"+idField);
				sp=" and ";
				
				paramMap.put(idField, val);
			}
			
			repository.commandUpdate(sb.toString(),paramMap);
			
		} catch(Exception e) {
			throw new CommandException(CommandExceptionMsg.BASE_JDBC_UPDATE,e);
		}
	}
}
