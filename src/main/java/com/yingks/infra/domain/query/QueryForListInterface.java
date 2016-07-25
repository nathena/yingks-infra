package com.yingks.infra.domain.query;

import java.util.List;

public interface QueryForListInterface<T> {

	public List<T> queryForList();
}
