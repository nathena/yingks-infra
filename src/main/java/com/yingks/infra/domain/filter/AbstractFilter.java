package com.yingks.infra.domain.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractFilter<T> implements FilterInterface<T> {

	protected List<String> fields = new ArrayList<>();
	protected StringBuilder namedFitler = new StringBuilder();
	protected Map<String, Object> namedParams = new HashMap<>();
	
	@Override
	public List<String> filterFields() {
		return fields;
	}

	@Override
	public String filter() {
		return namedFitler.toString();
	}

	@Override
	public Map<String, Object> filterParams() {
		return namedParams;
	}
}
