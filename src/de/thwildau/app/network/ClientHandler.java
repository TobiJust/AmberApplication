package de.thwildau.app.network;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import android.content.Intent;

import de.thwildau.app.amber.LoginActivity;
import de.thwildau.app.amber.VehicleActivity;
import de.thwildau.info.ClientMessage;
import de.thwildau.model.Event;
import de.thwildau.model.UserData;
import de.thwildau.model.Vehicle;

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
		System.out.println(message);
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
			UserData userData = (UserData)msg.getContent();
			System.out.println("LENGTH " + userData.getVehicles().size());
			for(Vehicle v : userData.getVehicles()){
				System.out.println(v);
				if(v != null){
					for(Event e : v.getEventList())
						System.out.println(e.getLatitude());
				}
			}
			Intent intent = new Intent(LoginActivity.getContext(), VehicleActivity.class);
			intent.putExtra("Vehiclelist", userData.getVehicles());
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			LoginActivity.getContext().startActivity(intent);
			break;
		case EVENT:
			Event ev = (Event)msg.getContent();
			System.out.println(ev.getVehicleID());
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