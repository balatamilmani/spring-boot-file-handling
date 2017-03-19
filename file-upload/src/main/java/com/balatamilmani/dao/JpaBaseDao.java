/*
 * Copyright (c) 2017. Balamurugan Tamilmani (balamurugan.leo@gmail.com). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are not permitted.
 */
package com.balatamilmani.dao;
/**
 * @author Balamurugan Tamilmani
 *
 */
public interface JpaBaseDao<T> {

	void persist(T t);
	
	T merge(T t);
	
	T find(Object primaryKey);
}
