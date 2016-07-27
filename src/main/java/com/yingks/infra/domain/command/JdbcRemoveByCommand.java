package com.yingks.infra.domain.command;

import java.util.HashMap;
import java.util.Map;

import com.yingks.infra.domain.filter.FilterInterface;
import com.yingks.infra.utils.CollectionUtil;

public class JdbcRemoveByCommand<T> extends JdbcAbstractCommand<T> implements CommandInterface<T> {

	private String where = "";
	private Map<String,Object> paramMap = new HashMap<String, Object>();
	
	public JdbcRemoveByCommand(FilterInterface filter)
	{
		super();
		
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
