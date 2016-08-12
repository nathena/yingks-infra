package com.yingks.infra.domain.filter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.yingks.infra.domain.store.AbstractEntityAware;

/**
 * 约定不支持占位符查询，使用命名参数查询
 * @Title: AbstractFilter.java
 * @Package com.yingks.infra.domain.filter
 * @author nathena  
 * @date 2016年8月3日 上午10:56:31
 * @version V1.0 
 * @UpdateHis:
 *      @author GaoWx 集成直接写sql语句执行的实现,开放这个接口会比较灵活
 */
public abstract class AbstractFilter implements FilterInterface {

	//查询过滤字段
	protected final List<String> fields = new ArrayList<>();
	
	//命名参数语句
	protected final StringBuilder namedFitler = new StringBuilder();
	
	//命名参数，根据写入顺序提供查询序列
	protected final Map<String, Object> namedParams = new LinkedHashMap<>();
	
	//分组
	protected final StringBuilder group = new StringBuilder();
	protected final StringBuilder having = new StringBuilder();
	protected final StringBuilder order = new StringBuilder();
	
	private String directFilter;//集成直接写sql语句执行的实现,开放这个接口会比较灵活
	
	private long page = 0;
	private long limit = 0;
	private long offset = 0;
	
	public AbstractFilter()
	{
		this.doFilter();
	}
	
	public abstract void doFilter();
	
	public void addField(String field)
	{
		fields.add(field);
	}
	
	public void removeField(String field)
	{
		fields.remove(field);
	}
	
	public void clearField()
	{
		fields.clear();
	}
	
	public String getDirectFilter(AbstractEntityAware<?> aware) {
		return directFilter;
	}

	public void setDirectFilter(String directFilter) {
		this.directFilter = directFilter;
	}
	
	public long getPage() {
		return page;
	}

	public void setPage(long page) {
		this.page = page;
	}

	public long getLimit() {
		
		if( 0 == limit )
		{
			if( 0 < page )
			{
				limit = ((page-1)*offset );
			}
		}
		
		return limit;
	}

	public void setLimit(long limit) {
		this.limit = limit;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public List<String> getFields() {
		return fields;
	}
	
	public String getNamedFitler() {
		return namedFitler.toString();
	}
	
	public Map<String, Object> getNamedParams() {
		return namedParams;
	}
	
	public String getGroup() {
		return group.toString();
	}
	
	public String getHaving() {
		return having.toString();
	}
	
	public String getOrder() {
		return order.toString();
	}
	
	protected void appendFilterSql(String sqlSeq) {
		if(namedFitler.length() <= 0)
			namedFitler.append(" (").append(sqlSeq).append(") ");//这里给每个过滤片段加上(),传进来的字符串就不需要再加括号了
		else
			namedFitler.append(" AND (").append(sqlSeq).append(")");//每个单独拼接起来的查询片段用且(AND)连接
	}
	
	protected void appendNamedParam(String key, Object value) {
		namedParams.put(key, value);
	}
}
