package com.yingks.infra.domain.filter;

import java.util.Map;

import com.yingks.infra.utils.CollectionUtil;
import com.yingks.infra.utils.StringUtil;

public class SimpleFilterImpl extends AbstractFilter {

	public SimpleFilterImpl(String codition, Map<String,Object> namedParams)
	{
		if( !StringUtil.isEmpty(codition) )
		{
			this.namedFitler.append(codition);
		}
		
		if(!CollectionUtil.isEmpty(namedParams))
		{
			this.namedParams.putAll(namedParams);
		}
	}
	
	public SimpleFilterImpl(String codition, String order, Map<String,Object> namedParams)
	{
		if( !StringUtil.isEmpty(codition) )
		{
			this.namedFitler.append(codition);
		}
		
		if( !StringUtil.isEmpty(order) )
		{
			this.order.append(order);
		}
		
		if(!CollectionUtil.isEmpty(namedParams))
		{
			this.namedParams.putAll(namedParams);
		}
	}
	
	//一个默认的查询配置
	public final static FilterInterface DEFAULT_QUERY_CONFIG = new SimpleFilterImpl(null, null);
}
