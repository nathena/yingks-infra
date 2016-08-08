package com.yingks.infra.domain.filter;

import java.util.HashMap;
import java.util.Map;

public class SimplePlaceholderFilterImpl extends SimpleFilterImpl {

	//namedFitler xxx = :_0 and yyy = :_1 
	public SimplePlaceholderFilterImpl(String namedFitler, Object ...params)
	{
		Map<String,Object> namedParams = new HashMap<String, Object>();
		int index = 0;
		for(Object param : params )
		{
			namedParams.put("_"+index, param);
			index++;
		}
		
		_init(namedFitler,namedParams,null,null,null);
	}
}
