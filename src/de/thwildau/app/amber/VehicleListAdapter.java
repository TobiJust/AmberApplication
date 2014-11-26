package de.thwildau.app.amber;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import de.thwildau.amber.R;
import de.thwildau.model.Vehicle;

public class VehicleListAdapter extends BaseAdapter{

	Context context;
	LayoutInflater inflater;
	ArrayList<Vehicle> vehiclelist;
	
	public VehicleListAdapter(Context context, ArrayList<Vehicle> vehiclelist){
		this.context = context;
		this.vehiclelist = vehiclelist;
		this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return vehiclelist.size();
	}

	@Override
	public Object getItem(int position) {
		return vehiclelist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.vehicle_list_item, parent, false);
        }

        Vehicle vehicle = getVehicle(position);
     
        ((TextView)view.findViewById(R.id.vehiclelist_textview1)).setText(vehicle.vehicleName);
        ((TextView)view.findViewById(R.id.vehiclelist_textview2)).setText(vehicle.date);
        ((ImageView)view.findViewById(R.id.vehiclelist_imageview)).setImageResource(vehicle.image);
        
        CheckBox cb = (CheckBox) view.findViewById(R.id.vehiclelist_cbBox);
        
        cb.setBackgroundColor(Color.WHITE);
        
        cb.setOnCheckedChangeListener(myCheckChangList);
        cb.setTag(position);
        cb.setChecked(vehicle.boxed);
        
        return view;
		
	}

	public Vehicle getVehicle(int position) {
        return ((Vehicle) getItem(position));
    }

    public ArrayList<Vehicle> getBoxed() {
        ArrayList<Vehicle> boxedarray = new ArrayList<Vehicle>();
        for (Vehicle v : vehiclelist) {
            if (v.boxed)
                boxedarray.add(v);
        }
        return boxedarray;
    }

    OnCheckedChangeListener myCheckChangList = new OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            getVehicle((Integer) buttonView.getTag()).boxed = isChecked;
        }
    };
}
