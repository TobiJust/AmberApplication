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
import android.widget.ListAdapter;
import android.widget.ListView;
import de.thwildau.amber.R;

public class VehicleActivity<T> extends ActionBarActivity{

	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_vehicle);
	        
	        /*get vehicles from database*/
	        
        	/*create vehicle list*/
	        
	        List valueList = new ArrayList<String>();
	        
	        /*create ListItems*/
	        
	        for (int i=0; i<2; i++){
	        	valueList.add("vehicle"+i);	
	        }
	        
	        /*format list*/
	        
	        ListAdapter adapter = new ArrayAdapter<T>(getApplicationContext(), /*R.layout.vehicle_list_item*/ android.R.layout.simple_list_item_1, valueList);
	        final ListView lv = (ListView)findViewById(R.id.listView_vehicleactivity);
	        
	        lv.setAdapter(adapter);
	        lv.setBackgroundColor(Color.BLACK);
	        lv.setDivider(new ColorDrawable(Color.parseColor("#ffcc33")));
	        lv.setDividerHeight(5);
	        
	        /*clicklistener of list*/
	        
	        lv.setOnItemClickListener(new OnItemClickListener() {
			
	        	@Override
	        	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
	        	
	        		switch(position)
	        		{
	        			case 0:	Intent newActivity0 = new Intent(VehicleActivity.this, EventsActivity.class);
	        				startActivity(newActivity0);
	        				break;
	        			case 1:	Intent newActivity1 = new Intent(VehicleActivity.this, EventsActivity.class);
							startActivity(newActivity1);
							break;
	        		}
	        		
		        	/*
		        		Intent intent = new Intent();
		        		intent.setClassName(getPackageName(), getPackageName()+".MainActivity2");
		        		//intent.putExtra("selected", lv.getAdapter().getItem(arg2).toString());
		        		startActivity(intent);
		        	 */
	        		
	        	}
	        	
	        });
	        
	 	}
	 
}
