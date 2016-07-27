package com.yingks.infra.domain;

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
import com.yingks.infra.domain.data.JdbcGeneralRepository;
import com.yingks.infra.domain.filter.FilterInterface;
import com.yingks.infra.domain.query.JdbcSimpleQueryForEntity;
import com.yingks.infra.domain.query.JdbcSimpleQueryForList;
import com.yingks.infra.domain.query.JdbcSimpleQueryForLong;
import com.yingks.infra.domain.query.JdbcSimpleQueryForPagination;

public abstract class BaseJdbcAggregateImpl implements BaseAggregateInterface {

	protected JdbcGeneralRepository generalRepository;
	
	public JdbcGeneralRepository getRepository() {
		return generalRepository;
	}

	public void setRepository(JdbcGeneralRepository generalRepository) {
		this.generalRepository = generalRepository;
	}
	
	@Override
	public <T> void executeCommand(CommandInterface<T> command) {
		
		JdbcAbstractCommand<T> _command = (JdbcAbstractCommand<T>)command;
		_command.setRepository(generalRepository);
		_command.executeCommand();
	}

	@Override
	public <T> void executeInsertCommand(T entity) {
		
		JdbcInsertCommand<T> command = new JdbcInsertCommand<T>(entity);
		command.setRepository(generalRepository);
		command.executeCommand();
	}

	@Override
	public <T> void executeBatchInsertCommand(List<T> entitys) {
		JdbcBatchInsertCommand<T> command = new JdbcBatchInsertCommand<>(entitys);
		command.setRepository(generalRepository);
		command.executeCommand();
	}

	@Override
	public <T> void executeUpdateCommand(T entity) {
		JdbcUpdateCommand<T> command = new JdbcUpdateCommand<T>(entity);
		command.setRepository(generalRepository);
		command.executeCommand();
	}

	@Override
	public <T> void executeUpdateByCommand(T entity, FilterInterface filter) {
		JdbcUpdateByCommand<T> command = new JdbcUpdateByCommand<T>(entity, filter);
		command.setRepository(generalRepository);
		command.executeCommand();
	}

	@Override
	public <T> void executeRemoveCommand(T entity) {
		JdbcRemoveCommand<T> command = new JdbcRemoveCommand<>(entity);
		command.setRepository(generalRepository);
		command.executeCommand();
	}

	@Override
	public <T> void executeRemoveByIdCommand(Class<T> clazz,Object idVal) {
		
		JdbcRemoveByIdCommand<T> command = new JdbcRemoveByIdCommand<>(idVal);
		command.setRepository(generalRepository);
		command.executeCommand();
	}

	@Override
	public <T> void executeRemoveByCommand(Class<T> clazz, FilterInterface filter) {
		JdbcRemoveByCommand<T> command = new JdbcRemoveByCommand<>(filter);
		command.setRepository(generalRepository);
		command.executeCommand();
	}

	@Override
	public <T> void executeStoreCommand(T entity) {
		JdbcStoreCommand<T> command = new JdbcStoreCommand<T>(entity);
		command.setRepository(generalRepository);
		command.executeCommand();
	}
	
	@Override
	public <T> T queryForEntity(Class<T> clazz, Object idVal) {
		
		JdbcSimpleQueryForEntity<T> query = new JdbcSimpleQueryForEntity<>(idVal);
		query.setRepository(generalRepository);
		
		return query.queryForEntity();
	}

	@Override
	public <T> T queryForEntity(Class<T> clazz, FilterInterface filter) {
		
		JdbcSimpleQueryForEntity<T> query = new JdbcSimpleQueryForEntity<>(filter);
		query.setRepository(generalRepository);
		
		return query.queryForEntity();
	}

	@Override
	public <T> List<T> queryForList(Class<T> clazz, FilterInterface filter) {
		
		JdbcSimpleQueryForList<T> query = new JdbcSimpleQueryForList<>(filter);
		query.setRepository(generalRepository);
		
		return query.queryForList();
	}

	@Override
	public <T> long queryForLong(Class<T> clazz, FilterInterface filter) {
		
		JdbcSimpleQueryForLong<T> query = new JdbcSimpleQueryForLong<>(filter);
		query.setRepository(generalRepository);
		
		return query.queryForLong();
	}

	@Override
	public <T> Pagination<T> queryForPagination(Class<T> clazz, FilterInterface filter) {
		
		JdbcSimpleQueryForPagination<T> query = new JdbcSimpleQueryForPagination<>(filter);
		query.setRepository(generalRepository);
		
		return query.queryForPagination();
	}
}
