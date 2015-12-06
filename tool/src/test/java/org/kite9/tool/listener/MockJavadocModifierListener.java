package org.kite9.tool.listener;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Set;

import junit.framework.Assert;

import org.kite9.framework.server.WorkItem;

public class MockJavadocModifierListener extends AbstractJavadocModifyingListener {

	int calls = 0;

	@Override
	protected void processReferences(WorkItem designItem) {
	}

	@Override
	protected void processClassLevelDoc(Class<?> cl, File htmlResource, OutputStreamWriter os, Set<WorkItem> mods2) throws IOException {
		if (cl.equals(Test2JavadocModifier.class)) {
			os.write("\n<h2>Modifying: " + cl.getCanonicalName() + "</h2>\n");
			calls++;
		}
	}

	@Override
	protected void processMethodLevelDoc(Class<?> cl, File htmlResource, String method, OutputStreamWriter os, Set<WorkItem> mods2) throws IOException {
		if (cl.equals(Test2JavadocModifier.class)) {
			os.write("\nModifying: " + cl.getCanonicalName() + ": method : " + method.substring(method.indexOf(";")+1) + "\n");
			calls++;
		}
	}

	@Override
	public void finished() {
		super.finished();
		Assert.assertEquals(4, calls);
	}

}
