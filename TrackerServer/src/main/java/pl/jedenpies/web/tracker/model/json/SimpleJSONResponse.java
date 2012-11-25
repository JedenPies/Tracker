package pl.jedenpies.web.tracker.model.json;

public class SimpleJSONResponse {

	public final static String STATUS_OK = "OK";
	public final static String STATUS_NOK = "NOK";
	
	public final static SimpleJSONResponse RESPONSE_OK = new SimpleJSONResponse();
	public final static SimpleJSONResponse RESPONSE_NOK = new SimpleJSONResponse(0, null);
	
	private String status;
	private int errorCode;
	private String errorMessage;
	
	public SimpleJSONResponse() {
		this.status = STATUS_OK;
	}
	
	public SimpleJSONResponse(int errorCode, String errorMessage) {
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
