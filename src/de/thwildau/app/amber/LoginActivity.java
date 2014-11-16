package de.thwildau.app.amber;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import de.thwildau.amber.R;
import de.thwildau.app.network.NetworkClient;
import de.thwildau.info.ClientMessage;
import de.thwildau.model.User;


public class LoginActivity extends ActionBarActivity {

	private static final String FILE_NAME = "client.properties";
	private final String PROJECT_NUMBER = "381372694375";

	private static Properties properties;
	private static NetworkClient nClient;	

	private GoogleCloudMessaging gcm;
	private String regid;
	private String registrationID;

	private Button loginButton;
	private Button registerButton;
	private EditText loginUsername;
	private EditText loginPass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		loadProperties();

		try {
			nClient = new NetworkClient(properties);
			(new Thread(nClient)).start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		loginButton = (Button) findViewById(R.id.loginButton);
		registerButton = (Button) findViewById(R.id.registerButton);
		loginUsername = (EditText) findViewById(R.id.loginUsername);
		loginPass = (EditText) findViewById(R.id.loginPass);

		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loginWithRegID();
			}
		});
		registerButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				showRegisterDialog();
			}
		});
	}

	public void loginWithRegID(){
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
					}
					regid = gcm.register(PROJECT_NUMBER);
					Log.i("GCM",  regid);

				} catch (IOException ex) {
					Log.e("GCM", "Error :" + ex.getMessage());

				}
				return regid;
			}

			@Override
			protected void onPostExecute(String regid) {
				User userLogin = new User(loginUsername.getText().toString(), passwordToHash(loginPass.getText().toString()));
				userLogin.setRegistrationID(regid);
				NetworkClient.getSession().write(new ClientMessage(ClientMessage.Ident.LOGIN, userLogin));

			}
		}.execute(null, null, null);
	}

	private void showRegisterDialog(){

		// custom dialog
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_register);
		dialog.setTitle("Register...");

		final EditText userName = (EditText) dialog.findViewById(R.id.nameField);
		final EditText userPass = (EditText) dialog.findViewById(R.id.passField);
		Button registerButton = (Button) dialog.findViewById(R.id.registerButton);
		Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);

		registerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				User userRegister = new User(userName.getText().toString(), passwordToHash(userPass.getText().toString()));
				NetworkClient.getSession().write(new ClientMessage(ClientMessage.Ident.REGISTER, userRegister));
			}
		});

		// if button is clicked, close the custom dialog
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		// show it
		dialog.show();
	}

	/**
	 * Load properties from file
	 */
	public void loadProperties(){

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
	private byte[] passwordToHash(String pass){
		byte[] hashed = null;
		try {
			// Create MessageDigest instance for MD5
			MessageDigest md = MessageDigest.getInstance("MD5");
			//Add password bytes to digest
			md.update(pass.getBytes());
			//Get the hash's bytes 
			hashed = md.digest();            
		} 
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		}
		return hashed;
	}
}
