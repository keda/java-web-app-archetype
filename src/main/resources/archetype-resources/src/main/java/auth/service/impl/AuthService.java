package ${package}.auth.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ${package}.auth.dao.IAuthDao;
import ${package}.auth.dto.BasMemu;
import ${package}.auth.dto.RoleInfo;
import ${package}.auth.service.IAuthService;
import ${package}.sysuser.dto.UserInfo;

@Service
public class AuthService implements IAuthService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IAuthDao authDao;
	
	@Override
	public UserInfo getUserByPwd(String loginId, String pwd) {
		
		return authDao.getUserUsePwd(loginId, pwd);
	}

	@Override
	public List<RoleInfo> getUserRoles(Long userId) {
		
		return authDao.getUserRoles(userId);
	}

	@Override
	public List<BasMemu> getAccessedMemu(Long... roleId) {
		
		return authDao.getAccessedMemu(roleId);
	}


}
