package pl.jedenpies.android.tracker.db.model;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Packet {

	private static final String LOGGER_NAME = Packet.class.getName();
	
	private Long id;
	private Integer size;
	private Long created;
	private Long sent;
	private Integer status;
	private List<Coordinates> coordinates;


	public Packet(long id, int size, long created, int status) {
		this.id = id;
		this.size = size;
		this.created = created;
		this.status = status;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public Long getCreated() {
		return created;
	}
	public void setCreated(Long created) {
		this.created = created;
	}
	public Long getSent() {
		return sent;
	}
	public void setSent(Long sent) {
		this.sent = sent;
	}
	public List<Coordinates> getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(List<Coordinates> coordinates) {
		this.coordinates = coordinates;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getStatus() {
		return status;
	}
	
	public JSONObject getJSONObject() {
		JSONObject res = new JSONObject();
		try {
			JSONArray coords = new JSONArray();
			for (Coordinates c : coordinates) {
				coords.put(c.getJSONObject());
			}
			res.put("coordinates", coords);
		} catch (JSONException e) {
			Log.e(LOGGER_NAME, "Error while making JSON object", e);
		}		
		return res;
	}
	
}
