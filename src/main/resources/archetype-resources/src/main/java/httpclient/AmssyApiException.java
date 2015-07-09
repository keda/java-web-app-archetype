package ${package}.httpclient;

public class AmssyApiException extends Exception {
	private static final long serialVersionUID = 1L;
	private String httpErrCode;
	private String errMsg;
	
	public String getHttpErrCode() {
		return httpErrCode;
	}

	public void setHttpErrCode(String httpErrCode) {
		this.httpErrCode = httpErrCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public AmssyApiException() {
		super();
	}
	
	public AmssyApiException(Throwable e) {
		super(e);
		this.errMsg = e.getMessage();
	}
	
	public AmssyApiException(String message, Throwable e) {
		super(message, e);
		this.errMsg = message + " > " + e.getMessage();
	}
	
	public AmssyApiException(String httpCode, String message, Throwable e) {
		super(message + "[httpCode="+httpCode+"]", e);
		this.httpErrCode = httpCode;
		this.errMsg = message + "[httpCode="+httpCode+"]" + e.getMessage();
	}
}
