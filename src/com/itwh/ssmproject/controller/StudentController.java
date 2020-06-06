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
 * ѧ����Ϣ����
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
	 * ѧ���б�ҳ��
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		model.setViewName("student/student_list");
		List<Clazz> clazzList = clazzService.findAll();
		//addObject����ҳ�洫��һ��ʵ�����
		model.addObject("clazzList", clazzList);
		model.addObject("clazzListJson", JSONArray.fromObject(clazzList));
		return model;
	}
	 
	/*
	 * ��ȡѧ���б�
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
			//˵����ѧ��
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
	//User user ������д�û���username������password����Ϊentity�������component,�����ύ����
	//springMVC���Զ����User���������û����������������username��password
	//��User��һ�£��еĻ����ֵ�������� 
	public Map<String, String> edit(Student student){
		Map<String, String> ret = new HashMap<String, String>();
		if(StringUtils.isEmpty(student.getUsername())) {
			ret.put("type","error");
			ret.put("msg", "ѧ��������Ϊ��");
			return ret;
		}
		if(StringUtils.isEmpty(student.getPassword())) {
			ret.put("type","error");
			ret.put("msg", "���벻��Ϊ��");
			return ret;
		}
		if(student.getClazzId() == null) {
			ret.put("type","error");
			ret.put("msg", "��ѡ�������༶");
			return ret;
		}
		//�ж���ӵ������Ƿ����
		if(isExist(student.getUsername(), student.getId())) {
			ret.put("type","error");
			ret.put("msg", "�������Ѵ��ڣ�");
			return ret;
		}
		String generateSn="S"+new Date().getTime()+"";
		student.setSn(generateSn);
		if(studentService.edit(student)<=0) {
			ret.put("type","error");
			ret.put("msg", "ѧ�����ʧ��");
			return ret;
		}
		
		ret.put("type","success");
		ret.put("msg", "ѧ����ӳɹ�");
		return ret;
	}
	
	/*
	 * ���ѧ����Ϣ
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> add(Student student){
		
		Map<String, String> ret = new HashMap<String, String>();
		if(StringUtils.isEmpty(student.getUsername())) {
			ret.put("type","error");
			ret.put("msg", "ѧ��������Ϊ��");
			return ret;
		}
		if(StringUtils.isEmpty(student.getPassword())) {
			ret.put("type","error");
			ret.put("msg", "���벻��Ϊ��");
			return ret;
		}
		if(student.getClazzId() == null) {
			ret.put("type","error");
			ret.put("msg", "��ѡ�������༶");
			return ret;
		}
		//�ж���ӵ������Ƿ����
		if(isExist(student.getUsername(), null)) {
			ret.put("type","error");
			ret.put("msg", "�������Ѵ��ڣ�");
			return ret;
		}
		String generateSn="S"+new Date().getTime()+"";
		student.setSn(generateSn);
		if(studentService.add(student)<=0) {
			ret.put("type","error");
			ret.put("msg", "ѧ�����ʧ��");
			return ret;
		}
		
		ret.put("type","success");
		ret.put("msg", "ѧ����ӳɹ�");
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
			if(studentService.delete(idString)<=0) {
				ret.put("type","error");
				ret.put("msg", "ɾ��ʧ�ܣ�");
				return ret;
			}
		} catch (Exception e) {
			// TODO: handle exception
			ret.put("type","error");
			ret.put("msg", "��ѧ���´���������Ϣ������ɾ����");
			return ret;
		}
		ret.put("type","success");
		ret.put("msg", "ɾ���ɹ���");
		return ret;
	}
	
	/*
	 * �ϴ��û�ͷ��ͼƬ
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
			//�ļ�û��ѡ��
			ret.put("type","error");
			ret.put("msg", "��ѡ���ļ���");
			return ret;
		}
		if(photo.getSize()>10485760) {
			//ͼƬ̫�󣬴���10M����springmvc�ļ����й涨
			ret.put("type","error");
			ret.put("msg", "�ļ���С����10M�����ϴ�С��10M��ͼƬ��");
			return ret;
		}
		//�ж��ļ��Ƿ�ϸ��õ���׺	
		String sufffix=photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf(".")+1, photo.getOriginalFilename().length());
		if(!"jpg,png,gif,jpeg".contains(sufffix.toLowerCase())) {
			ret.put("type","error");
			ret.put("msg", "�ļ���ʽ����ȷ�����ϴ�jpg,png,gif,jpeg��ʽ��ͼƬ��");
			return ret;
		}
		
		String savePath=request.getServletContext().getRealPath("/")+"\\upload\\";
		System.out.println(savePath);
		File savePathFile = new File(savePath);
		if(!savePathFile.exists()) {
			savePathFile.mkdir();//��������ڣ��򴴽�һ���ļ���upload
		}
		String filename=new Date().getTime()+"."+sufffix;
		//���ļ�ת�浽����ļ�����
		photo.transferTo(new File(savePath+filename));
		ret.put("type","success");
		ret.put("msg", "ͼƬ�ϴ��ɹ���");
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
