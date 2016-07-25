package com.yingks.infra.domain.command;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JdbcStoreCommand<T> extends JdbcAbstractCommand<T> implements CommandInterface<T> {

	private JdbcInsertCommand<T> insert;
	private JdbcUpdateCommand<T> update;
	
	public JdbcStoreCommand(T entity)
	{
		this.insert = new JdbcInsertCommand<T>(entity);
		this.update = new JdbcUpdateCommand<T>(entity);
	}
	
	@Override
	public void executeCommand() {
		try 
		{
			if(ifExist()) {
				update.executeCommand();;
			}
			else {
				insert.executeCommand();;
			}
		}
		catch(Exception e) {
			throw new CommandException(CommandExceptionMsg.BASE_JDBC_UPDATE,e);
		}
	}
	
	protected boolean ifExist() 
	{
		boolean isExisted = false;
		try 
		{
			Map<String,Object> paramMap = new HashMap<String, Object>();
			
			StringBuilder sb = new StringBuilder(" select 1 from `");
			sb.append(entityClass.tableName);
			sb.append("` where 1 ");
			String sp=" and ";
			
			String column = null;
			Method method = null;
			Object val = null;
			Set<String> fieldIter = entityClass.idFields;
			for(String fieldName:fieldIter)
			{
				column = entityClass.fieldToColumnMap.get(fieldName);
				method = entityClass.fieldToMethodMap.get(fieldName);
				val = method.invoke(entity);
				
				sb.append(sp).append("`").append(column).append("` = :"+fieldName);
				sp=" and ";
				
				paramMap.put(fieldName, val);
			}
			sb.append(" LIMIT 1");
			
			isExisted = repository.queryForInt(sb.toString(), paramMap) > 0;
		} 
		catch(Exception e) 
		{
			throw new CommandException(CommandExceptionMsg.BASE_JDBC_QUERY,e);
		}
		
		return isExisted;
	}
}
