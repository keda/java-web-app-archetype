package ${package}.utils;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;

public class JacksonConfig {
	private final static Logger logger = LoggerFactory.getLogger(JacksonConfig.class);
	
	public static void globalConfig(ObjectMapper om){
		JsonFactory jsonFactory = om.getFactory();
		jsonFactory.configure(Feature.WRITE_NUMBERS_AS_STRINGS, true);
		
		DefaultSerializerProvider dsp = new DefaultSerializerProvider.Impl();
		
		dsp.setNullValueSerializer(new JsonSerializer<Object>() {
			
			@Override
			public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
					throws IOException, JsonProcessingException {
				
				jgen.writeString(StringUtils.EMPTY);
			}
		});
		
		om.setSerializerProvider(dsp);
		
		om.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		om.enableDefaultTyping(DefaultTyping.NON_FINAL,As.WRAPPER_OBJECT);
		logger.info("===自定义JsonFactory::[{} = true]", "Feature.WRITE_NUMBERS_AS_STRINGS");
	}
	
}
