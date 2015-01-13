package de.thwildau.app.amber;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.thwildau.model.Vehicle;


/**
 * This class is an adapter for the VehicleList. It prepares the list and its items for a
 * better usability and access by the user and the program code and for a better display of
 * the individual list objects/items.
 * It fills the list items with the object information at the initial filling. 
 * Furthermore it make methods available for a better management and access.
 * @author Kulla
 * @version 1.0
 * @since 2015-01-08
 * @see BaseAdapter
 */
public class VehicleListAdapter extends BaseAdapter{

	Context context;
	LayoutInflater inflater;
	ArrayList<Vehicle> vehiclelist;
	View view;
	ViewGroup parent;

	/**
	 * Constructor, that initialize the adapter of the list.
	 * @param context The applicationcontext, in which the list will be displayed.
	 * @param vehiclelist The list of vehicleobjects that will be displayed.
	 */
	public VehicleListAdapter(Context context, ArrayList<Vehicle> vehiclelist){
		this.context = context;
		this.vehiclelist = vehiclelist;
		this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * Method to get the size of the list.
	 * @return int The size of the list.
	 */
	@Override
	public int getCount() {
		return vehiclelist.size();
	}

	/**
	 * Method to get the listobject by the position of the item in the list.
	 * @param position The position of the item in the list.
	 * @return Vehicle The vehicleobject of the list.
	 */
	@Override
	public Object getItem(int position) {
		return vehiclelist.get(position);
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
	 * It displays data like name, date of adding the vehicle, vehicle logo and status of the 
	 * event alarm.
	 * @param position The position of the data (item) in the list.
	 * @param convertView The incoming convert view.
	 * @param parent The source viewgroup of the view.
	 * @return view The view with the data to display.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		this.parent=parent;
		view = convertView;

		if (view == null) {
			view = inflater.inflate(R.layout.vehicle_list_item, parent, false);
		}

		/*Gets the vehicle objects attributes and put them into the list view for displaying.*/

		Vehicle vehicle = getVehicle(position);

		String time = vehicle.getDate();
		if(time!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			Date resultdate = new Date(Long.parseLong(time));
			((TextView)view.findViewById(R.id.vehiclelist_textview2)).setText(sdf.format(resultdate));
		}else{
			((TextView)view.findViewById(R.id.vehiclelist_textview2)).setText("");
		}

		/*Convert bytearray to image for displaying the vehicle logo in the list.*/

		ImageView image = (ImageView) view.findViewById(R.id.vehiclelist_imageview);
		if(vehicle.getImage()!=null){
			Bitmap bmp = BitmapFactory.decodeByteArray(vehicle.getImage(), 0, vehicle.getImage().length);
			image.setImageBitmap(bmp);
		}else{
			image.setImageResource(R.drawable.ic_launcher);
		}

		((TextView)view.findViewById(R.id.vehiclelist_textview1)).setText(vehicle.vehicleName);

		view.findViewById(R.id.vehiclelist_alarm_off).setTag(position);    
		view.findViewById(R.id.vehiclelist_alarm_on).setTag(position);       

		if(vehicle.getBoxed()==true){	
			((ImageView)view.findViewById(R.id.vehiclelist_alarm_off)).setVisibility(View.GONE);
			((ImageView)view.findViewById(R.id.vehiclelist_alarm_on)).setVisibility(View.VISIBLE);
		}else{
			((ImageView)view.findViewById(R.id.vehiclelist_alarm_on)).setVisibility(View.GONE);
			((ImageView)view.findViewById(R.id.vehiclelist_alarm_off)).setVisibility(View.VISIBLE);
		}

		return view;

	}

	/**
	 * Method to get view by committing the position.
	 * @param position
	 * @return calls {@link VehicleListAdapter#getView(int, View, ViewGroup)}
	 */
	public View getView(int position){
		return getView(position, view, parent);
	}

	/**
	 * This method returns the vehicle object from the specific list position.
	 * @param position The position in the list.
	 * @return Vehicle The vehicle object in the list.
	 */
	public Vehicle getVehicle(int position) {
		return ((Vehicle) getItem(position));
	}

}
