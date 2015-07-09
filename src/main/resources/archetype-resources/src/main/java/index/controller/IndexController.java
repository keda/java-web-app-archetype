package ${package}.index.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>Web controller used to display the index page.</p>
 *
 * @author Chris Schaefer
 */
@Controller
public class IndexController {
    /**
     * <p>The name of the index view.</p>
     */
    private static final String LOGIN_VIEW_NAME = "../../auth";
    private static final String INDEX_VIEW_NAME = "index";

    /**
     * <p>Renders the page used to display the index page.</p>
     *
     * @return {@link ModelAndView} the {@link ModelAndView} used to render the index page
     */
    @RequestMapping(value = { "/", "/login.html", "/login.htm" }, method = RequestMethod.GET)
    public ModelAndView renderLoginPage() {
       
    	return new ModelAndView(LOGIN_VIEW_NAME);
    }
    
    @RequestMapping(value = {"/index.html", "/index.htm"}, method = RequestMethod.GET)
    public ModelAndView renderIndexPage() {
    	
    	return new ModelAndView(INDEX_VIEW_NAME);
    }
}
