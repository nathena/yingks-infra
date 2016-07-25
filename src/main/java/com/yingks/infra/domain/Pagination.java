package com.yingks.infra.domain;

import java.io.Serializable;
import java.util.List;
public class Pagination<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long total = 0L;			//总数
	private List<T> rows;				//行信息
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
}
