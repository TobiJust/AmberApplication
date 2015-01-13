package de.thwildau.app.gcm;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * This class is the broadcast receiver for the gcm messages.
 * @author Kulla, Just
 * @version 1.0
 * @since 2015-01-12
 * @see WakefulBroadcastReceiver
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

	/**
	 * This method receives the gcm messages.
	 * @param context The Context in which the receiver is running.
	 * @param intent The Intent being received. 
	 */
	@Override
	public void onReceive(Context context, Intent intent) {

		/*Explicitly specify that GcmMessageHandler will handle the intent.*/
		ComponentName comp = new ComponentName(context.getPackageName(),
				GcmIntentService.class.getName());

		/*Start the service, keeping the device awake while it is launching.*/
		startWakefulService(context, (intent.setComponent(comp)));
		setResultCode(Activity.RESULT_OK);
	}
}
