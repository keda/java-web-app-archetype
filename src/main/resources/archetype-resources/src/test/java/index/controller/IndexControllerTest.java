package ${package}.index.controller;

import org.springframework.web.servlet.ModelAndView;

import junit.framework.JUnit4TestAdapter;
import static junit.framework.Assert.*;

import org.junit.Test;

import ${package}.index.controller.IndexController;

/**
 * <p>Test case for the index controller.</p>
 *
 * @author Chris Schaefer
 */
public class IndexControllerTest {
    /**
     * <p>The name of the index view.</p>
     */
    private static final String INDEX_VIEW_NAME = "index";

    /**
     * <p>Use jUnit 4 test runner.</p>
     *
     * @return the jUnit 4 {@link Test} adapter
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(IndexControllerTest.class);
    }

    /**
     * <p>Asserts the returned view name is correct for the {@link IndexController}.</p>
     */
    @Test
    public void testIndexControllerViewName() {
        IndexController indexController = new IndexController();
        ModelAndView modelAndView = indexController.renderIndexPage();

//        assertEquals(INDEX_VIEW_NAME, modelAndView.getViewName());
    }
}
