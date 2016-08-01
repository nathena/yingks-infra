/**
 * 
 */
package com.yingks.infra.domain.filter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author GaoWx
 * 直接写sql语句执行的实现,开放这个接口会比较灵活
 */
public abstract class AbstractDirectSQLQuery implements FilterInterface {
	protected Map<String, Object> namedParams = new HashMap<>();
	
	public Map<String, Object> filterParams() {
		return namedParams;
	}
	
	public abstract String sql();
}
