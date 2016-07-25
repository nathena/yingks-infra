package com.yingks.infra.domain;

import java.util.List;
import java.util.Map;

public class SimpleAggregateConditionImpl implements AggregateConditionInterface {

	protected List<String> fieldNames;
	
	protected String codition;
	protected Map<String,Object> namedParams;
	
	public SimpleAggregateConditionImpl()
	{
		
	}
	
	public SimpleAggregateConditionImpl(String codition,Map<String,Object> namedParams)
	{
		this.codition = codition;
		this.namedParams = namedParams;
	}
	
	public List<String> getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(List<String> fieldNames) {
		this.fieldNames = fieldNames;
	}

	@Override
	public List<String> fetchFieldNames() {
		return fieldNames;
	}
	@Override
	public String filterCondition() {
		return codition;
	}
	@Override
	public Map<String, Object> filterConditionNamedParams() {
		return namedParams;
	}
	
}
