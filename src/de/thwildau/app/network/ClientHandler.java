package de.thwildau.app.network;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import de.thwildau.app.amber.LoginActivity;
import de.thwildau.info.ClientMessage;
import de.thwildau.model.Event;

public class ClientHandler extends IoHandlerAdapter {

	private boolean finished;

	public boolean isFinished() {
		return finished;
	}

	@Override
	public void sessionOpened(IoSession session) {
		// send summation requests
	}

	@Override
	public void messageReceived(IoSession session, Object message) {
		ClientMessage msg = (ClientMessage) message;
		switch (msg.getId()) {
		case ERROR:
			System.out.println(msg.getContent());
			break;
		case REGISTER:
			System.out.println(msg.getContent());
			break;
		case LOGIN:            	
			System.out.println(msg.getContent());
			break;
		case EVENT:
			System.out.println(((Event)msg.getContent()).getEventImage());
		default:
			break;
		}
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		cause.printStackTrace();
		session.close(true);
	}
}