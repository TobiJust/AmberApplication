package de.thwildau.app.amber;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.thwildau.model.Event;
import de.thwildau.util.Constants;

/**
 * This class is an adapter for the EventList. It prepares the list and its items for a
 * better usability and access by the user and the program code and for a better display of
 * the individual list objects/items.
 * It fills the list items with the object information at the initial filling. 
 * Furthermore it makes methods available for a better management and access.
 * @author Kulla
 * @version 1.0
 * @since 2015-01-08
 */
public class EventListAdapter extends BaseAdapter{

	Context context;
	LayoutInflater inflater;
	ArrayList<Event> eventlist;

	/**
	 * Constructor, that initialize the adapter of the list.
	 * @param context The applicationcontext, in which the list will be displayed.
	 * @param eventlist The list of eventobjects that will be displayed.
	 */
	public EventListAdapter(Context context, ArrayList<Event> eventlist){
		this.context = context;
		this.eventlist = eventlist;
		this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * Method to get the size of the list.
	 * @return int The size of the list.
	 */
	@Override
	public int getCount() {
		return eventlist.size();
	}

	/**
	 * Method to get the listobject by the position of the item in the list.
	 * @param position The position of the item in the list.
	 * @return Event The eventobject of the list.
	 */
	@Override
	public Object getItem(int position) {
		return eventlist.get(position);
	}

	/**
	 * Method to get the position of the listobject.
	 * @param position The position of the item in the list.
	 * @return position The position of the item in the list.
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * This method gets a view that displays the data at the specified position in the data set.
	 * It displays data like type, date of the event, event logo.
	 * @param position The position of the data (item) in the list.
	 * @param convertView The incoming convert view.
	 * @param parent The source viewgroup of the view.
	 * @return view The view with the data to display.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.event_list_item, parent, false);
		}

		Event event = getEvent(position);
		//		
		//		System.out.println("ts: "+event.getTimeStamp());
		//
		if(event.getTimeStamp().equals("0")){

			((TextView)view.findViewById(R.id.eventlist_textview2)).setText("0.0.0000 00:00");

		}else{

			String parsedTimeStamp = parseTimeStampDate(event.getTimeStamp())+" "+parseTimeStampTime(event.getTimeStamp());
			((TextView)view.findViewById(R.id.eventlist_textview2)).setText(parsedTimeStamp);

		}

		String eventtype = event.getType();
		ImageView imageview = (ImageView) view.findViewById(R.id.eventlist_imageview1);

		if(eventtype.equals(Constants.EVENT_TYPE_ACC)){

			((TextView)view.findViewById(R.id.eventlist_textview1)).setText(Constants.EVENT_TYPE_ACC_GER);
			imageview.setImageResource(R.drawable.event_acc);

		}else if(eventtype.equals(Constants.EVENT_TYPE_FUEL)){

			((TextView)view.findViewById(R.id.eventlist_textview1)).setText(Constants.EVENT_TYPE_FUEL_GER);
			imageview.setImageResource(R.drawable.event_fuel);

		}else if(eventtype.equals(Constants.EVENT_TYPE_SPEED)){

			((TextView)view.findViewById(R.id.eventlist_textview1)).setText(Constants.EVENT_TYPE_SPEED_GER);
			imageview.setImageResource(R.drawable.event_speed);

		}else if(eventtype.equals(Constants.EVENT_TYPE_TURN)){

			((TextView)view.findViewById(R.id.eventlist_textview1)).setText(Constants.EVENT_TYPE_TURN_GER);
			imageview.setImageResource(R.drawable.event_bend);

		}else if(eventtype.equals(Constants.EVENT_TYPE_GYRO)){

			((TextView)view.findViewById(R.id.eventlist_textview1)).setText(Constants.EVENT_TYPE_GYRO_GER);
			imageview.setImageResource(R.drawable.event_gyro);

		}else{

			((TextView)view.findViewById(R.id.eventlist_textview1)).setText(Constants.EVENT_TYPE_DEFAULT_GER);
			imageview.setImageResource(R.drawable.event_warning);

		}

		return view;

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
	 * This method returns the event object from the specific list position.
	 * @param position The position in the list.
	 * @return Event The event object in the list.
	 */
	public Event getEvent(int position) {
		return ((Event) getItem(position));
	}

}
