package com.yingks.infra.domain.filter;

import java.util.Map;

import com.yingks.infra.utils.CollectionUtil;
import com.yingks.infra.utils.StringUtil;

public class SimpleFilterImpl extends AbstractFilter {

	public SimpleFilterImpl()
	{
		
	}
	public SimpleFilterImpl(String namedFitler)
	{
		String lowerCaseSql = namedFitler.toLowerCase();
		if(lowerCaseSql.startsWith("select") || lowerCaseSql.startsWith("select") || lowerCaseSql.startsWith("delete"))
			setDirectFilter(namedFitler);
		else
			_init(namedFitler,null,null,null,null);
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
	
	public SimpleFilterImpl(String sql, Object...nameParams)
	{
		String compileSql = sql;
		//TODO 替换占位符,但是这步好像有点多余,因为jdbc的查询本身也可以用?占位符(好像集合类型的不可以)
		int i = 0;
		while(compileSql.indexOf("?") >= 0) {
			if(nameParams.length <= i)
				throw new RuntimeException("占位符和参数数量不一致");
			
			compileSql = compileSql.replaceFirst("\\?", ":namedParam_" + i);
			namedParams.put("namedParam_" + i, nameParams[i]);
			i++;
		}
		
		String lowerCaseSql = compileSql.toLowerCase();
		if(lowerCaseSql.startsWith("select") || lowerCaseSql.startsWith("select") || lowerCaseSql.startsWith("delete"))
			setDirectFilter(compileSql);
		else
			namedFitler.append(compileSql);
		
	}
	
	public void addOperateFields(String... fields) {
		for(String field : fields)
			this.fields.add(field);
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
