/*
 * Copyright (c) 2017. Balamurugan Tamilmani (balamurugan.leo@gmail.com). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are not permitted.
 */
package com.balatamilmani;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.dialect.HSQLDialect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * @author Balamurugan Tamilmani
 *
 */
@SpringBootApplication
public class FileUploadApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(FileUploadApplication.class);
	}
	public static void main(String[] args) {
		SpringApplication.run(FileUploadApplication.class, args);
	}

	@Bean
	public DataSource dataSource() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		EmbeddedDatabase db = builder.setType(EmbeddedDatabaseType.HSQL).addScript("schema.sql").build();
		return db;
	}
	
	 @Bean(name = "entityManagerFactory")
	  public EntityManagerFactory entityManagerFactory()
	  {
	    LocalContainerEntityManagerFactoryBean lcemfb = new LocalContainerEntityManagerFactoryBean();
	    lcemfb.setDataSource(dataSource());
	    lcemfb.setJpaDialect(new HibernateJpaDialect());
	    lcemfb.setJpaVendorAdapter(jpaVendorAdapter());
	    lcemfb.setPersistenceUnitName("persistenceUnit");
	    lcemfb.setPackagesToScan("com.balatamilmani.model");
	    lcemfb.afterPropertiesSet();
	    return lcemfb.getObject();
	  }
	 
	  @Bean(name = "jpaVendorAdapter")
	  public JpaVendorAdapter jpaVendorAdapter()
	  {
	    HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
	    jpaVendorAdapter.setShowSql(true);
	    jpaVendorAdapter.setDatabase(Database.HSQL);
	    jpaVendorAdapter.setDatabasePlatform(HSQLDialect.class.getName());
	    jpaVendorAdapter.setGenerateDdl(false);
	    return jpaVendorAdapter;
	  }
}
