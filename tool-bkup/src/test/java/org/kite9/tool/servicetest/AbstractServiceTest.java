package org.kite9.tool.servicetest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.kite9.diagram.adl.Context;
import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.adl.Glyph;
import org.kite9.diagram.adl.Link;
import org.kite9.diagram.adl.Symbol;
import org.kite9.diagram.adl.Symbol.SymbolShape;
import org.kite9.diagram.adl.TextLine;
import org.kite9.diagram.position.Layout;
import org.kite9.diagram.primitives.Contained;
import org.kite9.framework.common.HelpMethods;

public class AbstractServiceTest {
	
	public static final String ROOT_URL_2 =  "http://kite9.com/post";
	
	public static final String ROOT_URL = "http://localhost:8080/grails-kite9-projects/post";

	public static final String SERVICE_USER_NAME = "rob";
	
	public static final String SECRET_ID = "TEST123ABC";

	
	public String getRootURL() {
		String url = System.getProperty("servicetest.url");
		url = (url == null) ? ROOT_URL : url;
		System.out.println("Using Service: "+url);
		return url;
	}

	public void sendErrorEmail(Throwable t) {
		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", "server.kite9.org");
			Session session = Session.getInstance(props);
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("servicetest@kite9.com"));
			msg.setRecipient(RecipientType.TO, new InternetAddress("rob@kite9.com"));
			msg.setSubject("Failure in Service Test: "+this.getClass().getName());
			StringWriter sw = new StringWriter(10000);
			PrintWriter pw = new PrintWriter(sw);
			t.printStackTrace(pw);
			pw.close();
			msg.setText(sw.toString());
			Transport.send(msg);
		} catch (Exception e) {
		} finally {
			t.printStackTrace();
		}
	}
	
	
	public Diagram generateDiagram(int containers, int connections, int glyphs, boolean layouts, String id) {
		Random r = new Random();
		Context[] contexts = new Context[containers];
		Glyph[] items = new Glyph[glyphs];
		for (int i = 0; i < items.length; i++) {
			Glyph g = new Glyph("item:g"+i, "", "g"+i, HelpMethods.createList(new TextLine("Some Text", HelpMethods.createList(new Symbol("Some text", 'S', SymbolShape.CIRCLE)))), null);
			items[i] = g;
		}
		
		int tc = 0;
		
		while (tc  < connections) {
			int g1 = r.nextInt(items.length);
			int g2 = r.nextInt(items.length);
			
			if (g1 != g2) {
				Glyph g1g = items[g1];
				Glyph g2g = items[g2];
				if (!g1g.isConnectedDirectlyTo(g2g)) {
					new Link(g1g, g2g);
					tc ++;
				}
			}
		}
		
		for (int i = 0; i < contexts.length; i++) {
			Layout l = layouts ? Layout.values()[r.nextInt(Layout.values().length)] : null;
			contexts[i] = new Context("c"+i, new ArrayList<Contained>(), true, new TextLine("Context "+i), l);
		}
		
		for (int i = 0; i < items.length; i++) {
			int c = r.nextInt(containers);
			contexts[c].getContents().add(items[i]);
		}
		
		List<Contained> cl = new ArrayList<Contained>(items.length);
		Collections.addAll(cl, contexts);
		
		Diagram out = new Diagram(id, cl, null);
		out.setName("Servicetest Diagram "+id);
		return out;
	}
}
