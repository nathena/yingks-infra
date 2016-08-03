package com.yingks.infra.domain.command;

import com.yingks.infra.domain.filter.FilterInterface;
import com.yingks.infra.utils.StringUtil;

public class JdbcRemoveByCommand<T> extends JdbcAbstractCommand<T> implements CommandInterface<T> {
	
	public JdbcRemoveByCommand(Class<T> clazz,FilterInterface filter)
	{
		super(clazz, filter);
	}
	
	@Override
	public void executeCommand() {
		try 
		{
			FilterInterface filter = getFilter();
			if( !StringUtil.isEmpty(filter.getDirectFilter()) ) 
			{
				repository.commandUpdate(filter.getDirectFilter(), filter.getNamedParams() );
			}
			else
			{
				StringBuilder namedSql = new StringBuilder(" DELETE FROM `").append(entityClass.tableName).append("`");
				namedSql.append(" WHERE 1 ");
				
				if(!StringUtil.isEmpty(filter.getNamedFitler()))
				{
					namedSql.append("AND (").append(filter.getNamedFitler()).append(")");
				}
				repository.commandUpdate(namedSql.toString(), filter.getNamedParams());
			}
		} 
		catch(Exception e) 
		{
			throw new CommandException(CommandExceptionMsg.BASE_JDBC_DELETE,e);
		}
	}
}
