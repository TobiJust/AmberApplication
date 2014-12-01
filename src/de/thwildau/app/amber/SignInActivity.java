package de.thwildau.app.amber;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import de.thwildau.app.network.NetworkClient;
import de.thwildau.info.ClientMessage;
import de.thwildau.model.User;

public class SignInActivity extends Activity implements OnKeyListener {

	private static final String FILE_NAME = "client.properties";
	private final String PROJECT_NUMBER = "381372694375";
	private static Properties properties;
	private static NetworkClient nClient;
	private GoogleCloudMessaging gcm;
	private String regid;
	private Button loginButton;
	private EditText loginUsername;
	private EditText loginPass;
	private static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		loadProperties();

		try {
			nClient = new NetworkClient(properties);
			(new Thread(nClient)).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		loginButton = (Button) findViewById(R.id.btnSingIn);
		loginUsername = (EditText) findViewById(R.id.etUserName);
		loginPass = (EditText) findViewById(R.id.etPass);
		loginPass.setOnKeyListener(this);
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Loading...",
						Toast.LENGTH_SHORT).show();
				loginWithRegID();
			}
		});

	}

	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {

		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& keyCode == KeyEvent.KEYCODE_ENTER) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(loginPass.getWindowToken(), 0);

			return true;
		}
		return false; // pass on to other listeners.

	}

	public void loginWithRegID() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging
								.getInstance(getApplicationContext());
					}
					regid = gcm.register(PROJECT_NUMBER);
					Log.i("GCM", regid);

				} catch (IOException ex) {
					Log.e("GCM", "Error :" + ex.getMessage());
				}
				return regid;
			}

			@Override
			protected void onPostExecute(String regid) {

				User userLogin = new User(loginUsername.getText().toString(),
						passwordToHash(loginPass.getText().toString()));
				userLogin.setRegistrationID(regid);
				NetworkClient.getSession()
						.write(new ClientMessage(ClientMessage.Ident.LOGIN,
								userLogin));

			}
		}.execute(null, null, null);
	}

	/**
	 * Load properties from file
	 */
	public void loadProperties() {

		properties = new Properties();
		AssetManager assetManager = this.getAssets();

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

	private byte[] passwordToHash(String pass) {
		byte[] hashed = null;
		try {
			// Create MessageDigest instance for MD5
			MessageDigest md = MessageDigest.getInstance("MD5");
			// Add password bytes to digest
			md.update(pass.getBytes());
			// Get the hash's bytes
			hashed = md.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hashed;
	}

	public static Context getContext() {
		return context;
	}

}