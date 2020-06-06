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
 * 班级信息管理
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
	 * 班级列表页面
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		model.setViewName("clazz/clazz_list");
		//addObject：向页面传递一个实体对象
		model.addObject("gradeList", gradeService.findAll());
		model.addObject("gradeListJson", JSONArray.fromObject(gradeService.findAll()));
		return model;
	}
	 
	/*
	 * 获取班级列表
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
	//User user ：表单填写用户名username和密码password，因为entity里面加了component,所以提交表单后
	//springMVC会自动检查User这个对象有没有在容器里，如果属性username和password
	//与User中一致，有的话会把值放在里面 
	public Map<String, String> edit(Clazz clazz){
		Map<String, String> ret = new HashMap<String, String>();
		if(StringUtils.isEmpty(clazz.getName())) {
			ret.put("type","error");
			ret.put("msg", "班级名称不能为空");
			return ret;
		}
		if(clazzService.edit(clazz)<=0) {
			ret.put("type","error");
			ret.put("msg", "修改失败");
			return ret;
		}
		ret.put("type","success");
		ret.put("msg", "修改成功");
		return ret;
	}
	
	/*
	 * 添加班级信息
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> add(Clazz clazz){
		
		Map<String, String> ret = new HashMap<String, String>();
		if(StringUtils.isEmpty(clazz.getName())) {
			ret.put("type","error");
			ret.put("msg", "班级名不能为空");
			return ret;
		}
		if(clazz.getGradeId() == null) {
			ret.put("type","error");
			ret.put("msg", "请选择所属年级");
			return ret;
		}
		if(clazzService.add(clazz)<=0) {
			ret.put("type","error");
			ret.put("msg", "班级添加失败");
			return ret;
		}
		
		ret.put("type","success");
		ret.put("msg", "班级添加成功");
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
			if(clazzService.delete(idString)<=0) {
				ret.put("type","error");
				ret.put("msg", "删除失败！");
				return ret;
			}
		} catch (Exception e) {
			// TODO: handle exception
			ret.put("type","error");
			ret.put("msg", "该班级下存在学生信息，请勿删除！");
			return ret;
		}
		ret.put("type","success");
		ret.put("msg", "删除成功！");
		return ret;
	}

	
}
