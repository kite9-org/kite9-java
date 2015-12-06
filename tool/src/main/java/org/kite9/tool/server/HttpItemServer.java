package org.kite9.tool.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.zip.ZipInputStream;

import org.kite9.diagram.adl.Diagram;
import org.kite9.framework.common.Kite9ProcessingException;
import org.kite9.framework.common.RepositoryHelp;
import org.kite9.framework.serialization.XMLHelper;
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

	public ZipInputStream serve(WorkItem item) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(10000);
		serve(item, baos);

		return new ZipInputStream(new ByteArrayInputStream(baos.toByteArray()));
	}

	public void serve(WorkItem item, OutputStream os) throws IOException {

		try {

			URL u = new URL(url);
			URLConnection conn = createConnection(u);

			OutputStream params = conn.getOutputStream();
			OutputStreamWriter w = new OutputStreamWriter(params);
			writeParams(w, item);
			w.flush();
			w.close();

			InputStream image = conn.getInputStream();
			RepositoryHelp.streamCopy(image, os, true);
		} catch (MalformedURLException e) {
			throw new Kite9ProcessingException("Could not connect to diagram server", e);
		} catch (IOException e) {
			throw new Kite9ProcessingException("Could not retrieve diagram server from " + url, e);
		}

	}

	private void writeParams(OutputStreamWriter w, WorkItem item) throws IOException {
		XMLHelper helper = new XMLHelper();
		encodeField(w, "xml", helper.toXML((Diagram) item.getDesignItem()), true);
		encodeField(w, "projectSecretId", item.getProjectSecretKey(), true);
		encodeField(w, "userSecretId", item.getUserSecretKey(), true);
		encodeField(w, "format", "PNG,MAP", false);
	}

	protected void encodeField(OutputStreamWriter w, String field, String value, boolean continues) throws IOException, UnsupportedEncodingException {
		String encoding = "UTF-8";
		w.write(URLEncoder.encode(field, encoding));
		w.write("=");
		w.write(URLEncoder.encode(value, encoding));
		if (continues) {
			w.write("&");
		}
	}

}
