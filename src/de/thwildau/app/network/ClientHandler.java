package de.thwildau.app.network;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import de.thwildau.app.amber.LoginActivity;
import de.thwildau.app.amber.SignInActivity;
import de.thwildau.app.amber.SplashScreenActivity;
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
		case LOGIN_CHECK:
			UserData userData = (UserData) msg.getContent();
			if(userData != null){
				Intent intent1 = new Intent(SplashScreenActivity.getContext(), VehicleActivity.class);
				intent1.putExtra("Vehiclelist", userData.getVehicles());
				intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				SplashScreenActivity.getContext().startActivity(intent1);
			}
			else{
				System.out.println("Not online");
				System.out.println(LoginActivity.getContext());
				Intent intent1 = new Intent(SplashScreenActivity.getContext(), LoginActivity.class);
				intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				SplashScreenActivity.getContext().startActivity(intent1);
			}
			break;
		case LOGIN:
			System.out.println(msg.getContent());
			userData = (UserData)msg.getContent();
			int userID = userData.getUserID();
			SharedPreferences prefs = LoginActivity.getContext().getSharedPreferences("de.thwildau", Context.MODE_PRIVATE);
			prefs.edit().putInt("userID", userID).commit();
			System.out.println("LENGTH " + userData.getVehicles().size());
			for(Vehicle v : userData.getVehicles()){
				System.out.println(v);
				if(v != null){
					for(Event e : v.getEventList())
						System.out.println(e.getLatitude());
				}
			}
			Intent intent = new Intent(SignInActivity.getContext(), VehicleActivity.class);
			intent.putExtra("Vehiclelist", userData.getVehicles());
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			SignInActivity.getContext().startActivity(intent);
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