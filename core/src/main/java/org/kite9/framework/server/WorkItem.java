package org.kite9.framework.server;

/**
 * Details of a single diagram request for the diagram server.
 * 
 * @author moffatr
 *
 */
public interface WorkItem {

    public String getSubjectId();
    
    public String getName();
    
    public Object getDesignItem();
    
    public String getProjectSecretKey();
    
    public String getUserSecretKey();
    
    /**
     * Comma-separated list of formats to return
     */
    public String getFormats();
    
}
