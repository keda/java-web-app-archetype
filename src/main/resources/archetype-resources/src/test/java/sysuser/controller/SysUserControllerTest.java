package ${package}.sysuser.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ${package}.AppTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@WebAppConfiguration
@ContextConfiguration(locations="classpath:financial-v2-servlet.xml")
public class SysUserControllerTest extends AppTest{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	private MockHttpSession mockHttpSession;
	
	@Before
	public void setUp() {
		mockHttpSession.setAttribute("nick", "test");
		
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).alwaysDo(new ResultHandler() {
			@Override
			public void handle(MvcResult arg0) throws Exception {
				logger.info(arg0.getResponse().getContentAsString());
			}
		}).build();
	}
	
	@Test
	public void testListAllUser() throws Exception {
		
		this.mockMvc.perform(get("/users/alluser.html").session(mockHttpSession))
		.andExpect(status().isOk());

		this.mockMvc.perform(get("/users/alluser.json").session(mockHttpSession))
		.andExpect(status().isOk());
	}

	@Test
	public void testGetUserInfo() throws Exception {
		this.mockMvc.perform(get("/users/{id}/get.html?format=json", 75018038876049408L).session(mockHttpSession))
		.andExpect(status().isOk());
	}

}
