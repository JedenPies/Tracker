package pl.jedenpies.android.tracker.service;

import pl.jedenpies.android.tracker.tools.Message;
import android.os.Parcel;
import android.os.Parcelable;

public class TrackerServiceMessage implements Message, Parcelable {

	public static final int TYPE_NORMAL = 0;
	
	private int level;
	private String text;
	
	public TrackerServiceMessage(Message message) {
		this.text = message.getText();
		this.level = message.getLevel();
	}
	
	public TrackerServiceMessage(int level, String text) {
		this.text = text;
		this.level = level;
	}
		
	public int getLevel() {
		return level;
	}
	public String getText() {
		return text;
	}
	
	@Override
	public int hashCode() {
		return (level + 1) * text.hashCode();
	}
	
	@Override
	public int describeContents() {
		return hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(level);		
		dest.writeString(text);
	}

	public static final Parcelable.Creator<TrackerServiceMessage> CREATOR =
			new Parcelable.Creator<TrackerServiceMessage>() {
		
		@Override
		public TrackerServiceMessage createFromParcel(Parcel source) {
			int type = source.readInt();
			String text = source.readString();
			TrackerServiceMessage message = new TrackerServiceMessage(type, text);
			return message;
		}
		
		@Override
		public TrackerServiceMessage[] newArray(int size) {
			return new TrackerServiceMessage[size];
		}		
	};

}
