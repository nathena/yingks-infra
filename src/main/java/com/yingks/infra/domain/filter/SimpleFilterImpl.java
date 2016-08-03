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

	private void _init(String namedFitler, Map<String,Object> namedParams,String order, String group, String having)
	{
		if( !StringUtil.isEmpty(namedFitler) )
		{
			this.namedFitler.append(namedFitler);
		}
		
		if(!CollectionUtil.isEmpty(namedParams))
		{
			this.namedParams.putAll(namedParams);
		}
		
		if(!CollectionUtil.isEmpty(namedParams))
		{
			this.order.append(order);
		}
	}
	
	
	//一个默认的查询配置
	public final static FilterInterface DEFAULT_FILTER = new SimpleFilterImpl();
}
