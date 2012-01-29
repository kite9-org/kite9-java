package org.kite9.framework.logging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Very simple abstraction class for all logging functions.  
 * 
 * @author robmoffat
 *
 */
public class Kite9Log {

	Logable logFor;
	
	static boolean logging = isLoggingOn();

	private static boolean isLoggingOn() {
		String propVal = System.getProperty("kite9.logging");
		if (propVal==null) {
			return true;
		}
		
		propVal=propVal.toLowerCase().trim();
		
		if ("on".equals(propVal) || "true".equals(propVal) || "yes".equals(propVal)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void setLogging(boolean state) {
		logging = state;
	}
	
	public boolean go() {
		return !logging;
	}

	public Kite9Log(Logable o) {
		this.logFor = o;
	}

	public void send(String string) {
		if (logFor.isLoggingEnabled() && logging)
			System.out.println(logFor.getPrefix() + " " + string);
	}

	public void send(String prefix, Collection<?> items) {
		if (logFor.isLoggingEnabled() && logging) {
			System.out.println(logFor.getPrefix() + " " + prefix);

			StringBuffer sb = new StringBuffer();
			for (Object o : items) {
				sb.append("\t");
				sb.append(o.toString());
				sb.append("\n");
			}

			System.out.println(sb.toString());

		}
	}

	public void send(String prefix, Table t) {
		if (logFor.isLoggingEnabled() && logging) {
			StringBuffer sb = new StringBuffer(1000);
			t.display(sb);
			send(prefix + sb.toString());
		}
	}

	public void send(String prefix, Map<?, ?> items) {
		if (logFor.isLoggingEnabled() && logging) {
			System.out.println(logFor.getPrefix() + " " + prefix);
			Table t = new Table();
			Set<?> keys = items.keySet();
			List<Object> keyList = new ArrayList<Object>(keys);
			Collections.sort(keyList, new Comparator<Object>() {

				public int compare(Object o1, Object o2) {
					return o1.toString().compareTo(o2.toString());
				}
				
			});

			for (Object object : keyList) {
				t.addRow(new Object[] { "\t", object.toString(), items.get(object).toString() });
			}

			StringBuffer sb = new StringBuffer();
			t.display(sb);
			System.out.println(sb.toString());
		}
	}

	public void error(String string) {
	    System.err.println(logFor.getPrefix()+" "+string);
	}

	public void error(String string, Throwable e) {
	    System.err.println(logFor.getPrefix()+" "+string);
	    e.printStackTrace();
	}
}
