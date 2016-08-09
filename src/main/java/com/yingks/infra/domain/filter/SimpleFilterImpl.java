package com.yingks.infra.domain.filter;

import java.util.Map;

import com.yingks.infra.utils.CollectionUtil;
import com.yingks.infra.utils.StringUtil;

public class SimpleFilterImpl extends AbstractFilter {

	public SimpleFilterImpl()
	{
		
	}
	
	public SimpleFilterImpl(String namedFitler, Map<String,Object> namedParams)
	{
		_init(namedFitler,namedParams,null,null,null);
	}
	
	public SimpleFilterImpl(String namedFitler, Map<String,Object> namedParams,String order)
	{
		_init(namedFitler,namedParams,order,null,null);
	}
	
	public SimpleFilterImpl(String namedFitler, Map<String,Object> namedParams,String order, String group)
	{
		_init(namedFitler,namedParams,order,group,null);
	}
	
	public SimpleFilterImpl(String namedFitler, Map<String,Object> namedParams,String order, String group, String having)
	{
		_init(namedFitler,namedParams,order,group,having);
	}
	
	@Override
	public void doFilter() {
		
	}

	protected void _init(String namedFitler, Map<String,Object> namedParams,String order, String group, String having)
	{
		if( !StringUtil.isEmpty(namedFitler) )
		{
			this.namedFitler.append(namedFitler);
		}
		
		if(!CollectionUtil.isEmpty(namedParams))
		{
			this.namedParams.putAll(namedParams);
		}
		
		if( !StringUtil.isEmpty(order) )
		{
			this.order.append(order);
		}
		
		if( !StringUtil.isEmpty(group) )
		{
			this.group.append(group);
		}
		
		if( !StringUtil.isEmpty(having) )
		{
			this.having.append(having);
		}
	}
	
	
	//一个默认的查询配置
	public final static FilterInterface DEFAULT_FILTER = new SimpleFilterImpl();
}
