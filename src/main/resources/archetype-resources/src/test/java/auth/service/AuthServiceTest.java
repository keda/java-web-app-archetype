package ${package}.auth.service;

import static org.junit.Assert.*;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ${package}.AppTest;
import ${package}.auth.dto.BasMemu;
import ${package}.utils.IdWorker;

public class AuthServiceTest extends AppTest{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private IAuthService authService;
	
	@Autowired
	private IdWorker idWorker;
	
	@Test
	public void testGetUserByPwd() {
		for (int i = 0; i < 1000; i++) {
			logger.info("id = {}", idWorker.nextId());
		}
	}

	@Test
	public void testGetUserRoles() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAccessedMemu() {
		List<BasMemu> memus = authService.getAccessedMemu(new Long[]{10000L, 10001L, 10002L});
		
		Assert.assertTrue(memus != null && memus.size() == 0);
		
	}

}
