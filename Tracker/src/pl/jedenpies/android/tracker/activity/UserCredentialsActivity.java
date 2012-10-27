package pl.jedenpies.android.tracker.activity;

import pl.jedenpies.android.tracker.R;
import pl.jedenpies.android.tracker.client.TrackerServerClient;
import pl.jedenpies.android.tracker.client.TrackerServerResponse;
import pl.jedenpies.android.tracker.tools.Preferences;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UserCredentialsActivity extends Activity {
	
	private TrackerServerClient client;
	private GuiHandler guiHandler;
	private TextChangedListener textChangedListener;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_credentials);
		
		guiHandler = new GuiHandler();
		textChangedListener = new TextChangedListener();
		
		guiHandler.txtUsername.addTextChangedListener(textChangedListener);
		guiHandler.txtPassword.addTextChangedListener(textChangedListener);
		
		client = new TrackerServerClient();		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onResume() {		
		super.onResume();
		Preferences prefs = new Preferences(getApplication());
		if (prefs.getUserLogin() != null) guiHandler.txtUsername.setText(prefs.getUserLogin());
		if (prefs.getUserPassword() != null) guiHandler.txtPassword.setText(prefs.getUserPassword());					
		validateButtonsState();		
	}
	
	public void onButtonClick(View view) {
		
		if (view == guiHandler.btnTest) {
			new TestCredentialsTask().execute();
			return;
		}
		if (view == guiHandler.btnSave) {
			Preferences prefs = new Preferences(getApplication());
			prefs.setUserLogin(guiHandler.txtUsername.getText().toString());
			prefs.setUserPassword(guiHandler.txtPassword.getText().toString());
			finish();
			return;
		}
		if (view == guiHandler.btnBack) {
			finish();
			return;
		}
		if (view == guiHandler.btnSignUp) {
			Intent intent = new Intent(this, UserSignUpActivity.class);
			startActivity(intent);
			return;
		}
	}
		
	private void validateButtonsState() {
		
		int unLen = guiHandler.txtUsername.getText().length();
		int pwLen = guiHandler.txtPassword.getText().length();
		if (unLen <= 0 || pwLen <= 0) {
			guiHandler.btnSignUp.setVisibility(View.VISIBLE);
			guiHandler.btnSignUp.setEnabled(true);			
			guiHandler.btnTest.setEnabled(false);
			guiHandler.btnSave.setEnabled(false);
		} 
		if (unLen < 5 || pwLen < 5) {
			guiHandler.btnTest.setEnabled(false);
			guiHandler.btnSave.setEnabled(false);
		}
		if (unLen >= 5 && pwLen >= 5) {
			guiHandler.btnTest.setEnabled(true);
			guiHandler.btnSave.setEnabled(true);
		}
		if (unLen > 0 && pwLen > 0) {
			guiHandler.btnSignUp.setVisibility(View.GONE);			
		}
	}	

	private class GuiHandler {
		
		private Button btnTest   = (Button) findViewById(R.id.btn_test);
		private Button btnSave   = (Button) findViewById(R.id.btn_save);
		private Button btnBack   = (Button) findViewById(R.id.btn_back);
		private Button btnSignUp = (Button) findViewById(R.id.btn_sign_up);
		private TextView txtUsername = (TextView) findViewById(R.id.txt_username);
		private TextView txtPassword = (TextView) findViewById(R.id.txt_password);
	}
	
	private class TextChangedListener implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {}
		@Override
		public void afterTextChanged(Editable s) {
			validateButtonsState();
		}		
	}
	
	private class TestCredentialsTask extends AsyncTask<Void, Void, TrackerServerResponse> {
		
		@Override
		protected TrackerServerResponse doInBackground(Void... params) {

			TrackerServerResponse answer = client.userLogin(
					guiHandler.txtUsername.getText().toString(), 
					guiHandler.txtPassword.getText().toString());
			client.userLogout(); 
			return answer;
		}

		@Override
		protected void onPostExecute(TrackerServerResponse result) {
			
			super.onPostExecute(result);
			if (TrackerServerResponse.STATUS_OK.equals(result.getStatus())) {
				Toast.makeText(getApplicationContext(), "Test OK", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "Test NOK", Toast.LENGTH_SHORT).show();
			}
		}
	}
		
}
