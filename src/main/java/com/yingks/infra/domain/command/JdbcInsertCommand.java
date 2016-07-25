package com.yingks.infra.domain.command;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.yingks.infra.domain.data.EntitySpecification;

public class JdbcInsertCommand<T> extends JdbcAbstractCommand<T> implements CommandInterface<T> {

	public JdbcInsertCommand(T entity)
	{
		super(entity);
	}
	
	@Override
	public void executeCommand() {
		try 
		{
			boolean autoKey = true;
			
			Map<String,Object> paramMap = new HashMap<String, Object>();
			
			StringBuilder sb = new StringBuilder(" insert into `");
			sb.append(this.entityClass.tableName).append("` ( ");
			String splite="";
			
			StringBuilder values = new StringBuilder();
			
			Object val = null;
			Set<String> fieldIter = this.entityClass.fieldToColumnMap.keySet();
			String column = null;
			Method method = null;
			for(String field : fieldIter)
			{
				column = this.entityClass.fieldToColumnMap.get(field);
				method = this.entityClass.fieldToMethodMap.get(field);
				val = method.invoke(this.entity);
				
				if(!this.entityClass.isTransientValue(field,val)) {
					sb.append(splite).append("`").append(column).append("`");
					values.append(splite).append(":"+field);
					paramMap.put(field, val);
					
					splite=" , ";
					
					if(this.entityClass.idFields.contains(field)){
						autoKey = false;
					}
				}
			}
			sb.append(") values ( ").append(values).append(" ) ");
			
			if(repository.commandUpdate(sb.toString(),paramMap)>0 && entityClass.idFields.size() == 1 && autoKey)  {
				String fieldName = entityClass.idFields.iterator().next();
				Field field = entityClass.clazz.getDeclaredField(fieldName);
				
				method = EntitySpecification.getWriteMethod(field);
				method.invoke(entity, repository.getAutoIncrementId());
			}
		}
		catch(Exception e) {
			throw new CommandException(CommandExceptionMsg.BASE_JDBC_CREATE,e);
		}
	}
	
}
