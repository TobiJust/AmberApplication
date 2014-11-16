package de.thwildau.app.network;

import java.net.InetSocketAddress;
import java.util.Properties;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 * Network client that connects to the server with the MINA libraries.
 * @author Tobias Just
 */
public class NetworkClient implements Runnable{

	public final static String CONNECTION_TIMEOUT = "ConnectionTimeout";
	public final static String HOST_NAME = "Hostname";
	public final static String PORT = "Port";

	private Properties properties = null;	
	private static IoSession session;

	public NetworkClient(Properties properties) {
		this.properties = properties;
	}

	public void run() {

		NioSocketConnector connector = new NioSocketConnector();

		// Connection timeout 5000 aus Konfigurationsdatei laden
		connector.setConnectTimeoutMillis(Integer.parseInt(properties.getProperty(CONNECTION_TIMEOUT)));
		//		connector.setConnectTimeoutMillis(5000);
		connector.getFilterChain().addLast("codec",	new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		connector.getFilterChain().addLast("logger", new LoggingFilter());
		connector.setHandler(new ClientHandler());

		while(true) {
			try {
				String host = properties.getProperty(HOST_NAME);
				int port = Integer.parseInt(properties.getProperty(PORT));
				System.out.println("Port " + port);
				ConnectFuture future = connector.connect(new InetSocketAddress(host, port));
				future.awaitUninterruptibly();

				session = future.getSession();
				break;
			} catch (RuntimeIoException e) {
				e.printStackTrace();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException ex) {
					//...
				}
			}
		}
		session.getCloseFuture().awaitUninterruptibly();
		connector.dispose();
	}

	public static IoSession getSession() {
		return session;
	}

}