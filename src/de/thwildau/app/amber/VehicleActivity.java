package de.thwildau.app.amber;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
//import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import de.thwildau.amber.R;
import de.thwildau.model.Vehicle;

public class VehicleActivity<T> extends ActionBarActivity{

	ArrayList<Vehicle> vehiclelist = new ArrayList<Vehicle>();
    ListAdapter boxAdapter;

      /** Called when the activity is first created. */
      public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);

        fillData();
        boxAdapter = new ListAdapter(this, vehiclelist);

        ListView lv = (ListView) findViewById(R.id.lvMain);
        lv.setAdapter(boxAdapter);
        
        lv.setBackgroundColor(Color.BLACK);
        lv.setDivider(new ColorDrawable(Color.parseColor("#ffcc33")));
        lv.setDividerHeight(5);
        
        lv.setOnItemClickListener(new OnItemClickListener() {
        	
        	@Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        		
        		Intent intent = new Intent(VehicleActivity.this, EventsActivity.class);
        		startActivity(intent);
        		
            }
        });
      }

      void fillData() {
    	  
    	  Intent intent = getIntent();
    	  ArrayList<String> vehicleList = (ArrayList<String>) intent.getExtras().get("Vehiclelist");
    	  
        for (int i = 0; i < vehicleList.size(); i++) {
         vehiclelist.add(new Vehicle(vehicleList.get(i),"0",0,false));
        }
      }
	 
}
