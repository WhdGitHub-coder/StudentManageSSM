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

import com.itwh.ssmproject.entity.Clazz;
import com.itwh.ssmproject.entity.Grade;
import com.itwh.ssmproject.entity.User;
import com.itwh.ssmproject.page.Page;
import com.itwh.ssmproject.service.ClazzService;
import com.itwh.ssmproject.service.GradeService;

import net.sf.json.JSONArray;

/*
 * �༶��Ϣ����
 * 
 */
@Controller
@RequestMapping("/clazz")
public class ClazzController {
	
	@Autowired
	private GradeService gradeService;
	@Autowired
	private ClazzService clazzService;
	
	/*
	 * �༶�б�ҳ��
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		model.setViewName("clazz/clazz_list");
		//addObject����ҳ�洫��һ��ʵ�����
		model.addObject("gradeList", gradeService.findAll());
		model.addObject("gradeListJson", JSONArray.fromObject(gradeService.findAll()));
		return model;
	}
	 
	/*
	 * ��ȡ�༶�б�
	 */
	@RequestMapping(value="/get_list",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getList(
			@RequestParam(value="name",required=false,defaultValue="") String name,
			@RequestParam(value="gradeId",required=false) Long gradeId,
			Page page
			){
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("name", "%"+name+"%");
		if(gradeId !=null) {
			queryMap.put("gradeId", gradeId);
		}
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		ret.put("rows", clazzService.findList(queryMap));
		ret.put("total", clazzService.getTotal(queryMap));
		return ret;
	}
	
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	//User user ������д�û���username������password����Ϊentity�������component,�����ύ����
	//springMVC���Զ����User���������û����������������username��password
	//��User��һ�£��еĻ����ֵ�������� 
	public Map<String, String> edit(Clazz clazz){
		Map<String, String> ret = new HashMap<String, String>();
		if(StringUtils.isEmpty(clazz.getName())) {
			ret.put("type","error");
			ret.put("msg", "�༶���Ʋ���Ϊ��");
			return ret;
		}
		if(clazzService.edit(clazz)<=0) {
			ret.put("type","error");
			ret.put("msg", "�޸�ʧ��");
			return ret;
		}
		ret.put("type","success");
		ret.put("msg", "�޸ĳɹ�");
		return ret;
	}
	
	/*
	 * ��Ӱ༶��Ϣ
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> add(Clazz clazz){
		
		Map<String, String> ret = new HashMap<String, String>();
		if(StringUtils.isEmpty(clazz.getName())) {
			ret.put("type","error");
			ret.put("msg", "�༶������Ϊ��");
			return ret;
		}
		if(clazz.getGradeId() == null) {
			ret.put("type","error");
			ret.put("msg", "��ѡ�������꼶");
			return ret;
		}
		if(clazzService.add(clazz)<=0) {
			ret.put("type","error");
			ret.put("msg", "�༶���ʧ��");
			return ret;
		}
		
		ret.put("type","success");
		ret.put("msg", "�༶��ӳɹ�");
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
			if(clazzService.delete(idString)<=0) {
				ret.put("type","error");
				ret.put("msg", "ɾ��ʧ�ܣ�");
				return ret;
			}
		} catch (Exception e) {
			// TODO: handle exception
			ret.put("type","error");
			ret.put("msg", "�ð༶�´���ѧ����Ϣ������ɾ����");
			return ret;
		}
		ret.put("type","success");
		ret.put("msg", "ɾ���ɹ���");
		return ret;
	}

	
}
