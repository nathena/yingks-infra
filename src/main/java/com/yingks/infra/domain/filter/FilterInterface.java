package com.yingks.infra.domain.filter;

import java.util.List;
import java.util.Map;

public interface FilterInterface<T> {

	public List<String> filterFields();
	public String filter();
	public Map<String,Object> filterParams();
}