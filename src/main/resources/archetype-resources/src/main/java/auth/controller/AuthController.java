package ${package}.auth.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value={"auth"})
public class AuthController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
     * <p>The name of the index view.</p>
     */
    private static final String INDEX_VIEW_NAME = "redirect:/index.html";
	
    @RequestMapping("login")
	public ModelAndView doAuth(@RequestParam(value="username")String username, @RequestParam(value="password")String password, HttpSession session) {
		logger.info("login::username={},password={}", username, password);
		
		session.setAttribute("nick", username);
		
		return new ModelAndView(INDEX_VIEW_NAME);
	}
	
}
