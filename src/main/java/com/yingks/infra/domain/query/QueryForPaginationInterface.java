package com.yingks.infra.domain.query;

public interface QueryForPaginationInterface<T> {

	public Pagination<T> queryForPagination();
}
