package org.kite9.tool.context;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.common.Kite9ProcessingException;
import org.kite9.framework.logging.Kite9Log;
import org.kite9.framework.logging.Logable;
import org.kite9.framework.repository.Repository;
import org.springframework.beans.factory.InitializingBean;

public class Kite9ContextImpl implements Kite9Context, Logable, InitializingBean {

	public Kite9ContextImpl(Aliaser a, Repository r) {
		super();
		this.a = a;
		this.repo = r;
	}

	Aliaser a;

	Kite9Log logger = new Kite9Log(this);

	public Aliaser getAliaser() {
		return a;
	}

	public Kite9Log getLogger() {
		return logger;
	}

	public String getPrefix() {
		return "KITE9";
	}

	public boolean isLoggingEnabled() {
		return true;
	}

	ClassLoader userClassLoader = null;
	
	public ClassLoader getUserClassLoader() {
		return userClassLoader;
	}

	public void afterPropertiesSet() {
		setupUserClassLoader();
		
		// check that the secret key and id have been set.
		if ((secretKey==null) || (secretKey.length()==0) || (projectId<=0)) {
			logger.error("Secret key and project ID not provided. \n"+
					"These must be set in your kite9.properties file or in the maven plugin configuration");
			
			
		}
	}

	public synchronized ClassLoader setupUserClassLoader() {
		try {
			if (userClassLoader == null) {
				getLogger().send("User specified class path: "+classPath);
				String[] parts = classPath.split(File.pathSeparator);
				URL[] urls = new URL[parts.length];

				for (int i = 0; i < parts.length; i++) {
					urls[i] = new File(parts[i]).toURL();
				}

				
				ClassLoader parentClassLoader = Kite9ContextImpl.class.getClassLoader();
				// System.out.println("Parent class loader: "+parentClassLoader);
				userClassLoader = new URLClassLoader(urls, parentClassLoader);

			}

			return userClassLoader;
		} catch (MalformedURLException e) {
			throw new Kite9ProcessingException("Could not set up class loader for user's classpath: ", e);
		}
	}

	private String classPath = "";

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public String getClassPath() {
		return classPath;
	}

	private Repository repo;

	public Repository getRepository() {
		return repo;
	}
	
	private int projectId = -1;
	
	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		try {
			this.projectId = Integer.parseInt(projectId);
		} catch (NumberFormatException e) {
		}
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	private String secretKey;
	
	

}
