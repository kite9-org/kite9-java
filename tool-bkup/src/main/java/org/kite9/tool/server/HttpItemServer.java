package org.kite9.tool.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.kite9.framework.common.Kite9ProcessingException;
import org.kite9.framework.serialization.XMLHelper;
import org.kite9.framework.server.Format;
import org.kite9.framework.server.ItemServer;
import org.kite9.framework.server.WorkItem;

/**
 * Connects to an ItemServer exposed using HTTP. Now supports HTTP proxies being
 * set manually, for maven / properties configuration
 * 
 * @author moffatr
 * 
 */
public class HttpItemServer extends HttpConnection implements ItemServer {

	public static final String REMOTE_SERVER_URL = "http://www.kite9.com/kite9/image.zip";

	String url = REMOTE_SERVER_URL;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public HttpItemServer() {
	}

	public InputStream serve(WorkItem item, String projectSecretId, String userID, Format format) {
		try {
			XMLHelper helper = new XMLHelper();
			String xml = helper.toXML(item);

			URL u = new URL(url + "?projectSecretId="+projectSecretId+"&format="+format.toString()+"&userId="+userID);
			
			URLConnection conn = createConnection(u);

			OutputStream params = conn.getOutputStream();
			OutputStreamWriter w = new OutputStreamWriter(params);
			w.write("xml=");
			w.write(xml);
			w.flush();
			w.close();

			InputStream image = conn.getInputStream();
			return image;
		} catch (MalformedURLException e) {
			throw new Kite9ProcessingException("Could not connect to diagram server", e);
		} catch (IOException e) {
			throw new Kite9ProcessingException("Could not retrieve diagram server from " + url, e);
		}
	}

}
