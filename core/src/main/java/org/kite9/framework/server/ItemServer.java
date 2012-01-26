package org.kite9.framework.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipInputStream;

/**
 * Handles remote processing of design items, allowing a number of responses to 
 * be sent in a zip file.
 * 
 * @author moffatr
 *
 */
public interface ItemServer {

    public ZipInputStream serve(WorkItem item) throws IOException;
    
    public void serve(WorkItem item, OutputStream os) throws IOException;

}
