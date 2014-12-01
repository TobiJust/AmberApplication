package de.thwildau.app.amber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import de.thwildau.model.Event;
import de.thwildau.model.Vehicle;

//import android.widget.ListAdapter;

public class VehicleActivity<T> extends ActionBarActivity {

	private Toast toast;
	private long lastBackPressTime = 1000;
	private static final int PICK_IMAGE = 1;
	ArrayList<Vehicle> vehiclelist = new ArrayList<Vehicle>();
	VehicleListAdapter boxAdapter;

	HashMap<String, ArrayList<Event>> eventHashList = new HashMap<String, ArrayList<Event>>();

//	private static Context context;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vehicle);
		
//		context = this.getApplicationContext();
		
		fillData();

		boxAdapter = new VehicleListAdapter(this, vehiclelist);

		ListView lv = (ListView) findViewById(R.id.vehicleactivity_listview1);
		lv.setAdapter(boxAdapter);

		lv.setBackgroundColor(Color.BLACK);
		lv.setDivider(new ColorDrawable(Color.parseColor("#ffcc33")));
		lv.setDividerHeight(5);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				/* ListItemClick */

				Intent startEventActivity = new Intent(VehicleActivity.this,
						EventsActivity.class);
				Vehicle clickedItem = (Vehicle) boxAdapter.getItem(position);
				// get Event ArrayList of seleted table row and push it to
				// eventlist activity
				startEventActivity.putExtra("Eventlist",
						clickedItem.getEventList());
				startActivity(startEventActivity);

				/* ImageClick */

				ImageView logo = (ImageView) findViewById(R.id.vehiclelist_imageview);
				logo.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
				
						
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
//						intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

						startActivityForResult(
								Intent.createChooser(intent, "Select Picture"),
								PICK_IMAGE);

						
//						Uri outputFileUri = Uri.fromFile(newfile);
//						Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
//						cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//						startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
					}
				});
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		ImageView imageView2;
		imageView2 = (ImageView) findViewById(R.id.vehiclelist_imageview);

		if (requestCode == PICK_IMAGE && data != null && data.getData() != null) {
			// Uri _uri = data.getData();
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();

			imageView2.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			// //User had pick an image.
			// Cursor cursor = getContentResolver().query(_uri, new String[] {
			// android.provider.MediaStore.Images.ImageColumns.DATA }, null,
			// null, null);
			// cursor.moveToFirst();
			//
			// //Link to the image
			// final String imageFilePath = cursor.getString(0);
			// cursor.close();
		}
		// super.onActivityResult(requestCode, resultCode, data);
		//
		// if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
		// Log.d("CameraDemo", "Pic saved");

	}

	@Override
	public void onBackPressed() {
		if (this.lastBackPressTime < System.currentTimeMillis() - 3000) {
			toast = Toast.makeText(this, "Press back again to close this app",
					4000);
			this.lastBackPressTime = System.currentTimeMillis();
			toast.show();
		} else if (this.lastBackPressTime < System.currentTimeMillis() + 3000) {
			this.lastBackPressTime = System.currentTimeMillis();
			Intent intent = new Intent(this, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("Exit me", true);
			startActivity(intent);
			finish();
			System.exit(0);
			super.onBackPressed();
		}
	}

	// if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
	// // Intent intent = new Intent(this, LoginActivity.class);
	// // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	// // intent.putExtra("Exit me", true);
	// // startActivity(intent);
	// // finish();
	// System.exit(0);
	// }
	// else {
	//
	// }
	// // System.exit(0);
	// }

	// @Override
	// public void onBackPressed() {
	// if (doubleBackToExitPressedOnce) {
	// super.onBackPressed();
	// return;
	// }
	//
	// this.doubleBackToExitPressedOnce = true;
	// Toast.makeText(this, "Please click BACK again to exit",
	// Toast.LENGTH_SHORT).show();
	//
	// new Handler().postDelayed(new Runnable() {
	//
	// @Override
	// public void run() {
	// // doubleBackToExitPressedOnce=false;
	// Application.Quit();
	// }
	// }, 2000);
	// }

	void fillData() {

		Intent intent = getIntent();
		ArrayList<Vehicle> vehicleList = (ArrayList<Vehicle>) intent
				.getExtras().get("Vehiclelist");

		// ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
		// ArrayList<Event> eventList2 = new ArrayList<Event>();
		// //Autos
		// Vehicle v1 = new Vehicle("Audi A4", "01.01.1999", 1, false);
		//
		//
		// byte[] b1 = new byte[]{(byte)0};
		// //Events
		// Event e1 = new Event("Accident", "00:00", 2, 2, b1);
		//
		//
		// //Events hinzufügen
		// eventList2.add(e1);
		//
		// //autos eventliste hinzufügen
		// v1.setEventList(eventList2);
		//
		// //autos in liste einfügen
		// vehicleList.add(v1);

		for (int i = 0; i < vehicleList.size(); i++) {
			vehiclelist.add(vehicleList.get(i));
		}
	}

}
