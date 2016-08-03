package com.yingks.infra.domain.store;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.EntityManager;

public class JpaGeneralRepository  {
	
	private EntityManager em;
	
	public EntityManager getEntityManager()
	{
		return em;
	}
	
	/**
	 * 
	 * <p>Title: create</p> 
	 * <p>Description: 保存实体</p> 
	 * @param entity
	 */
	public <T> T create(T entity)
	{
		em.persist(entity);
		return entity;
	}
	
	/**
	 * 
	 * <p>Title: create</p> 
	 * <p>Description: 批量保存</p> 
	 * @param entitys
	 * @return boolean
	 */
	public <T> boolean create(Collection<T> entitys)
	{
		for (T entity : entitys)
		{
			create(entity);
		}
		
		return true;
	}
	
	/**
	 * 
	 * <p>Title: save</p> 
	 * <p>Description: 不需确定新建实体是否已经存在 类似数据库replace操作</p> 
	 * @param entity
	 * @return T
	 */
	public <T> T save(T entity)
	{
		if(em.contains(entity))
		{
			return em.merge(entity);
		}
		
		em.persist(entity);
		return entity;
	}
	
	/**
	 * 
	 * <p>Title: save</p> 
	 * <p>Description: 批量替换</p> 
	 * @param entitys
	 * @return Collection<T>
	 */
	public <T> Collection<T> save(Collection<T> entitys)
	{
		Collection<T> collection = new HashSet<T>();
		for (T entity : entitys)
		{
			collection.add(save(entity));
		}
		return collection;
	}
	
	/**
	 * 
	 * <p>Title: update</p> 
	 * <p>Description: 更新实体</p> 
	 * @param entity
	 */
	public <T> T update(T entity)
	{
		return em.merge(entity);
	}
	
	/**
	 * 更新
	 * <p>Title: update</p> 
	 * <p>Description: 批量更新</p> 
	 * @param entitys
	 * @return Collection<T>
	 */
	public <T> Collection<T> update(Collection<T> entitys)
	{
		Collection<T> collection = new HashSet<T>();
		for (T entity : entitys)
		{
			collection.add(update(entity));
		}
		return collection;
	}
	
	/**
	 * 
	 * <p>Title: remove</p> 
	 * <p>Description: 删除实体</p> 
	 * @param entity
	 */
	public <T> void remove(T entity)
	{
		if (null == entity) {
			throw new RuntimeException("请求删除的对象为空!");
		}
		
		T deletedEntity = entity;
		if( !em.contains(entity))
		{
			deletedEntity = em.merge(entity);
		}
		em.remove(deletedEntity);
	}
	
	/**
	 * 
	 * <p>Title: remove</p> 
	 * <p>Description:批量删除 传入集合 </p> 
	 * @param entitys void
	 */
	public <T> void remove(Collection<T> entitys) 
	{
		for (T entity : entitys)
		{
			remove(entity);
		}
	}
	
	/**
	 * 
	 * <p>Title: clear</p> 
	 * <p>Description: 清除一级缓存</p>  void
	 */
	public void clear()
	{
		em.clear();
	}
	
	/**
	 * 
	 * <p>Title: flush</p> 
	 * <p>Description: 刷新缓存</p>  void
	 */
	public void flush()
	{
		em.flush();
	}
	
	/**
	 * 
	 * <p>Title: get</p> 
	 * <p>Description: 获取实体</p> 
	 * @param key
	 * @return T
	 */
	public <T> T getEntity(Class<T> clazz,Object key)
	{
		return em.find(clazz, key);
	}

//	
//	/**
//	 * 
//	 * <p>Title: getEntity</p> 
//	 * <p>Description: 获取实体</p> 
//	 * @param returnType
//	 * @param sql
//	 * @param paramValue
//	 * @return T
//	 */
//	public <T> T getEntity(Class<T> returnType, String sql, Object... paramValue)
//	{
//		Query query = em.createNativeQuery(sql, returnType);
//		int index=1;
//		for(Object param : paramValue )
//		{
//			query.setParameter(index,param);
//			index++;
//		}
//		return (T)query.getSingleResult();
//	}
//	
//	/**
//	 * 
//	 * <p>Title: getEntity</p> 
//	 * <p>Description: 获取实体</p> 
//	 * @param returnType
//	 * @param sql
//	 * @return T
//	 */
//	public <T> T getEntity(Class<T> returnType, String sql)
//	{
//		return getEntity(returnType,sql);
//	}
//	
//	/**
//	 * 
//	 * <p>Title: getEntity</p> 
//	 * <p>Description: 获取实体</p> 
//	 * @param returnType
//	 * @param sql
//	 * @param paramMap
//	 * @return T
//	 */
//	public <T> T getEntity(Class<T> returnType, String sql,Map<String,?> paramMap)
//	{
//		Query query = em.createNativeQuery(sql, returnType);
//		
//		Iterator<String> iterator = paramMap.keySet().iterator();
//		String key=null;
//		while(iterator.hasNext())
//		{
//			key = iterator.next();
//			query.setParameter(key, paramMap.get(key));
//		}
//		
//		return (T)query.getSingleResult();
//	}
//	
//	/**
//	 * 
//	 * <p>Title: getEntity</p> 
//	 * <p>Description: 获取实体</p> 
//	 * @param returnType
//	 * @param paramMap
//	 * @return T
//	 */
//	public <T> T getEntity(Class<T> returnType, Map<String,Map<String,Object>> paramMap)
//	{
//		if( paramMap !=null && paramMap.size()>0 )
//		{
//			String namedsql = paramMap.keySet().iterator().next();
//			Map<String,Object> _paramMap = paramMap.get(namedsql);
//			
//			return getEntity(returnType,namedsql,_paramMap);
//		}
//		
//		return null;
//	}
//	
//	/**
//	 * 获取列表
//	 * <p>Title: getResultList</p> 
//	 * <p>Description: 获取列表</p> 
//	 * @param returnType
//	 * @param sql
//	 * @param paramValue
//	 * @return List<T>
//	 */
//	public <T> List<T> getResultList (Class<T> returnType, String sql, Object... paramValue)
//	{
//		Query query = em.createNativeQuery(sql, returnType);
//		
//		int index=1;
//		for(Object param : paramValue )
//		{
//			query.setParameter(index,param);
//			index++;
//		}
//		
//		return query.getResultList();
//	}
//	
//	/**
//	 * 
//	 * <p>Title: getResultList</p> 
//	 * <p>Description: 获取列表无参数sql</p> 
//	 * @param returnType
//	 * @param sql
//	 * @return List<T>
//	 */
//	public <T> List<T> getResultList (Class<T> returnType, String sql)
//	{
//		return getResultList(returnType,sql);
//	}
//	
//	/**
//	 * 
//	 * <p>Title: getResultList</p> 
//	 * <p>Description: 获取列表</p> 
//	 * @param returnType
//	 * @param sql
//	 * @param paramMap
//	 * @return
//	 */
//	public <T> List<T> getResultList(Class<T> returnType, String sql, Map<String,?> paramMap)
//	{
//		Query query = em.createNativeQuery(sql, returnType);
//		
//		Iterator<String> iterator = paramMap.keySet().iterator();
//		String key=null;
//		while(iterator.hasNext())
//		{
//			key = iterator.next();
//			query.setParameter(key, paramMap.get(key));
//		}
//		
//		return query.getResultList();
//	}
//	
//	/**
//	 * 
//	 * <p>Title: find</p> 
//	 * <p>Description: 获取列表</p> 
//	 * @param returnType
//	 * @param paramMap
//	 * @return List<T>
//	 */
//	public <T> List<T> getResultList (Class<T> returnType, Map<String,Map<String,Object>> paramMap)
//	{
//		if( paramMap !=null && paramMap.size()>0 )
//		{
//			String namedsql = paramMap.keySet().iterator().next();
//			Map<String,Object> _paramMap = paramMap.get(namedsql);
//			
//			return getResultList(returnType,namedsql,_paramMap);
//		}
//		
//		return null;
//	}
//	
//	/**
//	 * 
//	 * <p>Title: getResultList</p> 
//	 * <p>Description: 分页查询</p> 
//	 * @param returnType
//	 * @param sql
//	 * @param pageNo
//	 * @param pageRows
//	 * @param paramValue
//	 * @return List<T>
//	 */
//	public <T> List<T> getResultList (Class<T> returnType, String sql, int pageNo,int pageRows, Object... paramValue)
//	{
//		Query query = em.createNativeQuery(sql, returnType);
//		
//		query.setFirstResult((pageNo-1)*pageRows).setMaxResults(pageRows);
//		
//		int index=1;
//		for(Object param : paramValue )
//		{
//			query.setParameter(index,param);
//			index++;
//		}
//		
//		return query.getResultList();
//	}
//	
//	/**
//	 * 
//	 * <p>Title: getResultList</p> 
//	 * <p>Description: 分页查询</p> 
//	 * @param returnType
//	 * @param sql
//	 * @param pageNo
//	 * @param pageRows
//	 * @return List<T>
//	 */
//	public <T> List<T> getResultList (Class<T> returnType, String sql,int pageNo,int pageRows)
//	{
//		return getResultList(returnType,sql,pageNo,pageRows);
//	}
//	
//	/**
//	 * 
//	 * <p>Title: getResultList</p> 
//	 * <p>Description: 分页查询</p> 
//	 * @param returnType
//	 * @param sql
//	 * @param pageNo
//	 * @param pageRows
//	 * @param paramMap
//	 * @return
//	 * @throws DataAccessException List<T>
//	 */
//	public <T> List<T> getResultList(Class<T> returnType, String sql,int pageNo,int pageRows, Map<String,?> paramMap)  throws DataAccessException
//	{
//		Query query = em.createNativeQuery(sql, returnType);
//		query.setFirstResult((pageNo-1)*pageRows).setMaxResults(pageRows);
//		
//		Iterator<String> iterator = paramMap.keySet().iterator();
//		String key=null;
//		while(iterator.hasNext())
//		{
//			key = iterator.next();
//			query.setParameter(key, paramMap.get(key));
//		}
//		
//		return query.getResultList();
//	}
//	
//	/**
//	 * 
//	 * <p>Title: find</p> 
//	 * <p>Description: 分页查询</p> 
//	 * @param returnType
//	 * @param paramMap
//	 * @return List<T>
//	 */
//	public <T> List<T> getResultList (Class<T> returnType,int pageNo,int pageRows, Map<String,Map<String,Object>> paramMap)
//	{
//		if( paramMap !=null && paramMap.size()>0 )
//		{
//			String namedsql = paramMap.keySet().iterator().next();
//			Map<String,Object> _paramMap = paramMap.get(namedsql);
//			
//			return getResultList(returnType,namedsql,pageNo,pageRows,_paramMap);
//		}
//		
//		return null;
//	}
}
