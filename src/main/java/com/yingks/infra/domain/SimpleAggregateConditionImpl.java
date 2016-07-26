package com.yingks.infra.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yingks.infra.utils.CollectionUtil;
import com.yingks.infra.utils.StringUtil;

public class SimpleAggregateConditionImpl implements AggregateConditionInterface {

	protected List<String> fieldNames = new ArrayList<>();
	
	protected StringBuilder coditions = new StringBuilder();
	protected Map<String,Object> namedParams = new HashMap<>();
	
	public SimpleAggregateConditionImpl()
	{
		
	}
	
	public SimpleAggregateConditionImpl(String codition,Map<String,Object> namedParams)
	{
		if( !StringUtil.isEmpty(codition) )
		{
			this.coditions.append(codition);
		}
		
		if(!CollectionUtil.isEmpty(namedParams))
		{
			this.namedParams.putAll(namedParams);
		}
	}
	
	public void andCondition(String codition)
	{
		if( coditions.length()>0 )
		{
			coditions.append(" and ");
		}
		coditions.append(codition);
	}
	
	public void addNamedParam(String key,Object val)
	{
		namedParams.put(key, val);
	}
	
	@Override
	public List<String> fetchFieldNames() {
		return fieldNames;
	}
	@Override
	public String filterCondition() {
		return coditions.toString();
	}
	@Override
	public Map<String, Object> filterConditionNamedParams() {
		return namedParams;
	}
	
}
