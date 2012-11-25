package pl.jedenpies.android.tracker.activity;

import pl.jedenpies.android.tracker.R;
import pl.jedenpies.android.tracker.client.TrackerServerClient;
import pl.jedenpies.android.tracker.client.TrackerServerResponse;
import pl.jedenpies.android.tracker.tools.Preferences;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class UserSignUpActivity extends Activity {
	
	private TrackerServerClient client;
	private GuiHandler guiHandler;
	private TextChangedListener textChangedListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_sign_up);
		guiHandler = new GuiHandler();		
		textChangedListener = new TextChangedListener();
		
		guiHandler.txtCaptchaAnswer.addTextChangedListener(textChangedListener);
		guiHandler.txtUsername.addTextChangedListener(textChangedListener);
		guiHandler.txtPassword.addTextChangedListener(textChangedListener);
		guiHandler.txtPassword2.addTextChangedListener(textChangedListener);
		guiHandler.txtCaptchaAnswer.addTextChangedListener(textChangedListener);
		
		client = new TrackerServerClient(this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		new LoadCaptchaTask().execute();
	}
	
	protected void onResume() {
		super.onResume();
		validateButtonsState();
	}
		
	public void onButtonClick(View view) {		
		
		if (view == guiHandler.btnBack) {
			finish();
			return;
		}
		if (view == guiHandler.btnSignUp) {
			Toast.makeText(getApplicationContext(), "Proba rejestracji", Toast.LENGTH_SHORT).show();
			new UserRegisterTask().execute();
		}
	}
	
	private void validateButtonsState() {
		int unLen = guiHandler.txtUsername.getText().length();
		int pw1Len = guiHandler.txtPassword.getText().length();
		int cpLen = guiHandler.txtCaptchaAnswer.getText().length();
		boolean pwSame = guiHandler.txtPassword.getText().toString().equals(guiHandler.txtPassword2.getText().toString());
		
		if (unLen < 5 || pw1Len < 5 || cpLen < 5 || !pwSame) {
			guiHandler.btnSignUp.setEnabled(false);
			return;
		}
		guiHandler.btnSignUp.setEnabled(true);
	}
	
	private class LoadCaptchaTask extends AsyncTask<Void, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(Void... params) {
			return client.getCaptcha();
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			guiHandler.imgCaptcha.setImageBitmap(result);
			guiHandler.txtCaptchaAnswer.getText().clear();
		}		
	}
	
	private class GuiHandler {
		
		private ImageView imgCaptcha 	  = (ImageView) findViewById(R.id.img_captcha);
		private EditText txtCaptchaAnswer = (EditText) findViewById(R.id.txt_captchaAnswer);
		private EditText txtUsername 	  = (EditText) findViewById(R.id.txt_username);
		private EditText txtEmail 		  = (EditText) findViewById(R.id.txt_email);
		private EditText txtPassword 	  = (EditText) findViewById(R.id.txt_password);
		private EditText txtPassword2 	  = (EditText) findViewById(R.id.txt_password2);
		private Button btnBack 	          = (Button) findViewById(R.id.btn_back);
		private Button btnSignUp 	      = (Button) findViewById(R.id.btn_sign_up);
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
	
	private class UserRegisterTask extends AsyncTask<Void, Void, TrackerServerResponse> {
				
		@Override
		protected void onPreExecute() {						
			super.onPreExecute();
		}
		
		@Override
		protected TrackerServerResponse doInBackground(Void... params) {
			
			client.checkStatus();
			return client.userRegister(
					guiHandler.txtUsername.getText().toString(),
					guiHandler.txtEmail.getText().toString(),
					guiHandler.txtPassword.getText().toString(),
					guiHandler.txtPassword2.getText().toString(),
					guiHandler.txtCaptchaAnswer.getText().toString());
		}
		
		protected void onPostExecute(TrackerServerResponse result) {
			
			if (TrackerServerResponse.STATUS_OK.equals(result.getStatus())) {
				Preferences prefs = new Preferences(getApplication());
				prefs.setUserLogin(guiHandler.txtUsername.getText().toString());
				prefs.setUserPassword(guiHandler.txtPassword.getText().toString());
				Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "fail: " + result.getErrorMessage(), Toast.LENGTH_LONG).show();
			}
		}

		
	}
}
