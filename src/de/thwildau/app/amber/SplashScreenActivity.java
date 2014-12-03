package de.thwildau.app.amber;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import de.thwildau.app.network.NetworkClient;
import de.thwildau.info.ClientMessage;

public class SplashScreenActivity extends Activity {

	// Splash screen timer
	private static Properties properties;
	private static NetworkClient nClient;
	private static final String FILE_NAME = "client.properties";
	private static Context context;
	//	Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreen);
		if (getIntent().getBooleanExtra("Exit me", false)) {
			finish();
		}
		context = this.getApplicationContext();
		loadProperties();
		try {
			nClient = new NetworkClient(properties);
			(new Thread(nClient)).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		while(NetworkClient.getSession() == null);
		SharedPreferences prefs = this.getSharedPreferences("de.thwildau", Context.MODE_PRIVATE);
		NetworkClient.getSession().write(new ClientMessage(ClientMessage.Ident.LOGIN_CHECK,prefs.getInt("userID", -1)));
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

	public static Context getContext() {
		// TODO Auto-generated method stub
		return context;
	}
}
