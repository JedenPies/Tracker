package pl.jedenpies.web.tracker.controller.mobile;

public class SimpleJSONResponse {

	public final static String STATUS_OK = "OK";
	public final static String STATUS_NOK = "NOK";
	
	public final static SimpleJSONResponse RESPONSE_OK = new SimpleJSONResponse();
	public final static SimpleJSONResponse RESPONSE_NOK = new SimpleJSONResponse(null);
	
	private String status;
	private String errorMessage;
	
	public SimpleJSONResponse() {
		this.status = STATUS_OK;
	}
	
	public SimpleJSONResponse(String errorMessage) {
		this.status = STATUS_NOK;
		this.errorMessage = errorMessage;
	}

	public String getStatus() {
		return status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
}
