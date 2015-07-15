package ${package}.utils;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
			JacksonConfig.globalConfig(this);
		}
		
	}
	
}
