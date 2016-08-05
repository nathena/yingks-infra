package com.yingks.infra.domain.store;

import java.util.List;

import com.yingks.infra.domain.command.CommandInterface;
import com.yingks.infra.domain.command.JdbcAbstractCommand;
import com.yingks.infra.domain.command.JdbcBatchInsertCommand;
import com.yingks.infra.domain.command.JdbcInsertCommand;
import com.yingks.infra.domain.command.JdbcRemoveByCommand;
import com.yingks.infra.domain.command.JdbcRemoveByIdCommand;
import com.yingks.infra.domain.command.JdbcRemoveCommand;
import com.yingks.infra.domain.command.JdbcStoreCommand;
import com.yingks.infra.domain.command.JdbcUpdateByCommand;
import com.yingks.infra.domain.command.JdbcUpdateCommand;
import com.yingks.infra.domain.filter.FilterInterface;
import com.yingks.infra.domain.query.JdbcSimpleQueryForEntity;
import com.yingks.infra.domain.query.JdbcSimpleQueryForList;
import com.yingks.infra.domain.query.JdbcSimpleQueryForLong;
import com.yingks.infra.domain.query.JdbcSimpleQueryForPagination;
import com.yingks.infra.domain.query.Pagination;

public class DbKit {

	private JdbcGeneralRepository repository;
	
	public DbKit(JdbcGeneralRepository repository)
	{
		this.repository = repository;
	}
	
	public JdbcGeneralRepository getRepository() {
		return repository;
	}

	public void setRepository(JdbcGeneralRepository repository) {
		this.repository = repository;
	}

	public <T> void executeCommand(CommandInterface<T> command) {
		
		JdbcAbstractCommand<T> _command = (JdbcAbstractCommand<T>)command;
		_command.setRepository(this.getRepository());
		_command.executeCommand();
	}

	
	public <T> void executeInsertCommand(T entity) {
		
		JdbcInsertCommand<T> command = new JdbcInsertCommand<T>(entity);
		command.setRepository(this.getRepository());
		command.executeCommand();
	}

	
	public <T> void executeBatchInsertCommand(Class<T> clazz,List<T> entitys) {
		JdbcBatchInsertCommand<T> command = new JdbcBatchInsertCommand<>(clazz,entitys);
		command.setRepository(this.getRepository());
		command.executeCommand();
	}

	
	public <T> void executeUpdateCommand(T entity) {
		JdbcUpdateCommand<T> command = new JdbcUpdateCommand<T>(entity);
		command.setRepository(this.getRepository());
		command.executeCommand();
	}

	
	public <T> void executeUpdateByCommand(T entity, FilterInterface filter) {
		JdbcUpdateByCommand<T> command = new JdbcUpdateByCommand<T>(entity, filter);
		command.setRepository(this.getRepository());
		command.executeCommand();
	}

	
	public <T> void executeRemoveCommand(T entity) {
		JdbcRemoveCommand<T> command = new JdbcRemoveCommand<>(entity);
		command.setRepository(this.getRepository());
		command.executeCommand();
	}

	
	public <T> void executeRemoveByIdCommand(Class<T> clazz,Object idVal) {
		
		JdbcRemoveByIdCommand<T> command = new JdbcRemoveByIdCommand<>(clazz,idVal);
		command.setRepository(this.getRepository());
		command.executeCommand();
	}

	
	public <T> void executeRemoveByCommand(Class<T> clazz, FilterInterface filter) {
		JdbcRemoveByCommand<T> command = new JdbcRemoveByCommand<>(clazz,filter);
		command.setRepository(this.getRepository());
		command.executeCommand();
	}

	
	public <T> void executeStoreCommand(T entity) {
		JdbcStoreCommand<T> command = new JdbcStoreCommand<T>(entity);
		command.setRepository(this.getRepository());
		command.executeCommand();
	}
	
	
	public <T> T queryForEntity(Class<T> clazz, Object idVal) {
		
		JdbcSimpleQueryForEntity<T> query = new JdbcSimpleQueryForEntity<T>(clazz,idVal);
		query.setRepository(this.getRepository());
		
		return query.queryForEntity();
	}

	
	public <T> T queryForEntity(Class<T> clazz, FilterInterface filter) {
		
		JdbcSimpleQueryForEntity<T> query = new JdbcSimpleQueryForEntity<T>(clazz,filter);
		query.setRepository(this.getRepository());
		
		return query.queryForEntity();
	}

	
	public <T> List<T> queryForList(Class<T> clazz, FilterInterface filter) {
		
		JdbcSimpleQueryForList<T> query = new JdbcSimpleQueryForList<T>(clazz,filter);
		query.setRepository(this.getRepository());
		
		return query.queryForList();
	}

	
	public <T> long queryForLong(Class<T> clazz, FilterInterface filter) {
		
		JdbcSimpleQueryForLong<T> query = new JdbcSimpleQueryForLong<T>(clazz,filter);
		query.setRepository(this.getRepository());
		
		return query.queryForLong();
	}

	
	public <T> Pagination<T> queryForPagination(Class<T> clazz, FilterInterface filter) {
		
		JdbcSimpleQueryForPagination<T> query = new JdbcSimpleQueryForPagination<T>(clazz,filter);
		query.setRepository(this.getRepository());
		
		return query.queryForPagination();
	}
}
