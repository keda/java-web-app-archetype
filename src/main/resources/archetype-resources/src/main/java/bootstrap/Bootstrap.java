package ${package}.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Bootstrap {
	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);
	
	public static void main(String[] args) {
		String[] configs = {"classpath:META-INF/spring/beans/application-beans.xml"};
		
		ApplicationContext ctx = new FileSystemXmlApplicationContext(configs);
		
		logger.info("{} startup!", ctx.getApplicationName());
	}
}
