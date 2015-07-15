package ${package}.utils;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OkdiMappingJackson extends ObjectMapper {
	
	private static final long serialVersionUID = 4930120822222001746L;
	private boolean useDefault = true;
	
	public OkdiMappingJackson (boolean useDefault) {
		this.useDefault = useDefault;
	}
	
	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		
		if(!useDefault) {
			JacksonConfig.globalConfig(this);
			this.enableDefaultTyping(DefaultTyping.NON_FINAL,As.WRAPPER_OBJECT);
		}
		
	}
}
