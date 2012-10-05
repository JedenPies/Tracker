package pl.jedenpies.android.tracker.service;

import java.util.Calendar;

public class Timer implements Runnable {

	public interface SleepyHead {
		public void wakeUp();
	}
	
	private SleepyHead sleepyHead;
	private Calendar timer;

	public Timer(SleepyHead sleepyHead) {
		this.sleepyHead = sleepyHead;
		Thread th = new Thread(this);
		th.start();
	}
	
	synchronized public void setTimer(Calendar timer) {
		this.timer = timer;
		notify();
	}

	@Override
	synchronized public void run() {
		try {
			while(true) {
				if (timer == null) {
					wait();
				} else {
					Calendar now = Calendar.getInstance();
					long timeToWait = timer.getTimeInMillis() - now.getTimeInMillis();
					if (timeToWait > 0) {
						wait(timeToWait);
						sleepyHead.wakeUp();
					} else {
						timer = null;
					}
				}
			}
		} catch (InterruptedException e) {}
	}
}
