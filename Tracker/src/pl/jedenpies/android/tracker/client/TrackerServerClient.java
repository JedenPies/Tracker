package pl.jedenpies.android.tracker.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import pl.jedenpies.android.tracker.db.model.Packet;
import android.util.Log;

public class TrackerServerClient {

	private static final String SERVER_PATH = 	    "http://192.168.0.102:8080/TrackerServer";
	private static final String CHECK_STATUS_PATH = "json/checkStatus";
	private static final String SEND_PACKET_PATH  = "json/sendPacket";	
	private static final String LOGGER_NAME = TrackerServerClient.class.getName();
	
	public boolean login() {
		// TODO: Zaimplementowac autoryzacje po stronie klienta
		return true;
	}
	
	public boolean checkStatus() {

		try {
			HttpGet httpGet = new HttpGet(SERVER_PATH + "/" + CHECK_STATUS_PATH);
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 1000);
			httpGet.setParams(params);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(httpGet);
			return response.getStatusLine().getStatusCode() == 200;
			
		} catch (ClientProtocolException e) {
			Log.e(LOGGER_NAME, "Unexpected exception while trying to get server status", e);
		} catch (ConnectTimeoutException e) {
			Log.d(LOGGER_NAME, "Connection timeout.");
		} catch (IOException e) {
			Log.v(LOGGER_NAME, "Problem connecting server", e);
		}
		return false;
	}
	
	public boolean sendPacket(Packet packet) {
		
		try {
			HttpPost httpPost = new HttpPost(SERVER_PATH + "/" + SEND_PACKET_PATH);
			httpPost.addHeader("Content-Type", "application/json");
			StringEntity se = new StringEntity(packet.getJSONObject().toString(), HTTP.UTF_8);
			httpPost.setEntity(se);
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpPost);
			Log.e(LOGGER_NAME, "Status code: " + httpResponse.getStatusLine().getStatusCode());
			return httpResponse.getStatusLine().getStatusCode() == 200;

		} catch (UnsupportedEncodingException e) {
			Log.e(LOGGER_NAME, "Unexpected exception while sending packet", e);			
		} catch (ClientProtocolException e) {
			Log.e(LOGGER_NAME, "Unexpected exception while sending packet", e);
		} catch (IOException e) {
			Log.v(LOGGER_NAME, "Problem connecting server");
		}
		return false;
	}
}
