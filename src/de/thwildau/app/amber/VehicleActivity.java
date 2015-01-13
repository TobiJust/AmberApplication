package de.thwildau.app.amber;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.thwildau.app.network.NetworkClient;
import de.thwildau.info.ClientMessage;
import de.thwildau.model.Vehicle;
import de.thwildau.util.Constants;

/**
 * This class displays the vehiclelist, which contains the vehicles of the user. The list objects
 * consists of data which will also displayed. The preparation and the displaying of the data is
 * managed by another class {@link VehicleListAdapter}. This class manages the user inputs, calls
 * server requests and handle the data of server responses.
 * @author Kulla
 * @version 1.0
 * @since 2015-01-08
 * @see ActionBarActivity
 */
public class VehicleActivity<T> extends ActionBarActivity{

	private Toast toast;
	private long lastBackPressTime = 1000;
	public ArrayList<Vehicle> vehiclelist = new ArrayList<Vehicle>();
	public VehicleListAdapter boxAdapter;
	public int remove_index;
	private static Context context;
	private View pressed_view;
	private ListView lv;
	private int user_id;
	private BackgroundTask task;
	private ProgressDialog dialog;

	/**
	 * Called when the activity is first created. It sets the activity content data 
	 * like view layout and title. It loads the userid from shared preferences in the
	 * local data storage. It registers the broadcast managers to broadcast server 
	 * response messages. It creates the list adapter (and passes the data that should
	 * displayed) to display the object information. Also it prepares the list for user
	 * interaction with creating click listeners.
	 * @param savedInstanceState The previously state of the activity.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vehicle);
		setTitle(Constants.APP_TITLE_VEHICLELIST);

		/*Get user id from shared preferences / local device storage*/

		SharedPreferences prefs = this.getSharedPreferences("de.thwildau", Context.MODE_PRIVATE);
		user_id = prefs.getInt("userID", -1);

		/*register modified action bar*/

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		/* Register broadcast managers for the broadcast messages of the functions 
		 * add vehicle, remove vehicle, toggle alarm.
		 */

		LocalBroadcastManager.getInstance(this).registerReceiver(MessageReceiver, 
				new IntentFilter("error-broadcast"));
		LocalBroadcastManager.getInstance(this).registerReceiver(MessageReceiver, 
				new IntentFilter("event-new-vehicle-id"));
		LocalBroadcastManager.getInstance(this).registerReceiver(MessageReceiver, 
				new IntentFilter("toggle-alarm"));
		LocalBroadcastManager.getInstance(this).registerReceiver(MessageReceiver, 
				new IntentFilter("unregister-response"));

		context = this.getApplicationContext();

		/* Create data set and fill it with data from the server.
		 * Initialize list adapter and assign it to the view.
		 * Set list design attributes and register click listener.
		 */

		fillData();

		boxAdapter = new VehicleListAdapter(this, vehiclelist);

		lv = (ListView) findViewById(R.id.vehicleactivity_listview1);
		lv.setAdapter(boxAdapter);

		setEmptyList(lv);

		lv.setBackgroundColor(Color.BLACK);
		lv.setDivider(new ColorDrawable(Color.parseColor("#ffcc33")));
		lv.setDividerHeight(5);

		lv.setOnItemClickListener(new OnItemClickListener() {

			/**
			 * This method handles the clicks of the user on the listelements in the 
			 * vehicle list. It gets the object attributes and sends a server request
			 * with the specific object information to the server (to load the list of 
			 * events).
			 * @param parent The View of the adapter of the listview.
			 * @param view The View in wich the listview is displayed.
			 * @param position Position of the listview element.
			 * @param id ID of the listview element.
			 * @return boolean Click event succeeded.
			 */
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Vehicle clickedItem = (Vehicle) boxAdapter.getItem(position);

				int vehicle_id = clickedItem.getID();

				if(NetworkClient.getSession() == null){
					NetworkClient.connect();
				}
				while(NetworkClient.getSession() == null);
				NetworkClient.getSession().write(new ClientMessage(ClientMessage.Ident.GET_EVENTLIST, vehicle_id));

				/*start progress dialog*/
				startProgressDialog(context);

			}
		});

		lv.setLongClickable(true);
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			/**
			 * This method handles the long clicks/press events of the user in the 
			 * vehiclelist to remove a vehicle from the list. It opens the popup
			 * menu and calls the remove method {@link VehicleActivity#removeListItem(int)}.
			 * @param parent The View of the adapter of the listview.
			 * @param view The View in wich the listview is displayed.
			 * @param position Position of the listview element.
			 * @param id ID of the listview element.
			 * @return boolean Click event succeeded.
			 */
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {

				PopupMenu pm = new PopupMenu(VehicleActivity.this, view);
				pm.getMenuInflater().inflate(R.menu.remove_vehicle_popup, pm.getMenu());
				pm.show();

				pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
					/**This method calls the remove method by pressing button in the popup menu.
					 * @param item Clicked item in the popup menu.
					 * @return boolean Click event succeeded.
					 */
					public boolean onMenuItemClick(MenuItem item) {  

						removeListItem(position);

						return true;  
					}  
				});  

				return true;
			}	
		});
	}

	/**
	 * This method handles the click events of the action bar icons. It contains the add
	 * vehicle dialog to add a new vehicle to the vehiclelist and the backpress event from
	 * back button.
	 * @param item Action bar item the user has clicked on.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item){

		switch (item.getItemId()){

		case R.id.vehiclelist_abb_add_vehicle:

			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			LayoutInflater inflater = getLayoutInflater();

			builder.setView(inflater.inflate(R.layout.add_vehicle_dialog, null));
			builder.setInverseBackgroundForced(true);

			final AlertDialog ad = builder.create();

			ad.setTitle(Constants.APP_TITLE_ADDVEHICLEPOPUP);

			ad.setCanceledOnTouchOutside(false);

			ad.show();

			Button okButton = (Button) ad.findViewById(R.id.add_vehicle_dialog_ok_button);
			okButton.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					try{
						int add_vehicleid = Integer.parseInt(((EditText)ad.findViewById(R.id.add_vehicle_dialog_vehiclename)).getText().toString());
						int currentid = 0;
						int check=0;
						for (int i=0; i<vehiclelist.size(); i++){
							currentid = vehiclelist.get(i).getID();				
							if(currentid!=0 && currentid==add_vehicleid){
								check=1;
								break;
							}
						}
						if(check==0){
							addListItem(add_vehicleid);
							ad.dismiss();
						}else{
							Toast.makeText(getApplicationContext(), Constants.TOAST_ADDVEHICLE_DUPLICATE,
									Toast.LENGTH_SHORT).show();
						}
					}catch(NumberFormatException e){
						Toast.makeText(getApplicationContext(), Constants.TOAST_ADDVEHICLE_NOINT,
								Toast.LENGTH_SHORT).show();
					}
				}
			});

			Button cancelButton = (Button) ad.findViewById(R.id.add_vehicle_dialog_cancel_button);
			cancelButton.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					/*close dialog*/
					ad.dismiss();
				}
			});
			return (super.onOptionsItemSelected(item));

		case android.R.id.home: 
			if (this.lastBackPressTime < System.currentTimeMillis() - 5000) {
				toast = Toast.makeText(this, Constants.TOAST_LOGOUT,
						Toast.LENGTH_SHORT);
				this.lastBackPressTime = System.currentTimeMillis();
				toast.show();
			} else if (this.lastBackPressTime < System.currentTimeMillis() + 5000) {
				this.lastBackPressTime = System.currentTimeMillis();

				if(NetworkClient.getSession() == null){
					NetworkClient.connect();
				}
				while(NetworkClient.getSession() == null);
				NetworkClient.getSession().write(new ClientMessage(ClientMessage.Ident.LOGOUT, user_id));
			}

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Method to create a modified action bar to add customized action bar buttons.
	 * @param menu The menu xml file which contains the elements.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.vehicle, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Method to handle back press / logout of the user. This method sends a logout request
	 * of the user to the server to change the database.
	 */
	@Override
	public void onBackPressed() {
		if (this.lastBackPressTime < System.currentTimeMillis() - 5000) {
			toast = Toast.makeText(this, Constants.TOAST_LOGOUT, Toast.LENGTH_SHORT);
			this.lastBackPressTime = System.currentTimeMillis();
			toast.show();
		} else if (this.lastBackPressTime < System.currentTimeMillis() + 5000) {
			this.lastBackPressTime = System.currentTimeMillis();

			if(NetworkClient.getSession() == null){
				NetworkClient.connect();
			}
			while(NetworkClient.getSession() == null);
			NetworkClient.getSession().write(new ClientMessage(ClientMessage.Ident.LOGOUT, user_id));

		}
	}

	/**
	 * This method gets the intent data from the server (vehicle objects from the user)
	 * and fills the vehiclelist with it.
	 */
	public void fillData() {

		Intent intent = getIntent();
		ArrayList<Vehicle> vehicleList = (ArrayList<Vehicle>)intent.getExtras().get("Vehiclelist");

		for (int i = 0; i < vehicleList.size(); i++) {
			vehiclelist.add(vehicleList.get(i));
		}
	}

	/**
	 * This method adds an vehicle to the vehiclelist and sends a request to the server to change
	 * the database.
	 * @param vehicle_id The unique id of an vehicle.
	 */
	public void addListItem(int vehicle_id){

		Object[] arrayobject = new Object[2];
		arrayobject[0] = user_id;
		arrayobject[1] = vehicle_id;

		if(NetworkClient.getSession() == null){
			NetworkClient.connect();
		}
		while(NetworkClient.getSession() == null);
		NetworkClient.getSession().write(new ClientMessage(ClientMessage.Ident.REGISTER_VEHICLE, arrayobject));

	}

	/**
	 * This method removes an vehicle of the vehiclelist and sends a request to the server to change
	 * the database.
	 * @param index The position/index of the vehicle object in the list.
	 */
	public void removeListItem(int index){

		Object[] arrayobject = new Object[3];
		arrayobject[0] = user_id;
		arrayobject[1] = vehiclelist.get(index).getID();
		arrayobject[2] = index;

		if(NetworkClient.getSession() == null){
			NetworkClient.connect();
		}
		while(NetworkClient.getSession() == null);
		NetworkClient.getSession().write(new ClientMessage(ClientMessage.Ident.UNREGISTER_VEHICLE, arrayobject));

	}

	/**
	 * This method changes the alarm status of a vehicle in the list and sends a request to
	 * the server. The alarm status represents the receiving of the notifications. 
	 * @param v View of the pressed listview element.
	 */
	public void toggleAlarm(View v){

		int pos = (Integer) v.getTag();

		pressed_view = v;

		Object[] arrayobject = new Object[4];
		arrayobject[0] = user_id;
		arrayobject[1] = vehiclelist.get(pos).getID();
		arrayobject[2] = !vehiclelist.get(pos).getBoxed();
		arrayobject[3] = pos;

		if(NetworkClient.getSession() == null){
			NetworkClient.connect();
		}
		while(NetworkClient.getSession() == null);
		NetworkClient.getSession().write(new ClientMessage(ClientMessage.Ident.TOGGLE_ALARM, arrayobject));

	}

	/**
	 * This method returns the application context.
	 * @return Context The application context.
	 */
	public static Context getContext() {
		return context;
	}

	/*create broadcast receiver to get messages of server responses for the functions
	 * add vehicle, remove vehicle, toggle alarm and error of any of these functions.
	 */
	private BroadcastReceiver MessageReceiver = new BroadcastReceiver() {

		/**
		 * This method receives the server responses and checks its content.
		 * Depending on its content there is different output.
		 * @param context The Context in which the receiver is running.
		 * @param intent The Intent being received.
		 */
		@Override
		public void onReceive(Context context, Intent intent) {

			/*Error intent/server response
			 *It receives the Error server response. There is a error output for the user.
			 */
			if(intent.getExtras().get("error-broadcast-extra")!=null){
				Toast.makeText(getApplicationContext(), Constants.TOAST_ERROR, Toast.LENGTH_LONG).show();
			}

			/*toggle alarm/change alarm status response
			 *It receives the toggle alarm event. The object attribute for the alarm will be set
			 *and the image which displays the alarm status will be changed.
			 */
			if(intent.getExtras().get("toggle-alarm-extra")!=null){

				Object[] alarmarray = (Object[]) intent.getExtras().get("toggle-alarm-extra");
				boolean alarmstatus = (boolean) alarmarray[0];
				int position = (int) alarmarray[1];			

				ImageView v = (ImageView) pressed_view;			

				if(alarmstatus){
					v.setImageResource(R.drawable.vehicle_alarm_on);
					vehiclelist.get(position).setBoxed(true);
				}else{
					v.setImageResource(R.drawable.vehicle_alarm_off);
					vehiclelist.get(position).setBoxed(false);	
				}
			}

			/*add vehicle response
			 *It receives the add vehicle event. The new vehicle object will be added to the 
			 *list and the view will be updated. The user gets a output.
			 */
			if(intent.getExtras().get("new-vehicle-extra")!=null){
				Vehicle new_vehicle = (Vehicle) intent.getExtras().get("new-vehicle-extra");				

				vehiclelist.add(new_vehicle);
				Toast.makeText(getApplicationContext(), Constants.TOAST_ADDVEHICLE_SUCCESS,
						Toast.LENGTH_SHORT).show();
				boxAdapter.notifyDataSetChanged();
			}

			/*remove vehicle response
			 *It receives the remove vehicle response. The vehicle will be deleted from the list
			 *and the list will be updated. If the list now is empty the method setEmptyList() 
			 *will be called. The user gets a output.
			 */
			if(intent.getExtras().get("unregister-response-extra")!=null){
				int unregisterposition = (int) intent.getExtras().get("unregister-response-extra");
				if(unregisterposition!=-1){
					vehiclelist.remove(unregisterposition);

					if(vehiclelist.size()==0){
						setEmptyList(lv);
					}

					Toast.makeText(getApplicationContext(), Constants.TOAST_REMOVEVEHICLE_SUCCESS,
							Toast.LENGTH_SHORT).show();
					boxAdapter.notifyDataSetChanged();
				}else{
					Toast.makeText(getApplicationContext(), Constants.TOAST_REMOVEVEHICLE_ERROR,
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	};

	/**
	 * This method is called if the list contains no vehicle objects. In the list an 
	 * information for the user will be displayed.
	 * @param lv The listview that should show the information.
	 */
	public void setEmptyList(ListView lv){
		TextView emptyView = new TextView(getApplicationContext());
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		emptyView.setBackgroundColor(Color.BLACK);
		emptyView.setTextColor(Color.parseColor("#ffcc33"));
		emptyView.setText(Constants.ELIST_VEHICLE);
		emptyView.setTextSize(20);
		emptyView.setVisibility(View.GONE);
		emptyView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

		((ViewGroup) lv.getParent()).addView(emptyView);
		lv.setEmptyView(emptyView);
	}

	/**
	 * This method will be called when the user clicks on a vehicle. It creates a background
	 * task which creates a progress dialog to tide over the time which is needed to load
	 * the eventlist and finally change the activity.
	 * @param context
	 */
	public void startProgressDialog(Context context){
		task = new BackgroundTask();
		task.execute();
	}

	/**
	 * Called when the activity is no longer visible to the user, because another 
	 * activity has been resumed and is covering this one.
	 */
	@Override
	protected void onStop() {
		if(dialog!=null){
			dialog.dismiss();
		}
		if(task!=null){
			task.cancel(true);
		}
		super.onStop();
	};

	/**
	 * The final call which will received before the activity is destroyed.
	 */
	@Override
	protected void onDestroy() {
		/* Unregister since the activity is about to be closed.*/
		LocalBroadcastManager.getInstance(this).unregisterReceiver(MessageReceiver);
		if(dialog!=null){
			dialog.dismiss();
		}
		if(task!=null){
			task.cancel(true);
		}
		super.onDestroy();

	}

	/**
	 * This class is for an background task which creates a progress dialog which will be 
	 * displayed while the eventlistdata of an vehicle are loading.
	 * @author Kulla
	 *
	 */
	class BackgroundTask extends AsyncTask<Void, Void, Void>{

		/**This method will be called before the initialization of the background task.
		 * This method initialize an progress dialog and show it.
		 */
		@Override
		protected void onPreExecute() {

			dialog = new ProgressDialog(VehicleActivity.this, ProgressDialog.THEME_HOLO_DARK);
			dialog.setMessage(Constants.PD_LOADING);
			dialog.setCancelable(false);
			dialog.show();
		}

		/**
		 * This method will be called when the background task is cancelled. The progress
		 * dialog will be closed.
		 */
		@Override
		protected void onCancelled(Void result) {
			dialog.dismiss();
		};

		/**
		 * This method will be called while the background task is running. The progress
		 * dialog will be shown.
		 */
		@Override
		protected Void doInBackground(Void... params) {
			dialog.show();
			return null;
		}
	}
}
