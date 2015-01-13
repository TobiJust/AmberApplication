package de.thwildau.app.network;


import java.util.ArrayList;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import de.thwildau.app.amber.DetailActivity;
import de.thwildau.app.amber.EventsActivity;
import de.thwildau.app.amber.LoginActivity;
import de.thwildau.app.amber.SignInActivity;
import de.thwildau.app.amber.SplashScreenActivity;
import de.thwildau.app.amber.VehicleActivity;
import de.thwildau.info.ClientMessage;
import de.thwildau.model.Event;
import de.thwildau.model.UserData;
import de.thwildau.model.Vehicle;

/**
 * This class manages the server responses from the server. It checks the message
 * content and passes data to the particular activity.
 * @author Kulla, Just
 * @version 1.0
 * @since 2015-01-12
 * @see IoHandlerAdapter
 */
public class ClientHandler extends IoHandlerAdapter{

	private boolean finished;
	private static Context context;

	/**
	 * This method checks if the session is finished.
	 * @return boolean The Value if the session is finished.
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * This method checks if a session is opened.
	 * @param session The current session.
	 */
	@Override
	public void sessionOpened(IoSession session) {
		/*send summation requests*/
	}

	/**
	 * This method handles the server responses and passes data to the
	 * particular activities.
	 * @param session The current session.
	 * @param message The message object from the server.
	 */
	@Override
	public void messageReceived(IoSession session, Object message) {

		ClientMessage msg = (ClientMessage) message;

		switch (msg.getId()) {
		case ERROR:
			Intent error_intent = new Intent("error-broadcast");
			error_intent.putExtra("error-broadcast-extra", true);
			LocalBroadcastManager.getInstance(getContext()).sendBroadcast(error_intent);
			break;
		case REGISTER:
			/*nothing*/
			break;
		case LOGIN_CHECK:
			UserData userData = (UserData) msg.getContent();
			if(userData != null){
				Intent intent1 = new Intent(SplashScreenActivity.getContext(), VehicleActivity.class);
				intent1.putExtra("Vehiclelist", userData.getVehicles());
				intent1.putExtra("Userid", userData.getUserID());
				intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				SplashScreenActivity.getContext().startActivity(intent1);
			}else{
				Intent intent1 = new Intent(SplashScreenActivity.getContext(), LoginActivity.class);
				intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				SplashScreenActivity.getContext().startActivity(intent1);
			}
			break;
		case LOGIN:

			userData = (UserData)msg.getContent();
			int userID = userData.getUserID();
			SharedPreferences prefs = LoginActivity.getContext().getSharedPreferences("de.thwildau", Context.MODE_PRIVATE);
			prefs.edit().putInt("userID", userID).commit();

			for(Vehicle v : userData.getVehicles()){
				System.out.println(v);
				if(v != null){
					for(Event e : v.getEventList())
						System.out.println(e.getLatitude());
				}
			}
			Intent intent = new Intent(SignInActivity.getContext(), VehicleActivity.class);
			ArrayList<Vehicle> vehiclelist = userData.getVehicles();

			intent.putExtra("Vehiclelist", vehiclelist);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			SignInActivity.getContext().startActivity(intent);
			break;
		case LOGOUT:
			Intent intent_logout = new Intent(VehicleActivity.getContext(), LoginActivity.class);
			intent_logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			VehicleActivity.getContext().startActivity(intent_logout);
			break;
		case EVENT_REQUEST:
			Event ev = (Event)msg.getContent();		
			Intent eventintent = new Intent("single-event-response");
			eventintent.putExtra("single-event-response-extra", ev);
			LocalBroadcastManager.getInstance(getContext()).sendBroadcast(eventintent);
			break;
		case EVENT_DETAIL:
			Object[] eventarray = new Object[2];
			Event event = (Event)msg.getContent();
			eventarray[0] = event;
			eventarray[1] = false;
			Intent startEventDetailActivity = new Intent(EventsActivity.getContext(),
					DetailActivity.class);
			startEventDetailActivity.putExtra("Event", eventarray);
			startEventDetailActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			EventsActivity.getContext().startActivity(startEventDetailActivity);
			break;
		case REGISTER_VEHICLE:
			Vehicle added_vehicle = (Vehicle)msg.getContent();
			Intent intent2 = new Intent("event-new-vehicle-id");
			intent2.putExtra("new-vehicle-extra", added_vehicle);
			LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent2);
			break;
		case UNREGISTER_VEHICLE:		
			int deleteposition = (int)msg.getContent();
			Intent unregister_intent = new Intent("unregister-response");
			unregister_intent.putExtra("unregister-response-extra", deleteposition);
			LocalBroadcastManager.getInstance(getContext()).sendBroadcast(unregister_intent);
			break;
		case TOGGLE_ALARM:
			Object[] alarmarray = (Object[])msg.getContent();
			Intent intent3 = new Intent("toggle-alarm");
			intent3.putExtra("toggle-alarm-extra", alarmarray);
			LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent3);

			intent3 = null;

			break;
		case GET_EVENTLIST:
			Object[] eventlistarray = new Object[3];
			Object[] serverarray = new Object[2];
			serverarray = (Object[])msg.getContent();			
			eventlistarray[0] = serverarray[0];/*Event*/
			eventlistarray[1] = false;
			eventlistarray[2] = serverarray[1];/*VehicleName*/
			Intent startEventActivity = new Intent(VehicleActivity.getContext(),
					EventsActivity.class);
			startEventActivity.putExtra("Eventlist", eventlistarray);
			startEventActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			VehicleActivity.getContext().startActivity(startEventActivity);
			break;
		case GET_EVENTLIST_BACKPRESS:
			Object[] eventlistarray2 = new Object[3];
			Object[] serverarray2 = new Object[2];
			serverarray2 = (Object[])msg.getContent();
			eventlistarray2[0] = serverarray2[0];
			eventlistarray2[1] = true;
			eventlistarray2[2] = serverarray2[1];
			Intent startEventActivity2 = new Intent(DetailActivity.getContext(),
					EventsActivity.class);
			startEventActivity2.putExtra("Eventlist", eventlistarray2);
			startEventActivity2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			DetailActivity.getContext().startActivity(startEventActivity2);	
			break;
		case GET_VEHICLELIST_BACKPRESS:		
			ArrayList<Vehicle> vehiclelist2 = (ArrayList<Vehicle>)msg.getContent();
			Intent startVehicleActivity = new Intent(EventsActivity.getContext(),
					VehicleActivity.class);
			startVehicleActivity.putExtra("Vehiclelist", vehiclelist2);
			startVehicleActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			EventsActivity.getContext().startActivity(startVehicleActivity);
			break;
		default:
			break;
		}
	}

	/**
	 * This method handles the 
	 * @param session The current session.
	 * @param cause The throwable cause.
	 */
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		cause.printStackTrace();
		session.close(true);
	}

	/**
	 * This method returns the application context.
	 * @return Context The application context.
	 */
	public static Context getContext(){
		return context;
	}
}