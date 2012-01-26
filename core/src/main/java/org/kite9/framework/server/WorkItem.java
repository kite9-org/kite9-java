package org.kite9.framework.server;

/**
 * @author moffatr
 *
 */
public interface WorkItem {

    public String getSubjectId();
    
    public String getName();
    
    public Object getDesignItem();
    
    public int getProjectId();
    
    public String getSecretKey();
    
}
