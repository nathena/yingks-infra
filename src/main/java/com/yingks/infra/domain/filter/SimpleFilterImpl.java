package com.yingks.infra.domain.filter;

import java.util.Map;

import com.yingks.infra.utils.CollectionUtil;
import com.yingks.infra.utils.StringUtil;

public class SimpleFilterImpl<T> extends AbstractFilter<T> implements FilterInterface<T> {

	public SimpleFilterImpl(String codition,Map<String,Object> namedParams)
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
}
