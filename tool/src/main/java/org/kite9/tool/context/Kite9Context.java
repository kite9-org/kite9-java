package org.kite9.tool.context;

import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.logging.Kite9Log;
import org.kite9.framework.repository.Repository;

/**
 * Settings specific to the current project.
 * 
 * @author moffatr
 *
 */
public interface Kite9Context {

    public Aliaser getAliaser();
    
    public Kite9Log getLogger();
    
    public String getClassPath();

    public ClassLoader getUserClassLoader();
    
    public Repository<?> getRepository();
    
    public int getProjectId();
    
    public String getSecretKey();
   
}
