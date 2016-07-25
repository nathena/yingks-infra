package com.yingks.infra.domain.query;

import com.yingks.infra.domain.Pagination;

public interface QueryForPaginationInterface<T> {

	public Pagination<T> queryForPagination();
}
