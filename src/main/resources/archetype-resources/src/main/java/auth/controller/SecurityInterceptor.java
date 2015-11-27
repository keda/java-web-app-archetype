package ${package}.auth.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SecurityInterceptor extends HandlerInterceptorAdapter {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final String STATIC_RESOURCES = "/resource-";
	
	@Value("${appver}") private String appVersion;
	
	private List<String> noAuthUrl;
	
	public List<String> getNoAuthUrl() {
		return noAuthUrl;
	}

	public void setNoAuthUrl(List<String> noAuthUrl) {
		this.noAuthUrl = noAuthUrl;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		String resources = STATIC_RESOURCES+appVersion;
		request.getSession().setAttribute("resources", resources);
		
		String requestURI = StringUtils.removeStart(request.getRequestURI(), request.getContextPath());
		
		for (String nau : noAuthUrl) {
			if(StringUtils.endsWithIgnoreCase(requestURI, nau)) return true;
		}
		
		if(StringUtils.startsWithIgnoreCase(requestURI, resources)) return true;
		logger.debug("preHandle...{}...{}...{}", request.getSession().getId(), requestURI, resources);
		
		HttpSession session = request.getSession(false);
		if(session != null) {
			String nick = (String)session.getAttribute("nick");
			if(nick == null) throw new SessionInvalidException();
		}else{
			throw new SessionInvalidException();
		}
		
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		super.postHandle(request, response, handler, modelAndView);
		
	}
	
}
