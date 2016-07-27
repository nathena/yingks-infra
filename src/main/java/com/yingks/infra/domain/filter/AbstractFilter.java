package com.yingks.infra.domain.filter;

import java.util.List;
import java.util.Map;

public abstract class AbstractFilter implements FilterInterface {

	@Override
	public List<String> fetchFieldNames() {
		throw new RuntimeException(" fetchFieldNames not Implemented");
	}

	@Override
	public String filterCondition() {
		throw new RuntimeException(" filterCondition not Implemented");
	}

	@Override
	public Map<String, Object> filterConditionNamedParams() {
		throw new RuntimeException(" filterConditionNamedParams not Implemented");
	}

	
}
