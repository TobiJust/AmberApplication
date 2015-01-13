package de.thwildau.app.amber;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import de.thwildau.app.network.NetworkClient;
import de.thwildau.info.ClientMessage;
import de.thwildau.model.Event;
import de.thwildau.util.Constants;

/**
 * This class displays the eventlist of an vehicle. The list objects consists of 
 * data attributes which will also displayed. The preparation and the displaying 
 * of the data is managed by another class {@link EventListAdapter}. This class 
 * manages the user inputs, calls server requests and handle the data of server 
 * responses.
 * @author Kulla
 * @version 1.0
 * @since 2015-01-08
 * @see ActionBarActivity
 */
public class EventsActivity<T> extends ActionBarActivity {

	private ArrayList<Event> eventlist;
	private EventListAdapter boxAdapter;
	private static Context context;
	private boolean isNotification = false;

	/**
	 * Called when the activity is first created. It sets the activity content data 
	 * like view layout and title. It creates the list adapter (and passes the data 
	 * that should displayed) to display the object information. Also it prepares 
	 * the list for user interaction with creating click listeners.
	 * @param savedInstanceState The previously state of the activity.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events);
		setTitle(Constants.APP_TITLE_EVENTLIST+(String)((Object[])getIntent().getExtras().get("Eventlist"))[2]);
		
		context = this.getApplicationContext();

		fillData();
		
		boxAdapter = new EventListAdapter(this, eventlist);

		ListView lv = (ListView) findViewById(R.id.listview_eventactivity);
		lv.setAdapter(boxAdapter);
		
		setEmptyList(lv);
		
		/*format listview*/
		lv.setBackgroundColor(Color.BLACK);
		lv.setDivider(new ColorDrawable(Color.parseColor("#ffcc33")));
		lv.setDividerHeight(5);
		/*focus on last event in listview*/
		lv.setSelection(boxAdapter.getCount()-1);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			/**
			 * This method handles the clicks of the user on the listelements in the 
			 * event list. It gets the object attributes and sends a server request
			 * with the specific object information to the server (to load the detail 
			 * event activity).
			 * @param parent The View of the adapter of the listview.
			 * @param view The View in wich the listview is displayed.
			 * @param position Position of the listview element.
			 * @param id ID of the listview element.
			 * @return boolean Click event succeeded.
			 */
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){

				Event clickedEvent = (Event) boxAdapter.getItem(position);
				int eventID = clickedEvent.getID();
				int obuID = clickedEvent.getVehicleID();
				
				Object[] object_array = new Object[2];
				object_array[0] = eventID;
				object_array[1] = obuID;
				
				if(NetworkClient.getSession() == null){
					NetworkClient.connect();
				}
				while(NetworkClient.getSession() == null);
				NetworkClient.getSession().write(new ClientMessage(ClientMessage.Ident.EVENT_DETAIL, object_array));
							
			}
		});
	}

	/**
	 * This method gets the intent data from the server (event objects from the 
	 * vehicle) and fills the eventlist with it.
	 */
	void fillData() {

		Intent intent = getIntent();
		Object[] objectarray = (Object[]) intent.getExtras().get("Eventlist");
		eventlist = (ArrayList<Event>) objectarray[0];
		
	}
	
	/**
	 * This method is called if the list contains no event objects. In the list an 
	 * information for the user will be displayed.
	 * @param lv The listview that should show the information.
	 */
	public void setEmptyList(ListView lv){
		TextView emptyView = new TextView(getApplicationContext());
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		emptyView.setBackgroundColor(Color.BLACK);
		emptyView.setTextColor(Color.parseColor("#ffcc33"));
		emptyView.setText(Constants.ELIST_EVENT);
		emptyView.setTextSize(20);
		emptyView.setVisibility(View.GONE);
		emptyView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

		((ViewGroup) lv.getParent()).addView(emptyView);
		lv.setEmptyView(emptyView);
	}

	/**
	 * Method to handle back press of the user. 
	 * If the user had a normal click order and ends up in this activity over 
	 * the Login and the Vehiclelist screen then the backpress is usal.
	 * But if the user had clicked on a notification and gets to this activity
	 * over DetailActivity (had pressed back in DetailActivity after Notification)
	 * this method sends a server request to load the vehiclelist because its not
	 * created yet.
	 */
	@Override
	public void onBackPressed() {
		
		Intent intent = getIntent();
		Object[] objectarray = (Object[]) intent.getExtras().get("Eventlist");
		isNotification = (boolean) objectarray[1];
		
		if(isNotification){
			SharedPreferences prefs = this.getSharedPreferences("de.thwildau", Context.MODE_PRIVATE);
			int user_id = prefs.getInt("userID", -1);
			if(NetworkClient.getSession() == null){
				NetworkClient.connect();
			}
			while(NetworkClient.getSession() == null);
			NetworkClient.getSession().write(new ClientMessage(ClientMessage.Ident.GET_VEHICLELIST_BACKPRESS, user_id));
		
		}else{
			super.onBackPressed();
		}
	}

	/**
	 * This method returns the application context.
	 * @return Context The application context.
	 */
	public static Context getContext() {
		return context;
	}
}
