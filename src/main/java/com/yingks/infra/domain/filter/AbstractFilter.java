package com.yingks.infra.domain.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractFilter implements FilterInterface {

	protected List<String> fields = new ArrayList<>();
	protected StringBuilder namedFitler = new StringBuilder();
	protected Map<String, Object> namedParams = new HashMap<>();
	protected StringBuilder order = new StringBuilder();
	
	public List<String> filterFields() {
		return fields;
	}
	
	public String filter() {
		return namedFitler.toString();
	}
	
	public Map<String, Object> filterParams() {
		return namedParams;
	}
	
	public String order() {
		return order.toString();
	}
}
