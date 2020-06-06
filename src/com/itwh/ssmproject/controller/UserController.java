package com.itwh.ssmproject.controller;
/*
 * （用户）管理员控制器
 */

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.itwh.ssmproject.entity.User;
import com.itwh.ssmproject.page.Page;
import com.itwh.ssmproject.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	public UserService userservice;
	
	/*
	 * 用户管理列表页
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		model.setViewName("user/user_list");
		return model;
	}
	
	/*
	 * 获取用户列表
	 */
	//required表示是否为必须，defaultValue表示默认值
	@RequestMapping(value="/get_list",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getList(
			@RequestParam(value="username",required=false,defaultValue="") String username,
			Page page
			){
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("username", "%"+username+"%");
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		ret.put("rows", userservice.findList(queryMap));
		ret.put("total", userservice.getTotal(queryMap));
		return ret;
	}
	
	/*
	 * 编辑删除操作
	 */
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> delete (
			@RequestParam(value="ids[]",required=true) Long[] ids
			){
		Map<String, String> ret = new HashMap<String, String>();
		
		String idString ="";
		for(Long id:ids) {
			idString+=id+",";
		}
		idString=idString.toString().substring(0, idString.length()-1);
		if(userservice.delete(idString)<=0) {
			ret.put("type","error");
			ret.put("msg", "删除失败！");
			return ret;
		}
		ret.put("type","success");
		ret.put("msg", "删除成功！");
		return ret;
	}

	/*
	 * 编辑用户操作
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	//User user ：表单填写用户名username和密码password，因为entity里面加了component,所以提交表单后
	//springMVC会自动检查User这个对象有没有在容器里，如果属性username和password
	//与User中一致，有的话会把值放在里面 
	public Map<String, String> edit(User user){
		Map<String, String> ret = new HashMap<String, String>();
		if(user == null) {
			ret.put("type","error");
			ret.put("msg", "数据绑定出错，请联系开发作者！");
			return ret;
		}
	 	
		if(StringUtils.isEmpty(user.getUsername())) {
			ret.put("type","error");
			ret.put("msg", "用户名不能为空");
			return ret;
		}
		if(StringUtils.isEmpty(user.getPassword())) {
			ret.put("type","error");
			ret.put("msg", "密码不能为空");
			return ret;
		}
		User existUser = userservice.findByUserName(user.getUsername());
		if(existUser!=null) {
			//如果只改密码，那么根据用户名查找，肯定存在用户
			//所以要判断id是否一样
			if(user.getId()!=existUser.getId()) {
				ret.put("type","error");
				ret.put("msg", "该用户名已经存在");
				return ret;
			}
			
		}
		if(userservice.edit(user)<=0) {
			ret.put("type","error");
			ret.put("msg", "修改失败");
			return ret;
		}
		
		ret.put("type","success");
		ret.put("msg", "修改成功");
		return ret;
	}
	
	/*
	 * 添加用户菜单
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	//User user ：表单填写用户名username和密码password，因为entity里面加了component,所以提交表单后
	//springMVC会自动检查User这个对象有没有在容器里，如果属性username和password
	//与User中一致，有的话会把值放在里面 
	public Map<String, String> add(User user){
		Map<String, String> ret = new HashMap<String, String>();
		if(user == null) {
			ret.put("type","error");
			ret.put("msg", "数据绑定出错，请联系开发作者！");
			return ret;
		}
	 	
		if(StringUtils.isEmpty(user.getUsername())) {
			ret.put("type","error");
			ret.put("msg", "用户名不能为空");
			return ret;
		}
		if(StringUtils.isEmpty(user.getPassword())) {
			ret.put("type","error");
			ret.put("msg", "密码不能为空");
			return ret;
		}
		User existUser = userservice.findByUserName(user.getUsername());
		if(existUser!=null) {
			ret.put("type","error");
			ret.put("msg", "该用户名已经存在");
			return ret;
		}
		if(userservice.add(user)<=0) {
			ret.put("type","error");
			ret.put("msg", "添加失败");
			return ret;
		}
		
		ret.put("type","success");
		ret.put("msg", "添加成功");
		return ret;
	}
	
	
	
}
