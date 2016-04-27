package org.kite9.tool.context;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractContextualizable {

	private Kite9Context context;

	@Autowired
	public void setContext(Kite9Context ctx) {
		this.context = ctx;
	}

	public Kite9Context getContext() {
		return context;
	}
}
