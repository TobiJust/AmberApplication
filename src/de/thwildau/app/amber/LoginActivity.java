package de.thwildau.app.amber;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import de.thwildau.util.Constants;

/**
 * This class is the starting activity and shows the amber logo. In this activity
 * it will be checked if the server is available. If it is available the user will
 * be transfered to the login screen. If not the user will get an information and
 * the screen does not changed.
 * @author Kulla, Just, Wannek
 * @version 1.0
 * @since 2015-01-12
 * @see ActionBarActivity, OnClickListener
 */
public class LoginActivity extends ActionBarActivity implements OnClickListener {

	private Button btnSignIn;
	private Button btnSignUp;
	private static Context context;
	private Toast toast;
	private long lastBackPressTime = 1000;

	/**
	 * Called when the activity is first created. It sets the activity content data 
	 * like view layout, title, elements the user can interact with (buttons).
	 * @param savedInstanceState The previously state of the activity.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_start);
		context = this.getApplicationContext();
		setTitle(Constants.APP_TITLE_AMBER);
		btnSignIn = (Button) findViewById(R.id.btnSingIn);
		btnSignUp = (Button) findViewById(R.id.btnSignUp);
		btnSignIn.setOnClickListener(this);
		btnSignUp.setOnClickListener(this);
	}

	/**This method handles the clicks on the buttons (login/registrierung) in the activity.
	 * It creates a new context for login or for registration.
	 * @param v View which the clickable elements are contained in.
	 */
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

	/**
	 * This method closes the application and return to the device home screen when the user
	 * presses the devices back button.
	 */
	public void onBackPressed() {
		if (this.lastBackPressTime < System.currentTimeMillis() - 5000) {
			toast = Toast.makeText(this, Constants.TOAST_BACKHOME,
					Toast.LENGTH_SHORT);
			this.lastBackPressTime = System.currentTimeMillis();
			toast.show();
		} else if (this.lastBackPressTime < System.currentTimeMillis() + 5000) {
			this.lastBackPressTime = System.currentTimeMillis();
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);			
		}
	}

	/**
	 * This method returns the application context.
	 * @return Context The application context.
	 */
	public static Context getContext() {
		return context;
	}
}
