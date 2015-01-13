package de.thwildau.app.amber;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.thwildau.app.network.NetworkClient;
import de.thwildau.info.ClientMessage;
import de.thwildau.model.User;
import de.thwildau.util.Constants;

/**
 * This class describes the SignUp/Registration Activity in which the user can enter a
 * user name and password to register a user object in the application.
 * @author Kulla, Just, Wannek
 * @version 1.0
 * @since 2015-01-12
 * @see {@link ActionBarActivity}
 */
public class SignUpActivity extends ActionBarActivity {

	private Button registerButton;
	private EditText loginUsername;
	private EditText loginPass;
	private Toast toast;
	private static Context context;

	/**
	 *  Called when the activity is first created. It sets the layout, title bar of the screen
	 *  and registers the input elements the user can interact with.
	 *  @param savedInstanceState The previously state of the activity.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		setTitle(Constants.APP_TITLE_REGISTER);
		context = this.getApplicationContext();

		registerButton = (Button) findViewById(R.id.btnSingIn);
		loginUsername = (EditText) findViewById(R.id.etUserName);
		loginPass = (EditText) findViewById(R.id.etPass);

		registerButton.setOnClickListener(new OnClickListener() {
			/**
			 * This method handles the click of the registration button, calls the method
			 * passwordToHash() and sends a server request 
			 * {@link SignUpActivity#passwordToHash()}.
			 */
			@Override
			public void onClick(View v) {

				if (loginUsername.getText().toString().trim().length() == 0) {
					toast = Toast.makeText(context, Constants.TOAST_LOGIN_USERNAME_MISSING,
							Toast.LENGTH_SHORT);
					toast.show();
				}
				else if (loginPass.getText().toString().trim().length() == 0) {
					toast = Toast.makeText(context, Constants.TOAST_LOGIN_PASSWORD_MISSING,
							Toast.LENGTH_SHORT);
					toast.show();
				}else{
					User userRegister = new User(
							loginUsername.getText().toString(),
							passwordToHash(loginPass.getText().toString()));
					if(NetworkClient.getSession() == null){
						NetworkClient.connect();
					}
					while(NetworkClient.getSession() == null);
					NetworkClient.getSession().write(new ClientMessage(ClientMessage.Ident.REGISTER,userRegister));
					Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
					startActivity(intent);
					Toast.makeText(getApplicationContext(), Constants.TOAST_REGISTER_SUCESS,
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	/**
	 * This method codes the password string with the hash algorithm to a hashed byte array.
	 * @param pass Password that the user entered.
	 * @return byte[] The hashed password.
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

	/**
	 * This method returns the application context.
	 * @return Context The application context.
	 */
	public static Context getContext() {
		return context;
	}

}