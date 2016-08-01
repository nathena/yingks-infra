package com.yingks.infra.domain;

import java.util.List;

import com.yingks.infra.domain.command.CommandInterface;
import com.yingks.infra.domain.filter.FilterInterface;

public interface BaseAggregateInterface {

	public <T> void executeCommand(CommandInterface<T> command);
	
	public <T> void executeInsertCommand(T entity);
	
	public <T> void executeBatchInsertCommand(Class<T> clazz,List<T> entitys);
	
	public <T> void executeUpdateCommand(T entity);
	
	public <T> void executeUpdateByCommand(T entity,FilterInterface<T> filter);
	
	public <T> void executeRemoveCommand(T entity);
	
	public <T> void executeRemoveByIdCommand(Class<T> clazz,Object idVal);
	
	public <T> void executeRemoveByCommand(Class<T> clazz,FilterInterface<T> filter);
	
	public <T> void executeStoreCommand(T entity);
	
	public <T> T queryForEntity(Class<T> clazz,Object idVal);
	public <T> T queryForEntity(Class<T> clazz,FilterInterface<T> filter);
	
	public <T> List<T> queryForList(Class<T> clazz,FilterInterface<T> filter);
	
	public <T> long queryForLong(Class<T> clazz,FilterInterface<T> filter);
	
	public <T> Pagination<T> queryForPagination(Class<T> clazz,FilterInterface<T> filter);
}
