package com.itwh.ssmproject.controller;

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

import com.itwh.ssmproject.entity.Grade;
import com.itwh.ssmproject.entity.User;
import com.itwh.ssmproject.page.Page;
import com.itwh.ssmproject.service.GradeService;

/*
 * �꼶��Ϣ����
 */
@Controller
@RequestMapping("/grade")
public class GradeController {
	
	@Autowired
	private GradeService gradeService;
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		model.setViewName("grade/grade_list");
		return model;
	}
	
	/*
	 * ��ȡ�꼶�б�
	 */
	@RequestMapping(value="/get_list",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getList(
			@RequestParam(value="name",required=false,defaultValue="") String name,
			Page page
			){
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("name", "%"+name+"%");
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		ret.put("rows", gradeService.findList(queryMap));
		ret.put("total", gradeService.getTotal(queryMap));
		return ret;
	}
	
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	//User user ������д�û���username������password����Ϊentity�������component,�����ύ����
	//springMVC���Զ����User���������û����������������username��password
	//��User��һ�£��еĻ����ֵ�������� 
	public Map<String, String> edit(Grade grade){
		Map<String, String> ret = new HashMap<String, String>();
		if(StringUtils.isEmpty(grade.getName())) {
			ret.put("type","error");
			ret.put("msg", "�꼶���Ʋ���Ϊ��");
			return ret;
		}
		if(gradeService.edit(grade)<=0) {
			ret.put("type","error");
			ret.put("msg", "�޸�ʧ��");
			return ret;
		}
		ret.put("type","success");
		ret.put("msg", "�޸ĳɹ�");
		return ret;
	}
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> add(Grade grade){
		
		Map<String, String> ret = new HashMap<String, String>();
		if(grade == null) {
			ret.put("type","error");
			ret.put("msg", "���ݰ󶨳�������ϵ�������ߣ�");
			return ret;
		}
	 	
		if(StringUtils.isEmpty(grade.getName())) {
			ret.put("type","error");
			ret.put("msg", "�꼶������Ϊ��");
			return ret;
		}
		
		if(gradeService.add(grade)<=0) {
			ret.put("type","error");
			ret.put("msg", "�꼶���ʧ��");
			return ret;
		}
		
		ret.put("type","success");
		ret.put("msg", "�꼶��ӳɹ�");
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
		try {
			if(gradeService.delete(idString)<=0) {
				ret.put("type","error");
				ret.put("msg", "ɾ��ʧ�ܣ�");
				return ret;
			}
		} catch (Exception e) {
			// TODO: handle exception
			ret.put("type","error");
			ret.put("msg", "���꼶�´��ڰ༶��Ϣ������ɾ����");
			return ret;
		}
		ret.put("type","success");
		ret.put("msg", "ɾ���ɹ���");
		return ret;
	}

	
}
