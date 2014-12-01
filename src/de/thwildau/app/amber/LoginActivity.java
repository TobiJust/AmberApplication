package de.thwildau.app.amber;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import android.content.Context;
import android.content.Intent;
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

import de.thwildau.app.network.NetworkClient;
import de.thwildau.info.ClientMessage;
import de.thwildau.model.User;

public class LoginActivity extends ActionBarActivity implements OnClickListener {

	private final String PROJECT_NUMBER = "381372694375";
	private GoogleCloudMessaging gcm;
	private String regid;
	private EditText loginUsername;
	private EditText loginPass;
	private Button btnSignIn;
	private Button btnSignUp;
	private static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getIntent().getBooleanExtra("Exit me", false)) {
			finish();
		}
		setContentView(R.layout.activity_start);
		context = this.getApplicationContext();
		btnSignIn = (Button) findViewById(R.id.btnSingIn);
		btnSignUp = (Button) findViewById(R.id.btnSignUp);
		btnSignIn.setOnClickListener(this);
		btnSignUp.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent i = null;
		switch (v.getId()) {
		case R.id.btnSingIn:
			i = new Intent(this, SignInActivity.class);
			break;
		case R.id.btnSignUp:
			i = new Intent(this, SignUpActivity.class);
			break;
		}
		startActivity(i);
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
