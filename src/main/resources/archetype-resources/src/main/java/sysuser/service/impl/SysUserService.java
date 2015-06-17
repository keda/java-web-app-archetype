package ${package}.sysuser.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import ${package}.sysuser.dao.ISysUserDao;
import ${package}.sysuser.dto.UserInfo;
import ${package}.sysuser.service.ISysUserService;

@Service
public class SysUserService implements ISysUserService {
	
	@Autowired
	private ISysUserDao sysUserDao;
	
	@Override
	public List<UserInfo> listAllUser(PageBounds pageBounds) {
		
		return sysUserDao.listAllUser(pageBounds);
	}

	@Override
	public UserInfo getUserById(Long userId) {
		
		return sysUserDao.getUserById(userId);
	}

	@Override
	public UserInfo getUserByName(String username) {
		
		return sysUserDao.getUserByName(username);
	}

	@Override
	public List<UserInfo> queryUserByName(String username, PageBounds pageBounds) {
		
		return sysUserDao.queryUserByName(username, pageBounds);
	}

	@Override
	public Long insertNewUser(UserInfo user) {
		
		return sysUserDao.insertNewUser(user);
	}

	@Override
	public int updateUser(UserInfo user) {
		
		return sysUserDao.updateUser(user);
	}
	
}
