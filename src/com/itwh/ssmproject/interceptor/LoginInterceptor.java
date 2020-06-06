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
 * ��½����������
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
			//��ʾδ��¼���ߵ�½ʧЧ
			System.out.println("δ��¼���¼ʧЧ��url="+url);
			//������ת����¼ҳ�棬getContextPath�����ǻ�ȡ������վ��Ŀ¼
			if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				//˵����Ajax����
				//Ajax������response.sendRedirect��ת����
				Map<String, String> ret = new HashMap<String, String>();
				ret.put("type","error");
				ret.put("msg", "��½״̬��ʧЧ��������ȥ��¼��");
				response.getWriter().write(net.sf.json.JSONObject.fromObject(ret).toString());
				return false; 
			}
			
			response.sendRedirect(request.getContextPath()+"/system/login");
			return false;
		}
		return true;
	}
	
}
