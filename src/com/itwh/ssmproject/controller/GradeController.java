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
 * 年级信息管理
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
	 * 获取年级列表
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
	//User user ：表单填写用户名username和密码password，因为entity里面加了component,所以提交表单后
	//springMVC会自动检查User这个对象有没有在容器里，如果属性username和password
	//与User中一致，有的话会把值放在里面 
	public Map<String, String> edit(Grade grade){
		Map<String, String> ret = new HashMap<String, String>();
		if(StringUtils.isEmpty(grade.getName())) {
			ret.put("type","error");
			ret.put("msg", "年级名称不能为空");
			return ret;
		}
		if(gradeService.edit(grade)<=0) {
			ret.put("type","error");
			ret.put("msg", "修改失败");
			return ret;
		}
		ret.put("type","success");
		ret.put("msg", "修改成功");
		return ret;
	}
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> add(Grade grade){
		
		Map<String, String> ret = new HashMap<String, String>();
		if(grade == null) {
			ret.put("type","error");
			ret.put("msg", "数据绑定出错，请联系开发作者！");
			return ret;
		}
	 	
		if(StringUtils.isEmpty(grade.getName())) {
			ret.put("type","error");
			ret.put("msg", "年级名不能为空");
			return ret;
		}
		
		if(gradeService.add(grade)<=0) {
			ret.put("type","error");
			ret.put("msg", "年级添加失败");
			return ret;
		}
		
		ret.put("type","success");
		ret.put("msg", "年级添加成功");
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
		try {
			if(gradeService.delete(idString)<=0) {
				ret.put("type","error");
				ret.put("msg", "删除失败！");
				return ret;
			}
		} catch (Exception e) {
			// TODO: handle exception
			ret.put("type","error");
			ret.put("msg", "该年级下存在班级信息，请勿删除！");
			return ret;
		}
		ret.put("type","success");
		ret.put("msg", "删除成功！");
		return ret;
	}

	
}
