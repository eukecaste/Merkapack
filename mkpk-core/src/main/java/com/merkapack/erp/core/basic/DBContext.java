package com.merkapack.erp.core.basic;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Date;

import org.jooq.DSLContext;
import org.jooq.TransactionalRunnable;
import org.jooq.conf.ParamType;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

public class DBContext {

	private static Settings SETTINGS = null;
	private static Settings getDefaultSettings() {
		if (SETTINGS == null) {
			SETTINGS = new Settings();
			//SETTINGS.setRenderSchema(false);
			SETTINGS.setParamType( ParamType.INLINED );
		}
		return SETTINGS;
	}
	
	private ILogger logger;	

	private Connection connection;
	private int domain;
	private String user;
	private DSLContext dslContext;
	
	public DBContext(Connection connection) {
		this(connection, "default");
	}
	public DBContext(Connection connection,String user) {
		this.connection = connection;
		this.user = user;
		this.dslContext = DSL.using(connection,getDefaultSettings());
	}
	public int getDomain() {
		return domain;
	}
	public String getUser() {
		return user;
	}
	public DSLContext getDslContext() {
		return dslContext;
	}
	@Override
	public void finalize() {
		close();
	}
	public void close() {
		try {
			if (connection!= null) { 
				connection.close();
			};
		} catch (SQLException e) {
		}
	}
	public boolean canWrite() {
		return true;
	}
	public boolean canRead() {
		return true;
	}

	public void checkRead() {
		if (!canRead()) 
			throw new SecurityException( "No tiene permisos suficientes para realizar la lectura de datos." );	
	}
	public void checkWrite() {
		if (!canWrite()) 
			throw new SecurityException( "No tiene permisos suficientes para realizar la escritura de datos." );	
	}
	
	public void transaction(TransactionalRunnable transactional) {
		getDslContext().transaction(transactional);
	}
		
	public ILogger log() {
		if (logger == null) {
			// implementacion basico de log. Revisar.
			logger = new ILogger() {

				@Override
				public void error(String msg) {
					System.out.println(MessageFormat.format(ERR,new Date(),DBContext.this.domain,msg));
				}

				@Override
				public void warn(String msg) {
					System.out.println(MessageFormat.format(WAR,new Date(),DBContext.this.domain,msg));
				}

				@Override
				public void info(String msg) {
					System.out.println(MessageFormat.format(INF,new Date(),DBContext.this.domain,msg));
				}

				@Override
				public void debug(String msg) {
					System.out.println(MessageFormat.format(DEB,new Date(),DBContext.this.domain,msg));
				}
				
			};
		}
		return logger; 
	}

}
