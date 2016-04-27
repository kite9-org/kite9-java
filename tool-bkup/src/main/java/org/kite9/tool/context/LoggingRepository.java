package org.kite9.tool.context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.kite9.framework.repository.Repository;

public class LoggingRepository<X> implements Repository<X> {

	Repository<X> underlying;
	Kite9Context ctx;
	
	public LoggingRepository(Kite9Context ctx, Repository<X> underlying) {
		this.underlying = underlying;
		this.ctx = ctx;
	}
	
	public OutputStream store(String subjectId, String type)
			throws IOException {
		
		X address = underlying.getAddress(subjectId, type, true);
		return store(address);
	}

	public InputStream retrieve(String subjectId, String type)
			throws IOException {
		X address = underlying.getAddress(subjectId, type, true);
		return underlying.retrieve(address);
	}

	public X getAddress(String subjectId, String type,
			boolean create) throws IOException {
		return underlying.getAddress(subjectId,type, create);
	}

	public OutputStream store(X address) throws IOException {
		ctx.getLogger().send("Storing: "+address);
		return underlying.store(address);
	}

	public InputStream retrieve(X address) throws IOException {
		return underlying.retrieve(address);
	}

	public void clear(String subjectId) throws IOException {
		underlying.clear(subjectId);
	}


	
}
