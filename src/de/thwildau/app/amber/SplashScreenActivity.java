package de.thwildau.app.amber;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import de.thwildau.app.network.NetworkClient;

public class SplashScreenActivity extends Activity {

	// Splash screen timer
	private static int SPLASH_TIME_OUT = 3000;
	private static Properties properties;
	private static NetworkClient nClient;
	private static final String FILE_NAME = "client.properties";
	Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreen);
		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity
				Intent i = new Intent(SplashScreenActivity.this,
						LoginActivity.class);
				startActivity(i);

				// close this activity
				finish();
			}
		}, SPLASH_TIME_OUT);

		loadProperties();

		try {
			nClient = new NetworkClient(properties);
			(new Thread(nClient)).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadProperties() {
		properties = new Properties();
		AssetManager assetManager = context.getAssets();

		InputStream is = null;
		try {
			is = assetManager.open(FILE_NAME);
			properties.load(is);
			// logger.log(Level.SEVERE, settings.toString());
		} catch (IOException ex) {

			// default Settings setzen
			properties.put(NetworkClient.CONNECTION_TIMEOUT, 5000);
			properties.put(NetworkClient.HOST_NAME, "amber-project.no-ip.org");
			properties.put(NetworkClient.PORT, 8080);

		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ex) {
					Log.e("Error", ex.toString());
				}
			}
		}
	}
}
