package pl.jedenpies.android.tracker.client;

import java.io.InputStream;
import java.security.KeyStore;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.HttpParams;

import pl.jedenpies.android.tracker.R;
import android.content.Context;
import android.util.Log;

public class TrackerHttpClient extends DefaultHttpClient {

	private static final String LOG = TrackerHttpClient.class.getName();
	
	private Context context;
			
	public TrackerHttpClient(Context context) {
		this.context = context;
	}
	public TrackerHttpClient(Context context, HttpParams params) {
		super(params);
		this.context = context;
	}

	@Override
	protected ClientConnectionManager createClientConnectionManager() {
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		registry.register(new Scheme("https", newSSLSocketFactory(), 443));
		return new SingleClientConnManager(getParams(), registry);
	}
	
	private SSLSocketFactory newSSLSocketFactory() {
		try {
			KeyStore trusted = KeyStore.getInstance("BKS");
			InputStream in = context.getResources().openRawResource(R.raw.tracker_keystore);
			try {
				trusted.load(in, "changeit".toCharArray());
			} finally {
				in.close();
			}
			SSLSocketFactory sf = new SSLSocketFactory(trusted);
			sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
			return sf;
		} catch (Exception e) {
			Log.d(LOG, "Problem instantiating client", e);
			e.printStackTrace();
			throw new AssertionError();
		}
		
	}
}
