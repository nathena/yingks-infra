package com.yingks.infra.domain.store;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @Description: 预定注解为字段映射，复合主键使用@IdClass 
 * @author nathena 
 * @date 2013-4-19 上午2:03:53 
 *
 */
public class EntitySpecification 
{
	private EntitySpecification(){}
	
	public static boolean isAnnotationPresent(Class<?> entityClass,Class<? extends Annotation> annotationClass)
	{
		return entityClass.isAnnotationPresent(annotationClass);
	}
	
	public static String getName(Class<?> entityClass)
	{
		if(entityClass.isAnnotationPresent(Table.class))
		{
			String name = entityClass.getAnnotation(Table.class).name();
			if(!name.isEmpty())
			{
				return name;
			}
		}
		
		return entityClass.getSimpleName();
	}
	
	public static String getName(AccessibleObject accessibleObject)
	{
		if(accessibleObject.isAnnotationPresent(Column.class)) 
		{
			String name = accessibleObject.getAnnotation(Column.class).name();
			if(!name.isEmpty())
			{
				return name;
			}
		}
		
		if (accessibleObject instanceof Field) 
		{
		    return ((Field)accessibleObject).getName();
		}
		
		throw new IllegalArgumentException("No column name can be derived from " + accessibleObject + ". Make sure it is a getter.");
	}
	
	public static Method getReadMethod(Field field)
	{
		try
		{
			PropertyDescriptor pd = new PropertyDescriptor(field.getName(),field.getDeclaringClass());
			Method method = pd.getReadMethod();
			method.setAccessible(true);
			
			return method;
		}
		catch(IntrospectionException e)
		{
			throw new RuntimeException(e);
		}
		//return Introspector.getBeanInfo(field.getDeclaringClass()).getPropertyDescriptors();
	}
	
	public static Method getWriteMethod(Field field)
	{
		try
		{
			PropertyDescriptor pd = new PropertyDescriptor(field.getName(),field.getDeclaringClass());
			Method method = pd.getWriteMethod();
			method.setAccessible(true);
			
			return method;
		}
		catch(IntrospectionException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static Map<Class<?>,Set<Field>> getAllAccessor(Class<?> type) 
	{
		Map<Class<?>,Set<Field>> accessor = new HashMap<Class<?>, Set<Field>>();
		
		Set<Field> ids = new HashSet<Field>();
		Set<Field> fields = new HashSet<Field>();
		
	    for (Field field : type.getDeclaredFields()) 
	    {
	    	if( isTransient(field) || isSerialVersionUID(field) )
	    	{
	    		continue;
	    	}
	    	
	    	if (isIdAccessor(field)) 
	    	{
	    		ids.add(field);
	    	}
	    	
	    	fields.add(field);
	    }
	    
	    accessor.put(Id.class, ids);
	    accessor.put(Column.class, fields);
	    
	    if( null != type.getSuperclass() )
	    {
	    	Map<Class<?>,Set<Field>> _accessor = getAllAccessor(type.getSuperclass());
	    	
	    	accessor.get(Id.class).addAll(_accessor.get(Id.class));
	    	accessor.get(Column.class).addAll(_accessor.get(Column.class));
	    }
	    
	    return accessor;
	}
	
	public static boolean isIdAccessor(AnnotatedElement annotatedElement)
	{
		return annotatedElement.isAnnotationPresent(Id.class);
	}
	
	public static boolean isEmbeddableAccessor(Object obj)
	{
		return obj.getClass().isAnnotationPresent(Embeddable.class);
	}
	
	public static boolean isTransient(AccessibleObject accessibleObject)
	{
		return Modifier.isTransient(((Member)accessibleObject).getModifiers()) || accessibleObject.isAnnotationPresent(Transient.class);
	}
	
	public static boolean isRelation(AccessibleObject accessibleObject)
	{
		return accessibleObject.isAnnotationPresent(OneToMany.class) || isToOneRelation(accessibleObject);
	}
	
	public static boolean isToOneRelation(AccessibleObject accessibleObject)
	{
		return accessibleObject.isAnnotationPresent(ManyToOne.class) || accessibleObject.isAnnotationPresent(OneToOne.class);
	}
	
	public static boolean isSerialVersionUID(Field field)
	{
		return Modifier.isStatic(field.getModifiers()) || "serialVersionUID".equalsIgnoreCase(field.getName());
	}
}
