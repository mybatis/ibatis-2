package com.ibatis.sqlmap.engine.mapping.result.loader.test;

public class Bean2 {

	private Bean1 bean1;
	

	public void setBean1(Bean1 bean1) {
		this.bean1 = bean1;
	}

	public void testDefaultAccess() {
		bean1.defaultMethod();
	}
	
}
