package com.yingks.infra.domain;

import java.util.List;
import java.util.Map;

public interface AggregateConditionInterface {

	public List<String> fetchFieldNames();
	public String filterCondition();
	public Map<String,Object> filterConditionNamedParams();
}
