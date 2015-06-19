package ${package}.sysuser.service;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.amssy.yunying.AppTest;
import com.amssy.yunying.sysuser.dto.UserInfo;
import com.amssy.yunying.utils.IdWorker;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

public class SysUserServiceTest extends AppTest{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private ISysUserService sysUserService;
	
	@Autowired
	private IdWorker idWorker;
	
	@Test
	public void testListAllUser() throws JsonGenerationException, JsonMappingException, IOException {
		PageBounds pageBounds = new PageBounds(1, 10);
		PageList<UserInfo> allUser = (PageList<UserInfo>) sysUserService.listAllUser(pageBounds );
		logger.info("total size: {}", allUser.getPaginator().getTotalCount());
		
		System.out.println(jsonUtil.writeValueAsString(allUser));
		
		UserInfo u1 = new UserInfo();
		u1.setUserName("afdadf");
		
		UserInfo u2 = new UserInfo();
		u1.setUserName("1323132");
		
		List<UserInfo> list = new ArrayList<UserInfo>();
		list.add(u1);
		list.add(u2);
		
		System.out.println(jsonUtil.writeValueAsString(list));
	}

	@Test
	public void testGetUserById() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetUserByName() {
		fail("Not yet implemented");
	}

	@Test
	public void testQueryUserByName() throws JsonProcessingException {
		List<UserInfo> users = sysUserService.queryUserByName("%sanfang%", new PageBounds());
		
		Assert.assertTrue(users.size() > 1);
		
		logger.info(jsonUtil.writeValueAsString(users));
	}

	@Test
	@Rollback
	public void testInsertNewUser() {

		char[] chars = "abcdefghijklmnopqrstuvwxyz_1234567890".toCharArray();
		char[] phone_chars = "1234567890".toCharArray();
		Set<String> uniqe = new HashSet<String>();
		for (int i = 0; i < 1000; i++) {
			UserInfo u = new UserInfo();
			u.setId(idWorker.nextId());
			String loginId = RandomStringUtils.random(RandomUtils.nextInt(10), chars);
			while(loginId.length() <= 2
					|| StringUtils.isNumeric(loginId.substring(0, 1))
					|| StringUtils.startsWith(loginId, "_")
					|| uniqe.contains(loginId)) {
				loginId = RandomStringUtils.random(RandomUtils.nextInt(10), chars);
			}
			u.setLoginId(loginId + "@amssy.com");
			u.setLoginPwd(RandomStringUtils.random(6, phone_chars));
			u.setUserName(loginId);
//			u.setCreateTime(new Timestamp(System.currentTimeMillis()));
			u.setCreateTime(new Date());
			u.setUserStatus((short)0);
			u.setPhone("135" + RandomStringUtils.random(8, phone_chars));
			Long newUserId = sysUserService.insertNewUser(u);
			logger.info("add new user {}", newUserId);
			uniqe.add(loginId);
		}
		
	}

	@Test
	public void testUpdateUser() {
		UserInfo userInfo = sysUserService.getUserByName("sanfang");
		
		userInfo.setUserStatus((short)1);
		
		int updatedRows = sysUserService.updateUser(userInfo);
		
		Assert.assertTrue(updatedRows == 1);
	}

}
