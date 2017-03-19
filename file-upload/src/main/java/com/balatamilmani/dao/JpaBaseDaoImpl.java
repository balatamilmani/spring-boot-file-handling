/*
 * Copyright (c) 2017. Balamurugan Tamilmani (balamurugan.leo@gmail.com). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are not permitted.
 */
package com.balatamilmani.dao;
import java.lang.reflect.ParameterizedType;

/**
 * @author Balamurugan Tamilmani
 *
 */
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Balamurugan Tamilmani
 *
 */
public class JpaBaseDaoImpl<T> implements JpaBaseDao<T>{

	private final Class<T> persistentClass;
	
	public JpaBaseDaoImpl() {
		this.persistentClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public void persist(T t) {
		entityManager.persist(t);
	}
	
	@Override
	public T merge(T t){
		return entityManager.merge(t);
	}

	@Override
	public T find(Object primaryKey) {
		return entityManager.find(persistentClass, primaryKey);
	}

	
}
