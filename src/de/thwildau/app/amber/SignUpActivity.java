package de.thwildau.app.amber;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.thwildau.app.network.NetworkClient;
import de.thwildau.info.ClientMessage;
import de.thwildau.model.User;

public class SignUpActivity extends Activity {

	private static final String FILE_NAME = "client.properties";
	private static Properties properties;
	private static NetworkClient nClient;
	private Button registerButton;
	private EditText loginUsername;
	private EditText loginPass;

	private static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		context = this.getApplicationContext();
		loadProperties();

		try {
			nClient = new NetworkClient(properties);
			(new Thread(nClient)).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		registerButton = (Button) findViewById(R.id.btnSingIn);
		loginUsername = (EditText) findViewById(R.id.etUserName);
		loginPass = (EditText) findViewById(R.id.etPass);

		registerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				User userRegister = new User(
						loginUsername.getText().toString(),
						passwordToHash(loginPass.getText().toString()));
				NetworkClient.getSession().write(
						new ClientMessage(ClientMessage.Ident.REGISTER,
								userRegister));
				Intent intent = new Intent(SignUpActivity.this,
						LoginActivity.class);
				startActivity(intent);
				Toast.makeText(getApplicationContext(), "You're registered",
						Toast.LENGTH_SHORT).show();
			}
		});
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