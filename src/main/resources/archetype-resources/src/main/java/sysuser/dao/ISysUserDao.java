package ${package}.sysuser.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import ${package}.sysuser.dto.UserInfo;

public interface ISysUserDao {
	public List<UserInfo> listAllUser(PageBounds pageBounds);
	
	public UserInfo getUserByName(@Param("username")String username);
	
	public UserInfo getUserById(@Param("userId")Long userId);
	
	public List<UserInfo> queryUserByName(@Param("username")String username, PageBounds pageBounds);

	public Long insertNewUser(@Param("user")UserInfo user);

	public int updateUser(@Param("user")UserInfo user);
}
