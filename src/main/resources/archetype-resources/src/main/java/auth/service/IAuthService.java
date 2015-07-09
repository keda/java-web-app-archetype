package ${package}.auth.service;

import java.util.List;

import ${package}.auth.dto.BasMemu;
import ${package}.auth.dto.RoleInfo;
import ${package}.sysuser.dto.UserInfo;

public interface IAuthService {
	
	public UserInfo getUserByPwd(String loginId, String pwd);
	
	public List<RoleInfo> getUserRoles(Long userId);
	
	public List<BasMemu> getAccessedMemu(Long... roleId);
	
}
