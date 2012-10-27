package pl.jedenpies.android.tracker.client;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import pl.jedenpies.android.tracker.db.model.Packet;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class TrackerServerClient extends JSONClient {

	private static final String SERVER_PATH = 	     "http://158.75.44.172:8080/TrackerServer/";
	private static final String CAPTCHA_PATH = 		 "mobile/user/getCaptcha";
	private static final String REGISTER_USER_PATH = "mobile/user/register";
	private static final String LOGIN_USER_PATH	=    "mobile/user/login";
	private static final String LOGOUT_USER_PATH   = "mobile/user/logout";
	private static final String CHECK_STATUS_PATH =  "mobile/checkStatus";
	private static final String SEND_PACKET_PATH  =  "mobile/sendPacket";	
	
	private static final String LOGGER_NAME = TrackerServerClient.class.getName();
	
	public String getEncoding() {
		return HTTP.UTF_8;
	}
	
	/**
	 * Returns captcha bitmap from server.
	 * @return captcha
	 */
	public Bitmap getCaptcha() {
		
		try {
			HttpGet httpGet = new HttpGet(SERVER_PATH + CAPTCHA_PATH);
			HttpClient httpClient = new DefaultHttpClient();
			
			HttpContext context = new BasicHttpContext();
			context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);			
			
			HttpResponse httpResponse = httpClient.execute(httpGet, context);
			Bitmap bmp = BitmapFactory.decodeStream(httpResponse.getEntity().getContent());
			return bmp;
			
		} catch (ClientProtocolException e) {
			Log.e(LOGGER_NAME, "Unexpected exception while getting captcha.", e);
		} catch (ConnectTimeoutException e) {
			Log.d(LOGGER_NAME, "Connection timeout.");
		} catch (IOException e) {
			Log.v(LOGGER_NAME, "Problem connecting server", e);
		}
		return null;
	}
	
	public boolean checkStatus() {
		
		try {			
			super.getJSONPacket(SERVER_PATH + CHECK_STATUS_PATH, makeParams(2000, 3000));
			return true;
			
		} catch (ClientProtocolException e) {
			Log.e(LOGGER_NAME, "Unexpected exception while trying to get server status", e);
		} catch (ConnectTimeoutException e) {
			Log.d(LOGGER_NAME, "Connection timeout.");
		} catch (IOException e) {
			Log.v(LOGGER_NAME, "Problem connecting server", e);
		} catch (JSONException e) {
			Log.v(LOGGER_NAME, "Problem parsing JSON response", e);
		}
		return false;
	}
	
	public TrackerServerResponse userRegister(
			String username, String email, String password, String password2, String captchaAnswer) {
		
		String errorMessage;
		int errorCode;

		try {
			
			JSONObject request = new JSONObject();
			request.putOpt("username", username);
			request.putOpt("email", email);
			request.putOpt("password", password);
			request.putOpt("password2", password2);
			request.putOpt("captchaAnswer", captchaAnswer);
			
			JSONObject response = sendJSONPacket(SERVER_PATH + REGISTER_USER_PATH, request);
			return parseResponse(response);
						
		// TODO Modify messages
		} catch (JSONException e) {
			errorCode = TrackerServerResponse.CODE_PARSING_RESPONSE;
			errorMessage = "JSONException";
		} catch (IOException e) {
			errorCode = TrackerServerResponse.CODE_INPUT_OUTPUT;
			errorMessage = "IOException";
		}
		return new TrackerServerResponse(errorCode, errorMessage);
	}

	private TrackerServerResponse parseResponse(JSONObject response) {
		
		try {
		if (!TrackerServerResponse.STATUS_OK.equals(response.getString("status"))) {
			return new TrackerServerResponse(
					response.getInt("errorCode"),
					response.getString("errorMessage"));				
		} else {
			return TrackerServerResponse.ANSWER_OK;				
		}
		} catch (JSONException e) {
			return new TrackerServerResponse(TrackerServerResponse.CODE_PARSING_RESPONSE, e.getMessage());
		}
	}
	
	public TrackerServerResponse userLogin(String username, String password) {
		
		TrackerServerResponse result;
		try {
			JSONObject object = new JSONObject();
			object.putOpt("username", username);
			object.putOpt("password", password);
			JSONObject response = super.sendJSONPacket(SERVER_PATH + LOGIN_USER_PATH, object);
			result = parseResponse(response);
			return result;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return TrackerServerResponse.ANSWER_NOK;
	}
	
	public TrackerServerResponse userLogout() {
		
		try {
			return parseResponse(getJSONPacket(SERVER_PATH + LOGOUT_USER_PATH));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public TrackerServerResponse sendPacket(Packet packet) {
		
		try {
			TrackerServerResponse response;
			response = parseResponse(sendJSONPacket(SERVER_PATH + "/" + SEND_PACKET_PATH, packet.getJSONObject()));
			return response;
		} catch (JSONException e) {
			return new TrackerServerResponse(TrackerServerResponse.CODE_PARSING_RESPONSE, e.getMessage());
		} catch (IOException e) {
			return new TrackerServerResponse(TrackerServerResponse.CODE_INPUT_OUTPUT, e.getMessage());
		}
	}
	
}
