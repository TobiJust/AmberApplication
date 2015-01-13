package de.thwildau.app.amber;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import de.thwildau.app.network.NetworkClient;
import de.thwildau.info.ClientMessage;
import de.thwildau.util.Constants;

/**
 * This class is the starting activity and shows the amber logo. In this activity
 * it will be checked if the server is available. If it is available the user will
 * be transfered to the login screen. If not the user will get an information and
 * the screen does not changed.
 * @author Kulla, Just, Wannek
 * @version 1.0
 * @since 2015-01-12
 * @see Activity
 */
public class SplashScreenActivity extends Activity {

	private static Context context;
	private boolean connection = false;
	private BackgroundTask bt;
	private static BackgroundTask2 bt2;
	private ProgressDialog pd;
	private Toast toast;

	/**
	 * Called when the activity is first created. It sets the activity content data 
	 * like view layout and title. It starts a method to check the server connection
	 * and shows information toasts for the user.
	 * @param savedInstanceState The previously state of the activity.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreen);

		context = this.getApplicationContext();	
		pd = new ProgressDialog(SplashScreenActivity.this, ProgressDialog.THEME_HOLO_DARK);

		checkServerconnection(1);

		View view = findViewById(R.id.splash_layout);
		view.setOnClickListener(new OnClickListener(){

			/**
			 * This method handles the user touch event on the screen.
			 * @param v The current screen view.
			 */
			@Override
			public void onClick(View v) {
				Toast.makeText(context, Constants.TOAST_RECONNECT, Toast.LENGTH_SHORT).show();
				bt2.cancel(true);
				checkServerconnection(2);
			}
		});

		pd.setOnDismissListener(new OnDismissListener(){

			/**
			 * This method is called if the progress dialog dismisses (no server connection is available).
			 * @param dialog The progress dialog which shows the try to connect to the server.
			 */
			@Override
			public void onDismiss(DialogInterface dialog) {
				if(!connection){
					toast = Toast.makeText(context, Constants.TOAST_NOSERVER, Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		});
	}

	/**
	 * This method creates tasks and checks the connection to the server.
	 * @param index
	 */
	public void checkServerconnection(int index){
		switch(index){
		case 1:
			bt = new BackgroundTask();
			bt2 = new BackgroundTask2();
			bt.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void)null);
			bt2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void)null);
			break;
		case 2:
			bt2 = new BackgroundTask2();
			bt2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void)null);
			break;
		}
	}

	/**
	 * This method returns the application context.
	 * @return Context The application context.
	 */
	public static Context getContext() {
		return context;
	}

	/**
	 * This class is the base for a task which trys to connect to the server.
	 * @author Kulla 
	 * @version 1.0
	 * @since 2015-01-12
	 * @see AsyncTask
	 */
	class BackgroundTask extends AsyncTask<Void, Void, Void>{
		/**
		 * Method will be called before the task is running.
		 */
		@Override
		protected void onPreExecute() {
		};

		/**
		 * Method will be called when the task is running.
		 * Trys to connect to the server.
		 */
		@Override
		protected Void doInBackground(Void... params) {
			try {

				NetworkClient.connect();
				while(NetworkClient.getSession() == null);
				connection = true;

			} catch (Exception e) {
				/*Error*/
			}	
			return null;
		}

		/**
		 * Method will be called after the task had run.
		 */
		@Override
		protected void onCancelled(Void result) {
			super.onCancelled(result);
			pd.cancel();
		}
	}

	/**
	 * This class is the base for a task which checks if a connection to the server is already done.
	 * @author Kulla 
	 * @version 1.0
	 * @since 2015-01-12
	 * @see AsyncTask
	 */
	class BackgroundTask2 extends AsyncTask<Void, Void, Void>{

		/**
		 * Method will be called before the task is running.
		 */
		@Override
		protected void onPreExecute() {
			pd.setMessage(Constants.PD_LOADING);
			pd.setCancelable(false);
			pd.show();
		};

		/**
		 * Method will be called when the task is running.
		 * Checks if a connection is done. If it is done a server request will
		 * be send.
		 */
		@Override
		protected Void doInBackground(Void... params) {
			try {
				pd.show();					

				if(!connection){
					Thread.sleep(1);
				}
				if(!connection){
					Thread.sleep(100);
				}
				if(!connection){
					Thread.sleep(400);
				}
				if(!connection){
					Thread.sleep(500);
				}
				if(!connection){
					Thread.sleep(1000);
				}
				if(!connection){
					Thread.sleep(1000);
				}
				if(!connection){
					Thread.sleep(2000);
				}

				if(connection){

					pd.cancel();
					bt.cancel(true);

					SharedPreferences prefs = SplashScreenActivity.this.getSharedPreferences("de.thwildau", Context.MODE_PRIVATE);
					NetworkClient.getSession().write(new ClientMessage(ClientMessage.Ident.LOGIN_CHECK,prefs.getInt("userID", -1)));

					toast.setText(Constants.TOAST_WELCOME);
					toast.show();

					bt2.cancel(true);
				}else if(!connection){
					pd.dismiss();
				}

			} catch (Exception e) {
				/*Error*/
			}	
			return null;
		}
	}
}
