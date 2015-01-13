package de.thwildau.app.amber;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
import de.thwildau.util.Constants;

/**
 * This class describes the SignIn/Login Activity in which the user can enter his
 * user name and password to login into the application.
 * @author Kulla, Just, Wannek
 * @version 1.0
 * @since 2015-01-12
 * @see {@link ActionBarActivity}, {@link OnKeyListener}
 */
public class SignInActivity extends ActionBarActivity implements OnKeyListener {

	private final String PROJECT_NUMBER = "381372694375";
	private GoogleCloudMessaging gcm;
	private String regid;
	private Button loginButton;
	private EditText loginUsername;
	private EditText loginPass;
	private static Context context;
	private Toast toast;

	/**
	 *  Called when the activity is first created. It sets the layout, title bar of the screen
	 *  and registers the input elements the user can interact with.
	 *  @param savedInstanceState The previously state of the activity.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		setTitle(Constants.APP_TITLE_LOGIN);
		context = this.getApplicationContext();

		loginButton = (Button) findViewById(R.id.btnSingIn);
		loginUsername = (EditText) findViewById(R.id.etUserName);
		loginPass = (EditText) findViewById(R.id.etPass);
		loginPass.setOnKeyListener(this);
		loginButton.setOnClickListener(new OnClickListener() {
			/**
			 * This method handles the click of the login button and calls the method
			 * loginWithRegID() {@link SignInActivity#loginWithRegID()}.
			 */
			@Override
			public void onClick(View v) {

				loginWithRegID();
			}
		});
	}

	/**
	 * This method allows by clicking the enter key on the mobile keyboard to change the 
	 * screen writing focus to the password edittextfield.
	 * @param view View of the Activity.
	 * @param keyCode Key code from the device.
	 * @param event Key event from the device.
	 */
	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& keyCode == KeyEvent.KEYCODE_ENTER) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(loginPass.getWindowToken(), 0);

			return true;
		}
		return false;
	}

	/**
	 * This method performs the user login. It registers the GCM for the notifications and
	 * checks if the user entered username and password. It sets user information in an user
	 * object and calls the passwordToHash() method. Finally it sends a request to the server 
	 * to login the user.
	 */
	public void loginWithRegID() {
		new AsyncTask<Void, Void, String>() {

			/**
			 * This method registers the GCM for the notifications.
			 * @param params null
			 * @return String The unique registration id from the GCM.
			 */
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

			/**
			 * This method checks the user inputs and sends a login request to the server.
			 * @param regid Unique registration id from the GCM.
			 */
			@Override
			protected void onPostExecute(String regid) {

				if (loginUsername.getText().toString().trim().length() == 0) {
					toast = Toast.makeText(context, Constants.TOAST_LOGIN_USERNAME_MISSING,
							Toast.LENGTH_SHORT);
					toast.show();
				}
				else if (loginPass.getText().toString().trim().length() == 0) {
					toast = Toast.makeText(context, Constants.TOAST_LOGIN_PASSWORD_MISSING,
							Toast.LENGTH_SHORT);
					toast.show();
				}

				else {
					Toast.makeText(SignInActivity.getContext(), Constants.PD_LOADING,
							Toast.LENGTH_SHORT).show();
					User userLogin = new User(loginUsername.getText()
							.toString(), passwordToHash(loginPass.getText()
									.toString()));

					userLogin.setRegistrationID(regid);
					if(NetworkClient.getSession() == null){
						NetworkClient.connect();
					}
					while(NetworkClient.getSession() == null);
					NetworkClient.getSession().write(new ClientMessage(ClientMessage.Ident.LOGIN,userLogin));
				}
			}
		}.execute(null, null, null);
	}

	/**
	 * This method codes the password string with the hash algorithm to a hashed byte array.
	 * @param pass Password that the user entered.
	 * @return byte[] The hashed password.
	 */
	private byte[] passwordToHash(String pass) {
		byte[] hashed = null;
		try {
			/*Create MessageDigest instance for MD5*/
			MessageDigest md = MessageDigest.getInstance("MD5");
			/*Add password bytes to digest*/
			md.update(pass.getBytes());
			/*Get the hash's bytes*/
			hashed = md.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hashed;
	}

	/**
	 * This method returns the application context.
	 * @return Context The application context.
	 */
	public static Context getContext() {
		return context;
	}

}