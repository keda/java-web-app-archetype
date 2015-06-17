package ${package}.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.github.miemiedev.mybatis.paginator.jackson2.PageListJsonMapper;

public class OkdiMappingJackson2JsonView extends PageListJsonMapper {
	private static final long serialVersionUID = 1L;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private boolean useDefault = true;
	
	public OkdiMappingJackson2JsonView (boolean useDefault) {
		this.useDefault = useDefault;
	}
	
	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		
		if(!useDefault) {
			JsonFactory jsonFactory = super.getFactory();
			jsonFactory.configure(Feature.WRITE_NUMBERS_AS_STRINGS, true);
			
			logger.info("===自定义JsonFactory::[{} = true]", "Feature.WRITE_NUMBERS_AS_STRINGS");
			
		}
		
	}
	
}
