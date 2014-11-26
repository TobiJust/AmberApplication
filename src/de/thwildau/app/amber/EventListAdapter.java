package de.thwildau.app.amber;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.thwildau.model.Event;

public class EventListAdapter extends BaseAdapter{

	Context context;
	LayoutInflater inflater;
	ArrayList<Event> eventlist;
	
	public EventListAdapter(Context context, ArrayList<Event> eventlist){
		this.context = context;
		this.eventlist = eventlist;
		this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return eventlist.size();
	}

	@Override
	public Object getItem(int position) {
		return eventlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.event_list_item, parent, false);
        }

        Event event = getEvent(position);
     
        ((TextView)view.findViewById(R.id.eventlist_textview1)).setText(event.getType());
        ((TextView)view.findViewById(R.id.eventlist_textview2)).setText(event.getTimeStamp());
        
        
        //get eventtype
        //put eventtypespecific icon
        
//        ImageView imgView = ((ImageView)view.findViewById(R.id.eventlist_imageview1));
//        Bitmap bm = BitmapFactory.decodeByteArray(event.getImage(), 0, event.getImage().length);
//        DisplayMetrics dm = new DisplayMetrics();
//        dm = context.getResources().getDisplayMetrics();
//        
//        imgView.setMinimumHeight(dm.heightPixels);
//        imgView.setMinimumWidth(dm.widthPixels);
//        
//        imgView.setImageBitmap(bm);
//        System.out.println(imgView.getHeight());
        return view;
		
	}

	public Event getEvent(int position) {
        return ((Event) getItem(position));
    }
	
}
