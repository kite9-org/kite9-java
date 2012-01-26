/**
 * 
 */
package org.kite9.tool.scanner;

import org.junit.Assert;
import org.kite9.framework.server.WorkItem;
import org.kite9.tool.listener.BuildListener;

public class MockBuildListener implements BuildListener {

    public MockBuildListener(String[] toContain) {
	super();
	this.toContain = toContain;
    }

    protected StringBuffer out = new StringBuffer();
    
    String[] toContain;
    
    public void initialize() {
    }

    public boolean canProcess(WorkItem designItem) {
        return true;
    }

    public void finished() {
	for (int i = 0; i < toContain.length; i++) {
	    Assert.assertTrue(out.toString().contains(toContain[i]));
	    
	}
    }

    public void process(WorkItem designItem) {
        out.append(designItem.getDesignItem());
        out.append("\n");
    }
    
    
}