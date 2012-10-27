package pl.jedenpies.android.tracker.client;

public class TrackerServerResponse {

	public static final String STATUS_OK = "OK";
	public static final String STATUS_NOK = "NOK";
	
	public static final String FIELD_STATUS = "status";
	public static final String FIELD_ERROR_MESSAGE = "errorMessage";
	
	public static final TrackerServerResponse ANSWER_OK = new TrackerServerResponse();
	public static final TrackerServerResponse ANSWER_NOK = new TrackerServerResponse(0, null);
	
	public static final int CODE_PARSING_RESPONSE = 10;
	public static final int CODE_INPUT_OUTPUT = 12;
	
	private String status;
	private int errorCode;
	private String errorMessage;	
	
	public TrackerServerResponse() {
		this(0, null);
		this.status = STATUS_OK;
	}
	
	public TrackerServerResponse(int errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.status = STATUS_NOK;
		this.errorMessage = errorMessage;
	}
	
	public String getStatus() {
		return status;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public int getErrorCode() {
		return errorCode;
	}
}
