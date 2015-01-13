package de.thwildau.app.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import de.thwildau.app.amber.DetailActivity;
import de.thwildau.app.amber.R;
import de.thwildau.util.Constants;

/**
 * This class specifies the GCM Notifications and handles its content.
 * @author Kulla, Just
 * @version 1.0
 * @since 2015-01-12
 * @see IntentService
 *
 */
public class GcmIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 1;
	private static final String TAG = "GCM";
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	int numMessages = 0;

	/**
	 * Constructor
	 */
	public GcmIntentService() {
		super("GcmIntentService");
	}

	/**
	 * Method to handle and preprocess intent.
	 * @param intent Intent that the GCM message contains.
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();

		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		/*The getMessageType() intent parameter must be the intent you received
		 *in your BroadcastReceiver.
		 */
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {  /*has effect of unparcelling Bundle*/
			/*
			 * Filter messages based on message type. Since it is likely that GCM
			 * will be extended in the future with new message types, just ignore
			 * any message types you're not interested in, or that you don't
			 * recognize.
			 */
			if (GoogleCloudMessaging.
					MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
				sendNotification(extras);
			} else if (GoogleCloudMessaging.
					MESSAGE_TYPE_DELETED.equals(messageType)) {
				sendNotification(extras);
				/*If it's a regular GCM message, do some work.*/
			} else if (GoogleCloudMessaging.
					MESSAGE_TYPE_MESSAGE.equals(messageType)) {

				/* Post notification of received message.*/
				sendNotification(extras);
				Log.i(TAG, "Received: " + extras.toString());
			}
		}
		/* Release the wake lock provided by the WakefulBroadcastReceiver.*/
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	/**
	 * Put the message into a notification and post it.
	 * Set appearance of the notification bar.
	 * @param extras Intent that the GCM message contains.
	 */
	private void sendNotification(Bundle extras) {
		mNotificationManager = (NotificationManager)
				this.getSystemService(Context.NOTIFICATION_SERVICE);

		String eventID = extras.getString("eventID");
		String message = extras.getString("message");
		String type = extras.getString("type");
		int obuID = Integer.parseInt(extras.getString("obuID"));

		if(type.equals(Constants.EVENT_TYPE_ACC)){
			type = Constants.EVENT_TYPE_ACC_GER;
			message = Constants.EVENT_TEXT_ACC;
		}else if(type.equals(Constants.EVENT_TYPE_COOLANT)){
			type = Constants.EVENT_TYPE_COOLANT_GER;
			message = Constants.EVENT_TEXT_COOLANT;
		}else if(type.equals(Constants.EVENT_TYPE_FUEL)){
			type = Constants.EVENT_TYPE_FUEL_GER;
			message = Constants.EVENT_TEXT_FUEL;
		}else if(type.equals(Constants.EVENT_TYPE_SPEED)){
			type = Constants.EVENT_TYPE_SPEED_GER;
			message = Constants.EVENT_TEXT_SPEED;
		}else if(type.equals(Constants.EVENT_TYPE_TURN)){
			type = Constants.EVENT_TYPE_TURN_GER;
			message = Constants.EVENT_TEXT_TURN;
		}else if(type.equals(Constants.EVENT_TYPE_GYRO)){
			type = Constants.EVENT_TYPE_GYRO_GER;
			message = Constants.EVENT_TEXT_GYRO;
		}

		Intent intent = new Intent(this, DetailActivity.class);
		intent.putExtra("EventID", eventID);
		intent.putExtra("Type", type);
		intent.putExtra("obuID", obuID);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.notification_icon)   
		.setContentTitle("Amber: "+type)
		.setStyle(new NotificationCompat.BigTextStyle()
		.bigText(""+message))
		.setContentText(""+message);

		mBuilder.setNumber(++numMessages);
		mBuilder.setLights(Color.YELLOW, 500, 500);
		long[] pattern = {500,500};
		mBuilder.setVibrate(pattern);
		Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		mBuilder.setSound(alarmSound);
		mBuilder.setAutoCancel(true);
		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
}
