package org.kite9.tool.integration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.BeforeClass;
import org.kite9.framework.common.HelpMethods;
import org.kite9.tool.Main;

/**
 * This base class runs the kite9 tool over a subset of the testing directory.  It also generates javadocs
 * for the same.  This allows the other tests to make sure that diagrams have been returned and javadocs 
 * generated.
 * 
 * @author robmoffat
 *
 */
public class AbstractRunnerTest extends HelpMethods {

	protected static final String TARGET_DOCS = "target/functional-test/docs";
	
	protected static final String LOCAL_REPO = "target/kite9-repo";
	
	private static boolean hasRun = false;
	
	@BeforeClass
	public static void runTool() throws Exception {
		if (!hasRun) {
			clearOutLocalRepo();
			generateJavadocs();
			invokeRunner();
			hasRun = true;
		}
	}

	private static void clearOutLocalRepo() throws IOException {
		File f = new File(LOCAL_REPO);
		delete(f);
		f.mkdirs();
	}
	
	public static void delete(File f) throws IOException {
		  if (f.isDirectory()) {
		    for (File c : f.listFiles())
		      delete(c);
		  }
		  if (!f.delete())
		    throw new FileNotFoundException("Failed to delete file: " + f);
		}

	private static void invokeRunner() throws Exception {

		Main.main(new String[] {
				"-p",
				"kite9-test.props",
				"scanner.base-package="
						+ AbstractRunnerTest.class.getPackage().getName() });
	}

	static class StreamGobbler extends Thread {
		InputStream is;
		String type;
		StringBuffer created = new StringBuffer(1000);

		StreamGobbler(InputStream is, String type) {
			this.is = is;
			this.type = type;
		}

		public void run() {
			try {
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				while ((line = br.readLine()) != null)
					created.append(type + ">" + line);
			} catch (IOException ioe) {
				System.out.println(created.toString());
				ioe.printStackTrace();
			}
		}
	}

	private static int exec(String cmd) {
		StreamGobbler errorGobbler = null;
		StreamGobbler outputGobbler = null;

		try {
			System.out.println("CMD: " + cmd);
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(cmd);

			// any error message?
			errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERR");

			// any output?
			outputGobbler = new StreamGobbler(proc.getInputStream(), "OUT");

			// kick them off
			errorGobbler.start();
			outputGobbler.start();

			// any error???
			int exitVal = proc.waitFor();
			System.out.println("ExitValue: " + exitVal);
			return exitVal;
		} catch (Throwable t) {
			return -1;
		} finally {
			if (outputGobbler != null)
				System.out.print(outputGobbler.created.toString());
			if (errorGobbler != null)
				System.err.print(errorGobbler.created.toString());
		}
	}

	private static void generateJavadocs() throws Exception {
		String cp = "\"" + System.getProperty("java.class.path") + "\"";
		String home = System.getProperty("java.home");

		// hack for spaces in cp-prevents error rather than fixing issue
		cp = cp.replace(' ', '_');

		String[] javadocargs = { "-d", TARGET_DOCS, "-classpath", cp,
				"-sourcepath", ("src/test/java"),
				AbstractRunnerTest.class.getPackage().getName() };

		StringBuffer args = new StringBuffer();
		for (String string : javadocargs) {
			args.append(string);
			args.append(" ");
		}

		String cmd = home + "/../bin/javadoc.exe ";
		int res = exec(cmd + args.toString());
		if (res == -1) {
			res = exec("javadoc " + args.toString());
		}

		if (res == -1) {
			throw new Exception(
					"Could not generate javadocs with either 'javadoc' or '"
							+ cmd + "'");
		}

	}
}
