package de.thwildau.app.amber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
//import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import de.thwildau.amber.R;
import de.thwildau.model.Event;
import de.thwildau.model.Vehicle;

public class VehicleActivity<T> extends ActionBarActivity{

	ArrayList<Vehicle> vehiclelist = new ArrayList<Vehicle>();
    VehicleListAdapter boxAdapter;
    
    HashMap<String, ArrayList<Event>> eventHashList = new HashMap<String, ArrayList<Event>>();

    private static Context context;
    
      /** Called when the activity is first created. */
      public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);

        context = this.getApplicationContext();
        
        fillData();
        
        boxAdapter = new VehicleListAdapter(this, vehiclelist);

        ListView lv = (ListView) findViewById(R.id.vehicleactivity_listview1);
        lv.setAdapter(boxAdapter);
        
        lv.setBackgroundColor(Color.BLACK);
        lv.setDivider(new ColorDrawable(Color.parseColor("#ffcc33")));
        lv.setDividerHeight(5);
        
        lv.setOnItemClickListener(new OnItemClickListener() {
        	
        	@Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        		
        		Intent startEventActivity = new Intent(VehicleActivity.this, EventsActivity.class);
        		        		
        		Intent intent = getIntent();
        		
        		//get text of selected table row
        		String selected = ((TextView) view.findViewById(R.id.vehiclelist_textview1)).getText().toString();
        		//get clickedItem
        		Vehicle clickedItem = (Vehicle) boxAdapter.getItem(position);       		
        		//get Event ArrayList of seleted table row and push it to eventlist activity
        		startEventActivity.putExtra("Eventlist", clickedItem.getEventList());
        		
        		startActivity(startEventActivity);
        		
            }
        });
      }

      void fillData() {
    	  
    	  Intent intent = getIntent();
    	  ArrayList<Vehicle> vehicleList = (ArrayList<Vehicle>) intent.getExtras().get("Vehiclelist");
    	  
    	  for (int i = 0; i < vehicleList.size(); i++) {
    		  vehiclelist.add(vehicleList.get(i));
    	  }
      }
	 
}
