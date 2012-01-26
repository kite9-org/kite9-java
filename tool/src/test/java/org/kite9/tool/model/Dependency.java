package org.kite9.tool.model;

import java.net.URL;
import java.util.TreeMap;

public class Dependency {

    // class has dependency on treemap and url
    
    public void someMethod() {
	new TreeMap<String, String>();
	
	URL u = getURL();
	System.out.println(u);
    }

    private URL getURL() {
	return null;
    }
}
