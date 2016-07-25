package com.yingks.infra.domain.command;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JdbcRemoveCommand<T> extends JdbcAbstractCommand<T> implements CommandInterface<T> {

	public JdbcRemoveCommand(T entity)
	{
		super(entity);
	}
	
	@Override
	public void executeCommand() {
		try {
			Map<String,Object> paramMap = new HashMap<String, Object>();
			
			StringBuilder sb = new StringBuilder(" delete from `");
			sb.append(entityClass.tableName);
			sb.append("` where 1 ");
			String sp=" and ";
			
			Object val = null;
			String column = null;
			Method method = null;
			Set<String> fieldIter = entityClass.idFields;
			for(String idFieldName : fieldIter)
			{
				column = entityClass.fieldToColumnMap.get(idFieldName);
				method = entityClass.fieldToMethodMap.get(idFieldName);
				
				val = method.invoke(entity);
				
				sb.append(sp).append("`").append(column).append("` = :"+idFieldName);
				sp=" and ";
				
				paramMap.put(idFieldName, val);
			}
			repository.commandUpdate(sb.toString(), paramMap);
			
		} catch(Exception e) {
			throw new CommandException(CommandExceptionMsg.BASE_JDBC_DELETE,e);
		}
	}
	
}
