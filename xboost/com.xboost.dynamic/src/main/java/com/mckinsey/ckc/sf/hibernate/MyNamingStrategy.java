package com.mckinsey.ckc.sf.hibernate;

import org.hibernate.cfg.DefaultNamingStrategy;

public class MyNamingStrategy extends DefaultNamingStrategy {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String tableName(String tableName) {
		return tableName ;
	}

}