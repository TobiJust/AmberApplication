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

public class EventsActivity<T> extends ActionBarActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);	
	    setContentView(R.layout.activity_events);
        
        /*get events from database*/
        
    	/*create event list*/
        
        List valueList = new ArrayList<String>();
        
        /*create ListItems*/
        
        for (int i=0; i<7; i++){
        	valueList.add("event"+i);	
        }
        
        /*format list*/
        
        ListAdapter adapter = new ArrayAdapter<T>(getApplicationContext(), android.R.layout.simple_list_item_1, valueList);
        final ListView lv = (ListView)findViewById(R.id.listView_eventsactivity);
        
        
        
        lv.setAdapter(adapter);
        lv.setBackgroundColor(Color.BLACK);
        lv.setDivider(new ColorDrawable(Color.parseColor("#ffcc33")));
        lv.setDividerHeight(5);
        
        /*clicklistener of list*/
        
        lv.setOnItemClickListener(new OnItemClickListener() {
		
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        	           		
            		Intent intent = new Intent(EventsActivity.this, DetailActivity.class);
            		//intent.setClassName(getPackageName(), getPackageName()+".DetailActivity");
            		intent.putExtra("selected", lv.getAdapter().getItem(position).toString());
            		startActivity(intent);	
            
        	}
        	
        });
        
	}

}
