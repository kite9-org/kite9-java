package org.kite9.tool.servicetest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.imageio.ImageIO;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.kite9.diagram.adl.Diagram;
import org.kite9.framework.common.RepositoryHelp;
import org.kite9.framework.server.Format;
import org.kite9.tool.server.HttpItemServer;

/**
 * Tests the secret-key approach to sending diagrams.  Requests each kind of format from the server.
 * 
 * @author robmoffat
 *
 */
public class FormatsTest extends AbstractServiceTest {

	File dir;
	long t;
	Diagram d;
	HttpItemServer http;
	
	
	@Before
	public void createDiagram() {
		
	}
	
	@Before
	public void setup() {
		this.dir = new File("target/functional-test/SecretServiceTest");
		dir.mkdirs();
		
		if (d == null) {
			d = generateDiagram(2, 5, 8, true, "servicetest-formats");
		}
		
		if (http == null) {
			http = new HttpItemServer();
			http.setUrl(getRootURL());
		}
		
		t = System.currentTimeMillis();
	}
	
	
	@Test
	public void requestPNGFile() throws Exception {
		try {
			InputStream is = http.serve(d, SECRET_ID, SERVICE_USER_NAME, Format.PNG);

			BufferedImage bi = ImageIO.read(is);
			Assert.assertTrue(bi.getWidth() > 100);
			Assert.assertTrue(bi.getHeight() > 100);
			File png = new File(dir, "diagram.png");
			ImageIO.write(bi, "PNG", png);
			
			checkTime();
		} catch (Exception e) {
			sendErrorEmail(e);
			throw e;
		}
	}

	protected void checkTime() {
		// check time
		t = System.currentTimeMillis() - t;
		Assert.assertTrue("Test took too long: " +t, t < 12000);
	}
			
	@Test
	public void requestMapFile() throws Exception {
		try {
			// now get the map
			InputStream is = http.serve(d, SECRET_ID, SERVICE_USER_NAME, Format.MAP);
			readInStringAndWriteToFile(new File(dir, "diagram.map"), is);
			checkTime();
		} catch (Exception e) {
			sendErrorEmail(e);
			throw e;
		}
	}
			

	@Test
	public void requestXMLFile() throws Exception {
		try {
			// get back arranged xml
			InputStream is = http.serve(d, SECRET_ID, SERVICE_USER_NAME, Format.XML);
			readInStringAndWriteToFile(new File(dir, "arranged.xml"), is);
			checkTime();
		} catch (Exception e) {
			sendErrorEmail(e);
			throw e;
		}
	}
			
	@Test
	public void requestPDFFile() throws Exception {
		try {
			// finally get the PDF
			InputStream is = http.serve(d, SECRET_ID, SERVICE_USER_NAME, Format.PDF);
			File pdf = new File(dir, "diagram.pdf");
			FileOutputStream fos = new FileOutputStream(pdf);
			RepositoryHelp.streamCopy(is, fos, true);
			Assert.assertTrue(pdf.getTotalSpace()> 200);
			checkTime();
		} catch (Exception e) {
			sendErrorEmail(e);
			throw e;
		}
	}

	protected String readInStringAndWriteToFile(File f, InputStream is)
			throws IOException {
		InputStreamReader isr = new InputStreamReader(is);
		StringWriter sw = new StringWriter();
		RepositoryHelp.streamCopy(isr, sw, false);
		String s = sw.toString();
		Assert.assertTrue(s.length() > 100);
		FileWriter fw = new FileWriter(f);
		fw.write(s);
		fw.close();
		return s;
	}
	
}
