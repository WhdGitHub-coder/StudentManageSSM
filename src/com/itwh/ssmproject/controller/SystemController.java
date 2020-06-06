package com.itwh.ssmproject.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.itwh.ssmproject.entity.Student;
import com.itwh.ssmproject.entity.User;
import com.itwh.ssmproject.service.StudentService;
import com.itwh.ssmproject.service.UserService;
import com.itwh.ssmproject.util.CpachaUtil;

@RequestMapping("/system")
@Controller
public class SystemController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private StudentService studentService;
	
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public ModelAndView index(ModelAndView model) {
		model.setViewName("system/index");
		return model;
	}
	/*
	 * 登陆页面
	 */
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public ModelAndView login(ModelAndView model) {
		model.setViewName("system/login"); 
		return model;
	}
	
	/*
	 * 注销登录
	 */
	@RequestMapping(value="/Login_out",method=RequestMethod.GET)
	public String login_out(HttpServletRequest request) {
		request.getSession().setAttribute("user", null);
		return "redirect:login";
	}
	
	/*
	 * 登录表单提交
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> login(
			@RequestParam(value="username",required=true) String username,
			@RequestParam(value="password",required=true) String password,
			@RequestParam(value="vcode",required=true) String vcode,
			@RequestParam(value="type",required=true) int type,
			HttpServletRequest request
			) {
		Map<String, String> ret=new HashMap<String, String>();
		if(StringUtils.isEmpty(username)) {
			ret.put("type", "error");
			ret.put("msg", "用户名不能为空");
			return ret;
		}
		if(StringUtils.isEmpty(password)) {
			ret.put("type", "error");
			ret.put("msg", "密码不能为空");
			return ret;
		}
		if(StringUtils.isEmpty(vcode)) {
			ret.put("type", "error");
			ret.put("msg", "验证码不能为空");
			return ret;
		}
		String loginCpacha = (String)request.getSession().getAttribute("loginCpacha");
		if(StringUtils.isEmpty(loginCpacha)) {
			ret.put("type", "error");
			ret.put("msg", "长时间未操作，会话已失效，请刷新后重试");
			return ret;
		}
		if(!vcode.toUpperCase().equals(loginCpacha.toUpperCase())) {
			ret.put("type", "error");
			ret.put("msg", "验证码错误");
			return ret;
		}
		//此时没错误，把验证码置空，不然浪费内存
		request.getSession().setAttribute("loginCpacha", null);
		
		if(type == 1) {
			//管理员
			//从数据库中去查找用户
			User user = userService.findByUserName(username);
			if(user == null) {
				ret.put("type", "error");
				ret.put("msg", "不存在该用户");
				return ret;
			}
			if(!password.equals(user.getPassword())) {
				ret.put("type", "error");
				ret.put("msg", "密码错误，请重新输入");
				return ret;
			}
			request.getSession().setAttribute("user", user);
		}
		if(type == 2) {
			//学生
			Student student = studentService.findByUserName(username);
			if(student == null) {
				ret.put("type", "error");
				ret.put("msg", "不存在该学生");
				return ret;
			}
			if(!password.equals(student.getPassword())) {
				ret.put("type", "error");
				ret.put("msg", "密码错误，请重新输入");
				return ret;
			}
			request.getSession().setAttribute("user", student);
		}
		request.getSession().setAttribute("userType", type);
		ret.put("type", "success");
		ret.put("msg", "登录成功");
		return ret;
	}
	
	/*
	 * 显示验证码
	 */
	@RequestMapping(value="/get_cpacha",method=RequestMethod.GET)
	public void getCpacha(HttpServletRequest request,
			@RequestParam(value="vl",defaultValue="4",required=false) Integer vl,
			@RequestParam(value="w",defaultValue="98",required=false) Integer w,
			@RequestParam(value="h",defaultValue="33",required=false) Integer h,
			HttpServletResponse response) {
		CpachaUtil cpachaUtil =new CpachaUtil(vl, w, h);
		//生成验证码
		String generatorVCode = cpachaUtil.generatorVCode();
		//会话，当打开一个网站，就建立一个会话，直到关闭或者长时间不用，就认为会话结束，把session销毁掉
		request.getSession().setAttribute("loginCpacha", generatorVCode);
		//带旋转的
		BufferedImage generatorRotateVCodeImage = cpachaUtil.generatorRotateVCodeImage(generatorVCode, true);
		//生成图片的bufferedimage放在response的getoutputstream，response自动返回前端页面
		try {
			ImageIO.write(generatorRotateVCodeImage, "gif", response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
