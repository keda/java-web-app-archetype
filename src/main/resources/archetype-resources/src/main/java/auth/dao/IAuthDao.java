package ${package}.auth.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import ${package}.auth.dto.BasMemu;
import ${package}.auth.dto.RoleInfo;
import ${package}.sysuser.dto.UserInfo;


public interface IAuthDao {
	
	public UserInfo getUserUsePwd(@Param("loginId")String loginId, @Param("pwd")String pwd);
	
	public List<RoleInfo> getUserRoles(@Param("userId")Long userId);
	
	public List<BasMemu> getAccessedMemu(@Param("roleId")Long... roleId);
	
}
