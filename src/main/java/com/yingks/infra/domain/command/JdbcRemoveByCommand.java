package com.yingks.infra.domain.command;

import java.util.HashMap;
import java.util.Map;

import com.yingks.infra.domain.AggregateConditionInterface;
import com.yingks.infra.utils.CollectionUtil;

public class JdbcRemoveByCommand<T> extends JdbcAbstractCommand<T> implements CommandInterface<T> {

	private String where = "";
	private Map<String,Object> paramMap = new HashMap<String, Object>();
	
	public JdbcRemoveByCommand(AggregateConditionInterface condition)
	{
		super();
		
		this.where = condition.filterCondition();
		
		if(!CollectionUtil.isEmpty(condition.filterConditionNamedParams()))
		{
			paramMap.putAll(condition.filterConditionNamedParams());
		}
	}
	
	@Override
	public void executeCommand() {
		try 
		{
			StringBuilder sb = new StringBuilder(" delete from `");
			sb.append(entityClass.tableName);
			sb.append(" where ").append(where);
			
			repository.commandUpdate(sb.toString(),paramMap);
		} 
		catch(Exception e) 
		{
			throw new CommandException(CommandExceptionMsg.BASE_JDBC_DELETE,e);
		}
	}
}
