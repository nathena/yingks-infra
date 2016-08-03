package com.yingks.infra.domain.command;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;

import com.yingks.infra.domain.store.EntitySpecification;

public class JdbcRemoveByIdCommand<T> extends JdbcAbstractCommand<T> implements CommandInterface<T> {

	private Object key;
	
	public JdbcRemoveByIdCommand(Class<T> clazz,Object key)
	{
		super(clazz);
		
		this.key = key;
	}
	
	@Override
	public void executeCommand() {
		try {
			Map<String,Object> paramMap = new HashMap<String, Object>();
			
			StringBuilder sb = new StringBuilder(" delete from `");
			sb.append(entityClass.tableName);
			sb.append("` where 1 ");
			String sp=" and ";
			
			if( entityClass.idFields.size()>1 && EntitySpecification.isEmbeddableAccessor(key)) {
				Map<Class<?>,Set<Field>> accessor = EntitySpecification.getAllAccessor(key.getClass());
				Set<Field> keyField = accessor.get(Column.class);
				
				String name = null;
				Method method = null;
				Object val = null;
				
				for(Field field:keyField)
				{
					method = EntitySpecification.getReadMethod(field);
					val = method.invoke(key);
					
					sb.append(sp).append("`").append(name).append("` = :"+name);
					sp=" and ";
					
					paramMap.put(name, val);
				}
			} else {
				String fieldName = entityClass.idFields.iterator().next();
				String column = entityClass.fieldToColumnMap.get(fieldName);
				
				sb.append(sp).append("`").append(column).append("` = :"+fieldName);
				paramMap.put(fieldName, key);
			}
			
			repository.commandUpdate(sb.toString(), paramMap);
		} catch(Exception e) {
			throw new CommandException(CommandExceptionMsg.BASE_JDBC_DELETE,e);
		}
	}
}
