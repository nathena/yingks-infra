package com.yingks.infra.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MustLoginedInterface {

	public boolean isLogined(HttpServletRequest request,HttpServletResponse response);
}
