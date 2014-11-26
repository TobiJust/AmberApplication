package de.thwildau.app.amber;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import de.thwildau.amber.R;
import de.thwildau.model.Event;

public class EventsActivity<T> extends ActionBarActivity {

	ArrayList<Event> eventlist;
    EventListAdapter boxAdapter;
   

      /** Called when the activity is first created. */
      public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        fillData();
        boxAdapter = new EventListAdapter(this, eventlist);

        ListView lv = (ListView) findViewById(R.id.listview_eventactivity);
        lv.setAdapter(boxAdapter);
        
        lv.setBackgroundColor(Color.BLACK);
        lv.setDivider(new ColorDrawable(Color.parseColor("#ffcc33")));
        lv.setDividerHeight(5);
        
        lv.setOnItemClickListener(new OnItemClickListener() {
        	
        	@Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        		
        		Intent intent = new Intent(EventsActivity.this, DetailActivity.class);

        		intent.putExtra("Event", (Event) boxAdapter.getItem(position));
        		
        		startActivity(intent);
        		
            }
        });
      }
	
      void fillData() {
      	  
 		Intent intent = getIntent();
    	  
    	eventlist = (ArrayList<Event>) intent.getExtras().get("Eventlist");
    	  	
      }

}
