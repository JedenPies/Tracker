package pl.jedenpies.android.tracker.service;

import android.os.Handler;
import android.os.Looper;

public class PacketsSender extends Thread {

	private Handler handler;
	
	public Handler getHandler() {
		return handler;
	}
	
	public void quit() {
		Looper.myLooper().quit();
	}
	
	public void run() {			
		Looper.prepare();
		handler = new Handler();
		Looper.loop();
	}
}
