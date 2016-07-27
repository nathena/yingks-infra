package com.yingks.infra.domain;

import java.util.List;

import com.yingks.infra.domain.command.CommandInterface;

public interface AggregateInterface {

	public <T> void executeCommand(CommandInterface<T> command);
	
	public <T> void executeInsertCommand(T entity);
	
	public <T> void executeBatchInsertCommand(List<T> entitys);
	
	public <T> void executeUpdateCommand(T entity);
	
	public <T> void executeUpdateByCommand(T entity,AggregateConditionInterface condition);
	
	public <T> void executeRemoveCommand(T entity);
	
	public <T> void executeRemoveByIdCommand(Class<T> clazz,Object idVal);
	
	public <T> void executeRemoveByCommand(Class<T> clazz,AggregateConditionInterface condition);
	
	public <T> void executeStoreCommand(T entity);
	
	public <T> T queryForEntity(Class<T> clazz,Object idVal);
	public <T> T queryForEntity(Class<T> clazz,AggregateConditionInterface condition);
	
	public <T> List<T> queryForList(Class<T> clazz,AggregateConditionInterface condition);
	public <T> long queryForLong(Class<T> clazz,AggregateConditionInterface condition);
	
	public <T> Pagination<T> queryForPagination(Class<T> clazz,AggregateConditionInterface condition);
}
