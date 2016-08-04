package com.yingks.infra.domain.filter;

import java.util.List;
import java.util.Map;

public interface FilterInterface {
	
	//支持直接查询
	public String getDirectFilter();
	//查询分页页数
	public long getPage();
	//查询分页起点
	public long getLimit();
	//查询分页长
	public long getOffset();
	//抓取的信息
	public List<String> getFields();
	//命名查询
	public String getNamedFitler();
	//命名参数
	public Map<String, Object> getNamedParams();
	//group by 分组
	public String getGroup();
	//having 过滤
	public String getHaving();
	//order 排序
	public String getOrder();
}
