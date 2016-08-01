package com.yingks.infra.domain;

import java.io.Serializable;
import java.util.List;
public class Pagination<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long total = 0L;			//总数
	private List<T> rows;				//行信息
	private Integer pageSize = 0;
	private Integer page = 0;
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public List<T> getRows() {
		return rows;
	}
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getPageTotal() {
		if(pageSize > 0)
			return (int) Math.ceil(total / pageSize);
		return 0;
	}
}
