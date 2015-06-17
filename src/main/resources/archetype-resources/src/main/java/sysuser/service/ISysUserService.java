package ${package}.sysuser.service;

import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import ${package}.sysuser.dto.UserInfo;

public interface ISysUserService {
	
	public List<UserInfo> listAllUser(PageBounds pageBounds);
	
	public UserInfo getUserById(Long userId);
	
	public UserInfo getUserByName(String username);
	
	public List<UserInfo> queryUserByName(String username, PageBounds pageBounds);
	
	public Long insertNewUser(UserInfo user);
	
	public int updateUser(UserInfo user);
	
}
