package com.yingks.infra.domain;

import java.util.List;

import com.yingks.infra.domain.command.CommandInterface;
import com.yingks.infra.domain.command.JdbcBatchInsertCommand;
import com.yingks.infra.domain.command.JdbcInsertCommand;
import com.yingks.infra.domain.command.JdbcRemoveByCommand;
import com.yingks.infra.domain.command.JdbcRemoveByIdCommand;
import com.yingks.infra.domain.command.JdbcRemoveCommand;
import com.yingks.infra.domain.command.JdbcStoreCommand;
import com.yingks.infra.domain.command.JdbcUpdateByCommand;
import com.yingks.infra.domain.command.JdbcUpdateCommand;
import com.yingks.infra.domain.data.JdbcGeneralRepository;
import com.yingks.infra.domain.query.JdbcSimpleQueryForEntity;
import com.yingks.infra.domain.query.JdbcSimpleQueryForList;
import com.yingks.infra.domain.query.JdbcSimpleQueryForLong;
import com.yingks.infra.domain.query.JdbcSimpleQueryForPagination;

public abstract class JdbcAggregateImpl implements AggregateInterface {

	protected JdbcGeneralRepository generalRepository;
	
	public JdbcGeneralRepository getRepository() {
		return generalRepository;
	}

	public void setRepository(JdbcGeneralRepository generalRepository) {
		this.generalRepository = generalRepository;
	}

	
	@Override
	public <T> void executeCommand(CommandInterface<T> command) {
		command.executeCommand();
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
	public <T> void executeUpdateByCommand(T entity, AggregateConditionInterface condition) {
		JdbcUpdateByCommand<T> command = new JdbcUpdateByCommand<T>(entity, condition);
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
	public <T> void executeRemoveByCommand(Class<T> clazz, AggregateConditionInterface condition) {
		JdbcRemoveByCommand<T> command = new JdbcRemoveByCommand<>(condition);
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
	public <T> T queryForEntity(Class<T> clazz, AggregateConditionInterface condition) {
		
		JdbcSimpleQueryForEntity<T> query = new JdbcSimpleQueryForEntity<>(condition);
		query.setRepository(generalRepository);
		
		return query.queryForEntity();
	}

	@Override
	public <T> List<T> queryForList(Class<T> clazz, AggregateConditionInterface condition) {
		
		JdbcSimpleQueryForList<T> query = new JdbcSimpleQueryForList<>(condition);
		query.setRepository(generalRepository);
		
		return query.queryForList();
	}

	@Override
	public <T> long queryForLong(Class<T> clazz, AggregateConditionInterface condition) {
		
		JdbcSimpleQueryForLong<T> query = new JdbcSimpleQueryForLong<>(condition);
		query.setRepository(generalRepository);
		
		return query.queryForLong();
	}

	@Override
	public <T> Pagination<T> queryForPagination(Class<T> clazz, AggregateConditionInterface condition) {
		
		JdbcSimpleQueryForPagination<T> query = new JdbcSimpleQueryForPagination<>(condition);
		query.setRepository(generalRepository);
		
		return query.queryForPagination();
	}

	
}
