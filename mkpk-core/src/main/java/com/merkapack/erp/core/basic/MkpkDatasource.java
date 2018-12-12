package com.merkapack.erp.core.basic;


import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import com.merkapack.erp.core.model.MkpkCoreException;

public class MkpkDatasource {
	
	private static DataSource DATASOURCE;
	
	private MkpkDatasource() {
	}
	

	public static DBContext getDBContext(int domainId, String user) {
		try {
			if (DATASOURCE == null) init();
			return new DBContext(DATASOURCE.getConnection());
		} catch (Throwable e) {
			throw new MkpkCoreException(e.getMessage(),e);
		}
	}
	
	 public static void init() {
		 DATASOURCE = new DataSource();
         PoolProperties p = new PoolProperties();
         p.setUrl("jdbc:mariadb://localhost:3306:merkapack");
         p.setDriverClassName("org.mariadb.jdbc.Driver");
         p.setUsername("dbuser");
         p.setPassword("serubd2000");
         p.setJmxEnabled(false);
         p.setTestWhileIdle(false);
         p.setTestOnBorrow(true);
         p.setValidationQuery("SELECT 1");
         p.setTestOnReturn(false);
         p.setValidationInterval(30000);
         p.setTimeBetweenEvictionRunsMillis(30000);
         p.setMaxActive(100);
         p.setInitialSize(10);
         p.setMaxWait(10000);
         p.setRemoveAbandonedTimeout(120);
         p.setMinEvictableIdleTimeMillis(30000);
         p.setMinIdle(10);
         p.setLogAbandoned(true);
         p.setRemoveAbandoned(true);
         p.setJdbcInterceptors(
           "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
           "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
         DATASOURCE = new DataSource();
         DATASOURCE.setPoolProperties(p);
     }
}
