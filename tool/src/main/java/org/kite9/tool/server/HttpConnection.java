package org.kite9.tool.server;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;


/**
 * Generalizes connecting to Kite9 services via http, with proxy support.
 * 
 * @author robmoffat
 *
 */
public class HttpConnection {	

	public synchronized URLConnection createConnection(URL u) throws IOException {
		setupProxy();
		URLConnection conn = u.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setConnectTimeout(10000);
		conn.setReadTimeout(15000);
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		resetProxy();
		return conn;
	}

	private String previousHttpProxyHost;
	private String previousHttpProxyPort;
	private String previousProxyExclusions;

	protected void resetProxy() {
		if (previousHttpProxyHost != null)
			System.setProperty("http.proxyHost", previousHttpProxyHost);
		if (previousHttpProxyPort != null)
			System.setProperty("http.proxyPort", previousHttpProxyPort);
		if (previousProxyExclusions != null)
			System.setProperty("http.nonProxyHosts", previousProxyExclusions);

	}

	protected void setupProxy() {
		previousHttpProxyHost = System.getProperty("http.proxyHost");
		previousHttpProxyPort = System.getProperty("http.proxyPort");
		previousProxyExclusions = System.getProperty("http.nonProxyHosts");
		final boolean hasProxy = (proxyHost != null && proxyHost.length() > 0);
		final boolean hasAuthentication = (proxyUser != null && proxyUser.length() > 0);

		if (hasProxy) {
			System.setProperty("http.proxyHost", proxyHost);
			System.setProperty("http.proxyPort", "" + proxyPort);
			System.getProperties().remove("http.nonProxyHosts");
		}

		if (hasProxy || hasAuthentication) {
			Authenticator.setDefault(new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					// TODO: ideally use getRequestorType() from JDK1.5 here...
					if (hasProxy && getRequestingHost().equals(proxyHost) && getRequestingPort() == proxyPort) {
						return new PasswordAuthentication(proxyUser, proxyPass.toCharArray());
					}

					if (hasAuthentication) {
						return new PasswordAuthentication(proxyUser, proxyPass.toCharArray());
					}

					return super.getPasswordAuthentication();
				}
			});
		} else {
			Authenticator.setDefault(null);
		}
	}

	private String proxyHost;
	private int proxyPort;
	private String proxyUser;
	private String proxyPass;

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getProxyUser() {
		return proxyUser;
	}

	public void setProxyUser(String proxyUser) {
		this.proxyUser = proxyUser;
	}

	public String getProxyPass() {
		return proxyPass;
	}

	public void setProxyPass(String proxyPass) {
		this.proxyPass = proxyPass;
	}


}