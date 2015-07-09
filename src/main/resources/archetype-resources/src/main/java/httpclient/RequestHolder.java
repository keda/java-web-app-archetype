package ${package}.httpclient;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

public class RequestHolder {
	
	private Map<String, String> appParams;
	
	private Map<String, String> requestMustParams;
	
	private Map<String, String> requestOptParams;

	public Map<String, String> getAppParams() {
		return appParams;
	}

	public void setAppParams(Map<String, String> appParams) {
		this.appParams = appParams;
	}

	public Map<String, String> getRequestMustParams() {
		return requestMustParams;
	}

	public void setRequestMustParams(Map<String, String> requestMustParams) {
		this.requestMustParams = requestMustParams;
	}

	public Map<String, String> getRequestOptParams() {
		return requestOptParams;
	}

	public void setRequestOptParams(Map<String, String> requestOptParams) {
		this.requestOptParams = requestOptParams;
	}
	
	public Map<String, String> getAllParams() {
		Map<String, String> allParams = new HashMap<String, String>();
		
		if(!(this.appParams == null || this.appParams.size() == 0)){
			allParams.putAll(this.appParams);
		}
		
		if(!(this.requestMustParams == null || this.requestMustParams.size() == 0)){
			allParams.putAll(this.requestMustParams);
		}
		
		if(!(this.requestOptParams == null || this.requestOptParams.size() == 0)){
			allParams.putAll(this.requestOptParams);
		}
		
		return allParams;
	}
	
}
