package pl.jedenpies.android.tracker.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public abstract class JSONClient {

	protected CookieStore cookieStore = new BasicCookieStore();
	
	protected abstract String getEncoding();
	protected abstract Context getContext();
	
	protected JSONObject sendJSONPacket(String targetUrl, JSONObject object) 
			throws ClientProtocolException, IOException, JSONException {
		return sendJSONPacket(targetUrl, new BasicHttpParams(), object);
	}
	protected JSONObject sendJSONPacket(String targetUrl, HttpParams httpParams, JSONObject object) 
			throws ClientProtocolException, IOException, JSONException {

		HttpClient httpClient = new TrackerHttpClient(getContext(), httpParams);	
		
		HttpPost httpPost = new HttpPost(targetUrl);
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.setEntity(new StringEntity(object.toString(), getEncoding()));
		
		HttpContext context = new BasicHttpContext();
		context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		
		HttpResponse httpResponse = httpClient.execute(httpPost, context);
		
		return parseJSON(httpResponse.getEntity().getContent());
	}

	protected JSONObject getJSONPacket(String targetUrl)
			throws IOException, JSONException {
		return getJSONPacket(targetUrl, new BasicHttpParams());		
	}
	protected JSONObject getJSONPacket(String targetUrl, HttpParams httpParams) 
			throws IOException, JSONException {
		
		HttpClient httpClient = new TrackerHttpClient(getContext(), httpParams);
		
		HttpGet httpGet = new HttpGet(targetUrl);
		
		HttpContext context = new BasicHttpContext();
		context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);			
		
		HttpResponse httpResponse = httpClient.execute(httpGet, context);
		return parseJSON(httpResponse.getEntity().getContent());
		
	}
	
	protected HttpParams makeParams(
			int connectionTimeout, int socketTimeout) {
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, connectionTimeout);
		HttpConnectionParams.setSoTimeout(params, socketTimeout);
		return params;
	}
	
	private JSONObject parseJSON(InputStream stream) throws IOException, JSONException {
		
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		
		String line;
		while ((line = reader.readLine()) != null) sb.append(line);
		
		JSONObject res = new JSONObject(sb.toString());
		stream.close();
		return res;
	}
	
}
