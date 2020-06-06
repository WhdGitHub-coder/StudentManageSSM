package com.itwh.ssmproject.controller;
/*
 * ���û�������Ա������
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
	 * �û������б�ҳ
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		model.setViewName("user/user_list");
		return model;
	}
	
	/*
	 * ��ȡ�û��б�
	 */
	//required��ʾ�Ƿ�Ϊ���룬defaultValue��ʾĬ��ֵ
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
	 * �༭ɾ������
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
			ret.put("msg", "ɾ��ʧ�ܣ�");
			return ret;
		}
		ret.put("type","success");
		ret.put("msg", "ɾ���ɹ���");
		return ret;
	}

	/*
	 * �༭�û�����
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	//User user ������д�û���username������password����Ϊentity�������component,�����ύ����
	//springMVC���Զ����User���������û����������������username��password
	//��User��һ�£��еĻ����ֵ�������� 
	public Map<String, String> edit(User user){
		Map<String, String> ret = new HashMap<String, String>();
		if(user == null) {
			ret.put("type","error");
			ret.put("msg", "���ݰ󶨳�������ϵ�������ߣ�");
			return ret;
		}
	 	
		if(StringUtils.isEmpty(user.getUsername())) {
			ret.put("type","error");
			ret.put("msg", "�û�������Ϊ��");
			return ret;
		}
		if(StringUtils.isEmpty(user.getPassword())) {
			ret.put("type","error");
			ret.put("msg", "���벻��Ϊ��");
			return ret;
		}
		User existUser = userservice.findByUserName(user.getUsername());
		if(existUser!=null) {
			//���ֻ�����룬��ô�����û������ң��϶������û�
			//����Ҫ�ж�id�Ƿ�һ��
			if(user.getId()!=existUser.getId()) {
				ret.put("type","error");
				ret.put("msg", "���û����Ѿ�����");
				return ret;
			}
			
		}
		if(userservice.edit(user)<=0) {
			ret.put("type","error");
			ret.put("msg", "�޸�ʧ��");
			return ret;
		}
		
		ret.put("type","success");
		ret.put("msg", "�޸ĳɹ�");
		return ret;
	}
	
	/*
	 * ����û��˵�
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	//User user ������д�û���username������password����Ϊentity�������component,�����ύ����
	//springMVC���Զ����User���������û����������������username��password
	//��User��һ�£��еĻ����ֵ�������� 
	public Map<String, String> add(User user){
		Map<String, String> ret = new HashMap<String, String>();
		if(user == null) {
			ret.put("type","error");
			ret.put("msg", "���ݰ󶨳�������ϵ�������ߣ�");
			return ret;
		}
	 	
		if(StringUtils.isEmpty(user.getUsername())) {
			ret.put("type","error");
			ret.put("msg", "�û�������Ϊ��");
			return ret;
		}
		if(StringUtils.isEmpty(user.getPassword())) {
			ret.put("type","error");
			ret.put("msg", "���벻��Ϊ��");
			return ret;
		}
		User existUser = userservice.findByUserName(user.getUsername());
		if(existUser!=null) {
			ret.put("type","error");
			ret.put("msg", "���û����Ѿ�����");
			return ret;
		}
		if(userservice.add(user)<=0) {
			ret.put("type","error");
			ret.put("msg", "���ʧ��");
			return ret;
		}
		
		ret.put("type","success");
		ret.put("msg", "��ӳɹ�");
		return ret;
	}
	
	
	
}
