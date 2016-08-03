package com.yingks.infra.domain.store;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.yingks.infra.utils.LogHelper;

public class JdbcGeneralRepository
{
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	
	public JdbcGeneralRepository()
	{
		
	}
	
	public JdbcGeneralRepository(NamedParameterJdbcTemplate namedJdbcTemplate)
	{
		this.namedJdbcTemplate = namedJdbcTemplate;
	}
	
	public NamedParameterJdbcTemplate getNamedJdbcTemplate() {
		return namedJdbcTemplate;
	}

	public void setNamedJdbcTemplate(NamedParameterJdbcTemplate namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
	}
	

	/**
	 * 适用于更新数据库,insert,update, delete
	 * @param sql
	 * @param paramValue
	 * @return
	 * @throws DataAccessException
	 */
	public int commandUpdate(String sql, Object... paramValue)
	{
		int affected = 0;
		try
		{
			affected = namedJdbcTemplate.getJdbcOperations().update(sql, paramValue);
		}
		catch(Exception e)
		{
			throw new JdbcGeneralRepositoryException(e.getMessage(),e);
		}
		return affected;
	}
	
	/**
	 * 适用于更新数据库,insert,update, delete
	 * @param sql
	 * @param paramValue
	 * @return
	 * @throws DataAccessException
	 */
	public int commandUpdate(String namedSql, Map<String,?> paramMap)
	{
		int affected = 0;
		try
		{
			affected = namedJdbcTemplate.update(namedSql, paramMap);
		}
		catch(Exception e)
		{
			throw new JdbcGeneralRepositoryException(e.getMessage(),e);
		}
		return affected;
	}
	
	/**
	 * 适用于更新数据库,insert,update, delete
	 * @param paramMap
	 * @return
	 * @throws DataAccessException
	 */
	public int commandUpdate(Map<String,Map<String,Object>> paramMap)
	{
		int affected = 0;
		if( paramMap !=null && paramMap.size()>0 )
		{
			String namedsql = paramMap.keySet().iterator().next();
			Map<String,?> _paramMap = paramMap.get(namedsql);
			
			affected = commandUpdate(namedsql, _paramMap);
		}
		return affected;
	}
	/**
	 * 适用于批量更新数据库,insert,update, delete
	 * @param paramMap
	 * @return
	 * @throws DataAccessException
	 */
	public int[] batchUpdate(String namedSql,Map<String,?>[] batchValues)
	{
		try
		{
			return namedJdbcTemplate.batchUpdate(namedSql, batchValues);
		}
		catch(Exception e)
		{
			throw new JdbcGeneralRepositoryException(e.getMessage(),e);
		}
	}
	
	/**
	 * 适用于批量更新数据库,insert,update, delete
	 * @param paramMap
	 * @return
	 * @throws DataAccessException
	 */
	public int[] batchUpdate(String namedSql,BatchPreparedStatementSetter setter)
	{
		try
		{
			return namedJdbcTemplate.getJdbcOperations().batchUpdate(namedSql, setter);
		}
		catch(BadSqlGrammarException e)
		{
			throw new JdbcGeneralRepositoryException(e.getMessage(),e);
		}
	}
	/**
	 * 查询数据列表
	 * @param sql
	 * @param returnType
	 * @param paramValue
	 * @return
	 */
	public <T> List<T> getList(Class<T> returnType, String sql, Map<String,?> paramMap)
	{
		try
		{
			RowMapper<T> rowMapper = new BeanPropertyRowMapper<T>(returnType,false);
			return namedJdbcTemplate.query(sql, paramMap, rowMapper);
		}
		catch(EmptyResultDataAccessException e)
		{
			LogHelper.debug(e.getMessage(), e);
		}
		catch(Exception e)
		{
			throw new JdbcGeneralRepositoryException(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 查询数据列表
	 * @param sql
	 * @param returnType
	 * @param paramValue
	 * @return
	 */
	public <T> List<T> getList(Class<T> returnType, String sql, Object... paramValue)
	{
		try
		{
			RowMapper<T> rowMapper = new BeanPropertyRowMapper<T>(returnType,false);
			return namedJdbcTemplate.getJdbcOperations().query(sql, rowMapper, paramValue);
		}
		catch(EmptyResultDataAccessException e)
		{
			LogHelper.debug(e.getMessage(), e);
		}
		catch(Exception e)
		{
			throw new JdbcGeneralRepositoryException(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 查询数据列表
	 * @param returnType
	 * @param paramMap
	 * @return
	 * @throws DataAccessException
	 */
	public <T> List<T> getList(Class<T> returnType, Map<String,Map<String,Object>> paramMap )
	{
		if( paramMap !=null && paramMap.size()>0 )
		{
			String namedsql = paramMap.keySet().iterator().next();
			Map<String,Object> _paramMap = paramMap.get(namedsql);
			
			return getList(returnType,namedsql,_paramMap);
		}
		
		return null;
	}
	
	/**
	 * 获取对象
	 * @param sql
	 * @param returnType
	 * @param paramValue
	 * @return
	 * @throws DataAccessException
	 */
	public <T> T getEntity(Class<T> returnType, String sql, Object... paramValue)
	{
		try
		{
			RowMapper<T> rowMapper = new BeanPropertyRowMapper<T>(returnType,false);
			return namedJdbcTemplate.getJdbcOperations().queryForObject(sql, rowMapper, paramValue);
		}
		catch(EmptyResultDataAccessException e)
		{
			LogHelper.debug(e.getMessage(), e);
		}
		catch(Exception e)
		{
			throw new JdbcGeneralRepositoryException(e.getMessage(),e);
		}
		return null;
		
	}
	
	/**
	 * 获取对象
	 * @param sql
	 * @param returnType
	 * @param paramValue
	 * @return
	 * @throws DataAccessException
	 */
	public <T> T getEntity(Class<T> returnType, String sql, Map<String,?> paramMap)
	{
		try
		{
			RowMapper<T> rowMapper = new BeanPropertyRowMapper<T>(returnType,false);
			return namedJdbcTemplate.queryForObject(sql, paramMap, rowMapper);
		}
		catch(EmptyResultDataAccessException e)
		{
			LogHelper.debug(e.getMessage(), e);
		}
		catch(Exception e)
		{
			throw new JdbcGeneralRepositoryException(e.getMessage(),e);
		}
		return null;
		
	}
	
	/**
	 * 获取对象
	 * @param returnType
	 * @param paramMap
	 * @return
	 * @throws DataAccessException
	 */
	public <T> T getEntity(Class<T> returnType, Map<String,Map<String,Object>> paramMap )
	{
		if( paramMap !=null && paramMap.size()>0 )
		{
			String namedsql = paramMap.keySet().iterator().next();
			Map<String,Object> _paramMap = paramMap.get(namedsql);
			
			return getEntity(returnType,namedsql,_paramMap);
		}
		
		return null;
	}
	
	/**
	 * 查询返回Integer值
	 * @param countSQL
	 * @param paramValue
	 * @return
	 */
	public Integer queryForInt(String sql, Object... paramValue)
	{
		try
		{
			return namedJdbcTemplate.getJdbcOperations().queryForObject(sql, Integer.class, paramValue);
		}
		catch(EmptyResultDataAccessException e)
		{
			LogHelper.debug(e.getMessage(), e);
		}
		catch(Exception e)
		{
			throw new JdbcGeneralRepositoryException(e.getMessage(),e);
		}
		return 0;
	}
	
	/**
	 * 查询返回Integer值
	 * @param countSQL
	 * @param paramMap
	 * @return
	 */
	public Integer queryForInt(String sql, Map<String,Object> paramMap) 
	{
		try
		{
			return namedJdbcTemplate.queryForObject(sql, paramMap, Integer.class);
		}
		catch(EmptyResultDataAccessException e)
		{
			LogHelper.debug(e.getMessage(), e);
		}
		catch(Exception e)
		{
			throw new JdbcGeneralRepositoryException(e.getMessage(),e);
		}
		return 0;
	}
	
	/**
	 * 查询返回Integer值
	 * @param paramMap
	 * @return
	 * @throws DataAccessException
	 */
	public Integer queryForInt(Map<String,Map<String,Object>> paramMap) 
	{
		int result = 0;
		if( paramMap !=null && paramMap.size()>0 )
		{
			String namedsql = paramMap.keySet().iterator().next();
			Map<String,Object> _paramMap = paramMap.get(namedsql);
			
			return queryForInt(namedsql,_paramMap);
		}
		return result;
	}
	
	public long queryForLong(String sql, Object... paramValue)
	{
		try
		{
			return namedJdbcTemplate.getJdbcOperations().queryForObject(sql, Long.class, paramValue);
		}
		catch(EmptyResultDataAccessException e)
		{
			LogHelper.debug(e.getMessage(), e);
		}
		catch(Exception e)
		{
			throw new JdbcGeneralRepositoryException(e.getMessage(),e);
		}
		return 0;
	}
	
	/**
	 * 查询返回Integer值
	 * @param countSQL
	 * @param paramMap
	 * @return
	 */
	public long queryForLong(String sql, Map<String,Object> paramMap) 
	{
		try
		{
			return namedJdbcTemplate.queryForObject(sql, paramMap, Long.class);
		}
		catch(EmptyResultDataAccessException e)
		{
			LogHelper.debug(e.getMessage(), e);
		}
		catch(Exception e)
		{
			throw new JdbcGeneralRepositoryException(e.getMessage(),e);
		}
		return 0;
	}
	
	/**
	 * 查询返回Integer值
	 * @param paramMap
	 * @return
	 * @throws DataAccessException
	 */
	public long queryForLong(Map<String,Map<String,Object>> paramMap) 
	{
		long result = 0;
		if( paramMap !=null && paramMap.size()>0 )
		{
			String namedsql = paramMap.keySet().iterator().next();
			Map<String,Object> _paramMap = paramMap.get(namedsql);
			
			return queryForLong(namedsql,_paramMap);
		}
		return result;
	}
	
	
	/**
	 * 查询返回String值
	 * @param countSQL
	 * @param paramValue
	 * @return
	 */
	public String queryForString(String sql, Object... paramValue)
	{
		try
		{
			return namedJdbcTemplate.getJdbcOperations().queryForObject(sql, String.class, paramValue);
		}
		catch(EmptyResultDataAccessException e)
		{
			LogHelper.debug(e.getMessage(), e);
		}
		catch(Exception e)
		{
			throw new JdbcGeneralRepositoryException(e.getMessage(),e);
		}
		return "";
	}
	
	/**
	 * 查询返回String值
	 * @param countSQL
	 * @param paramMap
	 * @return
	 */
	public String queryForString(String sql, Map<String,Object> paramMap) 
	{
		try
		{
			return namedJdbcTemplate.queryForObject(sql, paramMap, String.class);
		}
		catch(EmptyResultDataAccessException e)
		{
			LogHelper.debug(e.getMessage(), e);
		}
		catch(Exception e)
		{
			throw new JdbcGeneralRepositoryException(e.getMessage(),e);
		}
		return "";
	}
	
	/**
	 * 查询返回String值
	 * @param paramMap
	 * @return
	 * @throws DataAccessException
	 */
	public String queryForString(Map<String,Map<String,Object>> paramMap) 
	{
		if( paramMap !=null && paramMap.size()>0 )
		{
			String namedsql = paramMap.keySet().iterator().next();
			Map<String,Object> _paramMap = paramMap.get(namedsql);
			return queryForString(namedsql,_paramMap);
		}
		return "";
	}
	/**
	 * 查询返回Object
	 * @param countSQL
	 * @param paramValue
	 * @return
	 */
	public <T> T queryForObject(Class<T> returnType, String sql,Object... paramValue)
	{
		try
		{
			return namedJdbcTemplate.getJdbcOperations().queryForObject(sql, returnType, paramValue);
		}
		catch(EmptyResultDataAccessException e)
		{
			LogHelper.debug(e.getMessage(), e);
		}
		catch(Exception e)
		{
			throw new JdbcGeneralRepositoryException(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 查询返回Integer值
	 * @param countSQL
	 * @param paramMap
	 * @return
	 */
	public <T> T queryForObject(Class<T> returnType, String sql, Map<String,Object> paramMap) 
	{
		try
		{
			return namedJdbcTemplate.queryForObject(sql, paramMap, returnType);
		}
		catch(EmptyResultDataAccessException e)
		{
			LogHelper.debug(e.getMessage(), e);
		}
		catch(Exception e)
		{
			throw new JdbcGeneralRepositoryException(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 查询返回List
	 * @param countSQL
	 * @param paramValue
	 * @return
	 */
	public <T> List<T> queryForList(Class<T> returnType,String sql,Object... paramValue)
	{
		try
		{
			return namedJdbcTemplate.getJdbcOperations().queryForList(sql, returnType, paramValue);
		}
		catch(EmptyResultDataAccessException e)
		{
			LogHelper.debug(e.getMessage(), e);
		}
		catch(Exception e)
		{
			throw new JdbcGeneralRepositoryException(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 查询数据列表
	 * @param sql
	 * @param returnType
	 * @param paramValue
	 * @return
	 */
	public <T> List<T> queryForList(Class<T> returnType, String sql, Map<String,?> paramMap)
	{
		try
		{
			return namedJdbcTemplate.queryForList(sql, paramMap, returnType);
		}
		catch(EmptyResultDataAccessException e)
		{
			LogHelper.debug(e.getMessage(), e);
		}
		catch(Exception e)
		{
			throw new JdbcGeneralRepositoryException(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 查询数据列表
	 * @param returnType
	 * @param paramMap
	 * @return
	 * @throws DataAccessException
	 */
	public <T> List<T> queryForList(Class<T> returnType, Map<String,Map<String,Object>> paramMap )
	{
		if( paramMap !=null && paramMap.size()>0 )
		{
			String namedsql = paramMap.keySet().iterator().next();
			Map<String,Object> _paramMap = paramMap.get(namedsql);
			
			return queryForList(returnType,namedsql,_paramMap);
		}
		
		return null;
	}
	
	
	/**
	 * 查询返回List
	 * @param countSQL
	 * @param paramValue
	 * @return
	 */
	public List<Map<String,Object>> queryForList(String sql,Object... paramValue)
	{
		try
		{
			return namedJdbcTemplate.getJdbcOperations().queryForList(sql, paramValue);
		}
		catch(EmptyResultDataAccessException e)
		{
			LogHelper.debug(e.getMessage(), e);
		}
		catch(Exception e)
		{
			throw new JdbcGeneralRepositoryException(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 查询数据列表
	 * @param sql
	 * @param returnType
	 * @param paramValue
	 * @return
	 */
	public List<Map<String,Object>> queryForList(String sql, Map<String,?> paramMap)
	{
		try
		{
			return namedJdbcTemplate.queryForList(sql, paramMap);
		}
		catch(EmptyResultDataAccessException e)
		{
			LogHelper.debug(e.getMessage(), e);
		}
		catch(Exception e)
		{
			throw new JdbcGeneralRepositoryException(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 查询数据列表
	 * @param returnType
	 * @param paramMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<Map<String,Object>> queryForList(Map<String,Map<String,Object>> paramMap )
	{
		if( paramMap !=null && paramMap.size()>0 )
		{
			String namedsql = paramMap.keySet().iterator().next();
			Map<String,Object> _paramMap = paramMap.get(namedsql);
			
			return queryForList(namedsql,_paramMap);
		}
		
		return null;
	}
	
	public Map<String,Object> queryForMap(String sql,Object... paramValue)
	{
		try
		{
			return namedJdbcTemplate.getJdbcOperations().queryForMap(sql, paramValue);
		}
		catch(EmptyResultDataAccessException e)
		{
			LogHelper.debug(e.getMessage(), e);
		}
		catch(Exception e)
		{
			throw new JdbcGeneralRepositoryException(e.getMessage(),e);
		}
		return null;
	}
	
	public Map<String,Object> queryForMap(String sql,Map<String,?> paramMap)
	{
		try
		{
			return namedJdbcTemplate.queryForMap(sql,paramMap);
		}
		catch(EmptyResultDataAccessException e)
		{
			LogHelper.debug(e.getMessage(), e);
		}
		catch(Exception e)
		{
			throw new JdbcGeneralRepositoryException(e.getMessage(),e);
		}
		return null;
	}
	
	public Map<String,Object> queryForMap(Map<String,Map<String,?>> paramMap)
	{
		if( paramMap !=null && paramMap.size()>0 )
		{
			String namedsql = paramMap.keySet().iterator().next();
			Map<String,?> _paramMap = paramMap.get(namedsql);
			return queryForMap(namedsql,_paramMap);
		}
		return null;
	}

	public <T> List<T> query(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper) {
		return namedJdbcTemplate.query(sql, paramMap, rowMapper);
	}

	/**
	 * 自增主键
	 * @return
	 */
	public int getAutoIncrementId()
	{
		try
		{
			return namedJdbcTemplate.getJdbcOperations().queryForObject("select last_insert_id()", Integer.class);
		}
		catch(EmptyResultDataAccessException e)
		{
			LogHelper.debug(e.getMessage(), e);
		}
		catch(Exception e)
		{
			throw new JdbcGeneralRepositoryException(e.getMessage(),e);
		}
		return 0;
	}
}
