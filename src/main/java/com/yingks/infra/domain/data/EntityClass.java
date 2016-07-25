package com.yingks.infra.domain.data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.yingks.infra.utils.CollectionUtil;

public class EntityClass {
	
	public static Map<Class<?>, ClassSpecification<?> > cached = new ConcurrentHashMap<>();
	
	//此方法可能纯正并发问题，届时视情况加锁
	@SuppressWarnings("unchecked")
	public static <T> ClassSpecification<T> getEntityClass(T entity)
	{
		return (ClassSpecification<T>)getEntityClass(entity.getClass());
	}
	
	//此方法可能纯正并发问题，届时视情况加锁
	@SuppressWarnings("unchecked")
	public static <T> ClassSpecification<T> getEntityClass(Class<T> clazz)
	{
		if( !cached.containsKey(clazz) )
		{
			cached.put(clazz, new ClassSpecification<T>());
		}
		
		return (ClassSpecification<T>)cached.get(clazz);
	}
	
	
	public static class ClassSpecification<T>
	{
		public final Type type;
		public final Class<T> clazz;
		public final String tableName;
		
		public final Map<String, Method> fieldToMethodMap = new HashMap<String, Method>();
		public final Map<String, String> fieldToColumnMap = new HashMap<String, String>();
		public final Set<String> idFields = new HashSet<String>();
		public final Set<String> transientFields = new HashSet<String>();
		
		@SuppressWarnings("unchecked")
		private ClassSpecification()
		{
			this.clazz = (Class<T>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];;
			if( EntitySpecification.isAnnotationPresent(clazz, Entity.class))
			{
				this.type = Type.sql;
			}
			else
			{
				this.type = Type._sql;//default
			}
			
			this.tableName = EntitySpecification.getName(clazz);
			
			Map<Class<?>,Set<Field>> accessor = EntitySpecification.getAllAccessor(clazz);
			
			Set<Field> ids = accessor.get(Id.class);
			Set<Field> fields = accessor.get(Column.class);
			
			if(!CollectionUtil.isEmpty(ids)) {
				for(Field f : ids) {
					idFields.add(f.getName());
					fieldToMethodMap.put(f.getName(), EntitySpecification.getReadMethod(f));
					fieldToColumnMap.put(f.getName(), EntitySpecification.getName(f));
				}
			}
			
			if(!CollectionUtil.isEmpty(fields)) {
				for(Field f : fields) {
					if(PrimitiveTypeChecked.checkNumberType(f.getType())) {
						transientFields.add(f.getName());
					}
					fieldToMethodMap.put(f.getName(), EntitySpecification.getReadMethod(f));
					fieldToColumnMap.put(f.getName(), EntitySpecification.getName(f));
				}
			}
		}
		
		public boolean isTransientValue(String fieldame, Object value) {
			return value==null || ( transientFields.contains(fieldame) && ("0".equals(value.toString()) || "0.0".equals(value.toString())) );
		}
	}
	
	public static enum Type
	{
		_sql,sql,nosql
	}
}
