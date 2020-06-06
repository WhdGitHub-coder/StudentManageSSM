package com.itwh.ssmproject.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.itwh.ssmproject.entity.Clazz;
import com.itwh.ssmproject.entity.Grade;
import com.itwh.ssmproject.entity.Student;
import com.itwh.ssmproject.entity.User;
import com.itwh.ssmproject.page.Page;
import com.itwh.ssmproject.service.ClazzService;
import com.itwh.ssmproject.service.GradeService;
import com.itwh.ssmproject.service.StudentService;

import net.sf.json.JSONArray;

/*
 * 学生信息管理
 * 
 */
@Controller
@RequestMapping("/student")
public class StudentController {
	
	@Autowired
	private StudentService studentService;
	@Autowired
	private ClazzService clazzService;
	
	/*
	 * 学生列表页面
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		model.setViewName("student/student_list");
		List<Clazz> clazzList = clazzService.findAll();
		//addObject：向页面传递一个实体对象
		model.addObject("clazzList", clazzList);
		model.addObject("clazzListJson", JSONArray.fromObject(clazzList));
		return model;
	}
	 
	/*
	 * 获取学生列表
	 */
	@RequestMapping(value="/get_list",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getList(
			@RequestParam(value="username",required=false,defaultValue="") String username,
			@RequestParam(value="clazzId",required=false) Long clazzId,
			HttpServletRequest request,
			Page page
			){
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("username", "%"+username+"%");
		Object userType = request.getSession().getAttribute("userType");
		if("2".equals(userType.toString())){
			//说明是学生
			Student loginedStudent = (Student)request.getSession().getAttribute("user");
			queryMap.put("username", "%"+loginedStudent.getUsername()+"%");
		}
		if(clazzId !=null) {
			queryMap.put("clazzId", clazzId);
		}
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		ret.put("rows", studentService.findList(queryMap));
		ret.put("total", studentService.getTotal(queryMap));
		return ret;
	}
	
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	//User user ：表单填写用户名username和密码password，因为entity里面加了component,所以提交表单后
	//springMVC会自动检查User这个对象有没有在容器里，如果属性username和password
	//与User中一致，有的话会把值放在里面 
	public Map<String, String> edit(Student student){
		Map<String, String> ret = new HashMap<String, String>();
		if(StringUtils.isEmpty(student.getUsername())) {
			ret.put("type","error");
			ret.put("msg", "学生名不能为空");
			return ret;
		}
		if(StringUtils.isEmpty(student.getPassword())) {
			ret.put("type","error");
			ret.put("msg", "密码不能为空");
			return ret;
		}
		if(student.getClazzId() == null) {
			ret.put("type","error");
			ret.put("msg", "请选择所属班级");
			return ret;
		}
		//判断添加的姓名是否存在
		if(isExist(student.getUsername(), student.getId())) {
			ret.put("type","error");
			ret.put("msg", "该姓名已存在！");
			return ret;
		}
		String generateSn="S"+new Date().getTime()+"";
		student.setSn(generateSn);
		if(studentService.edit(student)<=0) {
			ret.put("type","error");
			ret.put("msg", "学生添加失败");
			return ret;
		}
		
		ret.put("type","success");
		ret.put("msg", "学生添加成功");
		return ret;
	}
	
	/*
	 * 添加学生信息
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> add(Student student){
		
		Map<String, String> ret = new HashMap<String, String>();
		if(StringUtils.isEmpty(student.getUsername())) {
			ret.put("type","error");
			ret.put("msg", "学生名不能为空");
			return ret;
		}
		if(StringUtils.isEmpty(student.getPassword())) {
			ret.put("type","error");
			ret.put("msg", "密码不能为空");
			return ret;
		}
		if(student.getClazzId() == null) {
			ret.put("type","error");
			ret.put("msg", "请选择所属班级");
			return ret;
		}
		//判断添加的姓名是否存在
		if(isExist(student.getUsername(), null)) {
			ret.put("type","error");
			ret.put("msg", "该姓名已存在！");
			return ret;
		}
		String generateSn="S"+new Date().getTime()+"";
		student.setSn(generateSn);
		if(studentService.add(student)<=0) {
			ret.put("type","error");
			ret.put("msg", "学生添加失败");
			return ret;
		}
		
		ret.put("type","success");
		ret.put("msg", "学生添加成功");
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
			if(studentService.delete(idString)<=0) {
				ret.put("type","error");
				ret.put("msg", "删除失败！");
				return ret;
			}
		} catch (Exception e) {
			// TODO: handle exception
			ret.put("type","error");
			ret.put("msg", "该学生下存在其他信息，请勿删除！");
			return ret;
		}
		ret.put("type","success");
		ret.put("msg", "删除成功！");
		return ret;
	}
	
	/*
	 * 上传用户头像图片
	 */
	@RequestMapping(value="/upload_photo",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> uploadPhoto(MultipartFile photo,
			HttpServletRequest request,
			HttpServletResponse response
			) throws IOException{
		response.setCharacterEncoding("UTF-8");
		Map<String, String> ret = new HashMap<String, String>();
		if(photo == null) {
			//文件没有选择
			ret.put("type","error");
			ret.put("msg", "请选择文件！");
			return ret;
		}
		if(photo.getSize()>10485760) {
			//图片太大，大于10M，在springmvc文件中有规定
			ret.put("type","error");
			ret.put("msg", "文件大小超过10M，请上传小于10M的图片！");
			return ret;
		}
		//判断文件是否合格，拿到后缀	
		String sufffix=photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf(".")+1, photo.getOriginalFilename().length());
		if(!"jpg,png,gif,jpeg".contains(sufffix.toLowerCase())) {
			ret.put("type","error");
			ret.put("msg", "文件格式不正确，请上传jpg,png,gif,jpeg格式的图片！");
			return ret;
		}
		
		String savePath=request.getServletContext().getRealPath("/")+"\\upload\\";
		System.out.println(savePath);
		File savePathFile = new File(savePath);
		if(!savePathFile.exists()) {
			savePathFile.mkdir();//如果不存在，则创建一个文件夹upload
		}
		String filename=new Date().getTime()+"."+sufffix;
		//把文件转存到这个文件夹下
		photo.transferTo(new File(savePath+filename));
		ret.put("type","success");
		ret.put("msg", "图片上传成功！");
		ret.put("src", request.getServletContext().getContextPath()+"/upload/"+filename); 
		return ret;
	}
	
	private boolean isExist(String username,Long id) {
		Student student=studentService.findByUserName(username);
		if(student!=null) {
			if(id==null) {
				return true;
			}
			if(student.getId().longValue()!=id.longValue()) {
				return true;
			}
		}
		return false;
	}
}
