package de.thwildau.app.network;

import java.net.InetSocketAddress;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import de.thwildau.util.Constants;

/**
 * Network client that connects to the server with the MINA libraries.
 * @author Just, Kulla
 * @version 1.0
 * @since 2015-01-12
 * @see Runnable
 */
public class NetworkClient implements Runnable{

	public final static String CONNECTION_TIMEOUT = Constants.CONNECTION_TIMEOUT;
	public final static String HOST_NAME = Constants.HOST_NAME;
	public final static String PORT = Constants.PORT;

	private static IoSession session;

	/**
	 * Constructor
	 */
	public NetworkClient() {

	}

	/**
	 * This method handles the socket connection.
	 */
	public void run() {

		NioSocketConnector connector = new NioSocketConnector();

		connector.setConnectTimeoutMillis(Integer.parseInt(CONNECTION_TIMEOUT));
		connector.getFilterChain().addLast("codec",	new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		connector.getFilterChain().addLast("logger", new LoggingFilter());
		connector.setHandler(new ClientHandler());

		while(true) {
			try {
				String host = HOST_NAME;
				int port = Integer.parseInt(PORT);
				System.out.println("Port " + port);
				ConnectFuture future = connector.connect(new InetSocketAddress(host, port));
				future.awaitUninterruptibly();

				session = future.getSession();
				break;
			} catch (RuntimeIoException e) {
				//				e.printStackTrace();
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

	/**
	 * This method gets the Io session.
	 * @return IoSession Gets the Io session.
	 */
	public static IoSession getSession() {

		return session;
	}

	/**
	 * This method creates a new network client.
	 */
	public static void connect(){
		try {
			NetworkClient nClient = new NetworkClient();
			(new Thread(nClient)).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}