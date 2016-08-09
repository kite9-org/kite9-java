package org.kite9.framework.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.kite9.diagram.adl.XMLElement;
import org.kite9.diagram.primitives.Contained;

public class HelpMethods {

    public static <X> List<X> createList(X... d1) {
    	ArrayList<X> out = new ArrayList<X>();
    	for (X x : d1) {
    		out.add(x);
    	}
    	return out;
    }
    
    public static List<XMLElement> listOf(XMLElement... d1) {
    	ArrayList<XMLElement> out = new ArrayList<XMLElement>();
    	for (XMLElement x : d1) {
    		out.add(x);
    	}
    	return out;
    }

    public static <X> Set<X> createSet(X... d1) {
    	HashSet<X> out = new HashSet<X>();
    	for (X x : d1) {
    		out.add(x);
    	}
    	return out;
    }
    
    public static <X extends Contained> Set<Contained> setOf(X... d1) {
    	HashSet<Contained> out = new HashSet<Contained>();
    	for (X x : d1) {
    		out.add(x);
    	}
    	return out;
    }

}
