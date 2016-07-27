package com.yingks.infra.domain.filter;

import java.util.List;
import java.util.Map;

public interface FilterInterface {

	public List<String> fetchFieldNames();
	public String filterCondition();
	public Map<String,Object> filterConditionNamedParams();
}
