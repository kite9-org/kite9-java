package org.kite9.framework.repository;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Provides local, location-independent way of storing and retrieving the artifacts 
 * generated for design items.
 * 
 * The repository is expected to remain unchanged by external agents for the lifetime of the build 
 * runner process. 
 * 
 * 
 * @author moffatr
 *
 */
public interface Repository<K> {

    public OutputStream store(String subjectId, String name, String type) throws IOException;
    
    public InputStream retrieve(String subjectId, String name, String type) throws IOException;
    
    public K getAddress(String subjectId, String name, String type, boolean create) throws IOException;
    
    public OutputStream store(K address) throws IOException;
    
    public InputStream retrieve(K address) throws IOException;
    
    public void clear(String subjectId, String name) throws IOException;
}
