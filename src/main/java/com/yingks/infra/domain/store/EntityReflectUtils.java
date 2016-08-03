package com.yingks.infra.domain.store;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
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

public class EntityReflectUtils {
	
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
			cached.put(clazz, new ClassSpecification<T>(clazz));
		}
		
		return (ClassSpecification<T>)cached.get(clazz);
	}
	
	/** 
     * 获得超类的参数类型，取第一个参数类型 
     * @param <T> 类型参数 
     * @param clazz 超类类型 
     */  
    @SuppressWarnings({"unchecked","rawtypes"})
    public static <T> Class<T> getClassGenricType(final Class clazz) {  
        return (Class<T>)getClassGenricType(clazz, 0);  
    }  
      
    /** 
     * 根据索引获得超类的参数类型 
     * @param clazz 超类类型 
     * @param index 索引 
     */  
    @SuppressWarnings({"rawtypes"})  
    public static Class getClassGenricType(final Class clazz, final int index) {  
        Type genType = clazz.getGenericSuperclass();  
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;  
        }  
        Type[] params = ((ParameterizedType)genType).getActualTypeArguments();  
        if (index >= params.length || index < 0) {  
            return Object.class;  
        }  
        if (!(params[index] instanceof Class)) {
            return Object.class;  
        }  
        return (Class) params[index];  
    }
	
	public static class ClassSpecification<T>
	{
		public final ClassSpecificationType type;
		public final Class<T> clazz;
		public final String tableName;
		
		public final Map<String, Method> fieldToMethodMap = new HashMap<String, Method>();
		public final Map<String, String> fieldToColumnMap = new HashMap<String, String>();
		public final Set<String> idFields = new HashSet<String>();
		public final Set<String> transientFields = new HashSet<String>();
		
		private ClassSpecification(Class<T> clazz)
		{
			this.clazz = clazz;
			Class<? super T> tableAnotationClass = clazz; 
			if( EntitySpecification.isAnnotationPresent(clazz, Entity.class))
			{
				this.type = ClassSpecificationType.sql;
			}
			else if(EntitySpecification.isAnnotationPresent(clazz.getSuperclass(), Entity.class))
			{	//TODO 这里让继承JPA规范Entity的直接子类也可以直接操作(注意:仅直接继承可以),但是这样可能造成问题:如果直接子类很多可能会造成缓存的entityClass很多
				tableAnotationClass = clazz.getSuperclass();
				this.type = ClassSpecificationType._sql;//default
			}
			else
			{
				this.type = ClassSpecificationType._sql;//default
			}
			
			this.tableName = EntitySpecification.getName(tableAnotationClass);
			
			Map<Class<?>,Set<Field>> accessor = EntitySpecification.getAllAccessor(tableAnotationClass);
			
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
	
	public static enum ClassSpecificationType
	{
		_sql,sql,nosql
	}
}
