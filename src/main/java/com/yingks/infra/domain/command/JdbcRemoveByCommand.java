package com.yingks.infra.domain.command;

import com.yingks.infra.domain.filter.AbstractFilter;
import com.yingks.infra.domain.filter.AbstractDirectSQLQuery;
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
			FilterInterface queryConfig = getQueryConfig();
			if(queryConfig instanceof AbstractDirectSQLQuery) {
				AbstractDirectSQLQuery query = (AbstractDirectSQLQuery)queryConfig;
				repository.commandUpdate(query.sql(), query.filterParams());
			} else if(queryConfig instanceof AbstractFilter) {
				AbstractFilter query = (AbstractFilter)queryConfig;
				
				StringBuilder namedSql = new StringBuilder(" DELETE FROM `").append(entityClass.tableName).append("`");
				namedSql.append(" WHERE 1 ");
				
				if(!StringUtil.isEmpty(query.filter()))
					namedSql.append("AND (").append(query.filter()).append(")");
				
				repository.commandUpdate(namedSql.toString(), query.filterParams());
			} else {
				throw new CommandException(CommandExceptionMsg.BASE_JDBC_DELETE);
			}
		} 
		catch(Exception e) 
		{
			throw new CommandException(CommandExceptionMsg.BASE_JDBC_DELETE,e);
		}
	}
}
