package com.itwh.ssmproject.dao;
/*
 * �༶dao
 */
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.itwh.ssmproject.entity.Clazz;
import com.itwh.ssmproject.entity.Grade;
import com.itwh.ssmproject.entity.Student;
@Repository
public interface StudentDao {
	public Student findByUserName(String username);
	public int add(Student student);
	public int edit(Student student);
	public int delete(String ids);
	public List<Student> findList(Map<String,Object> queryMap);
	public List<Student> findAll();
	public int getTotal(Map<String,Object> queryMap);
}
