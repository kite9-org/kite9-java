package org.kite9.tool;

import java.io.IOException;
import java.util.Properties;

import org.kite9.framework.common.Kite9ProcessingException;
import org.kite9.tool.context.Kite9Context;
import org.springframework.beans.factory.config.PropertyOverrideConfigurer;
import org.springframework.context.support.GenericXmlApplicationContext;

public class Tool {

	private String propertiesName = "kite9.properties";

	public void go(String[] args) throws Exception {
		propertiesName = parsePropertiesName(args);
		Properties props = getPreferences();
		parseArguments(args, props);
		go(props);
	}

	public void go(Properties props) {
		Kite9Runner runner;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		GenericXmlApplicationContext context = createSpringContext(props, classLoader);

		runner = (Kite9Runner) context.getBean("runner");
		runner.process();
		Thread.currentThread().setContextClassLoader(classLoader);
	}

	public GenericXmlApplicationContext createSpringContext(Properties props, ClassLoader classLoader) {
		PropertyOverrideConfigurer poc = new PropertyOverrideConfigurer();
		poc.setIgnoreInvalidKeys(true);
		poc.setProperties(props);
		GenericXmlApplicationContext context = new GenericXmlApplicationContext();
		context.getBeanFactory().setBeanClassLoader(classLoader);
		// System.out.println("Context class loader: " +
		// context.getClass().getClassLoader());
		context.setClassLoader(classLoader);
		context.setValidating(false);
		context.load("classpath:/kite9-configuration.xml");
		context.addBeanFactoryPostProcessor(poc);
		context.refresh();
		Kite9Context runContext = (Kite9Context) context.getBean("context");
		Thread.currentThread().setContextClassLoader(runContext.getUserClassLoader());
		return context;
	}

	/**
	 * Processes the command line arguments and overrides the properties with
	 * them
	 */
	protected void parseArguments(String[] args, Properties defaults) {
		for (int i = 0; i < args.length; i++) {
			String param = args[i];
			if (param.contains("=")) {
				String name = args[i].substring(0, args[i].indexOf("="));
				String value = args[i].substring(name.length() + 1);
				defaults.put(name, value);
			}
		}

	}

	/**
	 * Processes the command line arguments and overrides the properties with
	 * them
	 */
	protected String parsePropertiesName(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-p")) {
				if (args.length > i + 1)
					return args[i + 1];
				else
					throw new Kite9ProcessingException("Missing Properties file name after argument -p");
			}
		}

		return propertiesName;
	}

	protected Properties getPreferences() throws IOException {
		return PreferenceLoader.getPreferences(propertiesName);
	}
}
