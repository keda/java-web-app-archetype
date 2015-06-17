package ${package}.auth.dto;

import java.util.List;
import java.util.Set;

import ${package}.sysuser.dto.UserInfo;

public class AuthorizedUserInfo {
	
	private UserInfo userInfo;
	
	private List<RoleInfo> roles;
	
	private Set<BasMemu> memus;

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public List<RoleInfo> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleInfo> roles) {
		this.roles = roles;
	}

	public Set<BasMemu> getMemus() {
		return memus;
	}

	public void setMemus(Set<BasMemu> memus) {
		this.memus = memus;
	}
	
}
