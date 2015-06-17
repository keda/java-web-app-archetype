package ${package}.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class TimerInterceptor extends HandlerInterceptorAdapter {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final String __STARTTIME = "__starttime";
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		request.setAttribute(__STARTTIME, System.currentTimeMillis());
		
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		long start = (Long) request.getAttribute(__STARTTIME);
		
		long end = System.currentTimeMillis();
		
		String uri = request.getRequestURI();
		
		logger.info("handle **{}** cost {}ms", uri, (end - start));
	}
}
