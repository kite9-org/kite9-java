package org.kite9.diagram.builders;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.kite9.framework.common.TestingHelp;
import org.kite9.framework.common.DiffException;
import org.kite9.framework.common.FileDiff;

public class DiagramTestingEngine extends TestingHelp {

    public static boolean checkOutputs(Class<?> theTest, String subtest, String item) throws IOException {
	try {
	    File output = prepareFileName(theTest, subtest, item);
	    File check = getHandleToFileInClasspath(theTest, subtest + "/" + item);
	    FileDiff.filesContainSameLines(output, check);
	} catch (NullPointerException e) {
	    return false;
	} catch (DiffException e) {
	    Assert.fail(e.getMessage());
	    return false;
	}

	return true;
    }


}
