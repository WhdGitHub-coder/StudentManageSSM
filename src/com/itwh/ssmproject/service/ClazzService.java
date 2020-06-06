package com.itwh.ssmproject.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itwh.ssmproject.entity.Clazz;

/*
 * °à¼¶service
 */
@Service
public interface ClazzService {
	public int add(Clazz clazz);
	public int edit(Clazz clazz);
	public int delete(String ids);
	public List<Clazz> findList(Map<String,Object> queryMap);
	public List<Clazz> findAll();
	public int getTotal(Map<String,Object> queryMap);
}
