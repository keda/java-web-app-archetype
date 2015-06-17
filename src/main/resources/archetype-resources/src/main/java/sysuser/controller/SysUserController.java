package ${package}.sysuser.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.Order.Direction;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import ${package}.sysuser.dto.UserInfo;
import ${package}.sysuser.service.ISysUserService;

@Controller
@RequestMapping("users")
public class SysUserController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private ISysUserService sysUserService;
	
	@RequestMapping(value={"alluser.json"}, method=RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public PageList<UserInfo> listAllUser(@RequestParam(defaultValue="20", value="rows")int rows
			, @RequestParam(defaultValue="1", value="page")int page
			, @RequestParam(defaultValue="id", value="sidx")String sidx
			, @RequestParam(defaultValue="asc", value="sord")String sord) {
		
		PageList<UserInfo> listAllUser = (PageList<UserInfo>) sysUserService.listAllUser(new PageBounds(page, rows, new Order(sidx, Direction.fromString(sord), sord)));
		
		return listAllUser;
	}
	
	@RequestMapping(value={"alluser.html"}, method=RequestMethod.GET)
	public ModelAndView listAllUser(@RequestParam(defaultValue="20", value="rows")int rows
			, @RequestParam(defaultValue="1", value="page")int page
			, @RequestParam(defaultValue="id", value="sidx")String sidx
			, @RequestParam(defaultValue="asc", value="sord")String sord
			, ModelAndView model) {
		
		PageList<UserInfo> listAllUser = listAllUser(rows, page, sidx, sord);
		
		model.addObject(listAllUser);
		model.setViewName("user/users");
		
		return model;
	}
	
	@RequestMapping(value="update.html", method=RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public int updateUserInfo(@RequestParam("id")Long userId, @RequestParam("user_name")String userName
			, @RequestParam("phone")String phone, @RequestParam("oper")String oper){
		
		UserInfo user = new UserInfo();
		user.setId(userId);
		user.setUserName(userName);
		user.setPhone(phone);
		
		int updated = sysUserService.updateUser(user);
		
		return updated;
	}
	
	@RequestMapping(value="{userId:\\d+}/get.html", method=RequestMethod.GET)
	public String getUserInfo(@PathVariable("userId")Long userId, ModelMap model) {
		
		UserInfo user = sysUserService.getUserById(userId);
		
		model.addAttribute(user);
//		model.addObject(user);
//		model.setViewName("user/edit");
		
		return "user/edit";
	}
	
	@ModelAttribute("gender")
	public Map<Long, String> gender() {
		Map<Long, String> gender = new HashMap<Long, String>();
		
		gender.put(100000L, "未知");
		gender.put(100001L, "男");
		gender.put(100002L, "女");
		
		return gender;
	}
	
}
