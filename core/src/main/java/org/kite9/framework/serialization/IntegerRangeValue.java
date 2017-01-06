package org.kite9.framework.serialization;

import org.apache.batik.css.engine.value.AbstractValue;

/**
 * An integer range, from and to.  
 * Used for grid positioning in each axis.
 * 
 * @author robmoffat
 *
 */
public class IntegerRangeValue extends AbstractValue {

	private int from, to;
	
	public int getFrom() {
		return from;
	}

	public int getTo() {
		return to;
	}

	public IntegerRangeValue(int from, int to) {
		this.from = from;
		this.to = to;
	}

	@Override
	public String getCssText() {
		// TODO Auto-generated method stub
		return null;
	}

	public static boolean notSet(IntegerRangeValue in) {
		return (in == null) || (in.from == -1);
	}
}
