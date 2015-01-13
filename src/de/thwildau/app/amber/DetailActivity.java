package de.thwildau.app.amber;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import de.thwildau.app.network.NetworkClient;
import de.thwildau.info.ClientMessage;
import de.thwildau.model.Event;
import de.thwildau.util.Constants;

/**
 * This class shows the detailed event information. Data like position, time and
 * a snapshot of the cameras will be shown.
 * @author Kulla
 * @version 1.0
 * @since 2015-01-12
 * @see ActionBarActivity
 */
public class DetailActivity extends ActionBarActivity {

	private boolean isNotification=false;
	private Event event;
	private static Context context;
	private String parsedDate;
	private String parsedTime;

	/**
	 * Called when the activity is first created. It sets the activity content data 
	 * like view layout and title.
	 * @param savedInstanceState The previously state of the activity.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		context = this.getApplicationContext();

		View rootview = findViewById(R.id.eventdetail_textView2).getRootView();
		rootview.setBackgroundColor(Color.BLACK);

		/* Register broadcast receiver for receive server responses
		 * {@link DetailActivity#onReceive(Context, Intent)}
		 */
		LocalBroadcastManager.getInstance(this).registerReceiver(MessageReceiver, 
				new IntentFilter("single-event-response"));

		Intent intent = getIntent();

		/*By clicking on a notification the application jumps to this activity
		 * to show detailed information. The information data set will be requested
		 * from the server and then displayed on the screen (in onReceive() method).
		 */
		if(intent.getExtras().get("EventID")!=null){

			Object[] intent_array = new Object[2];
			int eventID = Integer.parseInt((String) intent.getExtras().get("EventID"));
			int obuID = (int)intent.getExtras().get("obuID");
			intent_array[0] = eventID;
			intent_array[1] = obuID;

			if(NetworkClient.getSession() == null){
				NetworkClient.connect();
			}
			while(NetworkClient.getSession() == null);
			NetworkClient.getSession().write(new ClientMessage(ClientMessage.Ident.EVENT_REQUEST,  intent_array));

		}else{

			Object[] intentarray = (Object[]) intent.getExtras().get("Event");
			Event event = (Event) intentarray[0];
			isNotification = (boolean) intentarray[1];

			/*set event data to display*/
			setEventData(event);

		}
	}

	/**
	 * Method to parse the server generated time string to a readable date.
	 * @param source The server generated time string.
	 * @return String The readable date string.
	 */
	public String parseTimeStampDate(String source){
		String[] array = source.split("-");
		String day = array[0].substring(0,2);
		String month = array[0].substring(2,4);
		String year = array[0].substring(4,8);
		String parsedDate = day+"."+month+"."+year;
		return parsedDate;
	}

	/**
	 * Method to parse the server generated time string to a readable time.
	 * @param source The server generated time string.
	 * @return String The readable time string.
	 */
	public String parseTimeStampTime(String source){
		String[] array = source.split("-");
		String hour = array[1].substring(0,2);
		String minute = array[1].substring(2,4);
		String second = array[1].substring(4,6);
		String parsedTime = hour+":"+minute+":"+second;
		return parsedTime;
	}

	/**
	 * This method handles the backpress action by the user. If the user click through the
	 * application and clicks on an event in the eventlist {@link EventsActivity} the backpress
	 * action is the usual one. But if the user had clicked on the notification the backpress 
	 * action returns him to the eventlist although there was no reversible application click flow.
	 */
	@Override
	public void onBackPressed() {
		if(isNotification){
			int vehicle_id = event.getVehicleID();
			if(NetworkClient.getSession() == null){
				NetworkClient.connect();
			}
			while(NetworkClient.getSession() == null);

			NetworkClient.getSession().write(new ClientMessage(ClientMessage.Ident.GET_EVENTLIST_BACKPRESS, vehicle_id));
		}else{
			super.onBackPressed();
		}
	};

	/**
	 * This method sets the event information data to the textfield so it will be
	 * displayed on the screen.
	 * @param event The event object which information should be displayed.
	 */
	public void setEventData(Event event){

		if(event.getTimeStamp().equals("0")){
			parsedDate = "0.0.0000";
			parsedTime = "00:00";
		}else{
			parsedDate = parseTimeStampDate(event.getTimeStamp());
			parsedTime = parseTimeStampTime(event.getTimeStamp());
		}

		((TextView)(findViewById(R.id.eventdetail_textView2))).setText(Constants.TEXT_EDETAIL_DATE+parsedDate);
		((TextView)(findViewById(R.id.eventdetail_textView3))).setText(Constants.TEXT_EDETAIL_TIME+parsedTime);
		((TextView)(findViewById(R.id.eventdetail_textView4))).setText(Constants.TEXT_EDETAIL_LAT+event.getLatitude());
		((TextView)(findViewById(R.id.eventdetail_textView5))).setText(Constants.TEXT_EDETAIL_LONG+event.getLongitude());

		String event_detail_text = Constants.EVENT_TEXT_DEFAULT;
		String title = (""+event.getVehicleName()+": Event");

		if((event.getType()).equals(Constants.EVENT_TYPE_ACC)){
			event_detail_text = Constants.EVENT_TEXT_ACC;
			title = ""+event.getVehicleName()+": Event \""+Constants.EVENT_TYPE_ACC_GER+"\"";
		}else if((event.getType()).equals(Constants.EVENT_TYPE_FUEL)){
			event_detail_text = Constants.EVENT_TEXT_FUEL;
			title = ""+event.getVehicleName()+": Event \""+Constants.EVENT_TYPE_FUEL_GER+"\"";
		}else if((event.getType()).equals(Constants.EVENT_TYPE_SPEED)){
			event_detail_text = Constants.EVENT_TEXT_SPEED;
			title = ""+event.getVehicleName()+": Event \""+Constants.EVENT_TYPE_SPEED_GER+"\"";
		}else if((event.getType()).equals(Constants.EVENT_TYPE_TURN)){
			event_detail_text = Constants.EVENT_TEXT_TURN;
			title = ""+event.getVehicleName()+": Event \""+Constants.EVENT_TYPE_TURN_GER+"\"";
		}else if((event.getType()).equals(Constants.EVENT_TYPE_GYRO)){
			event_detail_text = Constants.EVENT_TEXT_GYRO;
			title = ""+event.getVehicleName()+": Event \""+Constants.EVENT_TYPE_GYRO_GER+"\"";
		}

		setTitle(title);

		((TextView)(findViewById(R.id.eventdetail_textView6))).setText(""+event_detail_text);

		Bitmap bmp = BitmapFactory.decodeByteArray(event.getImage(), 0, event.getImage().length);
		ImageView image = (ImageView) findViewById(R.id.eventdetail_imageView);
		image.setImageBitmap(Bitmap.createScaledBitmap(bmp, 640, 480, false));

	}

	/* Create broadcast receiver to receive server responses.
	 * It's for the click on the notification by the user, which sends a request to
	 * the server. The resulting response message of the server contains the event data
	 * which can handled with the broadcast receiver.
	 */
	private BroadcastReceiver MessageReceiver = new BroadcastReceiver() {

		/**
		 * The broadcast receiver is listening for a receive message of the server.
		 * The server is sending the receive message if the user clicks on the displayed
		 * event notification. The receive message contains the detailed event information
		 * (geodata, image) in an event object that will be displayed in the activity 
		 * {@link DetailActivity#setEventData(Event)}. The boolean attribute isNotification 
		 * will be set to handle the backpress action in the application 
		 * {@link DetailActivity#onBackPressed()}. 
		 */
		@Override
		public void onReceive(Context context, Intent intent) {

			if(intent.getExtras().get("single-event-response-extra")!=null){

				isNotification=true;

				event = (Event) intent.getExtras().get("single-event-response-extra");

				setEventData(event);

			}
		}
	};

	/**
	 * This method return the current activity context.
	 * @return Context Context of the current activity.
	 */
	public static Context getContext() {
		return context;
	}
}
