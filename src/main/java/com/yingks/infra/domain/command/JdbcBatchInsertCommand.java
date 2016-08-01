package com.yingks.infra.domain.command;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 该方法为测试
 * @Title: JdbcBatchInsertCommand.java
 * @Package com.yingks.infra.domain.command
 * @author nathena  
 * @date 2016年7月20日 下午5:09:46
 * @version V1.0 
 * @UpdateHis:
 *      TODO
 */
public class JdbcBatchInsertCommand<T> extends JdbcAbstractCommand<T> implements CommandInterface<T> {

	private List<T> entitys;
	
	public JdbcBatchInsertCommand(Class<T> clazz,List<T> entitys)
	{
		super(clazz);
		
		if( entitys.size() == 0 ){
			throw new CommandException(CommandExceptionMsg.COMMAND_ARG_MUST_ENTITY);
		}
	}
	
	@Override
	public void executeCommand() {
		try 
		{
			List<Map<String,?>> mapList = new ArrayList<>();
			
			StringBuilder sb = new StringBuilder(" insert into `");
			sb.append(this.entityClass.tableName).append("` ( ");
			String splite="";
			
			StringBuilder values = new StringBuilder();
			
			Set<String> fieldIter = this.entityClass.fieldToColumnMap.keySet();
			String column = null;
			for(String field : fieldIter)
			{
				column = this.entityClass.fieldToColumnMap.get(field);
				
				sb.append(splite).append("`").append(column).append("`");
				values.append(splite).append(":"+field);
				
				splite=" , ";
			}
			sb.append(") values ( ").append(values).append(" ) ");
			
			Method method = null;
			Map<String,Object> paramMap = null;
			Object val = null;
			for(T entity : entitys)
			{
				paramMap = new HashMap<>();
				for(String field : fieldIter)
				{
					column = this.entityClass.fieldToColumnMap.get(field);
					method = this.entityClass.fieldToMethodMap.get(field);
					val = method.invoke(entity);
					
					paramMap.put(field, val);
				}
				
				mapList.add(paramMap);
			}
			
			Map<String,?>[] maps = new Map[mapList.size()];
			maps = mapList.toArray(maps);
			
			repository.batchUpdate(sb.toString(), maps);
		}
		catch(Exception e) {
			throw new CommandException(CommandExceptionMsg.BASE_JDBC_CREATE,e);
		}
	}
	
}
