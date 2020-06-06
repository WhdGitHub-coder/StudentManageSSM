package com.itwh.ssmproject.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonObject;
import com.itwh.ssmproject.entity.User;
/*
 * 登陆过滤拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		// TODO Auto-generated method stub
		String url = request.getRequestURI();
		Object user=request.getSession().getAttribute("user");
		if(user == null) {
			//表示未登录或者登陆失效
			System.out.println("未登录或登录失效，url="+url);
			//重新跳转到登录页面，getContextPath（）是获取到此网站根目录
			if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				//说明是Ajax请求
				//Ajax请求用response.sendRedirect跳转不了
				Map<String, String> ret = new HashMap<String, String>();
				ret.put("type","error");
				ret.put("msg", "登陆状态已失效，请重新去登录！");
				response.getWriter().write(net.sf.json.JSONObject.fromObject(ret).toString());
				return false; 
			}
			
			response.sendRedirect(request.getContextPath()+"/system/login");
			return false;
		}
		return true;
	}
	
}
