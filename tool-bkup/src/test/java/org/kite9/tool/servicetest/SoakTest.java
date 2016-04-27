package org.kite9.tool.servicetest;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Random;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.primitives.DiagramElement;
import org.kite9.diagram.primitives.IdentifiableDiagramElement;
import org.kite9.diagram.primitives.Label;
import org.kite9.diagram.visitors.DiagramElementVisitor;
import org.kite9.diagram.visitors.VisitorAction;
import org.kite9.framework.common.Kite9ProcessingException;
import org.kite9.framework.common.RepositoryHelp;
import org.kite9.framework.server.Format;
import org.kite9.tool.server.HttpItemServer;

@Ignore("This test seriously hit the server.  Enable manually to give it a good hiding.")
public class SoakTest extends AbstractServiceTest {
	
	long longestResponseTime = 0;
	long totalResponseTime = 0;
	int totalRequests = 0;
	
	public synchronized void addTime(long t) {
		longestResponseTime = Math.max(t, longestResponseTime);
		totalResponseTime += t;
		totalRequests +=1;
	}
	
	HttpItemServer http;
	
	@Before
	public void setup() {
		if (http == null) {
			http = new HttpItemServer();
			http.setUrl(getRootURL());
		}
	}
	
	
	/**
	 * Results: With 50 threads, I am seeing a 533 ms response time (1000 requests)
	 * 10 threads, 173 ms response time (140 requests)
	 * 20 threads, 201 ms response time (201 requests)
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void test10x1000() throws InterruptedException {
		int threads = 100;		// users
		final int diagramsPerThread = 14;		// length of test
		final int sleepTime = 2000;  // 2 secs between each request - v. hyperactive users.
		Thread t[] = new Thread[threads];
		Random r = new Random();
		
		for (int i = 0; i < threads; i++) {
			final int threadNo = i;
			Thread.sleep(r.nextInt(sleepTime));
			t[i] = new Thread(new Runnable() {
				
				public void run() {
					testRun(diagramsPerThread, sleepTime, "thread"+threadNo);
				}
			});
			t[i].start();
		}
		
		for (int i = 0; i < threads; ++i) {
			  t[i].join ();
		}		
		
		System.out.println("Requests "+totalRequests+" average "+(totalResponseTime / totalRequests));
	}
	
	private void testRun(int times, long sleep, String prefix) {
		for (int i = 0; i < times; i++) {
			try {
				long t = System.currentTimeMillis();
				
				Diagram d = generateDiagram(2, 7, 11, true, "servicetest-soak-"+prefix+"-"+i); 
				final String xml2 = serveSizes(d);
				
				new DiagramElementVisitor().visit(d, new VisitorAction() {
					
					public void visit(DiagramElement de) {
						if ((de instanceof IdentifiableDiagramElement) && (!(de instanceof Label))) {
							String id = ((IdentifiableDiagramElement) de).getID();
							Assert.assertTrue("Couldn't find: "+id+" in "+xml2, xml2.contains(id));
						}
					}
				});
				
				Assert.assertTrue(xml2.contains("position"));
				
				
				
				t = System.currentTimeMillis() - t;
				addTime(t);
				
				if (t > 4000) {
					System.err.println("Test took too long: " +t);
				} else {
					System.out.println("Run OK, t = "+t);
				}
				
				Thread.sleep(sleep);
				
			} catch (Throwable e) {
				System.err.println(e.getMessage());
			}
		}
	}

	
	public String serveSizes(Diagram d) {
		try {
			InputStream image = http.serve(d, SECRET_ID, SERVICE_USER_NAME, Format.XML);
			InputStreamReader isr = new InputStreamReader(image);
			StringWriter sw = new StringWriter();
			RepositoryHelp.streamCopy(isr, sw, true);
			return sw.toString();
		} catch (Exception e) {
			throw new Kite9ProcessingException("Could not connect to diagram server", e);
		} 

	}
}
